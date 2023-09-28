package com.alet.common.structure.type.trigger.conditions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.alet.common.util.ComponentSearch;
import com.alet.common.util.SignalingUtils;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
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
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerConditionIsSignal extends LittleTriggerCondition {
    
    public String name = "";
    public boolean[] state;
    public String value = "";
    
    public LittleTriggerConditionIsSignal(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean conditionPassed() {
        try {
            SignalTarget target = SignalTarget.parseTarget(new SignalPatternParser(name), false, false);
            ISignalComponent componet = target.getTarget(this.structure);
            if (componet != null)
                try {
                    if (BooleanUtils.equals(componet.getState(), this.state)) {
                        return true;
                    }
                } catch (CorruptedConnectionException | NotYetConnectedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        this.name = nbt.getString("name");
        this.value = nbt.getString("value");
        this.state = SignalingUtils.stringToBool(value);
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("name", this.name);
        nbt.setString("value", this.value);
        return nbt;
    }
    
    @Override
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        List<GuiSignalComponent> GuiSignalComponent = new ComponentSearch(previews, previews.getStructureType()).search(true,
            true, true);
        //this.outputName = GuiSignalComponent.get(0).totalName;
        List<String> list = new ArrayList<String>();
        int index = 0;
        int i = 0;
        for (GuiSignalComponent o : GuiSignalComponent) {
            if (!o.totalName.equals("allow") && !o.totalName.equals("completed")) {
                if (o.name.equals(name))
                    index = i;
                list.add(o.name);
                i++;
            }
        }
        panel.addControl(new GuiLabel("Is Signal", 0, 0));
        panel.addControl(new GuiLabel("Signal Connections", 0, 20));
        GuiComboBox box = new GuiComboBox("outList", 0, 33, 153, list);
        if (!list.isEmpty()) {
            box.select(index);
            panel.raiseEvent(new GuiControlChangedEvent(box));
        }
        panel.addControl(box);
        panel.addControl(new GuiLabel("If Signal is", 0, 57));
        GuiTextfield valueText = new GuiTextfield("value", value, 0, 70, 153, 14).setNumbersIncludingNegativeOnly();
        panel.addControl(valueText);
    }
    
    @Override
    public void guiChangedEvent(CoreControl source) {
        if (source.is("outList")) {
            GuiComboBox combo = (GuiComboBox) source;
            this.name = combo.getCaption();
        } else if (source.is("value")) {
            GuiTextfield valueText = (GuiTextfield) source;
            if (!valueText.text.equals(""))
                this.value = valueText.text;
        }
    }
    
}
