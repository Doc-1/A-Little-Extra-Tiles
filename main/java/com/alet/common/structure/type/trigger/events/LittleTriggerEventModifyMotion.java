package com.alet.common.structure.type.trigger.events;

import java.util.HashSet;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class LittleTriggerEventModifyMotion extends LittleTriggerEvent {
    
    public double xStrength = 0;
    public double yStrength = 0;
    public double zStrength = 0;
    public double forward = 0;
    
    public LittleTriggerEventModifyMotion(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound createNBT(NBTTagCompound nbt) {
        nbt.setDouble("xStrength", xStrength);
        nbt.setDouble("yStrength", yStrength);
        nbt.setDouble("zStrength", zStrength);
        nbt.setDouble("forward", forward);
        return nbt;
    }
    
    @Override
    public LittleTriggerEvent createFromNBT(NBTTagCompound nbt) {
        this.xStrength = nbt.getDouble("xStrength");
        this.yStrength = nbt.getDouble("yStrength");
        this.zStrength = nbt.getDouble("zStrength");
        this.forward = nbt.getDouble("forward");
        return this;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        wipeControls(panel);
        panel.addControl(new GuiLabel("Modify Motion", 0, 0));
        panel.addControl(new GuiAnalogeSlider("forward", 50, 79, 48, 10, forward, -5, 5) {
            @CustomEventSubscribe
            public void onSlotChange(GuiControlChangedEvent event) {
                System.out.println("test");
            }
        });
        panel.addControl(new GuiAnalogeSlider("xStr", 15, 19, 48, 10, xStrength, -5, 5));
        panel.addControl(new GuiAnalogeSlider("yStr", 15, 39, 48, 10, yStrength, -5, 5));
        panel.addControl(new GuiAnalogeSlider("zStr", 15, 59, 48, 10, zStrength, -5, 5));
        panel.addControl(new GuiLabel("ford", "Forward:", 0, 79));
        panel.addControl(new GuiLabel("x", "X:", 0, 19));
        panel.addControl(new GuiLabel("y", "Y:", 0, 39));
        panel.addControl(new GuiLabel("z", "Z:", 0, 59));
    }
    
    @Override
    public void updateValues(CoreControl source) {
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
        }
    }
    
    @Override
    public boolean runEvent(HashSet<Entity> entities) {
        System.out.println("da");
        for (Entity entity : entities) {
            
            entity.velocityChanged = true;
            
            double totalX = 0;
            double totalY = this.yStrength;
            double totalZ = 0;
            
            double x1 = -MathHelper.sin((entity.rotationYaw) / 180.0F * (float) Math.PI) * this.forward;
            double z1 = MathHelper.cos((entity.rotationYaw) / 180.0F * (float) Math.PI) * this.forward;
            double y1 = -MathHelper.sin((entity.rotationPitch) / 180.0F * (float) Math.PI) * 0.5;
            
            double x2 = -MathHelper.sin((270) / 180.0F * (float) Math.PI) * this.xStrength;
            double z2 = MathHelper.cos((270) / 180.0F * (float) Math.PI) * this.xStrength;
            
            double x3 = -MathHelper.sin((360) / 180.0F * (float) Math.PI) * this.zStrength;
            double z3 = MathHelper.cos((360) / 180.0F * (float) Math.PI) * this.zStrength;
            
            totalX = (this.forward != 0) ? x1 + x2 + x3 : x2 + x3;
            totalZ = (this.forward != 0) ? z1 + z2 + z3 : z2 + z3;
            totalY += (this.forward != 0) ? y1 : 0;
            //System.out.println(x1 + " " + x2 + " " + z1 + " " + z2);
            entity.motionX += totalX;
            entity.motionY += totalY;
            entity.motionZ += totalZ;
            entity.fallDistance = 0;
        }
        return true;
    }
    
}
