package com.alet.components.entities;

import net.minecraft.nbt.NBTTagCompound;

public class RopeData {
    
    public int color = 0;
    public double thickness = 1.0F;
    public double tautness = 0.0F;
    
    public RopeData(int color, double thickness, double tautness) {
        this.color = color;
        this.thickness = thickness;
        this.tautness = tautness;
    }
    
    public RopeData(NBTTagCompound nbt) {
        this(nbt.getInteger("color"), nbt.getDouble("thickness"), nbt.getDouble("tautness"));
    }
    
    public RopeData copy() {
        RopeData d = new RopeData(this.color, this.thickness, this.tautness);
        return d;
    }
    
    public NBTTagCompound writeData() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("color", color);
        nbt.setDouble("thickness", thickness);
        nbt.setDouble("tautness", tautness);
        return nbt;
    }
    
    @Override
    public boolean equals(Object data) {
        RopeData d = (RopeData) data;
        if (this.color == d.color && this.thickness == d.thickness && this.tautness == d.tautness)
            return true;
        else
            return false;
    }
}
