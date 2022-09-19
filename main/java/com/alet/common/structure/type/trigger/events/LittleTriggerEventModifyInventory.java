package com.alet.common.structure.type.trigger.events;

import java.util.HashSet;

import com.alet.client.gui.LittleItemSelector;
import com.alet.client.gui.controls.GuiConnectedCheckBoxes;
import com.alet.client.gui.controls.GuiFakeSlot;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.util.ingredient.LittleIngredients;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;
import com.creativemd.littletiles.common.util.ingredient.NotEnoughIngredientsException;
import com.creativemd.littletiles.common.util.ingredient.StackIngredient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerEventModifyInventory extends LittleTriggerEvent {
    
    public boolean addItem = true;
    public int stackCount = 1;
    public ItemStack stack = ItemStack.EMPTY;
    public boolean inHand = true;
    
    public LittleTriggerEventModifyInventory(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public LittleTriggerEvent createFromNBT(NBTTagCompound nbt) {
        addItem = nbt.getBoolean("addItem");
        stackCount = nbt.getInteger("stackCount");
        stack = new ItemStack(nbt.getCompoundTag("stack"));
        inHand = nbt.getBoolean("inHand");
        return this;
    }
    
    @Override
    public NBTTagCompound createNBT(NBTTagCompound nbt) {
        nbt.setBoolean("addItem", addItem);
        nbt.setInteger("stackCount", stackCount);
        NBTTagCompound nbtItemStack = new NBTTagCompound();
        stack.writeToNBT(nbtItemStack);
        nbt.setTag("stack", nbtItemStack);
        nbt.setBoolean("inHand", inHand);
        return nbt;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        GuiPanel panel = getPanel(parent);
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
                key.updateItemStack(preview.getSelected());
            }
        });
        GuiConnectedCheckBoxes addRemove = new GuiConnectedCheckBoxes("addRemove", 0, 45).addCheckBox("add", "Add Item").addCheckBox("remove", "Remove Item");
        if (this.addItem)
            addRemove.setSelected("add");
        else
            addRemove.setSelected("remove");
        panel.addControl(addRemove);
        GuiConnectedCheckBoxes handInventory = new GuiConnectedCheckBoxes("handInv", 0, 77).addCheckBox("inHand", "Check In Hand").addCheckBox("inInv", "Check In Inventory");
        if (this.inHand)
            handInventory.setSelected("inHand");
        else
            handInventory.setSelected("inInv");
        
        panel.addControl(handInventory);
    }
    
    @Override
    public void updateValues(CoreControl source) {
        if (source instanceof GuiFakeSlot) {
            GuiFakeSlot key = (GuiFakeSlot) source;
            this.stack = key.basic.getStackInSlot(0);
        } else if (source instanceof GuiConnectedCheckBoxes) {
            GuiConnectedCheckBoxes boxes = (GuiConnectedCheckBoxes) source;
            if (source.is("addRemove")) {
                this.addItem = boxes.getSelected().name.equals("add");
            } else if (source.is("handInv")) {
                this.inHand = boxes.getSelected().name.equals("inHand");
            }
        }
        
    }
    
    @Override
    public boolean runEvent(HashSet<Entity> entities) {
        for (Entity entity : entities) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (!stack.isEmpty()) {
                    LittleIngredients ingredients = new LittleIngredients(new StackIngredient(stack));
                    
                    try {
                        if (!addItem) {
                            if (this.inHand) {
                                if (player.getHeldItemMainhand().getItem().equals(stack.getItem())) {
                                    player.getHeldItemMainhand().shrink(1);
                                    return true;
                                }
                            } else {
                                LittleAction.checkAndTake(player, new LittleInventory(player), ingredients);
                                return true;
                            }
                        } else {
                            LittleAction.checkAndGive(player, new LittleInventory(player), ingredients);
                            return true;
                        }
                    } catch (NotEnoughIngredientsException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
    
}
