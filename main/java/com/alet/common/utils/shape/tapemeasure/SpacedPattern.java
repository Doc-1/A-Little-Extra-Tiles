package com.alet.common.utils.shape.tapemeasure;

import java.util.List;

import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.util.math.Vec3d;

public class SpacedPattern extends TapeMeasureShape {
    
    public SpacedPattern(List<Vec3d> listOfPoints, LittleGridContext context) {
        super(listOfPoints, context);
        this.pointsNeeded = 2;
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void getText(GuiOverlayTextList textList, int colorInt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void drawShape(float red, float green, float blue, float alpha, List<SelectLittleTile> listOfTilePos) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void calculateDistance() {
        // TODO Auto-generated method stub
        
    }
    
}
