package com.alet.common.structures.type.premade.signal;

import com.alet.common.utils.SignalingUtils;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitROM extends LittleCircuitPremade {
    
    public boolean[] state = new boolean[32];
    
    public LittleCircuitROM(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock, 1);
        this.setOutputIndexes(0);
    }
    
    @Override
    public void afterPlaced() {
        super.afterPlaced();
        boolean isBinary = true;
        boolean hasChar = false;
        if (this.name != null && this.name.length() >= 2) {
            String ch = this.name.substring(0, 1);
            String split = this.name.substring(1);
            if (ch.equalsIgnoreCase("b"))
                for (char c : split.toCharArray()) {
                    if (c != '0' && c != '1') {
                        isBinary = false;
                        break;
                    }
                }
            else if (ch.equalsIgnoreCase("d"))
                isBinary = false;
            for (char c : split.toCharArray()) {
                if (!Character.isDigit(c)) {
                    hasChar = true;
                    break;
                }
            }
            
            if (!hasChar)
                if (isBinary) {
                    int num = Integer.parseInt(split, 2);
                    BooleanUtils.intToBool(num, state);
                } else
                    BooleanUtils.intToBool(Integer.parseInt(split), state);
        }
    }
    
    @Override
    public void trigger(int clockValue) {
        try {
            LittleSignalOutput out = (LittleSignalOutput) this.children.get(0).getStructure();
            if (clockValue == 1)
                out.updateState(SignalingUtils.mirrorState(state));
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
        // TODO Auto-generated method stub
        
    }
    
}
