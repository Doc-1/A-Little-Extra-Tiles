package com.alet.common.packet;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.creativecore.common.packet.PacketHandler;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

public class PacketGetServerScoreboard extends CreativeCorePacket {
    
    private String[] list;
    private NBTTagCompound nbt;
    private String selected;
    private boolean isClientSent;
    
    public PacketGetServerScoreboard() {
        
    }
    
    public PacketGetServerScoreboard(String selected) {
        this.selected = selected;
        this.isClientSent = true;
    }
    
    public PacketGetServerScoreboard(String[] list, String selected) {
        this.list = list;
        nbt = new NBTTagCompound();
        this.selected = selected;
        this.isClientSent = false;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        buf.writeBoolean(isClientSent);
        if (list != null) {
            NBTTagList tagList = new NBTTagList();
            for (int i = 0; i < list.length; i++) {
                NBTTagCompound tempNBT = new NBTTagCompound();
                tempNBT.setString(i + "", list[i]);
                tagList.appendTag(tempNBT);
            }
            nbt.setTag("objectives", tagList);
            writeNBT(buf, nbt);
        }
        writeString(buf, selected);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        isClientSent = buf.readBoolean();
        if (!isClientSent)
            nbt = readNBT(buf);
        
        selected = readString(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        
        NBTTagList list = nbt.getTagList("objectives", 10);
        List<String> t = new ArrayList<String>();
        int i = 0;
        for (NBTBase each : list) {
            t.add(((NBTTagCompound) each).getString(i + ""));
            i++;
        }
        if (player.openContainer instanceof ContainerSub) {
            SubGui gui = ((ContainerSub) player.openContainer).gui.getTopLayer();
            GuiComboBox box = (GuiComboBox) gui.get("ls");
            box.lines = t;
            box.enabled = true;
            if (this.selected.isEmpty())
                box.select(0);
            else
                box.select(this.selected);
        }
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        Scoreboard sb = player.getWorldScoreboard();
        
        ArrayList<String> newList = new ArrayList<>();
        for (ScoreObjective so : sb.getScoreObjectives())
            newList.add(so.getName());
        String[] list = newList.toArray(new String[0]);
        PacketHandler.sendPacketToPlayer(new PacketGetServerScoreboard(list, selected), (EntityPlayerMP) player);
    }
    
}
