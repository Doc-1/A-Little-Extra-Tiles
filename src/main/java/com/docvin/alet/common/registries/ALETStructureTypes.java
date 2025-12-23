package com.docvin.alet.common.registries;

import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.signal.logic.SignalMode;
import com.docvin.alet.components.gui.frames.structure.blueprints.sound.LittleMusicComposerGui;
import com.docvin.alet.components.structures.types.LittleMusicComposer;

public class ALETStructureTypes {
    public static void registerStructureTypes() {

        LittleStructureRegistry.registerStructureType("music_composer", "sound", LittleMusicComposer.class,
                LittleStructureAttribute.TICKING, LittleMusicComposerGui.class).addOutput("play", 1, SignalMode.TOGGLE).addInput(
                "finished", 1);

    }
}
