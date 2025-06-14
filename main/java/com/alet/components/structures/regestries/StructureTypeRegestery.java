package com.alet.components.structures.regestries;

import com.alet.common.gui.structure.LittleAlwaysOnLightGui;
import com.alet.common.gui.structure.LittleCamPlayerGui;
import com.alet.common.gui.structure.LittleLockGui;
import com.alet.common.gui.structure.LittleRemoteActivatorGui;
import com.alet.common.gui.structure.LittleRopeConnectionGui;
import com.alet.common.gui.structure.LittleStateMutatorGui;
import com.alet.common.gui.structure.programable.LittleAdvancedProgramableStructureGui;
import com.alet.common.gui.structure.programable.LittleProgramableStructureGui;
import com.alet.common.gui.structure.sound.LittleMusicComposerGui;
import com.alet.components.structures.type.LittleAlwaysOnLight;
import com.alet.components.structures.type.LittleCamPlayer;
import com.alet.components.structures.type.LittleLock;
import com.alet.components.structures.type.LittleMusicComposer;
import com.alet.components.structures.type.LittleRemoteActivator;
import com.alet.components.structures.type.LittleRopeConnection;
import com.alet.components.structures.type.LittleStateMutator;
import com.alet.components.structures.type.programable.advanced.LittleAdvancedProgramableStructure;
import com.alet.components.structures.type.programable.basic.LittleProgramableStructure;
import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.StructureIngredientRule.StructureIngredientScalerVolume;
import com.creativemd.littletiles.common.structure.signal.logic.SignalMode;
import com.creativemd.littletiles.common.util.ingredient.StackIngredient;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class StructureTypeRegestery {
    public static void registerStructureTypes() {
        LittleStructureRegistry.registerStructureType("programable_structure", "advance",
            LittleAdvancedProgramableStructure.class,
            LittleStructureAttribute.NOCOLLISION | LittleStructureAttribute.COLLISION_LISTENER | LittleStructureAttribute.TICKING,
            LittleAdvancedProgramableStructureGui.class).addOutput("allow", 1, SignalMode.EQUAL).addInput("completed", 1)
                .preventImportInSurvival();
        
        LittleStructureRegistry.registerStructureType("always_on_light", "simple", LittleAlwaysOnLight.class,
            LittleStructureAttribute.LIGHT_EMITTER, LittleAlwaysOnLightGui.class).addIngredient(
                new StructureIngredientScalerVolume(8), () -> new StackIngredient(new ItemStack(Items.GLOWSTONE_DUST)));
        
        LittleStructureRegistry.registerStructureType("trigger_box", "advance", LittleProgramableStructure.class,
            LittleStructureAttribute.NOCOLLISION | LittleStructureAttribute.COLLISION_LISTENER | LittleStructureAttribute.TICKING,
            LittleProgramableStructureGui.class).addOutput("allow", 1, SignalMode.EQUAL).addOutput("activate", 1,
                SignalMode.EQUAL).addInput("completed", 1).preventImportInSurvival();
        
        LittleStructureRegistry.registerStructureType("door_lock", "door", LittleLock.class, LittleStructureAttribute.NONE,
            LittleLockGui.class).addOutput("lock", 1, SignalMode.TOGGLE, true);
        
        LittleStructureRegistry.registerStructureType("state_activator", "advance", LittleStateMutator.class,
            LittleStructureAttribute.NONE, LittleStateMutatorGui.class).addOutput("activate", 1, SignalMode.TOGGLE, true)
                .preventImportInSurvival();
        
        LittleStructureRegistry.registerStructureType("music_composer", "sound", LittleMusicComposer.class,
            LittleStructureAttribute.TICKING, LittleMusicComposerGui.class).addOutput("play", 1, SignalMode.TOGGLE).addInput(
                "finished", 1);
        
        LittleStructureRegistry.registerStructureType("lead_connection", "simple", LittleRopeConnection.class,
            LittleStructureAttribute.TICK_RENDERING, LittleRopeConnectionGui.class);
        
        LittleStructureRegistry.registerStructureType("remote_activator", "advance", LittleRemoteActivator.class,
            LittleStructureAttribute.NONE, LittleRemoteActivatorGui.class);
        
        if (Loader.isModLoaded("cmdcam"))
            LittleStructureRegistry.registerStructureType("cam_player", "advance", LittleCamPlayer.class,
                LittleStructureAttribute.TICKING, LittleCamPlayerGui.class).addOutput("play", 1, SignalMode.TOGGLE)
                    .preventImportInSurvival();
        
    }
}
