package com.alet.common.gui.structure;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.packets.PacketGetServerCams;
import com.alet.components.structures.type.LittleCamPlayer;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class LittleCamPlayerGui extends LittleStructureGuiParser {
    
    public LittleCamPlayerGui(GuiParent parent, AnimationGuiHandler handler) {
        super(parent, handler);
    }
    
    @Override
    protected void createControls(LittlePreviews previews, LittleStructure structure) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        
        LittleCamPlayer camPlayer = structure instanceof LittleCamPlayer ? (LittleCamPlayer) structure : null;
        if (camPlayer != null)
            PacketHandler.sendPacketToServer(new PacketGetServerCams(camPlayer.camToPlay));
        else
            PacketHandler.sendPacketToServer(new PacketGetServerCams(""));
        
        List<String> list = new ArrayList<String>();
        GuiComboBox box = new GuiComboBox("cameras", 0, 0, 100, list);
        
        box.enabled = false;
        parent.controls.add(box);
        parent.controls.add(
            new GuiCheckBox("plyrCam", "Player Is Camera", 0, 22, camPlayer == null ? true : camPlayer.playerIsCamera));
        parent.controls.add(new GuiLabel("Duration: ", 0, 40));
        parent.controls.add(new GuiLabel("Loop: ", 0, 60));
        parent.controls.add(new GuiTextfield("duration", camPlayer == null ? "0" : camPlayer.duration + "", 50, 40, 20, 10));
        parent.controls.add(new GuiTextfield("loop", camPlayer == null ? "0" : camPlayer.loop + "", 50, 60, 20, 10));
        
        parent.controls.add(
            new GuiTextBox("text", "Use the /cam-server add command to add a new path to the drop down menu. Duration cannot be zero. It will not play.", 110, 0, 82));
        
        if (!player.isCreative()) {
            for (GuiControl control : parent.controls) {
                control.enabled = false;
            }
            parent.controls.add(new GuiTextBox("message", "These settings are only avalible in creative mode", 140, 45, 50));
            parent.get("text").visible = false;
        }
    }
    
    @Override
    protected LittleStructure parseStructure(LittlePreviews previews) {
        LittleCamPlayer structure = createStructure(LittleCamPlayer.class, null);
        GuiComboBox cameras = (GuiComboBox) parent.get("cameras");
        GuiCheckBox plyrCam = (GuiCheckBox) parent.get("plyrCam");
        GuiTextfield duration = (GuiTextfield) parent.get("duration");
        GuiTextfield loop = (GuiTextfield) parent.get("loop");
        structure.camToPlay = cameras.getCaption();
        structure.playerIsCamera = plyrCam.value;
        structure.duration = Integer.parseInt(duration.text);
        structure.loop = Integer.parseInt(loop.text);
        return structure;
    }
    
    @Override
    protected LittleStructureType getStructureType() {
        return LittleStructureRegistry.getStructureType(LittleCamPlayer.class);
    }
    
}
