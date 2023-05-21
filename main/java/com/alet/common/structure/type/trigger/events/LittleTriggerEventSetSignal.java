package com.alet.common.structure.type.trigger.events;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.client.gui.signal.SubGuiDialogSignal.GuiSignalComponent;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.component.ISignalComponent;
import com.creativemd.littletiles.common.structure.signal.component.SignalComponentType;
import com.creativemd.littletiles.common.structure.signal.logic.SignalPatternParser;
import com.creativemd.littletiles.common.structure.signal.logic.SignalTarget;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerEventSetSignal extends LittleTriggerEvent {
    public String outputName = "";
    public int outputValue = 0;
    
    public LittleTriggerEventSetSignal(int id) {
        super(id);
    }
    
    @Override
    public boolean runEvent() {
        try {
            SignalTarget target = SignalTarget.parseTarget(new SignalPatternParser(outputName), true, false);
            ISignalComponent componet = (ISignalComponent) target.getTarget(this.structure);
            
            try {
                if (BooleanUtils.any(componet.getState())) {
                    componet.updateState(new boolean[] { false });
                } else {
                    componet.updateState(new boolean[] { true });
                }
            } catch (CorruptedConnectionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        this.outputName = nbt.getString("outputName");
        this.outputValue = nbt.getInteger("outputValue");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("outputName", outputName);
        nbt.setInteger("outputValue", outputValue);
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiParent parent, LittlePreviews previews) {
        List<GuiSignalComponent> GuiSignalComponent = new ComponentSearch(previews, previews.getStructureType()).search(false, true, true);
        //this.outputName = GuiSignalComponent.get(0).totalName;
        List<String> list = new ArrayList<String>();
        int index = 0;
        int i = 0;
        for (GuiSignalComponent o : GuiSignalComponent) {
            if (!o.totalName.equals("allow") && !o.totalName.equals("completed")) {
                if (o.totalName.equals(outputName))
                    index = i;
                list.add(o.name);
                i++;
            }
        }
        GuiPanel panel = this.getPanel(parent);
        GuiComboBox box = new GuiComboBox("outList", 0, 0, 100, list);
        if (!list.isEmpty())
            box.select(index);
        panel.addControl(box);
    }
    
    @Override
    public void guiChangedEvent(CoreControl source) {
        if (source.is("outList")) {
            GuiComboBox combo = (GuiComboBox) source;
            this.outputName = combo.getCaption();
        }
    }
    
    private static class ComponentSearch {
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
                    gatherInputs(child, child
                            .getStructureType(), prefix + "c" + i + ".", totalNamePrefix + (name != null ? name + "." : "c" + i + "."), list, includeRelations, false);
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
}
