package com.alet.common.utils.shape.tapemeasure;

import java.util.List;

import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.util.math.Vec3d;

public class Compass extends TapeMeasureShape {
    
    public Compass(List<Vec3d> listOfPoints, LittleGridContext context) {
        super(listOfPoints, context);
        this.pointsNeeded = 3;
    }
    
    public static String degree;
    
    @Override
    protected void getText(GuiOverlayTextList textList, int colorInt) {
        textList.addText(degree, colorInt);
    }
    
    @Override
    protected void drawShape(float red, float green, float blue, float alpha, List<SelectLittleTile> listOfTilePos) {
        SelectLittleTile tilePos1 = listOfTilePos.get(0);
        SelectLittleTile tilePos2 = listOfTilePos.get(1);
        SelectLittleTile tilePos3 = listOfTilePos.get(2);
        Line.drawLine(tilePos1, tilePos2, red, green, blue, alpha);
        Line.drawLine(tilePos2, tilePos3, red, green, blue, alpha);
        Box.drawBox(tilePos1, context.size, 1.0F, 0.0F, 0.0F, alpha);
        Box.drawBox(tilePos2, context.size, 0.0F, 1.0F, 0.0F, alpha);
        Box.drawBox(tilePos3, context.size, 0.0F, 0.0F, 1.0F, alpha);
        
    }
    
    @Override
    public void calculateDistance() {
        Vec3d C = listOfPoints.get(0); //is angle C
        Vec3d A = listOfPoints.get(1); //is angle A
        Vec3d B = listOfPoints.get(2); //is angle B
        
        double a = C.distanceTo(B);
        double b = A.distanceTo(C);
        double c = A.distanceTo(B);
        
        double a2 = Math.pow(a, 2);
        double b2 = Math.pow(b, 2);
        double c2 = Math.pow(c, 2);
        
        double val = (b2 + c2 - a2) / (2 * b * c);
        
        degree = "Degrees: " + cleanDouble(Math.toDegrees(Math.acos(val))) + "�";
        
    }
    
}
