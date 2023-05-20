package com.alet.common.entity;

import net.minecraft.util.math.Vec3d;

public class LeadConnectionData {
    
    public int connectionID;
    public int color;
    public double thickness;
    public double tautness;
    public Vec3d position1;
    public Vec3d axisCenter1;
    public Vec3d position2;
    public Vec3d axisCenter2;
    public float lightLevel;
    
    public LeadConnectionData(int color, double thickness, double tautness, float lightLevel) {
        this.color = color;
        this.thickness = thickness;
        this.tautness = tautness;
        this.lightLevel = lightLevel;
    }
    
    @Override
    public boolean equals(Object data) {
        LeadConnectionData d = (LeadConnectionData) data;
        if (this.color == d.color && this.thickness == d.thickness && this.tautness == d.tautness && this.lightLevel == d.lightLevel)
            return true;
        else
            return false;
    }
}
