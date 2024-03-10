package com.alet.common.utils.shape.tapemeasure;

import java.util.List;

import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.tiles.SelectLittleTile;

import net.minecraft.util.math.Vec3d;

public class SpacedPattern extends TapeMeasureShape {
    
    public SpacedPattern(List<Vec3d> listOfPoints, int contextSz) {
        super(listOfPoints, contextSz);
    }
    
    public SpacedPattern() {
        
    }
    
    @Override
    public void getText(GuiOverlayTextList textList, int colorInt) {
        
    }
    
    @Override
    public void drawShape(List<SelectLittleTile> listOfTilePos, int contextSize, float red, float green, float blue, float alpha) {
        
    }
    
    @Override
    protected void calculateDistance(List<Vec3d> listOfPoints, int contextSize) {
        
    }
    
}
