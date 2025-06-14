package com.alet.common.gui.structure;

import com.alet.components.structures.type.LittleAdvancedSeat;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiIconButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.littletiles.client.gui.controls.GuiTileViewer;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureAbsolute;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.type.door.LittleAdvancedDoor;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleAdvancedSeatGui extends LittleStructureGuiParser {
    
    public LittleAdvancedSeatGui(GuiParent parent, AnimationGuiHandler handler) {
        super(parent, handler);
    }
    
    @Override
    protected void createControls(LittlePreviews previews, LittleStructure structure) {
        LittleAdvancedDoor door = structure instanceof LittleAdvancedDoor ? (LittleAdvancedDoor) structure : null;
        
        GuiTileViewer viewer = new GuiTileViewer("tileviewer", 0, 0, 100, 100, previews.getContext());
        setViewer(viewer, door, previews.getContext());
        parent.controls.add(viewer);
        parent.controls.add(new GuiIconButton("reset view", 20, 107, 8) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                viewer.offsetX.set(0);
                viewer.offsetY.set(0);
                viewer.scale.set(40);
            }
        }.setCustomTooltip("reset view"));
        parent.controls.add(new GuiIconButton("change view", 40, 107, 7) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                switch (viewer.getAxis()) {
                    case X:
                        viewer.setViewAxis(EnumFacing.Axis.Y);
                        break;
                    case Y:
                        viewer.setViewAxis(EnumFacing.Axis.Z);
                        break;
                    case Z:
                        viewer.setViewAxis(EnumFacing.Axis.X);
                        break;
                    default:
                        break;
                }
            }
        }.setCustomTooltip("change view"));
        parent.controls.add(new GuiIconButton("flip view", 60, 107, 4) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                viewer.setViewDirection(viewer.getViewDirection().getOpposite());
            }
        }.setCustomTooltip("flip view"));
        
        parent.controls.add(new GuiIconButton("up", 124, 33, 1) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                viewer.moveY(GuiScreen.isCtrlKeyDown() ? viewer.context.size : 1);
            }
        });
        
        parent.controls.add(new GuiIconButton("right", 141, 50, 0) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                viewer.moveX(GuiScreen.isCtrlKeyDown() ? viewer.context.size : 1);
            }
        });
        
        parent.controls.add(new GuiIconButton("left", 107, 50, 2) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                viewer.moveX(-(GuiScreen.isCtrlKeyDown() ? viewer.context.size : 1));
            }
        });
        
        parent.controls.add(new GuiIconButton("down", 124, 50, 3) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                viewer.moveY(-(GuiScreen.isCtrlKeyDown() ? viewer.context.size : 1));
            }
        });
        parent.controls.add(new GuiCheckBox("even", 107, 0, viewer.isEven()));
        
        GuiStateButton contextBox = new GuiStateButton("grid", LittleGridContext.getNames().indexOf(viewer
                .getAxisContext() + ""), 107, 80, 20, 12, LittleGridContext.getNames().toArray(new String[0]));
        parent.controls.add(contextBox);
    }
    
    private void setViewer(GuiTileViewer viewer, LittleAdvancedDoor door, LittleGridContext axisContext) {
        viewer.visibleAxis = true;
        boolean even = false;
        if (door != null) {
            even = door.axisCenter.isEven();
            viewer.setEven(even);
            
            door.axisCenter.convertToSmallest();
            axisContext = door.axisCenter.getContext();
            viewer.setAxis(door.axisCenter.getBox(), axisContext);
            
        } else {
            viewer.setEven(false);
            viewer.setAxis(new LittleBox(0, 0, 0, 1, 1, 1), viewer.context);
        }
        handler.setCenter(new StructureAbsolute(new BlockPos(0, 75, 0), viewer.getBox().copy(), viewer.getAxisContext()));
    }
    
    @CustomEventSubscribe
    @SideOnly(Side.CLIENT)
    public void onButtonClicked(GuiControlClickEvent event) {
        GuiTileViewer viewer = (GuiTileViewer) event.source.parent.get("tileviewer");
        if (event.source.is("even")) {
            viewer.setEven(((GuiCheckBox) event.source).value);
        }
    }
    
    @CustomEventSubscribe
    @SideOnly(Side.CLIENT)
    public void onStateChange(GuiControlChangedEvent event) {
        if (event.source.is("grid")) {
            GuiStateButton contextBox = (GuiStateButton) event.source;
            LittleGridContext context;
            try {
                context = LittleGridContext.get(Integer.parseInt(contextBox.getCaption()));
            } catch (NumberFormatException e) {
                context = LittleGridContext.get();
            }
            
            GuiTileViewer viewer = (GuiTileViewer) event.source.parent.get("tileviewer");
            LittleBox box = viewer.getBox();
            box.convertTo(viewer.getAxisContext(), context);
            
            if (viewer.isEven())
                box.maxX = box.minX + 2;
            else
                box.maxX = box.minX + 1;
            
            if (viewer.isEven())
                box.maxY = box.minY + 2;
            else
                box.maxY = box.minY + 1;
            
            if (viewer.isEven())
                box.maxZ = box.minZ + 2;
            else
                box.maxZ = box.minZ + 1;
            
            viewer.setAxis(box, context);
        }
    }
    
    @Override
    protected LittleStructure parseStructure(LittlePreviews previews) {
        LittleAdvancedSeat structure = createStructure(LittleAdvancedSeat.class, null);
        GuiTileViewer viewer = ((GuiTileViewer) parent.get("tileviewer"));
        structure.axisCenter = new StructureRelative(viewer.getBox(), viewer.getAxisContext());
        
        return structure;
    }
    
    @Override
    protected LittleStructureType getStructureType() {
        return LittleStructureRegistry.getStructureType(LittleAdvancedSeat.class);
    }
    
}
