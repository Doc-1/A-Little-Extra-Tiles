package com.alet.client.gui.controls.programmer;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.littletiles.common.structure.LittleStructure;

import net.minecraft.nbt.NBTTagCompound;

public class BlueprintCompiler {
    
    private static NBTTagCompound script;
    public static LittleStructure structure;
    public static List<IFunction> functions = new ArrayList<IFunction>();
    
    public static void setStructure(LittleStructure structure) {
        BlueprintCompiler.structure = structure;
    }
    
    public static void setScript(NBTTagCompound script) {
        BlueprintCompiler.script = script;
    }
    
    public static void readScript() {
        
    }
}
