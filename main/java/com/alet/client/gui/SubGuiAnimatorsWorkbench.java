package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.premade.LittleAnimatorBench;
import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.creativemd.creativecore.common.gui.ContainerControl;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.container.SlotControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.GuiTimeline;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel.TimelineChannelDouble;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;

import net.minecraft.item.ItemStack;

public class SubGuiAnimatorsWorkbench extends SubGui {
    LittleAnimatorBench structure;
    public AnimationGuiHandler handler = new AnimationGuiHandler();
    
    public SubGuiAnimatorsWorkbench(LittleStructure structure) {
        this.structure = (LittleAnimatorBench) structure;
        this.width = 500;
        this.height = 300;
    }
    
    @Override
    public void createControls() {
        SubContainer container = this.container;
        
        GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 263, 0, 225, 224);
        controls.add(viewer);
        viewer.moveViewPort(0, 50);
        
        List<TimelineChannel> channels = new ArrayList<>();
        channels.add(new TimelineChannelDouble("door1 X"));
        
        controls.add(new GuiTimeline("timeline", 0, 0, 255, 67, 10, channels, handler).setSidebarWidth(30));
        addControl(new GuiButton("refresh", "Refresh", 0, 265, 0, 10) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                SlotControl slot = (SlotControl) container.get("input0");
                for (ContainerControl c : container.controls) {
                    System.out.println(c.name);
                }
                ItemStack stack = slot.slot.getStack();
                if (stack != null) {
                    System.out.println(stack);
                    viewer.onLoaded(new AnimationPreview(LittlePreview.getPreview(stack)));
                }
            }
        });
        
    }
    
}
