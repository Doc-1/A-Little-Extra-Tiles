package com.alet.client.gui;

import com.alet.common.packet.PacketUpdateStructureFromClient;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.common.structure.LittleStructure;

import net.minecraft.nbt.NBTTagCompound;

public abstract class PremadeGuiParser extends SubGui {
    
    public LittleStructure premade;
    
    public PremadeGuiParser(LittleStructure premade) {
        super(176, 166);
        this.premade = premade;
    }
    
    public PremadeGuiParser(int width, int height, LittleStructure premade) {
        super("gui", width, height);
        this.premade = premade;
    }
    
    public PremadeGuiParser(String name, int width, int height, LittleStructure premade) {
        super(name, width, height);
        this.premade = premade;
    }
    
    @Override
    public void createControls() {
        NBTTagCompound nbt = new NBTTagCompound();
        premade.writeToNBT(nbt);
        loadFromNBT(nbt);
    }
    
    public abstract void loadFromNBT(NBTTagCompound nbt);
    
    public abstract void writeToNBT(NBTTagCompound nbt);
    /*
    @Override
    public void onOpened() {
    	super.onOpened();
    	NBTTagCompound nbt = new NBTTagCompound();
    	premade.writeToNBT(nbt);
    	loadFromNBT(nbt);
    }*/
    
    @Override
    public void onClosed() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        premade.loadFromNBT(nbt);
        PacketHandler.sendPacketToServer(new PacketUpdateStructureFromClient(premade.getStructureLocation(), nbt));
        super.onClosed();
    }
    
}
