package com.alet.regestries;

import com.alet.components.structures.type.LittleAlwaysOnLight;
import com.alet.components.structures.type.LittleAlwaysOnLight.LittleAlwaysOnLightStructureParser;
import com.alet.components.structures.type.LittleCamPlayerALET;
import com.alet.components.structures.type.LittleLockALET;
import com.alet.components.structures.type.LittleMusicComposerALET;
import com.alet.components.structures.type.LittleRemoteActivatorALET;
import com.alet.components.structures.type.LittleRopeConnectionALET;
import com.alet.components.structures.type.LittleStateMutatorALET;
import com.alet.components.structures.type.programable.advanced.LittleProgramableStructureALET;
import com.alet.components.structures.type.programable.basic.LittleTriggerBoxStructureALET;
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
            LittleProgramableStructureALET.class,
            LittleStructureAttribute.NOCOLLISION | LittleStructureAttribute.COLLISION_LISTENER | LittleStructureAttribute.TICKING,
            LittleProgramableStructureALET.LittleProgramableStructureParser.class).addOutput("allow", 1, SignalMode.EQUAL)
                .addInput("completed", 1).preventImportInSurvival();
        LittleStructureRegistry.registerStructureType("always_on_light", "simple", LittleAlwaysOnLight.class,
            LittleStructureAttribute.LIGHT_EMITTER, LittleAlwaysOnLightStructureParser.class).addIngredient(
                new StructureIngredientScalerVolume(8), () -> new StackIngredient(new ItemStack(Items.GLOWSTONE_DUST)));
        LittleStructureRegistry.registerStructureType("trigger_box", "advance", LittleTriggerBoxStructureALET.class,
            LittleStructureAttribute.NOCOLLISION | LittleStructureAttribute.COLLISION_LISTENER | LittleStructureAttribute.TICKING,
            LittleTriggerBoxStructureALET.LittleTriggerBoxStructureParser.class).addOutput("allow", 1, SignalMode.EQUAL)
                .addOutput("activate", 1, SignalMode.EQUAL).addInput("completed", 1).preventImportInSurvival();
        LittleStructureRegistry.registerStructureType("door_lock", "door", LittleLockALET.class,
            LittleStructureAttribute.NONE, LittleLockALET.LittleLockParserALET.class).addOutput("lock", 1, SignalMode.TOGGLE,
                true);
        
        LittleStructureRegistry.registerStructureType("state_activator", "advance", LittleStateMutatorALET.class,
            LittleStructureAttribute.NONE, LittleStateMutatorALET.LittleStateMutatorParserALET.class).addOutput("activate",
                1, SignalMode.TOGGLE, true).preventImportInSurvival();
        
        LittleStructureRegistry.registerStructureType("music_composer", "sound", LittleMusicComposerALET.class,
            LittleStructureAttribute.TICKING, LittleMusicComposerALET.LittleMusicComposerParserALET.class).addOutput("play",
                1, SignalMode.TOGGLE).addInput("finished", 1);
        LittleStructureRegistry.registerStructureType("lead_connection", "simple", LittleRopeConnectionALET.class,
            LittleStructureAttribute.TICK_RENDERING, LittleRopeConnectionALET.LittleLeadConnectionParserALET.class);
        LittleStructureRegistry.registerStructureType("remote_activator", "advance", LittleRemoteActivatorALET.class,
            LittleStructureAttribute.NONE, LittleRemoteActivatorALET.LittleRemoteActivatorParserALET.class);
        
        if (Loader.isModLoaded("cmdcam"))
            LittleStructureRegistry.registerStructureType("cam_player", "advance", LittleCamPlayerALET.class,
                LittleStructureAttribute.TICKING, LittleCamPlayerALET.LittleCamPlayerParserALET.class).addOutput("play", 1,
                    SignalMode.TOGGLE).preventImportInSurvival();
        
    }
}
