package com.alet.common.gui.structure;

import com.alet.components.structures.type.LittleAlwaysOnLight;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiSteppedSlider;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleAlwaysOnLightGui extends LittleStructureGuiParser {
    
    public LittleAlwaysOnLightGui(GuiParent parent, AnimationGuiHandler handler) {
        super(parent, handler);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createControls(LittlePreviews previews, LittleStructure structure) {
        
        parent.addControl(
            new GuiSteppedSlider("level", 0, 0, 100, 12, structure instanceof LittleAlwaysOnLight ? ((LittleAlwaysOnLight) structure).level : 15, 0, 15));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public LittleAlwaysOnLight parseStructure(LittlePreviews previews) {
        LittleAlwaysOnLight structure = createStructure(LittleAlwaysOnLight.class, null);
        GuiSteppedSlider slider = (GuiSteppedSlider) parent.get("level");
        structure.level = (int) slider.value;
        return structure;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    protected LittleStructureType getStructureType() {
        return LittleStructureRegistry.getStructureType(LittleAlwaysOnLight.class);
    }
}