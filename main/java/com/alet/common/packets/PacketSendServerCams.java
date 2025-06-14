package com.alet.common.packets;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PacketSendServerCams extends CreativeCorePacket {
    
    public String[] ids;
    public NBTTagCompound nbt;
    public String selected;
    
    public PacketSendServerCams(String[] id, String selected) {
        this.ids = id;
        this.nbt = new NBTTagCompound();
        this.selected = selected;
    }
    
    public PacketSendServerCams() {
        
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < ids.length; i++) {
            NBTTagCompound tempNBT = new NBTTagCompound();
            tempNBT.setString(i + "", ids[i]);
            list.appendTag(tempNBT);
        }
        nbt.setTag("ids", list);
        writeNBT(buf, nbt);
        writeString(buf, selected);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        nbt = readNBT(buf);
        selected = readString(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        NBTTagList list = nbt.getTagList("ids", 10);
        List<String> t = new ArrayList<String>();
        int i = 0;
        for (NBTBase each : list) {
            t.add(((NBTTagCompound) each).getString(i + ""));
            i++;
        }
        if (player.openContainer instanceof ContainerSub) {
            SubGui gui = ((ContainerSub) player.openContainer).gui.getTopLayer();
            GuiComboBox box = (GuiComboBox) gui.get("cameras");
            box.lines = t;
            box.enabled = true;
            box.select(selected);
        }
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        
    }
    
}
