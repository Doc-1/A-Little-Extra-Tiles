package com.alet.common.structure.type.programable.basic.conditions;

import com.alet.client.gui.LittleItemSelector;
import com.alet.client.gui.controls.GuiConnectedCheckBoxes;
import com.alet.client.gui.controls.GuiFakeSlot;
import com.alet.common.structure.type.programable.basic.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerConditionHasItem extends LittleTriggerCondition {
    
    public int stackCount = 1;
    public ItemStack stack = ItemStack.EMPTY;
    public int slotIndex = 0;
    public boolean anyStackCount = false;
    public String slotSource = "";
    
    public LittleTriggerConditionHasItem(int id) {
        super(id);
    }
    
    @Override
    public boolean conditionPassed() {
        for (Entity e : this.getEntities()) {
            if (e instanceof EntityPlayerMP) {
                EntityPlayerMP en = (EntityPlayerMP) e;
                ItemStack stack = en.inventory.getStackInSlot(slotIndex).copy();
                if (anyStackCount)
                    stack.setCount(1);
                if (slotSource.equals("anySlot")) {
                    for (int i = 0; i < en.inventory.getSizeInventory(); i++) {
                        stack = en.inventory.getStackInSlot(i).copy();
                        if (anyStackCount)
                            stack.setCount(1);
                        if (ItemStack.areItemStacksEqual(stack, this.stack))
                            return true;
                    }
                } else if (slotSource.equals("mainHand")) {
                    if (ItemStack.areItemStacksEqual(en.getHeldItemMainhand(), this.stack))
                        return true;
                } else if (ItemStack.areItemStacksEqual(stack, this.stack))
                    return true;
                
            }
        }
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        stackCount = nbt.getInteger("stackCount");
        stack = new ItemStack(nbt.getCompoundTag("stack"));
        slotIndex = nbt.getInteger("slotID");
        anyStackCount = nbt.getBoolean("anyStackCount");
        slotSource = nbt.getString("slotSource");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setInteger("stackCount", stackCount);
        NBTTagCompound nbtItemStack = new NBTTagCompound();
        stack.writeToNBT(nbtItemStack);
        nbt.setTag("stack", nbtItemStack);
        nbt.setInteger("slotID", slotIndex);
        nbt.setBoolean("anyStackCount", anyStackCount);
        nbt.setString("slotSource", slotSource);
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        GuiFakeSlot stack = new GuiFakeSlot("stack", 0, 20, 18, 18);
        panel.addControl(stack);
        if (!this.stack.isEmpty())
            stack.updateItemStack(this.stack);
        panel.addControl(new GuiStackSelectorAll("preview", 0, 0, 110, player, new GuiStackSelectorAll.InventoryCollector(new LittleItemSelector()), true));
        
        panel.addControl(new GuiButton("use", "Use Item As Key", 25, 22) {
            @Override
            public void onClicked(int x, int y, int button) {
                GuiStackSelectorAll preview = (GuiStackSelectorAll) parent.get("preview");
                GuiFakeSlot key = (GuiFakeSlot) parent.get("stack");
                ItemStack temp = preview.getSelected().copy();
                temp.setCount(stackCount);
                key.updateItemStack(temp);
            }
        });
        
        panel.addControl(new GuiCheckBox("anyStack", "Any Stack Size", 0, 45, this.anyStackCount));
        
        panel.addControl(new GuiLabel("Stack Count:", 0, 62));
        GuiTextfield count = new GuiTextfield("count", this.stackCount + "", 66, 62, 25, 10).setNumbersOnly();
        if (this.anyStackCount)
            count.setEnabled(false);
        count.maxLength = 2;
        panel.addControl(count);
        GuiConnectedCheckBoxes slotSource = new GuiConnectedCheckBoxes("slotSource", 0, 80).addCheckBox("mainHand", "Check Main Hand").addCheckBox("anySlot", "Check Any Slot")
                .addCheckBox("specificSlot", "Check Specific Slot");
        panel.addControl(slotSource);
        panel.addControl(new GuiLabel("Slot ID:", 0, 130));
        GuiTextfield slotIndex = new GuiTextfield("slot", this.slotIndex + "", 40, 130, 25, 10).setNumbersOnly();
        
        if (!this.slotSource.equals("")) {
            slotSource.setSelected(this.slotSource);
            if (this.slotSource.equals("anySlot") || this.slotSource.equals("mainHand"))
                slotIndex.setEnabled(false);
        }
        panel.addControl(slotIndex);
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiFakeSlot) {
            GuiFakeSlot key = (GuiFakeSlot) source;
            this.stack = key.basic.getStackInSlot(0).copy();
        } else if (source.is("count")) {
            GuiTextfield count = (GuiTextfield) source;
            if (!count.text.equals("")) {
                int c = Integer.parseInt(count.text);
                if (c == 0) {
                    c = 1;
                    count.text = "1";
                } else if (c > 64) {
                    c = 64;
                    count.text = "64";
                }
                this.stackCount = c;
                if (!this.stack.isEmpty())
                    this.stack.setCount(this.stackCount);
            }
        } else if (source.is("slot")) {
            GuiTextfield slot = (GuiTextfield) source;
            if (!slot.text.equals("")) {
                int id = Integer.parseInt(slot.text);
                
                this.slotIndex = id;
            }
        } else if (source.is("anyStack")) {
            GuiTextfield count = (GuiTextfield) source.parent.get("count");
            count.setEnabled(!((GuiCheckBox) source).value);
            this.stack.setCount(1);
            this.anyStackCount = ((GuiCheckBox) source).value;
        } else if (source.is("slotSource")) {
            GuiTextfield slotIndex = (GuiTextfield) source.parent.get("slot");
            GuiConnectedCheckBoxes boxes = ((GuiConnectedCheckBoxes) source);
            if (boxes.getSelected().name.equals("specificSlot"))
                slotIndex.setEnabled(true);
            else
                slotIndex.setEnabled(false);
            this.slotSource = boxes.getSelected().name;
        }
        
    }
    
}
