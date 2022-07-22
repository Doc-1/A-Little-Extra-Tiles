package com.alet.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class InventoryCopier extends InventoryBasic {
    
    public InventoryCopier(String title, boolean customName, int slotCount) {
        super(title, customName, slotCount);
    }
    
    @Override
    public ItemStack addItem(ItemStack stack) {
        return stack;
    }
    
    @Override
    public ItemStack removeStackFromSlot(int index) {
        // TODO Auto-generated method stub
        return super.removeStackFromSlot(index);
    }
    
    @Override
    public ItemStack decrStackSize(int index, int count) {
        // TODO Auto-generated method stub
        return super.decrStackSize(index, count);
    }
    
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
}
