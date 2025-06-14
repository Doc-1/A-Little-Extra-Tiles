package com.alet.common.gui.structure;

import com.alet.common.gui.controls.GuiConnectedCheckBoxes;
import com.alet.components.structures.type.LittleRemoteActivator;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.util.math.BlockPos;

public class LittleRemoteActivatorGui extends LittleStructureGuiParser {
    
    public LittleRemoteActivatorGui(GuiParent parent, AnimationGuiHandler handler) {
        super(parent, handler);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void createControls(LittlePreviews previews, LittleStructure structure) {
        LittleRemoteActivator guiLinker = structure instanceof LittleRemoteActivator ? (LittleRemoteActivator) structure : null;
        parent.addControl(new GuiLabel("Pos X:", 0, 0));
        parent.addControl(new GuiLabel("Pos Y:", 0, 18));
        parent.addControl(new GuiLabel("Pos Z:", 0, 36));
        GuiTextfield x = new GuiTextfield("x", "0", 35, 0, 25, 10);
        GuiTextfield y = new GuiTextfield("y", "0", 35, 18, 25, 10);
        GuiTextfield z = new GuiTextfield("z", "0", 35, 36, 25, 10);
        if (guiLinker != null) {
            x.text = guiLinker.linkedBlock.getX() + "";
            y.text = guiLinker.linkedBlock.getY() + "";
            z.text = guiLinker.linkedBlock.getZ() + "";
        }
        parent.addControl(x);
        parent.addControl(y);
        parent.addControl(z);
        GuiConnectedCheckBoxes boxes = new GuiConnectedCheckBoxes("abs", 0, 55);
        boxes.addCheckBox("absolute", "Use Absolute Coordinates");
        boxes.addCheckBox("relative", "Use Relative Coordinates");
        if (guiLinker != null)
            boxes.setSelected(guiLinker.useAbsolutePos ? "absolute" : "relative");
        //GuiCheckBox box = new GuiCheckBox("absolute", "Use Absolute Coordinates", 0, 55, guiLinker != null ? guiLinker.useAbsolutePos : true);
        parent.addControl(boxes);
    }
    
    @Override
    protected LittleStructure parseStructure(LittlePreviews previews) {
        LittleRemoteActivator structure = createStructure(LittleRemoteActivator.class, null);
        GuiTextfield x = (GuiTextfield) parent.get("x");
        GuiTextfield y = (GuiTextfield) parent.get("y");
        GuiTextfield z = (GuiTextfield) parent.get("z");
        structure.linkedBlock = new BlockPos(Integer.parseInt(x.text), Integer.parseInt(y.text), Integer.parseInt(z.text));
        structure.useAbsolutePos = ((GuiCheckBox) parent.get("absolute")).value;
        return structure;
    }
    
    @Override
    protected LittleStructureType getStructureType() {
        return LittleStructureRegistry.getStructureType(LittleRemoteActivator.class);
    }
    
}