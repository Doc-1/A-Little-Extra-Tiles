package com.alet.common.structures.type;

import net.minecraft.block.state.IBlockState;

public class MutatorData {
    public IBlockState state;
    public int color;
    public boolean colision;
    
    public MutatorData() {
        
    }
    
    public MutatorData(IBlockState state, int color, boolean colision) {
        this.state = state;
        this.color = color;
        this.colision = colision;
    }
    
    public MutatorData copy() {
        return new MutatorData(this.state, this.color, this.colision);
    }
}