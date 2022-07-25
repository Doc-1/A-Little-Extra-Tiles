package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.GuiTimelineALET;
import com.alet.client.gui.controls.KeyControlALET;
import com.alet.client.gui.controls.TimelineChannelALET;
import com.alet.client.gui.controls.TimelineChannelALET.TimelineChannelDoorData;
import com.alet.client.gui.controls.menu.GuiTree;
import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.container.SlotControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel.TimelineChannelDouble;
import com.creativemd.creativecore.common.gui.premade.SubContainerEmpty;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.packet.gui.GuiLayerPacket;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;
import com.creativemd.littletiles.client.gui.SubGuiRecipe.LoadingThread;
import com.creativemd.littletiles.client.gui.controls.IAnimationControl;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiDoorEvents;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.animation.AnimationKey;
import com.creativemd.littletiles.common.structure.animation.AnimationTimeline;
import com.creativemd.littletiles.common.structure.animation.ValueTimeline;
import com.creativemd.littletiles.common.structure.animation.event.AnimationEvent;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.type.door.LittleAdvancedDoor;
import com.creativemd.littletiles.common.structure.type.door.LittleDoorBase;
import com.creativemd.littletiles.common.tile.parent.StructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiAnimationWorkbench2 extends SubGui implements IAnimationControl {
    
    public LittleAdvancedDoor structure;
    public LittlePreviews previews;
    public AnimationGuiHandler handler = new AnimationGuiHandler();
    private KeyControlALET keySelected = null;
    protected LoadingThread loadingThread;
    public LittleStructureGuiParser parser;
    public AnimationPreview animationPreview;
    
    @Override
    public void createControls() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onLoaded(AnimationPreview preview) {
        // TODO Auto-generated method stub
        
    }
    
    public class GuiRefreshButton extends GuiButton {
        
        public GuiRefreshButton(String name, int x, int y) {
            super(name, "Refresh", x, y, 40);
        }
        
        @Override
        public void onClicked(int x, int y, int button) {
            SubGuiAnimationWorkbench2 parent = (SubGuiAnimationWorkbench2) this.parent;
            SlotControl slot = (SlotControl) container.get("input0");
            ItemStack stack = slot.slot.getStack();
            if (stack != null && !stack.equals(ItemStack.EMPTY)) {
                LittlePreviews previews = LittlePreview.getPreview(stack);
                if (!previews.isEmpty()) {
                    GuiTree tree = (GuiTree) parent.get("tree");
                    GuiTimelineALET timeline = (GuiTimelineALET) parent.get("timeline");
                    LittleAdvancedDoor structure = (LittleAdvancedDoor) StructureTileList.create(previews.structureNBT, null);
                    GuiDoorEventsButtonALET children = (GuiDoorEventsButtonALET) parent.get("children_activate");
                    
                    children.update(previews, parent.structure);
                    String partName = addField(previews, timeline, null);
                    GuiTreePart part = new GuiTreePart(partName, EnumPartType.Title);
                    for (LittlePreviews child : previews.getChildren()) {
                        String childPartName = addField(child, timeline, part);
                        GuiTreePart childPart = new GuiTreePart(childPartName, EnumPartType.Branch);
                        part.addMenu(childPart);
                        collectStructures(child, timeline, childPart);
                    }
                    List<GuiTreePart> list = new ArrayList<GuiTreePart>();
                    list.add(part);
                    tree.replaceTree(list);
                    AnimationPreview anim = new AnimationPreview(previews);
                    //viewer.onLoaded(anim);
                    //loadStack(getHolder(previews, stack), previews, anim);
                }
            }
        }
        
        public void collectStructures(LittlePreviews child, GuiTimelineALET timeline, GuiTreePart part) {
            if (child.hasChildren()) {
                for (LittlePreviews child2 : child.getChildren()) {
                    String childPartName = addField(child2, timeline, part);
                    GuiTreePart childPart = new GuiTreePart(childPartName, EnumPartType.Leaf);
                    part.addMenu(childPart);
                }
            }
        }
        
        public String addField(LittlePreviews previews, GuiTimelineALET timeline, GuiTreePart part) {
            
            LittleAdvancedDoor structure = (LittleAdvancedDoor) StructureTileList.create(previews.structureNBT, null);
            PairList<Integer, double[]> keys = collectKeys(structure, timeline);
            
            if (previews.getStructureName() != null) {
                return field(previews.getStructureName(), timeline, keys);
            } else {
                return field(previews.getStructureId(), timeline, keys);
            }
        }
        
        public String field(String name, GuiTimelineALET timeline, PairList<Integer, double[]> keys) {
            TimelineChannelALET line = new TimelineChannelDoorData(name);
            line.index = timeline.channels.size();
            line.addKeys(keys);
            timeline.channels.add(line);
            timeline.refreshChannels();
            //updateTimeLine(timeline, previews);
            return name;
        }
        
        public void updateEvents() {
            
        }
        
        public void updateTimeline(GuiTimelineALET timeline, LittlePreviews previews) {
            
            LittleGridContext context = previews.getContext();
            GuiDoorEventsButtonALET children = (GuiDoorEventsButtonALET) parent.get("children_activate");
            GuiStateButton interpolationButton = (GuiStateButton) parent.get("interpolation");
            int interpolation = interpolationButton.getState();
            TimelineChannelDoorData door = (TimelineChannelDoorData) timeline.channels.get(0);
            AnimationTimeline animation = new AnimationTimeline(timeline.getDuration(), new PairList<>());
            
            if (door.getPairs() != null) {
                TimelineChannelDouble chOffX = new TimelineChannelDouble("offX");
                TimelineChannelDouble chOffY = new TimelineChannelDouble("offY");
                TimelineChannelDouble chOffZ = new TimelineChannelDouble("offZ");
                
                TimelineChannelDouble chRotX = new TimelineChannelDouble("rotX");
                TimelineChannelDouble chRotY = new TimelineChannelDouble("rotY");
                TimelineChannelDouble chRotZ = new TimelineChannelDouble("rotZ");
                
                for (Pair<Integer, double[]> pair : door.getPairs()) {
                    chOffX.addKey(pair.key, pair.value[0]);
                    chOffY.addKey(pair.key, pair.value[1]);
                    chOffZ.addKey(pair.key, pair.value[2]);
                    
                    chRotX.addKey(pair.key, pair.value[3]);
                    chRotY.addKey(pair.key, pair.value[4]);
                    chRotZ.addKey(pair.key, pair.value[5]);
                    
                }
                valueTimeline(interpolation, AnimationKey.offX, chOffX.getPairs(), animation.values);
                valueTimeline(interpolation, AnimationKey.offY, chOffY.getPairs(), animation.values);
                valueTimeline(interpolation, AnimationKey.offZ, chOffZ.getPairs(), animation.values);
                
                valueTimeline(interpolation, AnimationKey.rotX, chRotX.getPairs(), animation.values);
                valueTimeline(interpolation, AnimationKey.rotY, chRotY.getPairs(), animation.values);
                valueTimeline(interpolation, AnimationKey.rotZ, chRotZ.getPairs(), animation.values);
                
                handler.setTimeline(animation, children.events);
                timeline.handler = handler;
            }
            
        }
        
        public ValueTimeline valueTimeline(int interpolation, AnimationKey key, PairList<Integer, Double> pairList, PairList<AnimationKey, ValueTimeline> values) {
            ValueTimeline valueTimeline = ValueTimeline.create(interpolation, pairList);
            if (key.equals(AnimationKey.rotX) || key.equals(AnimationKey.rotY) || key.equals(AnimationKey.rotZ)) {
                if (valueTimeline != null)
                    values.add(key, valueTimeline.factor(16));
            } else if (valueTimeline != null)
                values.add(key, valueTimeline);
            return valueTimeline;
        }
        
        public PairList<Integer, double[]> collectKeys(LittleAdvancedDoor structure, GuiTimelineALET timeline) {
            PairList<Integer, double[]> key = new PairList<Integer, double[]>();
            collectKeys(key, structure.offX.getPointsCopy(), 0);
            collectKeys(key, structure.offY.getPointsCopy(), 1);
            collectKeys(key, structure.offZ.getPointsCopy(), 2);
            collectKeys(key, structure.rotX.getPointsCopy(), 3);
            collectKeys(key, structure.rotY.getPointsCopy(), 4);
            collectKeys(key, structure.rotZ.getPointsCopy(), 5);
            return key;
        }
        
        private void collectKeys(PairList<Integer, double[]> key, PairList<Integer, Double> pairList, int index) {
            for (Pair<Integer, Double> point : pairList) {
                double[] data = new double[6];
                if (key.containsKey(point.key)) {
                    data = key.getPair(point.key).value;
                    data[index] = point.value;
                    key.set(new Integer(point.key), data);
                } else {
                    data[index] = point.value;
                    key.add(new Integer(point.key), data);
                }
            }
        }
    }
    
    public static class GuiDoorEventsButtonALET extends SubGuiDoorEvents.GuiDoorEventsButton {
        
        public SubGuiDoorEvents gui;
        public LittlePreviews previews;
        public LittleDoorBase activator;
        public List<AnimationEvent> events = new ArrayList<>();
        
        public GuiDoorEventsButtonALET(String name, int x, int y, LittlePreviews previews, LittleDoorBase door) {
            super(name, x, y, previews, door);
            this.previews = previews;
            this.activator = door;
            if (activator != null)
                this.events = activator.events;
        }
        
        public void update(LittlePreviews previews, LittleDoorBase door) {
            this.previews = previews;
            this.activator = door;
            if (activator != null)
                this.events = activator.events;
        }
        
        @Override
        public void onClicked(int x, int y, int button) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean("dialog", true);
            SubGuiDoorEvents dialog = new SubGuiDoorEvents(this);
            dialog.gui = getParent().getGui().gui;
            PacketHandler.sendPacketToServer(new GuiLayerPacket(nbt, dialog.gui.getLayers().size() - 1, false));
            dialog.container = new SubContainerEmpty(getPlayer());
            dialog.gui.addLayer(dialog);
            dialog.onOpened();
        }
    }
}
