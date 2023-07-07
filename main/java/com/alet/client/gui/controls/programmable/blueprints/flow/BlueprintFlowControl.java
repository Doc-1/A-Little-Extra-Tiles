package com.alet.client.gui.controls.programmable.blueprints.flow;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;

import net.minecraft.nbt.NBTTagCompound;

public abstract class BlueprintFlowControl extends GuiBlueprint {
    
    public BlueprintFlowControl(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return nbt;
    }
    
    @Override
    public GuiBlueprint deserializeNBT(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        return this;
    }
    
    @Override
    public abstract GuiBlueprint getNextBlueprint();
    
}
