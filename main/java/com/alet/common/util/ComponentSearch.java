package com.alet.common.util;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.littletiles.client.gui.signal.SubGuiDialogSignal.GuiSignalComponent;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.component.ISignalComponent;
import com.creativemd.littletiles.common.structure.signal.component.SignalComponentType;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

public class ComponentSearch {
    public LittlePreviews previews;
    public LittleStructureType type;
    
    public ComponentSearch(LittlePreviews previews, LittleStructureType type) {
        this.previews = previews;
        this.type = type;
    }
    
    public List<GuiSignalComponent> search(boolean input, boolean output, boolean includeRelations) {
        List<GuiSignalComponent> list = new ArrayList<>();
        if (input)
            gatherInputs(previews, type, "", "", list, includeRelations, true);
        if (output)
            gatherOutputs(previews, type, "", "", list, includeRelations, true);
        return list;
    }
    
    protected void addInput(LittlePreviews previews, LittleStructureType type, String prefix, String totalNamePrefix, List<GuiSignalComponent> list, boolean includeRelations) {
        if (type != null && type.inputs != null)
            for (int i = 0; i < type.inputs.size(); i++)
                list.add(new GuiSignalComponent(prefix + "a" + i, totalNamePrefix, type.inputs.get(i), true, false, i));
        for (int i = 0; i < previews.childrenCount(); i++) {
            LittlePreviews child = previews.getChild(i);
            if (child == this.previews)
                continue;
            LittleStructureType structure = child.getStructureType();
            String name = child.getStructureName();
            if (structure instanceof ISignalComponent && ((ISignalComponent) structure).getType() == SignalComponentType.INPUT)
                list.add(new GuiSignalComponent(prefix + "i" + i, totalNamePrefix + (name != null ? name : "i" + i), (ISignalComponent) structure, true, i));
            else if (includeRelations)
                gatherInputs(child, child.getStructureType(), prefix + "c" + i + ".", totalNamePrefix + (name != null ? name + "." : "c" + i + "."), list, includeRelations, false);
        }
    }
    
    protected void gatherInputs(LittlePreviews previews, LittleStructureType type, String prefix, String totalNamePrefix, List<GuiSignalComponent> list, boolean includeRelations, boolean searchForParent) {
        if (previews == this.previews)
            addInput(previews, type, "", "", list, includeRelations);
        if (searchForParent && previews.hasParent() && includeRelations) {
            gatherInputs(previews.getParent(), previews.getParent().getStructureType(), "p." + prefix, "p." + totalNamePrefix, list, includeRelations, true);
            return;
        }
        if (previews != this.previews)
            addInput(previews, type, prefix, totalNamePrefix, list, includeRelations);
    }
    
    protected void addOutput(LittlePreviews previews, LittleStructureType type, String prefix, String totalNamePrefix, List<GuiSignalComponent> list, boolean includeRelations) {
        if (type != null && type.outputs != null)
            for (int i = 0; i < type.outputs.size(); i++)
                list.add(new GuiSignalComponent(prefix + "b" + i, totalNamePrefix, type.outputs.get(i), false, false, i));
        for (int i = 0; i < previews.childrenCount(); i++) {
            LittlePreviews child = previews.getChild(i);
            if (child == this.previews)
                continue;
            LittleStructureType structure = child.getStructureType();
            String name = child.getStructureName();
            if (structure instanceof ISignalComponent && ((ISignalComponent) structure).getType() == SignalComponentType.OUTPUT)
                list.add(new GuiSignalComponent(prefix + "o" + i, totalNamePrefix + (name != null ? name : "o" + i), (ISignalComponent) structure, true, i));
            else if (includeRelations)
                gatherOutputs(child, child
                        .getStructureType(), prefix + "c" + i + ".", totalNamePrefix + (name != null ? name + "." : "c" + i + "."), list, includeRelations, false);
        }
    }
    
    protected void gatherOutputs(LittlePreviews previews, LittleStructureType type, String prefix, String totalNamePrefix, List<GuiSignalComponent> list, boolean includeRelations, boolean searchForParent) {
        if (previews == this.previews)
            addOutput(previews, type, "", "", list, includeRelations);
        if (searchForParent && previews.hasParent() && includeRelations) {
            gatherOutputs(previews.getParent(), previews.getParent().getStructureType(), "p." + prefix, "p." + totalNamePrefix, list, includeRelations, searchForParent);
            return;
        }
        if (previews != this.previews)
            addOutput(previews, type, prefix, totalNamePrefix, list, includeRelations);
    }
}