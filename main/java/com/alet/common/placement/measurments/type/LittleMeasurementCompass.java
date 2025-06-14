package com.alet.common.placement.measurments.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.vecmath.Point3f;

import com.alet.common.placement.measurments.LittleMeasurementRegistry;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.item.ItemStack;

public class LittleMeasurementCompass extends LittleMeasurementType {
    
    public LittleMeasurementCompass(String name) {
        super(3, name);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void drawShape() {
        Point3f p1 = points.get(0);
        Point3f p2 = points.get(1);
        Point3f p3 = points.get(2);
        int size = this.getGrid().size;
        LittleMeasurementLine.drawLine(p1, p2, size, this.getRed(), this.getGreen(), this.getBlue(), this.getAlpha());
        LittleMeasurementLine.drawLine(p2, p3, size, this.getRed(), this.getGreen(), this.getBlue(), this.getAlpha());
        LittleMeasurementBox.drawCube(p1, size, 1.0F, 0.0F, 0.0F, this.getAlpha());
        LittleMeasurementBox.drawCube(p2, size, 0.0F, 1.0F, 0.0F, this.getAlpha());
        LittleMeasurementBox.drawCube(p3, size, 0.0F, 0.0F, 1.0F, this.getAlpha());
        drawText(points, getMeasurementUnits(points));
    }
    
    @Override
    protected List<String> getMeasurementUnits(HashMap<Integer, Point3f> points) {
        List<String> units = new ArrayList<>();
        Point3f C = points.get(0); //is angle C
        Point3f A = points.get(1); //is angle A
        Point3f B = points.get(2); //is angle B
        
        double a = C.distance(B);
        double b = A.distance(C);
        double c = A.distance(B);
        
        double a2 = Math.pow(a, 2);
        double b2 = Math.pow(b, 2);
        double c2 = Math.pow(c, 2);
        
        double val = (b2 + c2 - a2) / (2 * b * c);
        HashMap<Integer, Point3f> CA = new HashMap<>();
        CA.put(0, C);
        CA.put(1, A);
        HashMap<Integer, Point3f> AB = new HashMap<>();
        AB.put(0, A);
        AB.put(1, B);
        units.add("Degrees: " + cleanDouble(Math.toDegrees(Math.acos(val))));
        units.addAll(LittleMeasurementRegistry.getMeasurementShape("Line").tryGetMeasurementUnits(CA));
        units.addAll(LittleMeasurementRegistry.getMeasurementShape("Line").tryGetMeasurementUnits(AB));
        
        return units;
    }
    
    @Override
    public List<GuiControl> getCustomSettings(ItemStack tapeMeasure) {
        List<GuiControl> controls = new ArrayList<>();
        return controls;
    }
    
    @Override
    protected void drawText(HashMap<Integer, Point3f> points, List<String> measurementUnits) {
        drawStringOnLine(measurementUnits.get(0), this.getGrid().size, DrawPosition.Middle, points.get(1), points.get(1),
            ColorUtils.WHITE, true, 0);
        drawStringOnLine(measurementUnits.get(1), this.getGrid().size, DrawPosition.Middle, points.get(0), points.get(1),
            ColorUtils.WHITE, true, 0);
        drawStringOnLine(measurementUnits.get(2), this.getGrid().size, DrawPosition.Middle, points.get(1), points.get(2),
            ColorUtils.WHITE, true, 0);
        
    }
    
    @Override
    public boolean customSettingsChangedEvent(GuiControlChangedEvent event, ItemStack tapeMeasure) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public List<GuiControl> customSettingsUpdateControl(ItemStack tapeMeasure, boolean createControls) {
        return new ArrayList<>();
        
    }
}
