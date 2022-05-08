package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.GuiTimelineALET;
import com.alet.client.gui.controls.GuiTimelineALET.KeyALETDeselectedEvent;
import com.alet.client.gui.controls.GuiTimelineALET.KeyALETSelectedEvent;
import com.alet.client.gui.controls.GuiTree;
import com.alet.client.gui.controls.GuiTreePart;
import com.alet.client.gui.controls.GuiTreePart.EnumPartType;
import com.alet.client.gui.controls.KeyControlALET;
import com.alet.client.gui.controls.TimelineChannelALET;
import com.alet.client.gui.controls.TimelineChannelALET.TimelineChannelDoorData;
import com.alet.common.structure.type.premade.LittleAnimatorBench;
import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.container.SlotControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiIconButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SubGuiAnimatorsWorkbench extends SubGui {
    LittleAnimatorBench structure;
    public AnimationGuiHandler handler = new AnimationGuiHandler();
    private KeyControlALET selected = null;
    
    public SubGuiAnimatorsWorkbench(LittleStructure structure) {
        this.structure = (LittleAnimatorBench) structure;
        this.width = 500;
        this.height = 300;
    }
    
    @CustomEventSubscribe
    public void controlChanged(GuiControlChangedEvent event) {
        if (event.source.name.equals("maxTick")) {
            String dur = ((GuiTextfield) this.get("maxTick")).text;
            int duration = 1;
            if (!dur.equals(""))
                duration = Integer.parseInt(dur);
            ((GuiTimelineALET) this.get("timeline")).setDuration(duration);
        }
        if (event.source instanceof GuiTextfield) {
            GuiTextfield text = (GuiTextfield) event.source;
            if (selected != null) {
                double[] data = (double[]) selected.value;
                if (!text.text.equals(""))
                    if (text.name.equals("offX"))
                        data[0] = Double.parseDouble(text.text);
                    else if (text.name.equals("offY"))
                        data[1] = Double.parseDouble(text.text);
                    else if (text.name.equals("offZ"))
                        data[2] = Double.parseDouble(text.text);
                    else if (text.name.equals("rotX"))
                        data[3] = Double.parseDouble(text.text);
                    else if (text.name.equals("rotY"))
                        data[4] = Double.parseDouble(text.text);
                    else if (text.name.equals("rotZ"))
                        data[5] = Double.parseDouble(text.text);
            }
        }
    }
    
    @CustomEventSubscribe
    @SideOnly(Side.CLIENT)
    public void onKeySelected(KeyALETSelectedEvent event) {
        KeyControlALET key = (KeyControlALET) event.source;
        selected = key;
        System.out.println(key.value.hashCode());
        double[] data = (double[]) key.value;
        GuiTextfield offX = (GuiTextfield) get("offX");
        GuiTextfield offY = (GuiTextfield) get("offY");
        GuiTextfield offZ = (GuiTextfield) get("offZ");
        
        GuiTextfield rotX = (GuiTextfield) get("rotX");
        GuiTextfield rotY = (GuiTextfield) get("rotY");
        GuiTextfield rotZ = (GuiTextfield) get("rotZ");
        offX.text = data[0] + "";
        offY.text = data[1] + "";
        offZ.text = data[2] + "";
        
        rotX.text = data[3] + "";
        rotY.text = data[4] + "";
        rotZ.text = data[5] + "";
        
        offX.setCursorPositionEnd();
        offY.setCursorPositionEnd();
        offZ.setCursorPositionEnd();
        rotX.setCursorPositionEnd();
        rotY.setCursorPositionEnd();
        rotZ.setCursorPositionEnd();
        offX.enabled = true;
        offY.enabled = true;
        offZ.enabled = true;
        
        rotX.enabled = true;
        rotY.enabled = true;
        rotZ.enabled = true;
    }
    
    @CustomEventSubscribe
    @SideOnly(Side.CLIENT)
    public void onKeyDeselected(KeyALETDeselectedEvent event) {
        selected = null;
        GuiTextfield offX = (GuiTextfield) get("offX");
        GuiTextfield offY = (GuiTextfield) get("offY");
        GuiTextfield offZ = (GuiTextfield) get("offZ");
        
        GuiTextfield rotX = (GuiTextfield) get("rotX");
        GuiTextfield rotY = (GuiTextfield) get("rotY");
        GuiTextfield rotZ = (GuiTextfield) get("rotZ");
        offX.text = "";
        offY.text = "";
        offZ.text = "";
        
        rotX.text = "";
        rotY.text = "";
        rotZ.text = "";
        
        offX.setCursorPositionEnd();
        offY.setCursorPositionEnd();
        offZ.setCursorPositionEnd();
        rotX.setCursorPositionEnd();
        rotY.setCursorPositionEnd();
        rotZ.setCursorPositionEnd();
        offX.enabled = false;
        offY.enabled = false;
        offZ.enabled = false;
        
        rotX.enabled = false;
        rotY.enabled = false;
        rotZ.enabled = false;
    }
    
    @Override
    public void createControls() {
        SubContainer container = this.container;
        
        GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 307, 0, 181, 180);
        controls.add(viewer);
        viewer.moveViewPort(0, 109);
        GuiScrollBox scroll = new GuiScrollBox("scroll", 0, 0, 135, 180);
        controls.add(scroll);
        List<GuiTreePart> list = new ArrayList<GuiTreePart>();
        scroll.controls.add(new GuiTree("tree", 0, 0, 130, list, true, 0, 0, 50));
        
        GuiScrollBox scrollTimeline = new GuiScrollBox("scrollTimeline", 20, 187, 468, 101);
        controls.add(scrollTimeline);
        List<TimelineChannelALET> channels = new ArrayList<>();
        scrollTimeline.controls.add(new GuiTimelineALET("timeline", -1, -1, 462, 1000, 10, channels, handler).setSidebarWidth(32));
        
        controls.add(new GuiLabel("Tick At:", 219, 173));
        controls.add(new GuiTextfield("tickAt", "", 258, 174, 40, 6));
        
        controls.add(new GuiLabel("Ticks:", 140, 173));
        controls.add(new GuiTextfield("maxTick", "10", 172, 174, 40, 6));
        
        controls.add(new GuiLabel("Offset(X):", 203, 83));
        controls.add(new GuiLabel("Offset(Y):", 203, 97));
        controls.add(new GuiLabel("Offset(Z):", 203, 110));
        //13
        controls.add(new GuiTextfield("offX", "", 258, 84, 40, 6).setFloatOnly());
        controls.add(new GuiTextfield("offY", "", 258, 98, 40, 6).setFloatOnly());
        controls.add(new GuiTextfield("offZ", "", 258, 111, 40, 6).setFloatOnly());
        
        controls.add(new GuiLabel("Rotate(X):", 203, 127));
        controls.add(new GuiLabel("Rotate(Y):", 203, 140));
        controls.add(new GuiLabel("Rotate(Z):", 203, 153));
        
        controls.add(new GuiTextfield("rotX", "", 258, 128, 40, 6).setFloatOnly());
        controls.add(new GuiTextfield("rotY", "", 258, 141, 40, 6).setFloatOnly());
        controls.add(new GuiTextfield("rotZ", "", 258, 154, 40, 6).setFloatOnly());
        
        controls.add(new GuiIconButton("play", 0, 228, 10) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                handler.play();
            }
        });
        controls.add(new GuiIconButton("pause", 0, 252, 9) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                handler.pause();
            }
        });
        controls.add(new GuiIconButton("stop", 0, 276, 11) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                handler.stop();
            }
        });
        
        addControl(new GuiButton("save", "Save", 144, 122, 40) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                // TODO Auto-generated method stub
                
            }
        });
        
        addControl(new GuiButton("refresh", "Refresh", 144, 100, 40) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                SlotControl slot = (SlotControl) container.get("input0");
                ItemStack stack = slot.slot.getStack();
                if (stack != null) {
                    GuiTree tree = (GuiTree) this.parent.get("tree");
                    GuiTimelineALET timeline = (GuiTimelineALET) this.parent.get("timeline");
                    LittlePreviews previews = LittlePreview.getPreview(stack);
                    viewer.onLoaded(new AnimationPreview(previews));
                    String partName = addField(previews, timeline, tree, null);
                    GuiTreePart part = new GuiTreePart(partName, EnumPartType.Title);
                    for (LittlePreviews child : previews.getChildren()) {
                        String childPartName = addField(child, timeline, tree, part);
                        GuiTreePart childPart = new GuiTreePart(childPartName, EnumPartType.Branch);
                        part.addMenu(childPart);
                        collectStructures(child, timeline, tree, childPart);
                    }
                    List<GuiTreePart> list = new ArrayList<GuiTreePart>();
                    list.add(part);
                    tree.replaceTree(list);
                }
            }
            
            public String addField(LittlePreviews previews, GuiTimelineALET timeline, GuiTree tree, GuiTreePart part) {
                if (previews.getStructureName() != null) {
                    timeline.channels.add(new TimelineChannelDoorData(previews.getStructureName()));
                    return previews.getStructureName();
                } else {
                    timeline.channels.add(new TimelineChannelDoorData(previews.getStructureId()));
                    return previews.getStructureId();
                }
            }
            
            public void collectStructures(LittlePreviews child, GuiTimelineALET timeline, GuiTree tree, GuiTreePart part) {
                if (child.hasChildren()) {
                    for (LittlePreviews child2 : child.getChildren()) {
                        String childPartName = addField(child2, timeline, tree, part);
                        GuiTreePart childPart = new GuiTreePart(childPartName, EnumPartType.Leaf);
                        part.addMenu(childPart);
                    }
                }
                
            }
        });
        
    }
    
}
