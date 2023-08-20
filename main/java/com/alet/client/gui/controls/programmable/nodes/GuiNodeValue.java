package com.alet.client.gui.controls.programmable.nodes;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;

import net.minecraft.world.WorldServer;

public abstract class GuiNodeValue<V> extends GuiNode {
    
    V v;
    
    public GuiNodeValue(String name, String title, byte attributes) {
        super(name, title, attributes);
    }
    
    public abstract void setValue(V v, boolean updateModifiable);
    
    public V getValue(WorldServer server) {
        GuiBlueprint bp = (GuiBlueprint) this.parent;
        if (this.v == null)
            bp.setNodeValue(server);
        return this.v;
    }
    
    /*
     *  @Override
    public void createControls() {
        if (this.isModifiable()) {
            List<String> listIn = new ArrayList<String>();
            List<String> listOut = new ArrayList<String>();
            GuiComboBox signalInputBox = new GuiComboBox("signalInputBox", 0, 10, 25, listIn);
            GuiComboBox signalOutputtBox = new GuiComboBox("signalInputBox", 0, 10, 25, listOut);
            GuiTextfield value = new GuiTextfield("value", "0", 0, 7, 55, 7);
            
            if (this.isSender() && this.isModifiable()) {
                this.width = Math.max(67, this.width);
                value.posX = this.width - 67;
            }
            
            if (!this.isInput() && !this.isOutput()) {
                if (this.isInteger())
                    value.setNumbersIncludingNegativeOnly();
                this.addControl(value);
            } else if (this.isInput())
                this.addControl(signalInputBox);
            else if (this.isOutput())
                this.addControl(signalOutputtBox);
                
            this.height = 26;
        }
        
    }
     */
}
