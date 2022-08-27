package com.alet.client.gui.mutator.controls;

import com.alet.client.gui.controls.GuiStackSelectorAllMutator;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.littletiles.client.gui.LittleSubGuiUtils.LittleBlockSelector;

import net.minecraft.block.state.IBlockState;

public class GuiButtonAddMutationType extends GuiButton {
    
    public int depth = 0;
    public GuiScrollBox box;
    
    public GuiButtonAddMutationType(String caption, int x, int y, int width, GuiScrollBox box) {
        super(caption, x, y, width);
        this.box = box;
    }
    
    @Override
    public void onClicked(int x, int y, int button) {
        addMaterialMutation();
    }
    
    public void addMaterialMutation() {
        GuiMutatorPanel panel = new GuiMutatorPanel("panel" + depth / 28, 0, depth, 242, 21);
        panel.controls.add(new GuiStackSelectorAllMutator("a" + depth / 28, 0, 0, 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true));
        panel.controls.add(new GuiStackSelectorAllMutator("b" + depth / 28, 134, 0, 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true));
        
        panel.controls.add(new GuiLabel("To", 111, 2));
        panel.refreshControls();
        box.controls.add(panel);
        box.refreshControls();
        
        depth += 28;
    }
    
    public void addMaterialMutation(IBlockState materialA, IBlockState materialB, boolean colisionA, boolean colisionB, int colorA, int colorB) {
        GuiStackSelectorAllMutator a = new GuiStackSelectorAllMutator("a" + depth / 28, 0, (depth), 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true);
        box.controls.add(a);
        GuiStackSelectorAllMutator b = new GuiStackSelectorAllMutator("b" + depth / 28, 134, (depth), 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true);
        box.controls.add(b);
        box.controls.add(new GuiLabel("To", 111, (depth) + 2));
        a.setSelectedForce(materialA.getBlock().getPickBlock(materialA, null, null, null, null));
        a.noclip = colisionA;
        a.color = colorA;
        
        b.setSelectedForce(materialB.getBlock().getPickBlock(materialB, null, null, null, null));
        b.noclip = colisionB;
        b.color = colorB;
        box.refreshControls();
        
        depth += 28;
    }
    
    public class GuiMutatorPanel extends GuiPanel {
        
        public GuiMutatorPanel(String name, int x, int y, int width, int height) {
            super(name, x, y, width, height);
            // TODO Auto-generated constructor stub
        }
        
        @Override
        public void mouseReleased(int x, int y, int button) {
            if (isMouseOver(x, y) && button == 1) {
                
                GuiButtonAddMutationType.this.raiseEvent(new GuiControlClickEvent(this, x, y, button));
            }
        }
    }
}
