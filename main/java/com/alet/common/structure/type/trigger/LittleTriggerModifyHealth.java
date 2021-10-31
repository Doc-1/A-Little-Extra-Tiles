package com.alet.common.structure.type.trigger;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class LittleTriggerModifyHealth extends LittleTriggerEvent {
	
	public float damageAmount = 0;
	public float healAmount = 0;
	public String damageType = "";
	public int effectPerTick = 0;
	public boolean heal = false;
	public boolean harm = false;
	
	public LittleTriggerModifyHealth(String id) {
		super(id);
		name = "Modify Health";
	}
	
	public LittleTriggerModifyHealth(String id, float damageAmount, float healAmount, String damageType, int effectPerTick, boolean heal, boolean harm) {
		super(id);
		name = "Modify Health";
		this.damageAmount = damageAmount;
		this.healAmount = healAmount;
		this.damageType = damageType;
		this.effectPerTick = effectPerTick;
		this.heal = heal;
		this.harm = harm;
	}
	
	@Override
	public NBTTagCompound createNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("trigger", id);
		nbt.setBoolean("heal", heal);
		nbt.setBoolean("harm", harm);
		nbt.setFloat("damageAmount", damageAmount);
		nbt.setString("damageType", damageType);
		nbt.setInteger("effectPerTick", effectPerTick);
		nbt.setFloat("healAmount", healAmount);
		return nbt;
	}
	
	@Override
	public void updateControls(GuiParent parent) {
		GuiPanel panel = (GuiPanel) parent.get("content");
		List<String> sourceList = new ArrayList<String>();
		for (DamageSource forEachSource : LittleTriggerBoxStructureALET.sourceOfDmg) {
			sourceList.add(forEachSource.damageType);
		}
		wipeControls(panel);
		panel.addControl(new GuiLabel("Modify Health", 0, 0));
		panel.addControl(new GuiLabel("For Every Tick:", 0, 18));
		panel.addControl(new GuiCheckBox("harm", "Harm Entity", 0, 38, harm));
		panel.addControl(new GuiLabel("Damage Type:", 0, 58));
		panel.addControl(new GuiLabel("Total Damage:", 0, 81));
		
		panel.addControl(new GuiComboBox("sources", 73, 55, 80, sourceList));
		((GuiComboBox) parent.get("sources")).select(damageType);
		panel.addControl(new GuiTextfield("dmgPerTick", effectPerTick + "", 85, 15, 40, 14).setNumbersOnly());
		panel.addControl(new GuiAnalogeSlider("dmgAmount", 73, 78, 56, 14, damageAmount, 0, 20));
		
		panel.addControl(new GuiCheckBox("heal", "Heal Entity", 0, 110, heal));
		panel.addControl(new GuiLabel("Total Heal:", 0, 130));
		panel.addControl(new GuiAnalogeSlider("healAmount", 60, 127, 56, 14, healAmount, 0, 20));
		
		panel.get("sources").setEnabled(harm);
		panel.get("dmgAmount").setEnabled(harm);
		
		panel.get("healAmount").setEnabled(heal);
	}
	
	@Override
	public void updateValues(CoreControl source) {
		if (source instanceof GuiAnalogeSlider) {
			GuiAnalogeSlider slider = (GuiAnalogeSlider) source;
			if (slider.name.equals("dmgAmount"))
				damageAmount = (float) slider.value;
			else if (slider.name.equals("healAmount"))
				healAmount = (float) slider.value;
		} else if (source instanceof GuiTextfield) {
			GuiTextfield textField = (GuiTextfield) source;
			if (textField.name.equals("dmgPerTick"))
				effectPerTick = Integer.parseInt(textField.text);
		} else if (source instanceof GuiComboBox) {
			GuiComboBox combo = (GuiComboBox) source;
			if (combo.name.equals("sources"))
				damageType = combo.getCaption();
		} else if (source instanceof GuiCheckBox) {
			GuiCheckBox check = (GuiCheckBox) source;
			GuiPanel panel = (GuiPanel) check.getGui().get("content");
			
			if (check.name.equals("harm"))
				harm = check.value;
			else if (check.name.equals("heal"))
				heal = check.value;
			
			panel.get("sources").setEnabled(harm);
			panel.get("dmgAmount").setEnabled(harm);
			panel.get("healAmount").setEnabled(heal);
		}
	}
}
