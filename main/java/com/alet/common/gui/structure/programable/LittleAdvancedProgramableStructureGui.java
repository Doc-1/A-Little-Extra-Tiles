package com.alet.common.gui.structure.programable;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.alet.client.registries.FunctionRegistery;
import com.alet.common.gui.controls.GuiBezierCurve;
import com.alet.common.gui.controls.GuiDragablePanel;
import com.alet.common.gui.controls.GuiIconDepressedButton;
import com.alet.common.gui.controls.menu.GuiMenu;
import com.alet.common.gui.controls.menu.GuiMenuPart;
import com.alet.common.gui.controls.menu.GuiPopupMenu;
import com.alet.common.gui.controls.menu.GuiTree;
import com.alet.common.gui.controls.menu.GuiTreePart;
import com.alet.common.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.common.gui.controls.menu.GuiTreePartHolder;
import com.alet.common.gui.controls.programmable.GuiFunction;
import com.alet.common.gui.controls.programmable.GuiNodeValue;
import com.alet.common.gui.events.GuiControlDragEvent;
import com.alet.common.gui.events.GuiControlKeyPressed;
import com.alet.common.gui.events.GuiControlReleaseEvent;
import com.alet.common.utils.MouseUtils;
import com.alet.components.structures.type.programable.advanced.Function;
import com.alet.components.structures.type.programable.advanced.LittleAdvancedProgramableStructure;
import com.alet.components.structures.type.programable.advanced.activators.FunctionOnRightClick;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents.GuiSignalEventsButton;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleAdvancedProgramableStructureGui extends LittleStructureGuiParser {
    public List<GuiFunction> guiFunctions = new ArrayList<GuiFunction>();
    public LittlePreviews previews;
    public boolean runWhileCollided = false;
    public AxisAlignedBB collisionArea;
    public boolean consideredEventsConditions = false;
    public GuiNodeValue selectedNode;
    NBTTagList list = new NBTTagList();
    
    public LittleAdvancedProgramableStructureGui(GuiParent parent, AnimationGuiHandler handler) {
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
        
        LittleAdvancedProgramableStructure triggerBox = (LittleAdvancedProgramableStructure) structure;
        
        GuiDragablePanel drag = new GuiDragablePanel("drag", 150, 19, 412, 180, 1000, 1000);
        List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
        GuiMenuPart name = new GuiMenuPart("", "Delete", EnumPartType.Leaf);
        listOfMenus.add(name);
        GuiMenu menu = new GuiMenu("", 0, 0, 500, listOfMenus);
        menu.height = 500;
        GuiPopupMenu pop = new GuiPopupMenu("pop", menu, 0, 0, 0, 0);
        pop.listenFor(GuiFunction.class);
        drag.addControl(pop);
        /*
        if (triggerBox != null && !triggerBox.blueprints.isEmpty()) {
            this.blueprints = triggerBox.blueprints;
            for (GuiFunction d : this.blueprints)
                drag.addControl(d);
            
            for (GuiFunction d : this.blueprints)
                for (Pair<GuiNode, GuiNode> pair : d.nodeConnections) {
                    drag.addControl(new GuiBezierCurve("", pair.key, pair.value, 1000, 1000, pair.key.COLOR));
                }
        }
        */
        
        GuiTree tree = new GuiTree("tree", 0, 0, 194, FunctionRegistery.treeList(), true, 0, 0, 50);
        GuiScrollBox box = new GuiScrollBox("scrole_box", 0, 0, 143, 199);
        box.addControl(tree);
        parent.addControl(drag);
        parent.addControl(box);
        //drag.addControl(new GuiKeyListener("key_listener"));
        parent.addControl(new GuiIconDepressedButton("pointer", 150, 0, 4, true).setCustomTooltip("Not Implemented Yet"));
        parent.addControl(new GuiIconDepressedButton("move", 170, 0, 0, false).setCustomTooltip("Not Implemented Yet"));
        parent.addControl(new GuiIconDepressedButton("delete", 190, 0, 1, false).setCustomTooltip("Not Implemented Yet"));
        parent.addControl(new GuiIconDepressedButton("select", 210, 0, 2, false).setCustomTooltip("Not Implemented Yet"));
        parent.addControl(new GuiIconDepressedButton("drag_toggle", 230, 0, 3, false).setCustomTooltip(
            "Not Implemented Yet"));
        FunctionOnRightClick r = new FunctionOnRightClick(0);
        drag.addControl(FunctionRegistery.createFunctionGui("programmable.advanced.on_right_click.name", false, 0));
        drag.addControl(FunctionRegistery.createFunctionGui("programmable.advanced.debug_message.name", false, 1));
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
    
    GuiFunction selected;
    
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
    
    @CustomEventSubscribe
    public void controlReleased(GuiControlReleaseEvent event) {
        if (event.source instanceof GuiFunction) {
            if (selected != null && selected == event.source) {
                selected.selected = false;
                selected = null;
            }
        }
        
        if (event.source instanceof GuiTreePartHolder) {
            GuiDragablePanel drag = (GuiDragablePanel) this.parent.get("drag");
            GuiTreePartHolder<String> menu = (GuiTreePartHolder<String>) event.source;
            if (drag.isMouseOver()) {
                Vec3d pos = drag.getMousePos();
                GuiFunction bp = FunctionRegistery.createFunctionGui(menu.key, false, this.guiFunctions.size());
                
                bp.posX = (int) pos.x;
                bp.posY = (int) pos.y;
                this.guiFunctions.add(bp);
                
                drag.addControl(bp);
            }
        }
    }
    
    @CustomEventSubscribe
    public void controlClicked(GuiControlClickEvent event) {
        GuiDragablePanel drag = (GuiDragablePanel) this.parent.get("drag");
        if (event.source instanceof GuiNodeValue) {
            
            GuiFunction blueprint = (GuiFunction) topControl(drag.controls, GuiFunction.class);
            GuiNodeValue node = (GuiNodeValue) event.source;
            if (this.selectedNode != null) {
                
                GuiNodeValue sender = node.isSender() ? node : null;
                if (sender == null)
                    sender = selectedNode.isSender() ? selectedNode : null;
                GuiNodeValue receiver = node.isReciever() ? node : null;
                if (receiver == null)
                    receiver = selectedNode.isReciever() ? selectedNode : null;
                
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
                    drag.addControl(new GuiBezierCurve("", node, this.selectedNode, 1000, 1000, node.COLOR));
                    this.selectedNode.selected = false;
                    node.selected = false;
                    this.selectedNode = null;
                }
            } else {
                blueprint.nodeClicked(node, true);
                drag.addControl(new GuiBezierCurve("temp", node, null, 1000, 1000, node.COLOR));
                this.selectedNode = node;
            }
        }
        if (event.source instanceof GuiFunction) {
            GuiFunction function = (GuiFunction) topControl(drag.controls, GuiFunction.class);
            drag.moveControlToTop(function);
            if (selected == null) {
                selected = function;
                selected.selected = true;
            }
        }
        if (event.source instanceof GuiTreePart && event.source.is("tree")) {
            GuiTreePart part = (GuiTreePart) event.source;
            if (part.type.equals(EnumPartType.Leaf)) {
                GuiFunction pb = GuiFunction.createFunctionFrom(part.caption, this.guiFunctions.size() + 1);
                drag.addControl(pb);
                this.guiFunctions.add(pb);
            }
        }
        if (event.source instanceof GuiIconDepressedButton) {
            this.parent.controls.stream().filter(x -> x instanceof GuiIconDepressedButton && !x.equals(event.source))
                    .forEach(x -> ((GuiIconDepressedButton) x).value = false);
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
    public LittleAdvancedProgramableStructure parseStructure(LittlePreviews previews) {
        LittleAdvancedProgramableStructure structure = createStructure(LittleAdvancedProgramableStructure.class, null);
        //for (GuiFunction gui : guiFunctions)
        //structure.functions.add(FunctionRegistar.getFunctionFromGui(gui));
        
        for (int i = 0; i < guiFunctions.size(); i++) {
            GuiFunction gui = guiFunctions.get(i);
            Function function = structure.getFunctions().get(i);
        }
        
        return structure;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    protected LittleStructureType getStructureType() {
        return LittleStructureRegistry.getStructureType(LittleAdvancedProgramableStructure.class);
    }
}
