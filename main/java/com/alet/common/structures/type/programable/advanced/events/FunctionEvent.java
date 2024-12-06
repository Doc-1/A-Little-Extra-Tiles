package com.alet.common.structures.type.programable.advanced.events;

import com.alet.common.structures.type.programable.advanced.Function;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class FunctionEvent extends Function {
    
    public FunctionEvent(String name, int id, int color) {
        super(name, id, color);
        // TODO Auto-generated constructor stub
    }
    
    @SideOnly(Side.SERVER)
    public abstract void runEvent(WorldServer server);
}
