package com.alet.common.structure.type.trigger.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class LittleTriggerEventModifyHealth extends LittleTriggerEvent {
    
    public float damageAmount = 0;
    public float healAmount = 0;
    public String damageType = "";
    public boolean heal = false;
    public boolean harm = false;
    
    public static List<DamageSource> sourceOfDmg = new ArrayList<DamageSource>();
    static {
        if (sourceOfDmg.isEmpty()) {
            sourceOfDmg.add(DamageSource.ANVIL);
            sourceOfDmg.add(DamageSource.CACTUS);
            sourceOfDmg.add(DamageSource.CRAMMING);
            sourceOfDmg.add(DamageSource.DRAGON_BREATH);
            sourceOfDmg.add(DamageSource.DROWN);
            sourceOfDmg.add(DamageSource.FALL);
            sourceOfDmg.add(DamageSource.FALLING_BLOCK);
            sourceOfDmg.add(DamageSource.FIREWORKS);
            sourceOfDmg.add(DamageSource.FLY_INTO_WALL);
            sourceOfDmg.add(DamageSource.GENERIC);
            sourceOfDmg.add(DamageSource.HOT_FLOOR);
            sourceOfDmg.add(DamageSource.IN_FIRE);
            sourceOfDmg.add(DamageSource.IN_WALL);
            sourceOfDmg.add(DamageSource.LAVA);
            sourceOfDmg.add(DamageSource.LIGHTNING_BOLT);
            sourceOfDmg.add(DamageSource.MAGIC);
            sourceOfDmg.add(DamageSource.ON_FIRE);
            sourceOfDmg.add(DamageSource.OUT_OF_WORLD);
            sourceOfDmg.add(DamageSource.STARVE);
            sourceOfDmg.add(DamageSource.WITHER);
        }
    }
    
    public LittleTriggerEventModifyHealth(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound createNBT(NBTTagCompound nbt) {
        nbt.setBoolean("heal", heal);
        nbt.setBoolean("harm", harm);
        nbt.setFloat("damageAmount", damageAmount);
        nbt.setString("damageType", damageType);
        nbt.setFloat("healAmount", healAmount);
        return nbt;
    }
    
    @Override
    public LittleTriggerEvent createFromNBT(NBTTagCompound nbt) {
        this.heal = nbt.getBoolean("heal");
        this.harm = nbt.getBoolean("harm");
        this.damageAmount = nbt.getFloat("damageAmount");
        this.damageType = nbt.getString("damageType");
        this.healAmount = nbt.getFloat("healAmount");
        return this;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        List<String> sourceList = new ArrayList<String>();
        for (DamageSource forEachSource : sourceOfDmg) {
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
    
    @Override
    public boolean runEvent(HashSet<Entity> entities) {
        if (this.harm) {
            DamageSource damageSource = DamageSource.GENERIC;
            for (DamageSource source : sourceOfDmg) {
                if (this.damageType.equals(source.getDamageType())) {
                    damageSource = source;
                    break;
                }
            }
            for (Entity entity : entities) {
                entity.attackEntityFrom(damageSource, this.damageAmount);
            }
        }
        if (this.heal) {
            for (Entity entity : entities) {
                if (entity instanceof EntityLivingBase) {
                    EntityLivingBase living = (EntityLivingBase) entity;
                    living.heal(this.healAmount);
                }
            }
        }
        return true;
    }
    
    @Override
    public String getName() {
        return "Modify Health";
    }
}
