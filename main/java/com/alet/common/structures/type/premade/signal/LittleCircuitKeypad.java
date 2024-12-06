package com.alet.common.structures.type.premade.signal;

import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitKeypad extends LittleStructurePremade {
    
    public LittleCircuitKeypad(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void tick() {
        if (!this.isClient())
            try {
                LittleSignalOutput valid = (LittleSignalOutput) this.children.get(0).getStructure();
                LittleSignalInput pass = (LittleSignalInput) this.children.get(1).getStructure();
                
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        
    }
    
}
