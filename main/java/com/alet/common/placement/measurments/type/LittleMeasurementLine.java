package com.alet.common.placement.measurments.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Point3f;

import com.alet.ALETConfig;
import com.alet.components.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.item.ItemStack;

public class LittleMeasurementLine extends LittleMeasurementType {
    
    public LittleMeasurementLine(String name) {
        super(2, name);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected List<String> getMeasurementUnits(HashMap<Integer, Point3f> points) {
        List<String> units = new ArrayList<>();
        Point3f pos = points.get(0);
        Point3f pos2 = points.get(1);
        
        double xDist = getDistence(pos.x, pos2.x, this.getGrid().size);
        double yDist = getDistence(pos.y, pos2.y, this.getGrid().size);
        double zDist = getDistence(pos.z, pos2.z, this.getGrid().size);
        
        double dist = 0.0;
        if (xDist >= yDist && xDist >= zDist)
            dist = xDist;
        else if (yDist >= xDist && yDist >= zDist)
            dist = yDist;
        else if (zDist >= xDist && zDist >= yDist)
            dist = zDist;
        int denominator = this.getGrid().size;
        String[] distArr = String.valueOf(dist).split("\\.");
        double numerator = this.getGrid().size * Double.parseDouble("0." + distArr[1]);
        
        if (ItemTapeMeasure.measurementType == 0) {
            if ((int) (numerator) == 0)
                units.add(distArr[0] + " BLOCK");
            else if (Integer.parseInt(distArr[0]) == 0)
                units.add((int) (numerator) + "/" + denominator + " TILE");
            else
                units.add(distArr[0] + " BLOCK " + (int) (numerator) + "/" + denominator + " TILE");
            
        } else if (ItemTapeMeasure.measurementType == 1) {
            units.add(distArr[0] + numerator);
        } else {
            String measurementName = ALETConfig.tapeMeasure.measurementName.get(ItemTapeMeasure.measurementType - 1);
            double modifier = 1D / this.getGrid().size;
            units.add(cleanDouble(changeMesurmentType((Math.floor((pos.distance(pos2) + modifier) * this
                    .getGrid().size)) / this.getGrid().size)) + " " + measurementName);
        }
        return units;
        
    }
    
    @Override
    public List<GuiControl> getCustomSettings(ItemStack tapeMeasure) {
        List<GuiControl> controls = new ArrayList<>();
        return controls;
    }
    
    @Override
    public boolean customSettingsChangedEvent(GuiControlChangedEvent event, ItemStack tapeMeasure) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    protected void drawShape() {
        Point3f p1 = points.get(0);
        Point3f p2 = points.get(1);
        LittleMeasurementBox.drawCube(p1, this.getGrid().size, 1F, 0F, 0F, 1.0F);
        LittleMeasurementBox.drawCube(p2, this.getGrid().size, 0F, 1F, 0F, 1.0F);
        HashMap<Integer, Point3f> linePoints = drawLine(p1, p2, this.getGrid().size, this.getRed(), this.getBlue(), this
                .getRed(), this.getAlpha());
        drawText(linePoints, getMeasurementUnits(linePoints));
    }
    
    @Override
    protected void drawText(HashMap<Integer, Point3f> points, List<String> measurementUnits) {
        drawStringOnLine(measurementUnits.get(0), this.getGrid().size, DrawPosition.Middle, points.get(0), points.get(1),
            ColorUtils.WHITE, true, 0);
    }
    
    @Override
    public List<GuiControl> customSettingsUpdateControl(ItemStack tapeMeasure, boolean createControls) {
        return new ArrayList<>();
        
    }
}
