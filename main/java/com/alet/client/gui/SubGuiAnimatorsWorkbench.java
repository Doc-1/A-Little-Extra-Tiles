package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.GuiTimelineALET;
import com.alet.client.gui.controls.GuiTimelineALET.KeyALETDeselectedEvent;
import com.alet.client.gui.controls.GuiTimelineALET.KeyALETSelectedEvent;
import com.alet.client.gui.controls.KeyControlALET;
import com.alet.client.gui.controls.TimelineChannelALET;
import com.alet.client.gui.controls.TimelineChannelALET.TimelineChannelDoorData;
import com.alet.client.gui.controls.menu.GuiTree;
import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.container.SlotControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiIconButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel.TimelineChannelDouble;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel.TimelineChannelInteger;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.premade.SubContainerEmpty;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.packet.gui.GuiLayerPacket;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.SubGuiRecipe.LoadingThread;
import com.creativemd.littletiles.client.gui.SubGuiRecipe.StructureHolder;
import com.creativemd.littletiles.client.gui.controls.IAnimationControl;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiDoorEvents;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.animation.AnimationKey;
import com.creativemd.littletiles.common.structure.animation.AnimationTimeline;
import com.creativemd.littletiles.common.structure.animation.ValueTimeline;
import com.creativemd.littletiles.common.structure.animation.event.AnimationEvent;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.type.door.LittleAdvancedDoor;
import com.creativemd.littletiles.common.structure.type.door.LittleDoorBase;
import com.creativemd.littletiles.common.tile.parent.StructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SubGuiAnimatorsWorkbench extends SubGui implements IAnimationControl {
    
    public LittleStructure structure;
    public AnimationGuiHandler handler = new AnimationGuiHandler();
    private KeyControlALET keySelected = null;
    protected LoadingThread loadingThread;
    public LittleStructureGuiParser parser;
    public AnimationPreview animationPreview;
    public StructureHolder selected;
    
    public SubGuiAnimatorsWorkbench(LittleStructure structure) {
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
        if (event.source.is("timeline")) {
            if (keySelected != null) {
                GuiTextfield tickAt = (GuiTextfield) get("tickAt");
                tickAt.text = keySelected.tick + "";
            }
        }
        if (event.source instanceof GuiTextfield) {
            GuiTextfield text = (GuiTextfield) event.source;
            if (keySelected != null) {
                double[] data = (double[]) keySelected.value;
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
        keySelected = key;
        double[] data = (double[]) key.value;
        GuiTextfield offX = (GuiTextfield) get("offX");
        GuiTextfield offY = (GuiTextfield) get("offY");
        GuiTextfield offZ = (GuiTextfield) get("offZ");
        
        GuiTextfield rotX = (GuiTextfield) get("rotX");
        GuiTextfield rotY = (GuiTextfield) get("rotY");
        GuiTextfield rotZ = (GuiTextfield) get("rotZ");
        GuiTextfield tickAt = (GuiTextfield) get("tickAt");
        
        offX.text = data[0] + "";
        offY.text = data[1] + "";
        offZ.text = data[2] + "";
        
        rotX.text = data[3] + "";
        rotY.text = data[4] + "";
        rotZ.text = data[5] + "";
        
        tickAt.text = key.tick + "";
        
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
        keySelected = null;
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
    public void onTick() {
        super.onTick();
        if (loadingThread != null && loadingThread.result != null) {
            animationPreview = loadingThread.result;
            loadingThread = null;
            onLoaded(animationPreview);
            if (parser != null)
                parser.onLoaded(animationPreview);
        }
        if (animationPreview != null)
            handler.tick(animationPreview.previews, animationPreview.animation.structure, animationPreview.animation);
    }
    
    protected static StructureHolder addPreviews(LittlePreviews previews, StructureHolder holder) {
        holder = new StructureHolder(null, -1, 0);
        holder.previews = previews;
        holder.prefix = "";
        
        ItemStack stack = new ItemStack(LittleTiles.multiTiles);
        LittlePreviews newPreviews = new LittlePreviews(previews.getContext());
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        for (LittlePreview preview : previews) {
            
            newPreviews.addWithoutCheckingPreview(preview.copy());
            minX = Math.min(minX, preview.box.minX);
            minY = Math.min(minY, preview.box.minY);
            minZ = Math.min(minZ, preview.box.minZ);
            minX = Math.min(minX, preview.box.maxX);
            minY = Math.min(minY, preview.box.maxY);
            minZ = Math.min(minZ, preview.box.maxZ);
        }
        
        for (LittlePreview preview : newPreviews) {
            preview.box.sub(minX, minY, minZ);
        }
        LittlePreview.savePreview(newPreviews, stack);
        holder.explicit = stack;
        return holder;
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
        controls.add(new GuiDoorEventsButtonALET("children_activate", 93, 107, null, null));
        
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
        controls.add(new GuiStateButton("interpolation", 0, 140, 107, 40, 7, ValueTimeline.interpolationTypes));
        
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
                if (stack != null && !stack.equals(ItemStack.EMPTY)) {
                    LittlePreviews previews = LittlePreview.getPreview(stack);
                    if (!previews.isEmpty()) {
                        GuiTree tree = (GuiTree) this.parent.get("tree");
                        GuiTimelineALET timeline = (GuiTimelineALET) this.parent.get("timeline");
                        LittleAdvancedDoor structure = (LittleAdvancedDoor) StructureTileList.create(previews.structureNBT, null);
                        SubGuiAnimatorsWorkbench.this.structure = structure;
                        GuiDoorEventsButtonALET children = (GuiDoorEventsButtonALET) parent.get("children_activate");
                        children.update(previews, structure);
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
                        AnimationPreview anim = new AnimationPreview(previews);
                        viewer.onLoaded(anim);
                        loadStack(getHolder(previews, stack), previews, anim);
                    }
                    
                }
            }
            
            public String addField(LittlePreviews previews, GuiTimelineALET timeline, GuiTree tree, GuiTreePart part) {
                
                LittleAdvancedDoor structure = (LittleAdvancedDoor) StructureTileList.create(previews.structureNBT, null);
                PairList<Integer, double[]> keys = collectKeys(structure, timeline);
                
                if (previews.getStructureName() != null) {
                    TimelineChannelALET line = new TimelineChannelDoorData(previews.getStructureName());
                    line.index = timeline.channels.size();
                    line.addKeys(keys);
                    timeline.channels.add(line);
                    timeline.refreshChannels();
                    updateTimeLine(timeline, previews);
                    return previews.getStructureName();
                } else {
                    TimelineChannelALET line = new TimelineChannelDoorData(previews.getStructureId());
                    line.index = timeline.channels.size();
                    line.addKeys(keys);
                    timeline.channels.add(line);
                    timeline.refreshChannels();
                    updateTimeLine(timeline, previews);
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
            
            public void updateTimeLine(GuiTimelineALET timeline, LittlePreviews previews) {
                LittleGridContext context = previews.getContext();
                GuiDoorEventsButtonALET children = (GuiDoorEventsButtonALET) parent.get("children_activate");
                GuiStateButton interpolationButton = (GuiStateButton) parent.get("interpolation");
                int interpolation = interpolationButton.getState();
                TimelineChannelDoorData door = (TimelineChannelDoorData) timeline.channels.get(0);
                AnimationTimeline animation = new AnimationTimeline(timeline.getDuration(), new PairList<>());
                
                if (door.getPairs() != null) {
                    TimelineChannelInteger chOffX = new TimelineChannelInteger("offX");
                    TimelineChannelInteger chOffY = new TimelineChannelInteger("offY");
                    TimelineChannelInteger chOffZ = new TimelineChannelInteger("offZ");
                    
                    TimelineChannelDouble chRotX = new TimelineChannelDouble("rotX");
                    TimelineChannelDouble chRotY = new TimelineChannelDouble("rotY");
                    TimelineChannelDouble chRotZ = new TimelineChannelDouble("rotZ");
                    
                    for (Pair<Integer, double[]> pair : door.getPairs()) {
                        chOffX.addKey(pair.key, (int) pair.value[0]);
                        chOffY.addKey(pair.key, (int) pair.value[1]);
                        chOffZ.addKey(pair.key, (int) pair.value[2]);
                        
                        chRotX.addKey(pair.key, pair.value[3]);
                        chRotY.addKey(pair.key, pair.value[4]);
                        chRotZ.addKey(pair.key, pair.value[5]);
                        
                    }
                    ValueTimeline rotX = ValueTimeline.create(interpolation, chRotX.getPairs());
                    if (rotX != null)
                        animation.values.add(AnimationKey.rotX, rotX);
                    
                    ValueTimeline rotY = ValueTimeline.create(interpolation, chRotY.getPairs());
                    if (rotY != null)
                        animation.values.add(AnimationKey.rotY, rotY);
                    
                    ValueTimeline rotZ = ValueTimeline.create(interpolation, chRotZ.getPairs());
                    if (rotZ != null)
                        animation.values.add(AnimationKey.rotZ, rotZ);
                    
                    ValueTimeline offX = ValueTimeline.create(interpolation, chOffX.getPairs());
                    if (offX != null)
                        animation.values.add(AnimationKey.offX, offX.factor(16));
                    
                    ValueTimeline offY = ValueTimeline.create(interpolation, chOffY.getPairs());
                    if (offY != null)
                        animation.values.add(AnimationKey.offY, offY.factor(16));
                    
                    ValueTimeline offZ = ValueTimeline.create(interpolation, chOffZ.getPairs());
                    if (offZ != null)
                        animation.values.add(AnimationKey.offZ, offZ.factor(16));
                    
                    handler.setTimeline(animation, children.events);
                    timeline.handler = handler;
                }
                
            }
            
            public PairList<Integer, double[]> collectKeys(LittleAdvancedDoor structure, GuiTimelineALET timeline) {
                PairList<Integer, double[]> key = new PairList<Integer, double[]>();
                
                for (Pair<Integer, Double> point : structure.offX.getPointsCopy()) {
                    double[] data = new double[6];
                    if (key.containsKey(point.key)) {
                        data = key.getPair(point.key).value;
                        data[0] = point.value;
                        key.set(new Integer(point.key), data);
                    } else {
                        data[0] = point.value;
                        key.add(new Integer(point.key), data);
                    }
                }
                for (Pair<Integer, Double> point : structure.offY.getPointsCopy()) {
                    double[] data = new double[6];
                    if (key.containsKey(point.key)) {
                        data = key.getPair(point.key).value;
                        data[1] = point.value;
                        key.set(new Integer(point.key), data);
                    } else {
                        data[1] = point.value;
                        key.add(new Integer(point.key), data);
                    }
                }
                for (Pair<Integer, Double> point : structure.offZ.getPointsCopy()) {
                    double[] data = new double[6];
                    if (key.containsKey(point.key)) {
                        data = key.getPair(point.key).value;
                        data[2] = point.value;
                        key.set(new Integer(point.key), data);
                    } else {
                        data[2] = point.value;
                        key.add(new Integer(point.key), data);
                    }
                }
                for (Pair<Integer, Double> point : structure.rotX.getPointsCopy()) {
                    double[] data = new double[6];
                    if (key.containsKey(point.key)) {
                        data = key.getPair(point.key).value;
                        data[3] = point.value;
                        key.set(new Integer(point.key), data);
                    } else {
                        data[3] = point.value;
                        key.add(new Integer(point.key), data);
                    }
                }
                for (Pair<Integer, Double> point : structure.rotY.getPointsCopy()) {
                    double[] data = new double[6];
                    if (key.containsKey(point.key)) {
                        data = key.getPair(point.key).value;
                        data[4] = point.value;
                        key.set(new Integer(point.key), data);
                    } else {
                        data[4] = point.value;
                        key.add(new Integer(point.key), data);
                    }
                }
                for (Pair<Integer, Double> point : structure.rotZ.getPointsCopy()) {
                    double[] data = new double[6];
                    if (key.containsKey(point.key)) {
                        data = key.getPair(point.key).value;
                        data[5] = point.value;
                        key.set(new Integer(point.key), data);
                    } else {
                        data[5] = point.value;
                        key.add(new Integer(point.key), data);
                    }
                }
                
                return key;
                
            }
            
        });
        
    }
    
    public StructureHolder getHolder(LittlePreviews previews, ItemStack stack) {
        StructureHolder holder = new StructureHolder(null, -1, 0);
        holder.previews = previews;
        holder.prefix = "";
        holder.explicit = stack;
        return holder;
    }
    
    public void loadStack(StructureHolder holder, LittlePreviews previews, AnimationPreview anim) {
        this.selected = holder;
        animationPreview = null;
        if (loadingThread != null && loadingThread.isAlive())
            loadingThread.stop();
        loadingThread = new LoadingThread(previews);
        getParser(previews);
    }
    
    public void getParser(LittlePreviews previews) {
        if (this.parser != null)
            removeListener(this.parser);
        parser = LittleStructureRegistry.getParserNotFound(this, handler, this.structure);
    }
    
    public void onLoaded(GuiParent parent, AnimationPreview animationPreview) {
        for (GuiControl control : parent.controls) {
            if (control instanceof IAnimationControl)
                ((IAnimationControl) control).onLoaded(animationPreview);
            if (control instanceof GuiParent)
                onLoaded((GuiParent) control, animationPreview);
        }
    }
    
    @Override
    public void onLoaded(AnimationPreview preview) {
        onLoaded(this, animationPreview);
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
