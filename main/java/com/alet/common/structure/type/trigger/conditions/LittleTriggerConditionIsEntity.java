package com.alet.common.structure.type.trigger.conditions;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class LittleTriggerConditionIsEntity extends LittleTriggerCondition {
    
    public String entityType;
    
    public LittleTriggerConditionIsEntity(int id) {
        super(id);
    }
    
    @Override
    public boolean conditionPassed() {
        for (Entity entity : this.getEntities()) {
            ResourceLocation resourcelocation = new ResourceLocation(entityType);
            if (EntityList.isMatchingName(entity, resourcelocation)) {
                return EntityList.isRegistered(resourcelocation);
            }
        }
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        this.entityType = nbt.getString("entityType");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("entityType", this.entityType);
        return nbt;
    }
    
    @Override
    public void createGuiControls(GuiParent parent, LittlePreviews previews) {
        GuiPanel panel = getPanel(parent);
        wipeControls(panel);
        List<String> nameList = new ArrayList<String>();
        nameList.add("player");
        for (ResourceLocation res : EntityList.getEntityNameList()) {
            String name = EntityList.getTranslationName(res);
            if (name != null)
                nameList.add(name);
        }
        GuiComboBox comboBox = new GuiComboBox("nameList", 0, 0, 153, nameList) {
            @Override
            public void setCaption(String caption) {
                this.caption = caption;
            }
        };
        if (this.entityType != null)
            comboBox.setCaption(entityType);
        panel.addControl(comboBox);
    }
    
    @Override
    public void guiChangedEvent(CoreControl source) {
        if (source.is("nameList")) {
            GuiComboBox comboBox = (GuiComboBox) source;
            this.entityType = comboBox.getCaption();
        }
        
    }
    
}
