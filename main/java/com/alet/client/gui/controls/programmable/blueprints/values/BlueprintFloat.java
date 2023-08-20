package com.alet.client.gui.controls.programmable.blueprints.values;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeFloat;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class BlueprintFloat extends BlueprintValue {
    public BlueprintFloat(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    private float value = 1;
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setFloat("value", value);
        return nbt;
    }
    
    @Override
    public GuiBlueprint deserializeNBT(NBTTagCompound nbt) {
        value = nbt.getFloat("value");
        return this;
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeFloat("float", "float", (byte) (GuiNode.SENDER | GuiNode.MODIFIABLE)));
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        GuiNodeFloat node = (GuiNodeFloat) this.getNode("float");
        node.setValue(this.value, true);
    }
    
    @Override
    public void controlChanged(GuiControl source) {
        if (source instanceof GuiTextfield) {
            GuiTextfield field = (GuiTextfield) source;
            if (!field.text.isEmpty()) {
                float val = Float.valueOf(field.text);
                String.valueOf(val);
                this.value = 0f;
                GuiNodeFloat node = (GuiNodeFloat) this.getNode("float");
                node.setValue(this.value, false);
                
            }
        }
        
    }
}
