package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.packet.PacketUpdateStructureFromClient;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiListBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents;
import com.creativemd.littletiles.client.gui.signal.SubGuiDialogSignal.GuiSignalComponent;
import com.creativemd.littletiles.common.structure.LittleStructure;

import net.minecraft.nbt.NBTTagCompound;

public class SubGuiSignalEventsALET extends SubGuiSignalEvents {
    
    public SubGuiSignalEventsALET(GuiSignalEventsButton button) {
        super(button);
    }
    
    @Override
    public void createControls() {
        
        GuiScrollBox box = new GuiScrollBox("content", 0, 0, 170, 172);
        
        List<String> values = new ArrayList<>();
        values.add("Components:");
        
        for (GuiSignalComponent component : button.inputs)
            values.add(component.display());
        
        GuiListBox components = new GuiListBox("components", 180, 0, 120, 180, values);
        
        controls.add(components);
        controls.add(box);
        
        for (GuiSignalEvent event : events)
            addEntry(event);
        
        controls.add(new GuiButton("save", 146, 180) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                
                SubGuiSignalEventsALET.this.button.events = events;
                LittleStructure struc = SubGuiSignalEventsALET.this.button.activator;
                SubGuiSignalEventsALET.this.button.setEventsInStructure(struc);
                NBTTagCompound nbt = new NBTTagCompound();
                struc.writeToNBT(nbt);
                PacketHandler.sendPacketToServer(new PacketUpdateStructureFromClient(struc.getStructureLocation(), nbt));
                closeGui();
            }
        });
        controls.add(new GuiButton("cancel", 0, 180) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                closeGui();
            }
        });
    }
}
