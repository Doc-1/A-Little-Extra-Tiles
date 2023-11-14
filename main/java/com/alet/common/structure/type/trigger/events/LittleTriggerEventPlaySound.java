package com.alet.common.structure.type.trigger.events;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.packet.PacketPlaySound;
import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerEventPlaySound extends LittleTriggerEvent {
    
    public float volume = 1;
    public float pitch = 1;
    public List<String> possibleLines = new ArrayList<>();
    public String selected;
    
    public LittleTriggerEventPlaySound(int id) {
        super(id);
        for (ResourceLocation location : SoundEvent.REGISTRY.getKeys())
            possibleLines.add(location.toString());
    }
    
    @Override
    public boolean runEvent() {
        for (Entity entity : this.getEntities()) {
            if (entity instanceof EntityPlayer)
                PacketHandler.sendPacketToPlayer(new PacketPlaySound(pitch, volume, false, this.structure
                        .getPos(), selected), (EntityPlayerMP) entity);
        }
        return true;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        this.volume = nbt.getFloat("volume");
        this.pitch = nbt.getFloat("pitch");
        this.selected = nbt.getString("selected");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setFloat("volume", volume);
        nbt.setFloat("pitch", pitch);
        nbt.setString("selected", selected);
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        GuiTextfield search = new GuiTextfield("search", "", 0, 0, 170, 14);
        panel.addControl(search);
        GuiComboBox box = new GuiComboBox("sounds", 0, 22, 170, possibleLines);
        if (selected != null)
            box.select(selected);
        else
            selected = box.getCaption();
        
        panel.addControl(box);
        panel.addControl(new GuiLabel(I18n.translateToLocal("gui.sound.volume") + ":", 0, 44));
        panel.addControl(new GuiAnalogeSlider("volume", 60, 44, 60, 8, volume, 0F, 1F));
        panel.addControl(new GuiLabel(I18n.translateToLocal("gui.sound.pitch") + ":", 0, 64));
        panel.addControl(new GuiAnalogeSlider("pitch", 60, 64, 60, 8, pitch, 0.5F, 2F));
        
        panel.addControl(new GuiButton("play", 140, 44) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                SoundEvent event = getSelected();
                if (event != null)
                    playSound(event, LittleTriggerEventPlaySound.this.volume, LittleTriggerEventPlaySound.this.pitch);
            }
        });
    }
    
    @SideOnly(Side.CLIENT)
    public SoundEvent getSelected() {
        ResourceLocation location = getSelectionLocation();
        if (location != null)
            return SoundEvent.REGISTRY.getObject(location);
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public ResourceLocation getSelectionLocation() {
        if (selected != null && !selected.isEmpty())
            return new ResourceLocation(selected);
        return null;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiComboBox)
            selected = ((GuiComboBox) source).getCaption();
        if (source instanceof GuiAnalogeSlider) {
            GuiAnalogeSlider slider = (GuiAnalogeSlider) source;
            if (source.is("volume"))
                this.volume = (float) slider.value;
            if (source.is("pitch"))
                this.pitch = (float) slider.value;
        } else if (source instanceof GuiTextfield && source.is("search")) {
            GuiTextfield search = (GuiTextfield) source;
            GuiComboBox sounds = (GuiComboBox) source.parent.get("sounds");
            List<String> foundLines = new ArrayList<>();
            if (search.text.isEmpty())
                foundLines.addAll(possibleLines);
            else
                for (int i = 0; i < possibleLines.size(); i++)
                    if (possibleLines.get(i).contains(search.text))
                        foundLines.add(possibleLines.get(i));
            sounds.lines = foundLines;
            if (!sounds.select(selected))
                sounds.select(0);
        }
    }
    
}
