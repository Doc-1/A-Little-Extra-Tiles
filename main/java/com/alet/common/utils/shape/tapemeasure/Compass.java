package com.alet.common.utils.shape.tapemeasure;

import java.util.List;

import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.tiles.SelectLittleTile;

import net.minecraft.util.math.Vec3d;

public class Compass extends TapeMeasureShape {
    
    public static String degree;
    
    public Compass(List<Vec3d> listOfPoints, int contextSz) {
        super(listOfPoints, contextSz);
    }
    
    public Compass() {
        
    }
    
    @Override
    public void getText(GuiOverlayTextList textList, int colorInt) {
        textList.addText(degree, colorInt);
    }
    
    @Override
    public void drawShape(List<SelectLittleTile> listOfTilePos, int contextSize, float red, float green, float blue, float alpha) {
        SelectLittleTile tilePos1 = listOfTilePos.get(0);
        SelectLittleTile tilePos2 = listOfTilePos.get(1);
        SelectLittleTile tilePos3 = listOfTilePos.get(2);
        Line.drawLine(tilePos1, tilePos2, red, green, blue, alpha);
        Line.drawLine(tilePos2, tilePos3, red, green, blue, alpha);
        Box.drawBox(tilePos1, contextSize, 1.0F, 0.0F, 0.0F, alpha);
        Box.drawBox(tilePos2, contextSize, 0.0F, 1.0F, 0.0F, alpha);
        Box.drawBox(tilePos3, contextSize, 0.0F, 0.0F, 1.0F, alpha);
        
    }
    
    @Override
    protected void calculateDistance(List<Vec3d> listOfPoints, int contextSize) {
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
