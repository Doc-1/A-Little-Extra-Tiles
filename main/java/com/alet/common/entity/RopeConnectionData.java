package com.alet.common.entity;

import javax.vecmath.Vector3d;

import com.alet.common.structure.connection.RopeConnection;
import com.alet.common.structure.type.LittleRopeConnectionALET;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;

public class RopeConnectionData {
    
    public int color;
    public double thickness;
    public double tautness;
    private Vector3d targetCenter;
    public RopeConnection headConnection;
    
    public RopeConnectionData(int color, double thickness, double tautness) {
        this.color = color;
        this.thickness = thickness;
        this.tautness = tautness;
    }
    
    public RopeConnectionData copy() {
        RopeConnectionData d = new RopeConnectionData(this.color, this.thickness, this.tautness);
        d.targetCenter = this.targetCenter;
        return d;
    }
    
    public Vector3d getTargetCenter() {
        if (this.targetCenter == null) {
            
            try {
                LittleRopeConnectionALET struct = (LittleRopeConnectionALET) headConnection.getStructure();
                targetCenter = struct.axisCenter.getCenter();
                
                targetCenter.x += struct.getPos().getX();
                targetCenter.y += struct.getPos().getY();
                targetCenter.z += struct.getPos().getZ();
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                return new Vector3d(0, 0, 0);
            }
        }
        return targetCenter;
    }
    
    @Override
    public boolean equals(Object data) {
        RopeConnectionData d = (RopeConnectionData) data;
        if (this.color == d.color && this.thickness == d.thickness && this.tautness == d.tautness)
            return true;
        else
            return false;
    }
}
