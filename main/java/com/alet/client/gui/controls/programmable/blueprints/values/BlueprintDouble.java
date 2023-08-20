package com.alet.client.gui.controls.programmable.blueprints.values;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeDouble;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class BlueprintDouble extends BlueprintValue {
    
    public BlueprintDouble(int id) {
        super(id);
    }
    
    private double value = 0;
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setDouble("value", value);
        return nbt;
    }
    
    @Override
    public GuiBlueprint deserializeNBT(NBTTagCompound nbt) {
        value = nbt.getDouble("value");
        return this;
    }
    
    @Override
    public void setNodes() {
        this.nodes.add(new GuiNodeDouble("double", "double", (byte) (GuiNode.SENDER | GuiNode.MODIFIABLE)));
    }
    
    @Override
    public void setNodeValue(WorldServer server) {
        GuiNodeDouble node = (GuiNodeDouble) this.getNode("double");
        node.setValue(this.value, true);
    }
    
    @Override
    public void controlChanged(GuiControl source) {
        if (source instanceof GuiTextfield) {
            GuiTextfield field = (GuiTextfield) source;
            this.value = field.parseInteger();
            GuiNodeDouble node = (GuiNodeDouble) this.getNode("float");
            node.setValue(this.value, false);
            
        }
        
    }
    
}
