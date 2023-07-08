package com.alet.client.gui.override;

import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxCategory;
import com.creativemd.littletiles.client.gui.SubGuiRecipe;

public class SubGuiOverrideRecipe extends SubGuiOverride {
    
    private String oldName = "";
    
    public SubGuiOverrideRecipe(boolean shouldUpdate) {
        super(shouldUpdate);
    }
    
    @Override
    public void modifyControls(SubGui gui) {}
    
    @Override
    public void updateControls(SubGui gui) {
        SubGuiRecipe recipeGui = (SubGuiRecipe) gui;
        GuiComboBoxCategory typesComboBox = (GuiComboBoxCategory) recipeGui.get("types");
        String name = (String) typesComboBox.getSelected().key;
        int index = typesComboBox.index;
        if (!name.equals(oldName)) {
            hasUpdated = false;
            reload(gui, recipeGui);
        }
        if (!hasUpdated) {
            recipeGui.get("types").width = 115;
            recipeGui.get("hierarchy").posX = 120;
            recipeGui.controls.remove(recipeGui.get("renderer"));
            GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 208, 30, 136, 135);
            recipeGui.controls.add(4, viewer);
            try {
                viewer.onLoaded(recipeGui.animationPreview);
                
            } catch (NullPointerException e) {}
            recipeGui.refreshControls();
            if (name.equals("structure.adv_trigger_box.name")) {
                
                gui.height = 270;
                gui.width = 580;
                recipeGui.controls.remove(recipeGui.get("play"));
                recipeGui.controls.remove(recipeGui.get("pause"));
                recipeGui.controls.remove(recipeGui.get("stop"));
                recipeGui.controls.remove(recipeGui.get("tilescount"));
                recipeGui.controls.remove(recipeGui.get("renderer"));
                recipeGui.get("panel").width = 574;
                recipeGui.get("panel").height = 211;
                
                recipeGui.get("save").posY = 243;
                recipeGui.get("name").posY = 243;
                recipeGui.get("clear").posY = 243;
                
                recipeGui.refreshControls();
                hasUpdated = true;
            } else if (name.equals("structure.music_composer.name")) {
                gui.height = 270;
                gui.width = 406;
                recipeGui.get("tilescount").posX = 258;
                recipeGui.get("pause").posX = 298;
                recipeGui.get("play").posX = 318;
                recipeGui.get("stop").posX = 338;
                recipeGui.get("panel").width = 256;
                recipeGui.get("panel").height = 211;
                
                recipeGui.get("save").posY = 243;
                recipeGui.get("name").posY = 243;
                recipeGui.get("clear").posY = 243;
                viewer.moveViewPort(-25, 32);
                viewer.posX = 258;
                
                recipeGui.refreshControls();
                typesComboBox.select(index);
                hasUpdated = true;
                
            } else if (name.equals("structure.door_lock.name")) {
                gui.width = 406;
                recipeGui.get("tilescount").posX = 258;
                recipeGui.get("pause").posX = 298;
                recipeGui.get("play").posX = 318;
                recipeGui.get("stop").posX = 338;
                recipeGui.get("panel").width = 256;
                
                viewer.moveViewPort(-25, 0);
                viewer.posX = 258;
                
                recipeGui.refreshControls();
                typesComboBox.select(index);
                hasUpdated = true;
                
            } else if (name.equals("structure.state_activator.name")) {
                gui.width = 456;
                recipeGui.get("tilescount").posX = 308;
                recipeGui.get("pause").posX = 348;
                recipeGui.get("play").posX = 368;
                recipeGui.get("stop").posX = 388;
                recipeGui.get("panel").width = 306;
                viewer.moveViewPort(-50, 0);
                viewer.posX = 308;
                recipeGui.refreshControls();
                typesComboBox.select(index);
                hasUpdated = true;
            } else if (name.equals("structure.trigger_box.name")) {
                gui.height = 300;
                gui.width = 456;
                recipeGui.get("tilescount").posX = 308;
                recipeGui.get("pause").posX = 348;
                recipeGui.get("play").posX = 368;
                recipeGui.get("stop").posX = 388;
                recipeGui.get("panel").width = 306;
                recipeGui.get("panel").height = 241;
                
                recipeGui.get("save").posY = 273;
                recipeGui.get("name").posY = 273;
                recipeGui.get("clear").posY = 273;
                viewer.posX = 308;
                viewer.moveViewPort(-50, 30);
                recipeGui.refreshControls();
                
                typesComboBox.select(index);
                hasUpdated = true;
                
            } else {
                reload(gui, recipeGui);
                hasUpdated = true;
            }
            oldName = new String(name);
        }
    }
    
    public void reload(SubGui gui, SubGuiRecipe recipeGui) {
        
        gui.width = 356;
        gui.height = 206;
        recipeGui.get("tilescount").posX = 208;
        recipeGui.get("panel").width = 206;
        recipeGui.get("panel").height = 141;
        recipeGui.get("pause").posX = 248;
        recipeGui.get("play").posX = 268;
        recipeGui.get("stop").posX = 288;
        
        recipeGui.get("save").posY = 176;
        recipeGui.get("name").posY = 176;
        recipeGui.get("clear").posY = 176;
        recipeGui.get("save").posX = 150;
        recipeGui.get("name").posX = 2;
        recipeGui.get("clear").posX = 105;
        
        if (recipeGui.get("renderer") instanceof GuiAnimationViewerAlet) {
            GuiAnimationViewerAlet viewer = (GuiAnimationViewerAlet) recipeGui.get("renderer");
            viewer.posX = 208;
            viewer.moveViewPort(0, 0);
        }
    }
    
}
