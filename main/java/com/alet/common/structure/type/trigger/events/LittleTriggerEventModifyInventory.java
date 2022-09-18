package com.alet.common.structure.type.trigger.events;

import java.util.HashSet;

import com.alet.client.gui.controls.GuiFakeSlot;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;

import net.minecraft.entity.Entity;
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
        GuiPanel panel = getPanel(parent);
        panel.addControl(new GuiFakeSlot("", 0, 0, 18, 18));
    }
    
    @Override
    public void updateValues(CoreControl source) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean runEvent(HashSet<Entity> entities) {
        return false;
    }
    
}
