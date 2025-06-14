package com.alet.common.gui.tool;

import org.lwjgl.util.Color;

import com.alet.common.gui.controls.GuiColorPickerAlet;
import com.alet.common.gui.controls.tutorial.GuiTutorialBox;
import com.alet.common.gui.controls.tutorial.TutorialTip;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiLittleRope extends SubGuiConfigure {
    
    public SubGuiLittleRope(ItemStack stack) {
        super(134, 139, stack);
    }
    
    @Override
    public void createControls() {
        NBTTagCompound nbt = new NBTTagCompound();
        if (this.stack.hasTagCompound())
            nbt = this.stack.getTagCompound();
        
        int color = nbt.hasKey("color") ? nbt.getInteger("color") : ColorUtils.BLACK;
        double thickness = nbt.hasKey("thickness") ? nbt.getDouble("thickness") : 0.3F;
        double tautness = nbt.hasKey("tautness") ? nbt.getDouble("tautness") : 0.5F;
        
        controls.add(new GuiLabel("Rope's Radius:", 0, 0));
        controls.add(new GuiLabel("Rope's Tautness:", 0, 35));
        controls.add(new GuiLabel("Rope's Color:", 0, 70));
        
        controls.add(new GuiAnalogeSlider("thickness", 0, 15, 100, 10, thickness, 0.01, 0.5));
        controls.add(new GuiAnalogeSlider("tautness", 0, 50, 100, 10, tautness, 0, 10));
        controls.add(new GuiColorPickerAlet("color", 0, 85, ColorUtils.IntToRGBA(color), true, 0));
        
        GuiTutorialBox tutBox = new GuiTutorialBox(name, 0, 0, 180, this.width, this.height);
        tutBox.tutorialMap.add(new TutorialTip(get(
            "thickness"), "leftout", "This is used to set the radius of the rope. At 0.5 it will be the same size as a block."));
        tutBox.tutorialMap.add(new TutorialTip(get(
            "tautness"), "leftout", "This is used to set how far the rope droops. At 0.1 the curve will be half a block down."));
        tutBox.tutorialMap.add(new TutorialTip(get("color"), "leftout", "This is used to set the color of the rope."));
        GuiColorPickerAlet picker = (GuiColorPickerAlet) get("color");
        tutBox.tutorialMap.add(new TutorialTip(picker.get("r"), "leftout", "This is the red color slider."));
        tutBox.tutorialMap.add(new TutorialTip(picker.get("g"), "leftout", "This is the green color slider."));
        tutBox.tutorialMap.add(new TutorialTip(picker.get("b"), "leftout", "This is the blue color slider."));
        tutBox.tutorialMap.add(new TutorialTip(picker.get(
            "a"), "leftout", "This is the alpha slider. How transparent the color is."));
        tutBox.tutorialMap.add(new TutorialTip(picker.get(
            "s"), "leftout", "This is the shader slider. It allows you to easly change how dark or light a color is."));
        tutBox.tutorialMap.add(new TutorialTip(picker.get(
            "more"), "leftout", "Click on this to open the color palette. It allows you to save your currently selected color for later use."));
        controls.add(tutBox);
    }
    
    @Override
    public void saveConfiguration() {
        NBTTagCompound nbt = stack.getTagCompound();
        double thickness = ((GuiAnalogeSlider) this.get("thickness")).value;
        double tautness = ((GuiAnalogeSlider) this.get("tautness")).value;
        Color color = ((GuiColorPickerAlet) this.get("color")).color;
        
        nbt.setDouble("thickness", thickness);
        nbt.setDouble("tautness", tautness);
        nbt.setInteger("color", ColorUtils.RGBAToInt(color));
    }
    
}
