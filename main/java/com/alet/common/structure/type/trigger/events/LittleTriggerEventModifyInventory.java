package com.alet.common.structure.type.trigger.events;

import com.alet.client.gui.LittleItemSelector;
import com.alet.client.gui.controls.GuiConnectedCheckBoxes;
import com.alet.client.gui.controls.GuiFakeSlot;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.ingredient.LittleIngredients;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;
import com.creativemd.littletiles.common.util.ingredient.NotEnoughIngredientsException;
import com.creativemd.littletiles.common.util.ingredient.StackIngredient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RangedWrapper;

public class LittleTriggerEventModifyInventory extends LittleTriggerEvent {
    
    public boolean addItem = true;
    public int stackCount = 1;
    public ItemStack stack = ItemStack.EMPTY;
    public final byte mainHand = 0, offHand = 1, helm = 2, chest = 3, legs = 4, boots = 5, specific = 6;
    public byte selected = 1;
    public int slotID = 0;
    
    public LittleTriggerEventModifyInventory(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    public IItemHandler getSlot(EntityPlayer player) {
        InventoryPlayer inv = player.inventory;
        System.out.println(inv.getSizeInventory());
        //off-hand = 40
        //helm = 39
        //chest = 38
        //legs = 37
        //boots = 36
        switch (0) {
            //main hand
            case 0:
                return new RangedWrapper((IItemHandlerModifiable) player.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                    null), inv.currentItem, inv.currentItem - 1 + 1);
            //off-hand
            case 1:
                return new RangedWrapper((IItemHandlerModifiable) player.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 40, 41);
            //helm
            case 2:
                return new RangedWrapper((IItemHandlerModifiable) player.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 39, 40);
            //chest
            case 3:
                return new RangedWrapper((IItemHandlerModifiable) player.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 38, 39);
            //legs
            case 4:
                return new RangedWrapper((IItemHandlerModifiable) player.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 37, 38);
            //boots
            case 5:
                return new RangedWrapper((IItemHandlerModifiable) player.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), 36, 37);
            //specific
            case 6:
                return new RangedWrapper((IItemHandlerModifiable) player.getCapability(
                    CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), this.slotID, this.slotID + 1);
            //Any Slots
            default:
                return player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        }
    }
    
    @Override
    public LittleTriggerEvent deserializeNBT(NBTTagCompound nbt) {
        addItem = nbt.getBoolean("addItem");
        stackCount = nbt.getInteger("stackCount");
        stack = new ItemStack(nbt.getCompoundTag("stack"));
        selected = nbt.getByte("selected");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setBoolean("addItem", addItem);
        nbt.setInteger("stackCount", stackCount);
        NBTTagCompound nbtItemStack = new NBTTagCompound();
        stack.writeToNBT(nbtItemStack);
        nbt.setTag("stack", nbtItemStack);
        nbt.setByte("selected", selected);
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
        panel.addControl(
            new GuiStackSelectorAll("preview", 0, 0, 110, player, new GuiStackSelectorAll.InventoryCollector(new LittleItemSelector()), true));
        
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
        panel.addControl(new GuiLabel("Stack Count:", 0, 45));
        GuiTextfield count = new GuiTextfield("count", this.stackCount + "", 66, 45, 25, 10).setNumbersOnly();
        count.maxLength = 2;
        panel.addControl(count);
        GuiConnectedCheckBoxes addRemove = new GuiConnectedCheckBoxes("addRemove", 0, 65).addCheckBox("add",
            "Add Item").addCheckBox("remove", "Remove Item");
        if (this.addItem)
            addRemove.setSelected("add");
        else
            addRemove.setSelected("remove");
        panel.addControl(addRemove);
        /*
        GuiConnectedCheckBoxes handInventory = new GuiConnectedCheckBoxes("handInv", 0, 97).addCheckBox(
            "inHand", "Add/Remove to Hand").addCheckBox("inInv", "Add/Remove to Inventory");
        if (this.inHand)
            handInventory.setSelected("inHand");
        else
            handInventory.setSelected("inInv");
        
        panel.addControl(handInventory);*/
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiFakeSlot) {
            GuiFakeSlot key = (GuiFakeSlot) source;
            this.stack = key.basic.getStackInSlot(0).copy();
        } else if (source instanceof GuiConnectedCheckBoxes) {
            GuiConnectedCheckBoxes boxes = (GuiConnectedCheckBoxes) source;
            if (source.is("addRemove")) {
                this.addItem = boxes.getSelected().name.equals("add");
            }
        } else if (source.is("selectedSlot")) {
            //this.selected = boxes.getSelected();
        } else if (source.is("count")) {
            GuiTextfield count = (GuiTextfield) source;
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
        
    }
    
    @Override
    public boolean runEvent() {
        for (Entity entity : this.getEntities()) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (!stack.isEmpty()) {
                    LittleIngredients ingredients = new LittleIngredients(new StackIngredient(stack));
                    try {
                        if (!addItem) {
                            LittleAction.checkAndTake(player, new LittleInventory(player, getSlot(player)),
                                ingredients);
                            return true;
                            
                        } else {
                            LittleAction.checkAndGive(player, new LittleInventory(player, getSlot(player)),
                                ingredients);
                            
                            return true;
                        }
                    } catch (NotEnoughIngredientsException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
    
}
