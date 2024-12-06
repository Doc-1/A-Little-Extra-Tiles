package com.alet.client.gui.origins;

import com.alet.common.packets.PacketUpdateStructureFromClient;
import com.alet.common.structures.type.premade.transfer.LittleTransferItemScanner;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.packet.PacketHandler;

import net.minecraft.nbt.NBTTagCompound;

public class SubGuiItemScanner extends SubGui {
    
    LittleTransferItemScanner structure;
    NBTTagCompound nbt = new NBTTagCompound();
    
    public SubGuiItemScanner(LittleTransferItemScanner structure) {
        super(500, 500);
        this.structure = structure;
    }
    
    private void openGui() {
        structure.writeToNBT(nbt);
        
    }
    
    @Override
    public void onClosed() {
        
        PacketHandler.sendPacketToServer(new PacketUpdateStructureFromClient(structure.getStructureLocation(), nbt));
        super.onClosed();
    }
    
    @Override
    public void createControls() {
        
        openGui();
    }
    
}
