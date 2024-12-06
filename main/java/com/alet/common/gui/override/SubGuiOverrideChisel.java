package com.alet.common.gui.override;

import org.lwjgl.util.Color;

import com.alet.common.gui.controls.GuiColorPickerAlet;
import com.alet.common.gui.controls.tutorial.GuiTutorialBox;
import com.alet.common.gui.controls.tutorial.TutorialData;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.SubGuiChisel;
import com.creativemd.littletiles.common.item.ItemLittleChisel;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;

public class SubGuiOverrideChisel extends SubGuiOverride {
    
    public SubGuiOverrideChisel(boolean shouldUpdate) {
        super(shouldUpdate);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void modifyControls(SubGui gui) {
        SubGuiChisel chiselGui = (SubGuiChisel) gui;
        chiselGui.controls.remove(chiselGui.get("picker"));
        LittlePreview preview = ItemLittleChisel.getPreview(chiselGui.stack);
        
        Color color = ColorUtils.IntToRGBA(preview.getColor());
        chiselGui.controls.add(1, new GuiColorPickerAlet("picker", 2, 2, color, LittleTiles.CONFIG.isTransparencyEnabled(chiselGui.getPlayer()), LittleTiles.CONFIG
                .getMinimumTransparency(chiselGui.getPlayer())));
        GuiColorPickerAlet picker = (GuiColorPickerAlet) chiselGui.get("picker");
        GuiTutorialBox box = new GuiTutorialBox("box", 0, 0, 180, gui.width, gui.height);
        box.tutorialMap.add(new TutorialData(chiselGui.get("picker"), "leftout", "This is the color picker. With it you can set the color your chisel will place in."));
        box.tutorialMap.add(new TutorialData(chiselGui
                .get("picker"), "leftout", "Each color slider can go from 0 to 255. To change the value:\n-You can use the arrows on either side of the sliders.\n-You can click and drag on a slider. -You can right click a slider and enter a value manualy."));
        box.tutorialMap.add(new TutorialData(picker.get("r"), "leftout", "This is the red color slider."));
        box.tutorialMap.add(new TutorialData(picker.get("g"), "leftout", "This is the green color slider."));
        box.tutorialMap.add(new TutorialData(picker.get("b"), "leftout", "This is the blue color slider."));
        box.tutorialMap.add(new TutorialData(picker.get("a"), "leftout", "This is the alpha slider. How transparent the color is."));
        box.tutorialMap.add(new TutorialData(picker.get("s"), "leftout", "This is the shader slider. It allows you to easly change how dark or light a color is."));
        box.tutorialMap.add(new TutorialData(picker
                .get("more"), "leftout", "Click on this to open the color palette. It allows you to save your currently selected color for later use."));
        box.tutorialMap.add(new TutorialData(chiselGui
                .get("preview"), "leftout", "This is the material drop down menu. With it you can select the block type that the chisel will place tiles as."));
        box.tutorialMap.add(new TutorialData(chiselGui
                .get("shape"), "leftout", "This is the draw shape drop down menu. With it you can select the shape that the chisel will place tiles in."));
        
        chiselGui.controls.add(0, box);
        chiselGui.refreshControls();
    }
    
    @Override
    public void updateControls(SubGui gui) {
        // TODO Auto-generated method stub
        
    }
    
}
