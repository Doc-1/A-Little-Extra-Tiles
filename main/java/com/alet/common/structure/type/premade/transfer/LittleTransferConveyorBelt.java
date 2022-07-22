package com.alet.common.structure.type.premade.transfer;

import com.creativemd.littletiles.common.structure.directional.StructureDirectional;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.LittleNoClipStructure;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

public class LittleTransferConveyorBelt extends LittleStructurePremade {
    @StructureDirectional
    public EnumFacing facing;
    
    public LittleTransferConveyorBelt(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        
    }
    
    @Override
    public void tick() {
        try {
            LittleNoClipStructure noclip = (LittleNoClipStructure) this.children.get(1).getStructure();
            for (Entity entity : noclip.entities) {
                double scale = 0.05;
                Vec3d vec = new Vec3d(this.facing.getDirectionVec()).normalize();
                double x = vec.x * scale;
                double y = vec.y * scale;
                double z = vec.z * scale;
                entity.addVelocity(x, y, z);
            }
            noclip.entities.clear();
        } catch (CorruptedConnectionException | NotYetConnectedException e) {}
    }
}
