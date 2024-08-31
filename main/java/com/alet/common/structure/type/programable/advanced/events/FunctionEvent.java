package com.alet.common.structure.type.programable.advanced.events;

import com.alet.common.structure.type.programable.advanced.Function;

import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class FunctionEvent extends Function {
    
    public FunctionEvent(String name, int id, int color, boolean sender, boolean reciever) {
        super(name, id, color, sender, reciever);
        // TODO Auto-generated constructor stub
    }
    
    @SideOnly(Side.SERVER)
    public abstract void runEvent(WorldServer server);
}
