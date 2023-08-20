package com.alet.common.structure.type.trigger.advanced;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.alet.client.gui.controls.GuiBezierCurve;
import com.alet.client.gui.controls.GuiDragablePanel;
import com.alet.client.gui.controls.GuiIconDepressedButton;
import com.alet.client.gui.controls.GuiKeyListener;
import com.alet.client.gui.controls.menu.GuiMenu;
import com.alet.client.gui.controls.menu.GuiMenuPart;
import com.alet.client.gui.controls.menu.GuiPopupMenu;
import com.alet.client.gui.controls.menu.GuiTree;
import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.client.gui.controls.menu.GuiTreePartHolder;
import com.alet.client.gui.controls.programmable.blueprints.BlueprintExecutor;
import com.alet.client.gui.controls.programmable.blueprints.BlueprintRegistar;
import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.controls.programmable.blueprints.activators.BlueprintActivator;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.event.gui.GuiControlDragEvent;
import com.alet.client.gui.event.gui.GuiControlKeyPressed;
import com.alet.client.gui.event.gui.GuiControlReleaseEvent;
import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.alet.common.structure.type.trigger.activator.LittleTriggerActivator;
import com.alet.common.util.MouseUtils;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents.GuiSignalEventsButton;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.entity.EntityAnimation;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleAdvTriggerBoxALET extends LittleStructure {
    public HashSet<Entity> entities = new HashSet<>();
    public List<UUID> entitiesToLoad;
    public int tick = 0;
    public int currentEvent = 0;
    public boolean run = false;
    public boolean considerEventsConditions = false;
    private List<GuiBlueprint> blueprints = new ArrayList<GuiBlueprint>();
    public List<GuiBlueprint> activator = new ArrayList<GuiBlueprint>();
    
    public LittleAdvTriggerBoxALET(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("blueprints", NBT.TAG_COMPOUND);
        for (NBTBase base : list) {
            if (base instanceof NBTTagCompound) {
                NBTTagCompound nb = (NBTTagCompound) base;
                GuiBlueprint obj = GuiBlueprint.createBlueprintFromNBT(nb);
                obj.structure = this;
                this.blueprints.add(obj);
            }
        }
        
        for (NBTBase base : list) {
            if (base instanceof NBTTagCompound) {
                NBTTagCompound nb = (NBTTagCompound) base;
                GuiBlueprint obj = GuiBlueprint.getBlueprintObjectFromNBT(blueprints, nb);
                NBTTagList ls = GuiBlueprint.getNodeNBTList(nb);
                for (NBTBase b : ls) {
                    if (b instanceof NBTTagCompound) {
                        NBTTagCompound n = (NBTTagCompound) b;
                        List<Pair<GuiBlueprint, GuiNode>> bpList = GuiBlueprint.getBlueprintFromNodeNBT(this.blueprints, n);
                        for (Pair<GuiBlueprint, GuiNode> bp : bpList)
                            bp.value.connect((GuiNode) obj.get(n.getString("node")));
                    }
                }
            }
        }
        if (!blueprints.isEmpty())
            organizeBlueprint();
    }
    
    public void organizeBlueprint() {
        if (blueprints.stream().anyMatch(x -> BlueprintActivator.class.isAssignableFrom(x.getClass()))) {
            activator = blueprints.stream().filter(x -> BlueprintActivator.class.isAssignableFrom(x.getClass())).collect(Collectors.toList());
            //blueprints.removeAll(events);
        }
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        NBTTagList list = new NBTTagList();
        for (GuiBlueprint d : this.blueprints) {
            list.appendTag(d.createNBT());
        }
        nbt.setTag("blueprints", list);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World worldIn, IParentTileList parent, BlockPos pos, Entity entityIn) {
        if (worldIn.isRemote)
            return;
        boolean intersected = false;
        for (LittleTile tile : parent) {
            if (tile.getBox().getBox(parent.getContext(), pos).intersects(entityIn.getEntityBoundingBox())) {
                intersected = true;
                break;
            }
        }
        if (intersected) {
            //this.triggerActivator.onCollision(worldIn, entityIn);
        }
        
    }
    
    @Override
    public void checkForAnimationCollision(EntityAnimation animation, HashMap<Entity, AxisAlignedBB> entities) throws CorruptedConnectionException, NotYetConnectedException {
        if (animation.world.isRemote)
            return;
        //this.triggerActivator.onCollision(animation.world, entities.keySet());
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        //if (!this.isClient())
        //this.triggerActivator.onRightClick(worldIn, tile, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ, action);
        for (GuiBlueprint event : this.activator)
            if (event instanceof BlueprintActivator) {
                BlueprintActivator bpa = (BlueprintActivator) event;
                bpa.onRightClick(worldIn, tile, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ, action);
            }
        return true;
    }
    
    @Override
    public void tick() {
        if (!this.isClient())
            if (!this.entities.isEmpty()) {
                for (GuiBlueprint event : this.activator)
                    BlueprintExecutor.startScript((BlueprintActivator) event, entities, (WorldServer) this.getWorld());
                this.entities = new HashSet<Entity>();
            }
    }
    
    /*
    @Override
    public void tick() {
        
        if (!this.isClient()) {
            World world = this.getWorld();
            if (world instanceof SubWorldServer)
                world = ((SubWorldServer) world).getRealWorld();
            WorldServer server = (WorldServer) world;
            if (this.entitiesToLoad != null) {
                for (Iterator<UUID> iterator = entitiesToLoad.iterator(); iterator.hasNext();) {
                    UUID uuid = (UUID) iterator.next();
                    Entity en = server.getEntityFromUuid(uuid);
                    if (en != null) {
                        this.entities.add(en);
                        iterator.remove();
                    }
                }
                if (entitiesToLoad.isEmpty())
                    entitiesToLoad = null;
            }
            
            //this.run = this.triggerActivator.shouldRun(world, entities);
            if (!this.entities.isEmpty() && this.run) {
                boolean hasCondition = LittleTriggerObject.hasCondition(this.triggerObjs); //Will reset the run and tick values
                boolean shouldContinue = true; //Will stop the for loop and reset the run and tick values
                boolean flag = false; //Will reset the run and tick values
                //for loops through all trigger conditions and events in order.
                while (this.currentEvent < this.triggerObjs.size()) {
                    LittleTriggerObject triggerObj = this.triggerObjs.get(this.currentEvent);
                    if (triggerObj instanceof LittleTriggerCondition) {
                        if (this.triggerObjs.size() > this.currentEvent) {
                            LittleTriggerCondition condition = (LittleTriggerCondition) triggerObj;
                            shouldContinue = condition.conditionRunEvent();
                            if (!shouldContinue && !condition.shouldLoop) {
                                flag = true;
                                this.entities.clear();
                            }
                        }
                    } else if (shouldContinue && triggerObj instanceof LittleTriggerEvent) {
                        LittleTriggerEvent triggerEvent = (LittleTriggerEvent) triggerObj;
                        boolean event = triggerEvent.runEvent();
                        if (this.considerEventsConditions)
                            shouldContinue = event;
                    }
                    if (!shouldContinue)
                        break;
                    this.currentEvent++;
                    
                }
                //this.triggerActivator.loopRules(entities, shouldContinue, flag);
                if (hasCondition && shouldContinue && !flag)
                    this.getInput(0).updateState(new boolean[] { true });
                else
                    this.getInput(0).updateState(new boolean[] { false });
                //If there is no conditions then there is no need to loop.
                if (this.currentEvent >= triggerObjs.size() || flag) {
                    this.run = false;
                    this.tick = 0;
                    this.currentEvent = 0;
                    this.entities.clear();
                    for (LittleTriggerObject triggerObj : this.triggerObjs) {
                        if (triggerObj instanceof LittleTriggerCondition)
                            ((LittleTriggerCondition) triggerObj).completed = false;
                    }
                }
            }
        }
        
    }*/
    
    public static class LittleAdvTriggerBoxStructureParser extends LittleStructureGuiParser {
        public List<GuiBlueprint> blueprints = new ArrayList<GuiBlueprint>();
        LittleTriggerObject trigger = null;
        public LittlePreviews previews;
        public boolean runWhileCollided = false;
        public AxisAlignedBB collisionArea;
        public LittleTriggerActivator triggerActivator;
        public boolean consideredEventsConditions = false;
        public GuiNode selectedNode;
        NBTTagList list = new NBTTagList();
        
        public LittleAdvTriggerBoxStructureParser(GuiParent parent, AnimationGuiHandler handler) {
            super(parent, handler);
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public void create(LittlePreviews previews, @Nullable LittleStructure structure) {
            createControls(previews, structure);
            parent.controls.add(new GuiSignalEventsButton("signal", 0, 222, previews, structure, getStructureType()));
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public void createControls(LittlePreviews previews, LittleStructure structure) {
            
            LittleAdvTriggerBoxALET triggerBox = (LittleAdvTriggerBoxALET) structure;
            
            GuiDragablePanel drag = new GuiDragablePanel("drag", 150, 19, 412, 180, 1000, 1000);
            List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
            GuiMenuPart name = new GuiMenuPart("", "Delete", EnumPartType.Leaf);
            listOfMenus.add(name);
            GuiMenu menu = new GuiMenu("", 0, 0, 500, listOfMenus);
            menu.height = 500;
            GuiPopupMenu pop = new GuiPopupMenu("pop", menu, 0, 0, 0, 0);
            pop.listenFor(GuiBlueprint.class);
            drag.addControl(pop);
            if (triggerBox != null && !triggerBox.blueprints.isEmpty()) {
                this.blueprints = triggerBox.blueprints;
                for (GuiBlueprint d : this.blueprints)
                    drag.addControl(d);
                
                for (GuiBlueprint d : this.blueprints)
                    for (Pair<GuiNode, GuiNode> pair : d.nodeConnections) {
                        drag.addControl(new GuiBezierCurve("", pair.key, pair.value, 1000, 1000));
                    }
            }
            GuiTree tree = new GuiTree("tree", 0, 0, 194, BlueprintRegistar.treeList(), true, 0, 0, 50);
            GuiScrollBox box = new GuiScrollBox("scrole_box", 0, 0, 143, 199);
            box.addControl(tree);
            parent.addControl(drag);
            parent.addControl(box);
            drag.addControl(new GuiKeyListener("key_listener"));
            parent.addControl(new GuiIconDepressedButton("pointer", 150, 0, 4, true).setCustomTooltip("Not Implemented Yet"));
            parent.addControl(new GuiIconDepressedButton("move", 170, 0, 0, false).setCustomTooltip("Not Implemented Yet"));
            parent.addControl(new GuiIconDepressedButton("delete", 190, 0, 1, false).setCustomTooltip("Not Implemented Yet"));
            parent.addControl(new GuiIconDepressedButton("select", 210, 0, 2, false).setCustomTooltip("Not Implemented Yet"));
            parent.addControl(new GuiIconDepressedButton("drag_toggle", 230, 0, 3, false).setCustomTooltip("Not Implemented Yet"));
            /*
            BlueprintInteger i = new BlueprintInteger(0);
            BlueprintDouble bp = new BlueprintDouble(1);
            BlueprintCastDoubleToInt ci = new BlueprintCastDoubleToInt(2);
            BlueprintEqualTo eq = new BlueprintEqualTo(3);
            BlueprintTileCollision tc = new BlueprintTileCollision(4);
            BlueprintBranch b = new BlueprintBranch(5);
            BlueprintModifyMotion z = new BlueprintModifyMotion(6);
            drag.addControl(z);
            drag.addControl(i);
            drag.addControl(eq);
            drag.addControl(tc);
            drag.addControl(ci);
            drag.addControl(bp);
            drag.addControl(b);
            this.blueprints.add(z);
            this.blueprints.add(b);
            this.blueprints.add(i);
            this.blueprints.add(bp);
            this.blueprints.add(ci);
            this.blueprints.add(eq);
            this.blueprints.add(tc);
            */
        }
        
        @CustomEventSubscribe
        public void controlChanged(GuiControlChangedEvent event) {
            if (event.source instanceof GuiMenuPart) {
                GuiMenuPart menu = (GuiMenuPart) event.source;
                
            }
        }
        
        @CustomEventSubscribe
        public void controlDragged(GuiControlDragEvent event) {
            if (event.source instanceof GuiTreePartHolder) {
                GuiDragablePanel drag = (GuiDragablePanel) this.parent.get("drag");
                GuiTreePart menu = (GuiTreePart) event.source;
            }
        }
        
        @CustomEventSubscribe
        public void keyPressed(GuiControlKeyPressed event) {
            if (event.source.is("key_listener")) {
                if (event.ctrl && Keyboard.KEY_W == event.key) {
                    parent.raiseEvent(new GuiControlClickEvent(parent.get("move"), 0, 0, 0));
                    MouseUtils.setCursor("move");
                }
                if (event.ctrl && Keyboard.KEY_A == event.key) {
                    parent.raiseEvent(new GuiControlClickEvent(parent.get("delete"), 0, 0, 0));
                    MouseUtils.setCursor("close");
                }
                if (event.ctrl && Keyboard.KEY_S == event.key) {
                    parent.raiseEvent(new GuiControlClickEvent(parent.get("select"), 0, 0, 0));
                    MouseUtils.setCursor("dotted_line");
                }
                if (event.ctrl && Keyboard.KEY_D == event.key) {
                    parent.raiseEvent(new GuiControlClickEvent(parent.get("drag_toggle"), 0, 0, 0));
                    MouseUtils.setCursor("open_hand");
                }
                if (event.ctrl && Keyboard.KEY_Q == event.key) {
                    parent.raiseEvent(new GuiControlClickEvent(parent.get("pointer"), 0, 0, 0));
                    MouseUtils.resetCursor();
                }
            }
        }
        
        @SuppressWarnings("unchecked")
        @CustomEventSubscribe
        public void controlReleased(GuiControlReleaseEvent event) {
            if (event.source instanceof GuiNode) {
                
            }
            
            if (event.source instanceof GuiTreePartHolder) {
                GuiDragablePanel drag = (GuiDragablePanel) this.parent.get("drag");
                GuiTreePartHolder<Class<? extends GuiBlueprint>> menu = (GuiTreePartHolder<Class<? extends GuiBlueprint>>) event.source;
                try {
                    if (drag.isMouseOver()) {
                        Vec3d pos = drag.getMousePos();
                        GuiBlueprint bp = menu.key.getConstructor(int.class).newInstance(this.blueprints.size());
                        bp.posX = (int) pos.x;
                        bp.posY = (int) pos.y;
                        this.blueprints.add(bp);
                        
                        drag.addControl(bp);
                    }
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        @CustomEventSubscribe
        public void controlClicked(GuiControlClickEvent event) {
            GuiDragablePanel drag = (GuiDragablePanel) this.parent.get("drag");
            if (event.source instanceof GuiNode) {
                
                GuiBlueprint blueprint = (GuiBlueprint) topControl(drag.controls, GuiBlueprint.class);
                GuiNode node = (GuiNode) event.source;
                if (this.selectedNode != null) {
                    
                    GuiNode sender = node.isSender() ? node : null;
                    if (sender == null)
                        sender = selectedNode.isSender() ? selectedNode : null;
                    GuiNode receiver = node.isReceiver() ? node : null;
                    if (receiver == null)
                        receiver = selectedNode.isReceiver() ? selectedNode : null;
                    
                    if (node.equals(this.selectedNode)) {
                        drag.removeControl(drag.get("temp"));
                        drag.refreshControls();
                        this.selectedNode = null;
                        blueprint.nodeClicked(node, false);
                    } else if (sender.canConnect(receiver)) {
                        blueprint.nodeClicked(node, true);
                        sender.connect(receiver);
                        drag.removeControl(drag.get("temp"));
                        drag.refreshControls();
                        drag.addControl(new GuiBezierCurve("", node, this.selectedNode, 1000, 1000));
                        this.selectedNode.selected = false;
                        node.selected = false;
                        this.selectedNode = null;
                    }
                } else {
                    blueprint.nodeClicked(node, true);
                    drag.addControl(new GuiBezierCurve("temp", node, null, 1000, 1000));
                    this.selectedNode = node;
                }
            }
            if (event.source instanceof GuiBlueprint) {
                GuiBlueprint blueprint = (GuiBlueprint) topControl(drag.controls, GuiBlueprint.class);
                drag.moveControlToTop(blueprint);
            }
            if (event.source instanceof GuiTreePart && event.source.is("tree")) {
                GuiTreePart part = (GuiTreePart) event.source;
                if (part.type.equals(EnumPartType.Leaf)) {
                    GuiBlueprint pb = GuiBlueprint.createBlueprintFrom(part.caption, this.blueprints.size() + 1);
                    drag.addControl(pb);
                    this.blueprints.add(pb);
                }
            }
            if (event.source instanceof GuiIconDepressedButton) {
                this.parent.controls.stream().filter(x -> x instanceof GuiIconDepressedButton && !x.equals(event.source)).forEach(x -> ((GuiIconDepressedButton) x).value = false);
                ((GuiIconDepressedButton) event.source).value = true;
            }
        }
        
        public GuiControl topControl(List<GuiControl> controls, Class<? extends GuiControl> search) {
            for (GuiControl control : controls) {
                if (search.isAssignableFrom(control.getClass()) && control.isMouseOver())
                    return control;
            }
            return null;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public LittleAdvTriggerBoxALET parseStructure(LittlePreviews previews) {
            LittleAdvTriggerBoxALET structure = createStructure(LittleAdvTriggerBoxALET.class, null);
            structure.blueprints = blueprints;
            return structure;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        protected LittleStructureType getStructureType() {
            return LittleStructureRegistry.getStructureType(LittleAdvTriggerBoxALET.class);
        }
        
        @CustomEventSubscribe
        public void onControlChanged(GuiControlChangedEvent event) {}
        
        public void updateList() {}
        
    }
    
}
