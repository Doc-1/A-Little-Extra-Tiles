package com.alet.client.gui.controls.programmable.blueprints.values;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeInteger;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class BlueprintInteger extends BlueprintValue {
    
    public BlueprintInteger(int id) {
        super(id);
    }
    
    private int value = 0;
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setInteger("value", value);
        return nbt;
    }
    
    @Override
    public GuiBlueprint deserializeNBT(NBTTagCompound nbt) {
        value = nbt.getInteger("value");
        return this;
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeInteger("int", "int", (byte) (GuiNode.SENDER | GuiNode.MODIFIABLE)));
    }
    
    @Override
    public void setNodeValue(Entity entity) {
        GuiNodeInteger node = (GuiNodeInteger) this.getNode("int");
        node.setValue(this.value, true);
    }
    
    @Override
    public void controlChanged(GuiControl source) {
        if (source instanceof GuiTextfield) {
            GuiTextfield field = (GuiTextfield) source;
            this.value = field.parseInteger();
            GuiNodeInteger node = (GuiNodeInteger) this.getNode("int");
            node.setValue(this.value, false);
            
        }
        
    }
}
