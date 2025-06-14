package com.alet.common.gui.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.alet.common.gui.controls.GuiColorPickerAlet;
import com.alet.common.gui.controls.GuiStackSelectorAllMutator;
import com.alet.common.gui.controls.GuiStackSelectorExtensionMutator;
import com.alet.common.gui.controls.menu.GuiMenu;
import com.alet.common.gui.controls.menu.GuiMenuPart;
import com.alet.common.gui.controls.menu.GuiPopupMenu;
import com.alet.common.gui.controls.menu.GuiTreePart;
import com.alet.common.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.common.utils.ColorUtilsAlet;
import com.alet.components.structures.type.LittleStateMutator;
import com.alet.components.structures.type.MutatorData;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.mc.BlockUtils;
import com.creativemd.littletiles.client.gui.LittleSubGuiUtils.LittleBlockSelector;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents.GuiSignalEventsButton;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleStateMutatorGui extends LittleStructureGuiParser {
    public LinkedHashMap<String, MutatorData> mutateMaterial = new LinkedHashMap<String, MutatorData>();
    protected CoreControl rightClickedControl;
    
    public LittleStateMutatorGui(GuiParent parent, AnimationGuiHandler handler) {
        super(parent, handler);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void create(LittlePreviews previews, @Nullable LittleStructure structure) {
        createControls(previews, structure);
        parent.controls.add(new GuiSignalEventsButton("signal", 0, 122, previews, structure, getStructureType()));
    }
    
    @Override
    protected void createControls(LittlePreviews previews, LittleStructure structure) {
        LittleStateMutator mutator = (LittleStateMutator) structure;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        
        if (player.isCreative()) {
            
            GuiScrollBox box = new GuiScrollBox("box", 0, 0, 294, 100);
            List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
            GuiMenuPart moveUp = new GuiMenuPart("", "Move Up", EnumPartType.Leaf);
            GuiMenuPart moveDown = new GuiMenuPart("", "Move Down", EnumPartType.Leaf);
            GuiMenuPart swap = new GuiMenuPart("", "Swap States", EnumPartType.Leaf);
            GuiMenuPart delete = new GuiMenuPart("", "Delete", EnumPartType.Leaf);
            listOfMenus.add(moveUp);
            listOfMenus.add(moveDown);
            listOfMenus.add(swap);
            listOfMenus.add(delete);
            GuiMenu menu = new GuiMenu("", 0, 0, 68, listOfMenus);
            menu.height = 100;
            
            GuiPopupMenu popup = new GuiPopupMenu("popup", menu, 0, 0, 0, 0);
            box.addControl(popup);
            GuiCheckBox check = new GuiCheckBox("check", "Allow Right Click", 0, 107, mutator != null ? mutator.allowRightClick : true);
            GuiButton add = new GuiButton("Add Mutation", 234, 111, 60) {
                @Override
                public void onClicked(int x, int y, int button) {
                    int size = (mutateMaterial.size() / 2);
                    addMutations(size, new MutatorData(Blocks.STONE.getDefaultState(), 0xFFFFFFFF, true),
                        new MutatorData(Blocks.STONE.getDefaultState(), 0xFFFFFFFF, true));
                }
            };
            parent.controls.add(box);
            if (mutator != null && !mutator.mutateMaterial.isEmpty()) {
                this.mutateMaterial = mutator.mutateMaterial;
                showExistingMutations();
            }
            parent.controls.add(add);
            parent.controls.add(check);
        } else
            parent.controls.add(new GuiTextBox("message", "These settings are only avalible in creative mode", 0, 0, 100));
    }
    
    protected void showExistingMutations() {
        int i = 0;
        for (Entry<String, MutatorData> entry : this.mutateMaterial.entrySet()) {
            if (entry.getKey().contains("a")) {
                addMutations(i, entry.getValue(), null);
            } else if (entry.getKey().contains("b")) {
                addMutations(i, null, entry.getValue());
                i++;
            }
        }
    }
    
    protected void swapMutations(int position) {
        List<MutatorData> tempList = new ArrayList<MutatorData>();
        for (Entry<String, MutatorData> entry : this.mutateMaterial.entrySet())
            tempList.add(entry.getValue());
        Collections.swap(tempList, position * 2, (position * 2) + 1);
        LinkedHashMap<String, MutatorData> mutateMaterialTemp = new LinkedHashMap<String, MutatorData>();
        int i = 0;
        int step = 0;
        for (MutatorData data : tempList) {
            if (step == 0) {
                mutateMaterialTemp.put("a" + i, data);
                step++;
            } else if (step == 1) {
                mutateMaterialTemp.put("b" + i, data);
                step = 0;
                i++;
            }
        }
        this.mutateMaterial = mutateMaterialTemp;
        refreshMutations();
    }
    
    protected void moveMutations(int distance, int position) {
        List<MutatorData> tempList = new ArrayList<MutatorData>();
        for (Entry<String, MutatorData> entry : this.mutateMaterial.entrySet())
            tempList.add(entry.getValue());
        Collections.swap(tempList, (position * 2), (position * 2) + (distance * 2));
        Collections.swap(tempList, (position * 2) + 1, (position * 2) + (distance * 2) + 1);
        LinkedHashMap<String, MutatorData> mutateMaterialTemp = new LinkedHashMap<String, MutatorData>();
        int i = 0;
        int step = 0;
        for (MutatorData data : tempList) {
            if (step == 0) {
                mutateMaterialTemp.put("a" + i, data);
                step++;
            } else if (step == 1) {
                mutateMaterialTemp.put("b" + i, data);
                step = 0;
                i++;
            }
        }
        this.mutateMaterial = mutateMaterialTemp;
        refreshMutations();
    }
    
    protected void refreshMutations() {
        int i = 0;
        GuiScrollBox box = (GuiScrollBox) this.parent.get("box");
        box.removeControls("popup");
        LinkedHashMap<String, MutatorData> mutateMaterialTemp = new LinkedHashMap<String, MutatorData>();
        for (Entry<String, MutatorData> entry : this.mutateMaterial.entrySet()) {
            if (entry.getKey().contains("a")) {
                mutateMaterialTemp.put("a" + i, entry.getValue());
            } else if (entry.getKey().contains("b")) {
                mutateMaterialTemp.put("b" + i, entry.getValue());
                i++;
            }
        }
        this.mutateMaterial = mutateMaterialTemp;
        showExistingMutations();
    }
    
    protected void addMutations(int position, @Nullable MutatorData value1, @Nullable MutatorData value2) {
        GuiScrollBox box = (GuiScrollBox) this.parent.get("box");
        GuiMutatorPanel panel = new GuiMutatorPanel(position + "", 0, 28 * position, 286, 21);
        
        if (value1 != null) {
            mutateMaterial.put("a" + position, value1);
            GuiStackSelectorAllMutator a = new GuiStackSelectorAllMutator("a" + position, 0, 0, 105, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true);
            panel.controls.add(a);
            IBlockState materialA = value1.state;
            a.setSelectedForce(materialA.getBlock().getPickBlock(materialA, null, null, null, null));
            a.color = value1.color;
            this.mutateMaterial.put("a" + position, value1);
        }
        if (value2 != null) {
            mutateMaterial.put("b" + position, value2);
            GuiStackSelectorAllMutator b = new GuiStackSelectorAllMutator("b" + position, 153, 0, 105, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true);
            panel.controls.add(b);
            IBlockState materialB = value2.state;
            b.setSelectedForce(materialB.getBlock().getPickBlock(materialB, null, null, null, null));
            b.color = value2.color;
            this.mutateMaterial.put("b" + position, value2);
        }
        
        panel.controls.add(new GuiLabel("To", 134, 2));
        panel.refreshControls();
        box.controls.add(panel);
        box.refreshControls();
    }
    
    @CustomEventSubscribe
    public void controlClicked(GuiControlClickEvent event) {
        if (event.button == 1 && event.source instanceof GuiMutatorPanel) {
            rightClickedControl = event.source;
        }
    }
    
    @CustomEventSubscribe
    public void controlChanged(GuiControlChangedEvent event) {
        if (event.source instanceof GuiStackSelectorAllMutator) {
            GuiStackSelectorAllMutator mutator = (GuiStackSelectorAllMutator) event.source;
            this.mutateMaterial.put(mutator.name, new MutatorData(BlockUtils.getState(mutator
                    .getSelected()), mutator.color, true));
        }
        if (event.source instanceof GuiColorPickerAlet) {
            GuiColorPickerAlet color = (GuiColorPickerAlet) event.source;
            GuiStackSelectorAllMutator mutator = (GuiStackSelectorAllMutator) ((GuiStackSelectorExtensionMutator) event.source.parent).comboBox;
            mutator.color = ColorUtilsAlet.RGBAToInt(color.color);
            this.mutateMaterial.put(mutator.name, new MutatorData(BlockUtils.getState(mutator.getSelected()), ColorUtilsAlet
                    .RGBAToInt(color.color), true));
        }
        if (event.source instanceof GuiMenuPart) {
            String caption = ((GuiMenuPart) event.source).caption;
            if (caption.equals("Move Up")) {
                this.moveMutations(-1, Integer.parseInt(rightClickedControl.name));
            } else if (caption.equals("Move Down")) {
                this.moveMutations(1, Integer.parseInt(rightClickedControl.name));
            } else if (caption.equals("Delete")) {
                this.mutateMaterial.remove("a" + rightClickedControl.name);
                this.mutateMaterial.remove("b" + rightClickedControl.name);
                refreshMutations();
            } else if (caption.equals("Swap States")) {
                this.swapMutations(Integer.parseInt(rightClickedControl.name));
            }
        }
    }
    
    @Override
    protected LittleStructure parseStructure(LittlePreviews previews) {
        LittleStateMutator structure = createStructure(LittleStateMutator.class, null);
        
        structure.mutateMaterial = this.mutateMaterial;
        structure.allowRightClick = ((GuiCheckBox) this.parent.get("check")).value;
        return structure;
    }
    
    @Override
    protected LittleStructureType getStructureType() {
        return LittleStructureRegistry.getStructureType(LittleStateMutator.class);
    }
    
    public class GuiMutatorPanel extends GuiPanel {
        
        public GuiMutatorPanel(String name, int x, int y, int width, int height) {
            super(name, x, y, width, height);
        }
        
        @Override
        public void mouseReleased(int x, int y, int button) {
            if (isMouseOver(x, y) && button == 1) {
                this.getParent().raiseEvent(new GuiControlClickEvent(this, x, y, button));
            }
        }
    }
}