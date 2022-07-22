package com.alet.common.util.shape;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.math.box.LittleBoxes;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.shape.ShapeSelection;
import com.creativemd.littletiles.common.util.shape.ShapeSelection.ShapeSelectPos;
import com.creativemd.littletiles.common.util.shape.type.LittleShapeSelectable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;

public class DragShapeFill extends LittleShapeSelectable {
    private ShapeSelection selection;
    
    public DragShapeFill() {
        super(1);
    }
    
    @Override
    protected void addBoxes(LittleBoxes boxes, ShapeSelection selection, boolean lowResolution) {
        this.selection = selection;
        findTile(boxes, 6, 4);
    }
    
    private boolean findTile(LittleBoxes boxes, int x, int y) {
        for (ShapeSelectPos pos : this.selection) {
            if (pos.result.isComplete()) {
                if (pos.result.parent.isStructure())
                    continue;
                for (LittleTile tile : pos.result.te.noneStructureTiles()) {
                    LittleBox box = tile.getBox();
                    switch (pos.ray.sideHit) {
                    case NORTH:
                        
                        break;
                    case EAST:
                        
                        break;
                    case SOUTH:
                        
                        break;
                    case WEST:
                        
                        break;
                    case UP:
                        if (LittleBox.intersectsWith(box, new LittleBox(new LittleVec(x, box.minY + 1, y)))) {
                            System.out.println("found");
                        }
                        break;
                    case DOWN:
                        
                        break;
                    
                    default:
                        break;
                    }
                    System.out.println(tile.getBox());
                }
            }
        }
        return false;
    }
    
    @Override
    public void addExtraInformation(NBTTagCompound nbt, List<String> list) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context) {
        List<GuiControl> controls = new ArrayList<>();
        return controls;
    }
    
    @Override
    public void saveCustomSettings(GuiParent gui, NBTTagCompound nbt, LittleGridContext context) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void rotate(NBTTagCompound nbt, Rotation rotation) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void flip(NBTTagCompound nbt, Axis axis) {
        // TODO Auto-generated method stub
        
    }
    
}
