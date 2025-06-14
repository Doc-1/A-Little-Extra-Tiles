package com.alet.common.placement.measurments.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Point3f;

import com.alet.common.gui.controls.GuiDepressedCheckBox;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;

import net.minecraft.item.ItemStack;

public class LittleMeasurementPattern extends LittleMeasurementType {
    
    public LittleMeasurementPattern(String shapeName) {
        super(2, shapeName);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void drawText(HashMap<Integer, Point3f> points, List<String> measurementUnits) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void drawShape() {
        Point3f p1 = points.get(0);
        Point3f p2 = new Point3f((float) (p1.x + this.getGrid().pixelSize), p1.y, p1.z);
        LittleMeasurementBox.drawCube(p1, this.getGrid().size, 1F, 0F, 0F, 1.0F);
        LittleMeasurementBox.drawCube(p2, this.getGrid().size, 0F, 1F, 0F, 1.0F);
        drawLine(p1, p2, this.getGrid().size, this.getRed(), this.getGreen(), this.getBlue(), this.getAlpha());
    }
    
    @Override
    protected List<String> getMeasurementUnits(HashMap<Integer, Point3f> points) {
        
        ArrayList list = new ArrayList<String>();
        list.add("");
        return list;
    }
    
    @Override
    public List<GuiControl> getCustomSettings(ItemStack tapeMeasure) {
        List<GuiControl> controls = new ArrayList<>();
        /*
        controls.add(new GuiDepressedCheckBox("north", "N", 19, 7, 17, 17, "", facing.equals("north")));
        controls.add(new GuiDepressedCheckBox("east", "E", 36, 23, 17, 17, "", facing.equals("east")));
        controls.add(new GuiDepressedCheckBox("south", "S", 19, 39, 17, 17, "", facing.equals("south")));
        controls.add(new GuiDepressedCheckBox("west", "W", 2, 23, 17, 17, "", facing.equals("west")));
        controls.add(new GuiDepressedCheckBox("bottom", "B", 19, 23, 17, 17, "", facing.equals("bottom")));
        controls.add(new GuiDepressedCheckBox("top", "T", 53, 23, 17, 17, "", facing.equals("top")));*/
        return controls;
    }
    
    @Override
    public boolean customSettingsChangedEvent(GuiControlChangedEvent event, ItemStack tapeMeasure) {
        if (event.source instanceof GuiDepressedCheckBox) {
            GuiDepressedCheckBox north = (GuiDepressedCheckBox) event.source.parent.get("north");
            GuiDepressedCheckBox east = (GuiDepressedCheckBox) event.source.parent.get("east");
            GuiDepressedCheckBox south = (GuiDepressedCheckBox) event.source.parent.get("south");
            GuiDepressedCheckBox west = (GuiDepressedCheckBox) event.source.parent.get("west");
            GuiDepressedCheckBox bottom = (GuiDepressedCheckBox) event.source.parent.get("bottom");
            GuiDepressedCheckBox top = (GuiDepressedCheckBox) event.source.parent.get("top");
            String facing = event.source.name;
            
            north.value = facing.equals("north");
            east.value = facing.equals("east");
            south.value = facing.equals("south");
            west.value = facing.equals("west");
            bottom.value = facing.equals("bottom");
            top.value = facing.equals("top");
            
            return true;
        }
        return false;
    }
    
    @Override
    public List<GuiControl> customSettingsUpdateControl(ItemStack tapeMeasure, boolean createControls) {
        List<GuiControl> controls = createControls ? this.getCustomSettings(tapeMeasure) : new ArrayList<>();
        
        return controls;
    }
}
