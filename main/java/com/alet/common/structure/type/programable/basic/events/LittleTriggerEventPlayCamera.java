package com.alet.common.structure.type.programable.basic.events;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.packet.PacketGetServerCams;
import com.alet.common.structure.type.programable.basic.LittleTriggerObject;
import com.creativemd.cmdcam.common.packet.StartPathPacket;
import com.creativemd.cmdcam.common.utils.CamPath;
import com.creativemd.cmdcam.server.CMDCamServer;
import com.creativemd.cmdcam.server.CamCommandServer;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerEventPlayCamera extends LittleTriggerEvent {
    
    public String camToPlay = "";
    public boolean playerIsCamera = true;
    public int duration = 0;
    public int loop = 0;
    
    public LittleTriggerEventPlayCamera(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean runEvent() {
        playCam();
        return false;
    }
    
    public void playCam() {
        World world = structure.getWorld();
        if (world.isRemote)
            return;
        CamPath path = CMDCamServer.getPath(world, camToPlay);
        if (path != null) {
            if (CamCommandServer.StringToDuration(this.duration + "") != 0) {
                if (!path.isRunning()) {
                    if (playerIsCamera)
                        path.mode = "default";
                    else
                        path.mode = "outside";
                    long duration = CamCommandServer.StringToDuration(this.duration + "");
                    path.duration = duration;
                    path.loop = loop;
                    for (Entity entity : this.getEntities())
                        if (entity instanceof EntityPlayer)
                            PacketHandler.sendPacketToPlayer(new StartPathPacket(path), (EntityPlayerMP) entity);
                } else
                    for (Entity entity : this.getEntities()) {
                        if (entity instanceof EntityPlayer)
                            entity.sendMessage(new TextComponentString("Path '" + this.camToPlay + "' is already running"));
                    }
                
            }
        } else {
            for (Entity entity : this.getEntities()) {
                if (entity instanceof EntityPlayer)
                    entity.sendMessage(new TextComponentString("Path '" + this.camToPlay + "' could not be found!"));
            }
            
        }
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        camToPlay = nbt.getString("cam");
        playerIsCamera = nbt.getBoolean("playerIsCam");
        duration = nbt.getInteger("duration");
        loop = nbt.getInteger("loop");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("cam", camToPlay);
        nbt.setBoolean("playerIsCam", playerIsCamera);
        nbt.setInteger("duration", duration);
        nbt.setInteger("loop", loop);
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        
        PacketHandler.sendPacketToServer(new PacketGetServerCams(camToPlay));
        
        List<String> list = new ArrayList<String>();
        GuiComboBox box = new GuiComboBox("cameras", 0, 0, 100, list);
        
        box.enabled = false;
        panel.addControl(box);
        panel.addControl(new GuiCheckBox("plyrCam", "Player Is Camera", 0, 22, playerIsCamera));
        panel.addControl(new GuiLabel("Duration: ", 0, 40));
        panel.addControl(new GuiLabel("Loop: ", 0, 60));
        panel.addControl(new GuiTextfield("duration", duration + "", 50, 40, 20, 10));
        panel.addControl(new GuiTextfield("loop", loop + "", 50, 60, 20, 10));
        
        panel.addControl(
            new GuiTextBox("text", "Use the /cam-server add command to add a new path to the drop down menu. Duration cannot be zero. It will not play.", 110, 0, 82));
        
        if (!player.isCreative()) {
            for (GuiControl control : panel.controls) {
                control.enabled = false;
            }
            panel.addControl(new GuiTextBox("message", "These settings are only avalible in creative mode", 140, 45, 50));
            panel.get("text").visible = false;
        }
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source.is("cameras"))
            this.camToPlay = ((GuiComboBox) source).getCaption();
        if (source.is("plyrCam"))
            this.playerIsCamera = ((GuiCheckBox) source).value;
        if (source.is("duration"))
            this.duration = Integer.parseInt(((GuiTextfield) source).text);
        if (source.is("loop"))
            this.loop = Integer.parseInt(((GuiTextfield) source).text);
    }
    
}
