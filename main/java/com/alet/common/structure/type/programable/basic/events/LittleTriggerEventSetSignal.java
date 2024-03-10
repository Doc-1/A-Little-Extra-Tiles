package com.alet.common.structure.type.programable.basic.events;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.programable.basic.LittleTriggerObject;
import com.alet.common.utils.ComponentSearch;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.client.gui.signal.SubGuiDialogSignal.GuiSignalComponent;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.signal.component.ISignalComponent;
import com.creativemd.littletiles.common.structure.signal.logic.SignalPatternParser;
import com.creativemd.littletiles.common.structure.signal.logic.SignalTarget;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.signal.output.SignalExternalOutputHandler;
import com.creativemd.littletiles.common.structure.signal.output.SignalOutputHandler;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
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
            ISignalComponent componet = target.getTarget(this.structure);
            SignalOutputHandler handler = null;
            if (componet instanceof InternalSignalOutput)
                handler = ((InternalSignalOutput) componet).handler;
            else if (componet instanceof SignalExternalOutputHandler)
                handler = ((SignalExternalOutputHandler) componet).handler;
            else if (componet instanceof LittleSignalOutput) {
                LittleSignalOutput o = (LittleSignalOutput) componet;
                if (o.getParent() != null) {
                    SignalExternalOutputHandler x = o.getParent().getStructure().getExternalOutputHandler(o
                            .getParent().childId);
                    if (x != null)
                        handler = x.handler;
                }
                if (handler == null) {
                    boolean[] arr = new boolean[o.getBandwidth()];
                    BooleanUtils.intToBool(outputValue, arr);
                    o.updateState(arr);
                }
            }
            
            if (handler != null)
                try {
                    boolean[] arr = new boolean[handler.getBandwidth()];
                    BooleanUtils.intToBool(outputValue, arr);
                    handler.schedule(arr);
                } catch (CorruptedConnectionException | NotYetConnectedException e) {
                    e.printStackTrace();
                }
        } catch (ParseException | CorruptedConnectionException | NotYetConnectedException e) {
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
        List<GuiSignalComponent> GuiSignalComponent = new ComponentSearch(previews, previews.getStructureType()).search(
            false, true, true);
        //this.outputName = GuiSignalComponent.get(0).totalName;
        List<String> list = new ArrayList<String>();
        List<String> title = new ArrayList<String>();
        int index = 0;
        int i = 0;
        for (GuiSignalComponent o : GuiSignalComponent) {
            if (!o.totalName.equals("allow") && !o.totalName.equals("completed")) {
                if (o.totalName.equals(outputName))
                    index = i;
                title.add(o.display());
                list.add(o.name);
                i++;
            }
        }
        
        GuiTitledComboBox box = new GuiTitledComboBox("outList", 0, 0, 153, title, list);
        if (!list.isEmpty()) {
            box.select(index);
            panel.raiseEvent(new GuiControlChangedEvent(box));
        }
        panel.addControl(box);
        
        GuiTextfield text = new GuiTextfield("value", outputValue + "", 0, 23, 50, 8).setNumbersOnly();
        panel.addControl(text);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source.is("outList")) {
            GuiTitledComboBox combo = (GuiTitledComboBox) source;
            this.outputName = combo.getSelected();
        }
        if (source.is("value")) {
            GuiTextfield text = (GuiTextfield) source;
            if (!text.text.isEmpty())
                outputValue = Integer.parseInt(text.text);
        }
    }
    
    protected class GuiTitledComboBox extends GuiComboBox {
        
        List<String> titles = new ArrayList<>();
        
        public GuiTitledComboBox(String name, int x, int y, int width, List<String> titles, List<String> lines) {
            super(name, x, y, width, titles);
            this.titles = lines;
        }
        
        public String getSelected() {
            return titles.get(index);
        }
    }
    
}
