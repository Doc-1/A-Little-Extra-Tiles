package com.alet.components.structures.type.programable.basic.conditions;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.alet.common.utils.ComponentSearch;
import com.alet.components.structures.type.programable.basic.LittleTriggerObject;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerConditionIsSignal extends LittleTriggerCondition {
    
    public String name = "";
    public int value = 0;
    
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
                    boolean[] arr = new boolean[componet.getBandwidth()];
                    BooleanUtils.intToBool(value, arr);
                    return BooleanUtils.equals(componet.getState(), arr);
                } catch (CorruptedConnectionException | NotYetConnectedException e) {
                    return false;
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        this.name = nbt.getString("name");
        this.value = nbt.getInteger("value");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("name", this.name);
        nbt.setInteger("value", this.value);
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        List<GuiSignalComponent> GuiSignalComponent = new ComponentSearch(previews, previews.getStructureType()).search(true,
            true, true);
        //this.outputName = GuiSignalComponent.get(0).totalName;
        List<String> list = new ArrayList<String>();
        List<String> title = new ArrayList<String>();
        int index = 0;
        int i = 0;
        for (GuiSignalComponent o : GuiSignalComponent) {
            if (!o.totalName.equals("allow") && !o.totalName.equals("completed") && !o.totalName.equals("activate")) {
                if (o.totalName.equals(name))
                    index = i;
                title.add(o.display());
                list.add(o.name);
                i++;
            }
        }
        panel.addControl(new GuiLabel("Is Signal Equal", 0, 0));
        panel.addControl(new GuiLabel("Signal Connections", 0, 20));
        GuiTitledComboBox box = new GuiTitledComboBox("outList", 0, 33, 153, title, list);
        if (!list.isEmpty()) {
            box.select(index);
            panel.raiseEvent(new GuiControlChangedEvent(box));
        }
        
        panel.addControl(box);
        panel.addControl(new GuiLabel("If Signal is equal to", 0, 57));
        GuiTextfield valueText = new GuiTextfield("value", value + "", 0, 70, 153, 14).setNumbersOnly();
        valueText.setCustomTooltip("Enter value as a decimal");
        panel.addControl(valueText);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source.is("outList")) {
            GuiTitledComboBox combo = (GuiTitledComboBox) source;
            this.name = combo.getSelected();
        } else if (source.is("value")) {
            GuiTextfield valueText = (GuiTextfield) source;
            if (!valueText.text.equals(""))
                this.value = Integer.parseInt(valueText.text);
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
