package com.alet.client.gui.override;

import java.util.HashMap;
import java.util.Map.Entry;

import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxCategory;
import com.creativemd.littletiles.client.gui.SubGuiRecipe;

public class SubGuiOverrideRecipe extends SubGuiOverride {
    
    private String oldName = "";
    private HashMap<Integer, GuiControl> tempDeletedControls = new HashMap<Integer, GuiControl>();
    
    public SubGuiOverrideRecipe(boolean shouldUpdate) {
        super(shouldUpdate);
    }
    
    @Override
    public void onClose(SubGui gui) {
        tempDeletedControls.clear();
        System.out.println("closed");
    }
    
    @Override
    public void modifyControls(SubGui gui) {
        SubGuiRecipe recipeGui = (SubGuiRecipe) gui;
        recipeGui.controls.remove(recipeGui.get("renderer"));
        GuiComboBoxCategory typesComboBox = (GuiComboBoxCategory) recipeGui.get("types");
        String name = (String) typesComboBox.getSelected().key;
        GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 208, 30, 136, 135);
        recipeGui.controls.add(4, viewer);
        
        try {
            viewer.onLoaded(recipeGui.animationPreview);
        } catch (NullPointerException e) {}
        
        recipeGui.refreshControls();
        if (name.equals("structure.music_composer.name")) {
            viewer.moveViewPort(-25, 32);
            viewer.posX = 258;
        } else if (name.equals("structure.door_lock.name")) {
            viewer.moveViewPort(-25, 0);
            viewer.posX = 258;
        } else if (name.equals("structure.state_activator.name")) {
            viewer.moveViewPort(-50, 0);
            viewer.posX = 308;
        } else if (name.equals("structure.trigger_box.name")) {
            viewer.posX = 308;
            viewer.moveViewPort(-50, 30);
        } else {
            reload(recipeGui);
        }
    }
    
    @Override
    public void updateControls(SubGui gui) {
        SubGuiRecipe recipeGui = (SubGuiRecipe) gui;
        GuiComboBoxCategory typesComboBox = (GuiComboBoxCategory) recipeGui.get("types");
        String name = (String) typesComboBox.getSelected().key;
        int index = typesComboBox.index;
        if (!name.equals(oldName))
            hasUpdated = false;
        if (!hasUpdated) {
            recipeGui.get("types").width = 115;
            recipeGui.get("hierarchy").posX = 120;
            recipeGui.refreshControls();
            if (name.equals("structure.adv_trigger_box.name")) {
                recipeGui.height = 270;
                recipeGui.width = 580;
                recipeGui.get("panel").width = 574;
                recipeGui.get("panel").height = 211;
                
                temporaryRemoval(recipeGui, recipeGui.get("play"), recipeGui.get("pause"), recipeGui.get("stop"), recipeGui.get("tilescount"), recipeGui.get("renderer"));
                
                recipeGui.get("save").posY = 243;
                recipeGui.get("name").posY = 243;
                recipeGui.get("clear").posY = 243;
                
            } else if (name.equals("structure.music_composer.name")) {
                recipeGui.height = 270;
                recipeGui.width = 406;
                recipeGui.get("tilescount").posX = 258;
                recipeGui.get("pause").posX = 298;
                recipeGui.get("play").posX = 318;
                recipeGui.get("stop").posX = 338;
                recipeGui.get("panel").width = 256;
                recipeGui.get("panel").height = 211;
                
                recipeGui.get("save").posY = 243;
                recipeGui.get("name").posY = 243;
                recipeGui.get("clear").posY = 243;
                typesComboBox.select(index);
                
            } else if (name.equals("structure.door_lock.name")) {
                recipeGui.width = 406;
                recipeGui.get("tilescount").posX = 258;
                recipeGui.get("pause").posX = 298;
                recipeGui.get("play").posX = 318;
                recipeGui.get("stop").posX = 338;
                recipeGui.get("panel").width = 256;
                
                typesComboBox.select(index);
            } else if (name.equals("structure.state_activator.name")) {
                recipeGui.width = 456;
                recipeGui.get("tilescount").posX = 308;
                recipeGui.get("pause").posX = 348;
                recipeGui.get("play").posX = 368;
                recipeGui.get("stop").posX = 388;
                recipeGui.get("panel").width = 306;
                typesComboBox.select(index);
            } else if (name.equals("structure.trigger_box.name")) {
                recipeGui.height = 300;
                recipeGui.width = 456;
                recipeGui.get("tilescount").posX = 308;
                recipeGui.get("pause").posX = 348;
                recipeGui.get("play").posX = 368;
                recipeGui.get("stop").posX = 388;
                recipeGui.get("panel").width = 306;
                recipeGui.get("panel").height = 241;
                
                recipeGui.get("save").posY = 273;
                recipeGui.get("name").posY = 273;
                recipeGui.get("clear").posY = 273;
                
                typesComboBox.select(index);
                
            } else
                reload(recipeGui);
            
            hasUpdated = true;
            recipeGui.refreshControls();
            oldName = new String(name);
        }
    }
    
    private void temporaryRemoval(SubGuiRecipe recipeGui, GuiControl... controls) {
        boolean flag = false;
        for (int i = 0; i < recipeGui.controls.size(); i++) {
            GuiControl c = recipeGui.controls.get(i);
            System.out.println(c);
            for (GuiControl control : controls) {
                if (c.equals(control)) {
                    flag = true;
                    this.tempDeletedControls.put(i, control);
                    break;
                }
            }
        }
        if (flag)
            for (GuiControl control : controls)
                recipeGui.controls.remove(control);
        System.out.println(this.tempDeletedControls);
    }
    
    private void reload(SubGuiRecipe recipeGui) {
        for (Entry<Integer, GuiControl> set : this.tempDeletedControls.entrySet()) {
            recipeGui.controls.add(set.getKey(), set.getValue());
            System.out.println(set.getValue());
        }
        this.tempDeletedControls.clear();
        System.out.println(recipeGui.controls);
        recipeGui.width = 356;
        recipeGui.height = 206;
        recipeGui.get("tilescount").posX = 208;
        recipeGui.get("panel").width = 206;
        recipeGui.get("panel").height = 141;
        recipeGui.get("play").posX = 268;
        recipeGui.get("pause").posX = 248;
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
