package com.alet.client.gui.controls.programmable.blueprints.values;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeString;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class BlueprintString extends BlueprintValue {
    
    public BlueprintString(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    private String value = "";
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("value", value);
        return nbt;
    }
    
    @Override
    public GuiBlueprint deserializeNBT(NBTTagCompound nbt) {
        this.value = nbt.getString("value");
        
        return this;
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeString("String", "String", (byte) (GuiNode.SENDER | GuiNode.MODIFIABLE)));
        
    }
    
    @Override
    public void setNodeValue(Entity entity) {
        GuiNodeString node = (GuiNodeString) this.getNode("String");
        node.setValue(this.value, true);
    }
    
    @Override
    public void controlChanged(GuiControl source) {
        if (source instanceof GuiTextfield) {
            GuiTextfield field = (GuiTextfield) source;
            this.value = field.text;
            System.out.println(this.value);
            GuiNodeString node = (GuiNodeString) this.getNode("String");
            node.setValue(this.value, false);
            
        }
    }
}
