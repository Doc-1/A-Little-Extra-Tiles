package com.alet.components.structures.type.programable.basic.events;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerEventSetSpawn extends LittleTriggerEvent {
    
    public double posX = 0;
    public double posY = 0;
    public double posZ = 0;
    
    public LittleTriggerEventSetSpawn(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setDouble("posX", posX);
        nbt.setDouble("posY", posY);
        nbt.setDouble("posZ", posZ);
        return nbt;
    }
    
    @Override
    public LittleTriggerEvent deserializeNBT(NBTTagCompound nbt) {
        this.posX = nbt.getDouble("posX");
        this.posY = nbt.getDouble("posY");
        this.posZ = nbt.getDouble("posZ");
        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        panel.addControl(new GuiLabel("Set Spawn", 0, 0));
        panel.addControl(new GuiTextfield("xStr", posX + "", 15, 19, 48, 10).setFloatOnly());
        panel.addControl(new GuiTextfield("yStr", posY + "", 15, 39, 48, 10).setFloatOnly());
        panel.addControl(new GuiTextfield("zStr", posZ + "", 15, 59, 48, 10).setFloatOnly());
        panel.addControl(new GuiLabel("x", "X:", 0, 19));
        panel.addControl(new GuiLabel("y", "Y:", 0, 39));
        panel.addControl(new GuiLabel("z", "Z:", 0, 59));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiTextfield) {
            GuiTextfield textField = (GuiTextfield) source;
            if (textField.name.equals("xStr"))
                posX = Integer.parseInt(textField.text);
            if (textField.name.equals("yStr"))
                posY = Integer.parseInt(textField.text);
            if (textField.name.equals("zStr"))
                posZ = Integer.parseInt(textField.text);
        }
    }
    
    @Override
    public boolean runEvent() {
        for (Entity entity : this.getEntities()) {
            if (entity instanceof EntityPlayerMP) {
                ((EntityPlayerMP) entity).setSpawnPoint(new BlockPos(posX, posY, posZ), true);
            }
            
        }
        return false;
    }
    
}
