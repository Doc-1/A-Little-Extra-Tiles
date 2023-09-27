package com.alet.common.structure.type.programable.functions.activators;

import com.alet.common.structure.type.programable.nodes.values.NodeInteger;

import net.minecraft.nbt.NBTTagCompound;

public class FunctionOnRightClick extends FunctionActivator {
    
    public FunctionOnRightClick() {
        super("on_right_click", ACTIVATOR_COLOR, true, false);
        
    }
    
    @Override
    public void setValues() {
        
    }
    
    @Override
    public NBTTagCompound createNBT() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setFunctionNodes() {
        this.senderNodes.add(new NodeInteger("int_out", "int", true, false, true));
    }
    
}
