package com.alet.common.structure.type.trigger.events;

import com.alet.client.gui.controls.GuiConnectedCheckBoxes;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerEventModifyMotion extends LittleTriggerEvent {
    
    public double xStrength = 0;
    public double yStrength = 0;
    public double zStrength = 0;
    public double forward = 0;
    public double strafe = 0;
    public boolean addTo = true;
    public double maxVelocity = 0;
    public double minVelocity = 0;
    
    public LittleTriggerEventModifyMotion(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setDouble("xStrength", xStrength);
        nbt.setDouble("yStrength", yStrength);
        nbt.setDouble("zStrength", zStrength);
        nbt.setDouble("forward", forward);
        nbt.setDouble("strafe", strafe);
        nbt.setDouble("maxVelocity", maxVelocity);
        nbt.setDouble("minVelocity", minVelocity);
        nbt.setBoolean("addTo", addTo);
        return nbt;
    }
    
    @Override
    public LittleTriggerEvent deserializeNBT(NBTTagCompound nbt) {
        this.xStrength = nbt.getDouble("xStrength");
        this.yStrength = nbt.getDouble("yStrength");
        this.zStrength = nbt.getDouble("zStrength");
        this.forward = nbt.getDouble("forward");
        this.strafe = nbt.getDouble("strafe");
        this.maxVelocity = nbt.getDouble("maxVelocity");
        this.minVelocity = nbt.getDouble("minVelocity");
        this.addTo = nbt.getBoolean("addTo");
        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        panel.addControl(new GuiLabel("Modify Motion", 0, 0));
        int m = 5;
        panel.addControl(new GuiAnalogeSlider("xStr", 15, 19, 48, 10, xStrength, -m, m));
        panel.addControl(new GuiAnalogeSlider("yStr", 15, 39, 48, 10, yStrength, -m, m));
        panel.addControl(new GuiAnalogeSlider("zStr", 15, 59, 48, 10, zStrength, -m, m));
        panel.addControl(new GuiAnalogeSlider("forward", 50, 79, 48, 10, forward, -m, m));
        panel.addControl(new GuiAnalogeSlider("strafe", 50, 99, 48, 10, strafe, -m, m));
        panel.addControl(new GuiLabel("x", "X:", 0, 19));
        panel.addControl(new GuiLabel("y", "Y:", 0, 39));
        panel.addControl(new GuiLabel("z", "Z:", 0, 59));
        panel.addControl(new GuiLabel("ford", "Forward:", 0, 79));
        panel.addControl(new GuiLabel("straf", "Strafe:", 0, 99));
        GuiConnectedCheckBoxes checkBoxes = new GuiConnectedCheckBoxes("", -3, 115).addCheckBox("add", "Add To Velocity")
                .addCheckBox("set", "Set To Velocity");
        panel.addControl(checkBoxes);
        if (addTo)
            checkBoxes.setSelected("add");
        else
            checkBoxes.setSelected("set");
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiAnalogeSlider) {
            GuiAnalogeSlider slider = (GuiAnalogeSlider) source;
            if (slider.name.equals("xStr"))
                xStrength = slider.value;
            if (slider.name.equals("yStr"))
                yStrength = slider.value;
            if (slider.name.equals("zStr"))
                zStrength = slider.value;
            if (slider.name.equals("forward"))
                forward = slider.value;
            if (slider.name.equals("strafe"))
                strafe = slider.value;
        }
        if (source instanceof GuiConnectedCheckBoxes) {
            GuiConnectedCheckBoxes boxes = (GuiConnectedCheckBoxes) source;
            if (boxes.getSelected().name.equals("add"))
                this.addTo = true;
            else if (boxes.getSelected().name.equals("set"))
                this.addTo = false;
        }
    }
    
    @Override
    public boolean runEvent() {
        for (Entity entity : this.getEntities()) {
            entity.velocityChanged = true;
            if (addTo) {
                entity.addVelocity(xStrength, yStrength, zStrength);
            } else {
                entity.motionX = xStrength;
                entity.motionY = yStrength;
                entity.motionZ = zStrength;
            }
            entity.moveRelative(strafe != 0F ? 1F : 0F, 0F, 0F, (float) strafe);
            entity.moveRelative(0F, 0F, forward != 0F ? 1F : 0F, (float) forward);
            entity.fallDistance = 0;
        }
        return true;
    }
    
}
