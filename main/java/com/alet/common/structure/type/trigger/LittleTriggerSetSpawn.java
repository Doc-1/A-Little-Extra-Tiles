package com.alet.common.structure.type.trigger;

import java.util.HashSet;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class LittleTriggerSetSpawn extends LittleTriggerEvent {
    
    public double posX = 0;
    public double posY = 0;
    public double posZ = 0;
    
    public LittleTriggerSetSpawn(String id) {
        super(id);
    }
    
    public LittleTriggerSetSpawn(String id, NBTTagCompound nbt) {
        super(id);
        this.posX = nbt.getDouble("posX");
        this.posY = nbt.getDouble("posY");
        this.posZ = nbt.getDouble("posZ");
    }
    
    @Override
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("trigger", id);
        nbt.setDouble("posX", posX);
        nbt.setDouble("posY", posY);
        nbt.setDouble("posZ", posZ);
        return nbt;
    }
    
    @Override
    public LittleTriggerEvent createFromNBT(NBTTagCompound nbt) {
        this.posX = nbt.getDouble("posX");
        this.posY = nbt.getDouble("posY");
        this.posZ = nbt.getDouble("posZ");
        return this;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        wipeControls(panel);
        panel.addControl(new GuiLabel("Set Spawn", 0, 0));
        panel.addControl(new GuiTextfield("xStr", posX + "", 15, 19, 48, 10).setFloatOnly());
        panel.addControl(new GuiTextfield("yStr", posY + "", 15, 39, 48, 10).setFloatOnly());
        panel.addControl(new GuiTextfield("zStr", posZ + "", 15, 59, 48, 10).setFloatOnly());
        panel.addControl(new GuiLabel("x", "X:", 0, 19));
        panel.addControl(new GuiLabel("y", "Y:", 0, 39));
        panel.addControl(new GuiLabel("z", "Z:", 0, 59));
    }
    
    @Override
    public void updateValues(CoreControl source) {
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
    public void runEvent(HashSet<Entity> entities, Integer tick) {
        for (Entity entity : entities) {
            if (entity instanceof EntityPlayerMP) {
                ((EntityPlayerMP) entity).setSpawnPoint(new BlockPos(posX, posY, posZ), true);
            }
            
        }
    }
    
    @Override
    public String getName() {
        return "Set Spawn";
    }
    
}
