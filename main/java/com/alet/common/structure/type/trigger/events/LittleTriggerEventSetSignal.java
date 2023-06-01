package com.alet.common.structure.type.trigger.events;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.alet.common.util.ComponentSearch;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.client.gui.signal.SubGuiDialogSignal.GuiSignalComponent;
import com.creativemd.littletiles.common.structure.signal.component.ISignalComponent;
import com.creativemd.littletiles.common.structure.signal.logic.SignalPatternParser;
import com.creativemd.littletiles.common.structure.signal.logic.SignalTarget;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.signal.output.SignalExternalOutputHandler;
import com.creativemd.littletiles.common.structure.signal.output.SignalOutputHandler;
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
            SignalOutputHandler handler = null;
            if (componet instanceof InternalSignalOutput) {
                handler = ((InternalSignalOutput) componet).handler;
            } else if (componet instanceof SignalExternalOutputHandler) {
                handler = ((SignalExternalOutputHandler) componet).handler;
            }
            handler.schedule(new boolean[] { false });
            handler.schedule(new boolean[] { true });
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
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
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
        GuiComboBox box = new GuiComboBox("outList", 0, 0, 100, list);
        if (!list.isEmpty())
            box.select(index);
        panel.addControl(box);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source.is("outList")) {
            GuiComboBox combo = (GuiComboBox) source;
            this.outputName = combo.getCaption();
        }
    }
    
}
