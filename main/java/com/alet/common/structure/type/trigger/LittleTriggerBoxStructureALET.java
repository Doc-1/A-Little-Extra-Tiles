package com.alet.common.structure.type.trigger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.alet.client.gui.controls.menu.GuiMenu;
import com.alet.client.gui.controls.menu.GuiMenuPart;
import com.alet.client.gui.controls.menu.GuiPopupMenu;
import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.common.structure.type.trigger.conditions.LittleTriggerCondition;
import com.alet.common.structure.type.trigger.events.LittleTriggerEvent;
import com.alet.common.util.NBTUtils;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxCategory;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtensionCategory;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerBoxStructureALET extends LittleStructure {
    public HashSet<Entity> entities = new HashSet<>();
    public int tick = 0;
    public int currentEvent = 0;
    public boolean breakBlock = false;
    public boolean run = false;
    public boolean runWhileCollided = false;
    public boolean collisionListener = false;
    public boolean aabbCollisionListener = false;
    public boolean rightClickListener = false;
    public boolean considerEventsConditions = false;
    public AxisAlignedBB collisionArea;
    public List<LittleTriggerObject> triggerObjs = new ArrayList<LittleTriggerObject>();
    
    public LittleTriggerBoxStructureALET(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("triggers", Constants.NBT.TAG_COMPOUND);
        int i = 0;
        for (NBTBase base : list) {
            if (base instanceof NBTTagCompound) {
                NBTTagCompound n = (NBTTagCompound) base;
                LittleTriggerObject triggerObj = LittleTriggerRegistrar.getFromNBT((NBTTagCompound) n.getTag(i + ""));
                triggerObjs.add(triggerObj);
                triggerObj.structure = this;
                i++;
            }
        }
        tick = nbt.getInteger("currentTick");
        currentEvent = nbt.getInteger("currentEvent");
        if (nbt.hasKey("runWhileCollided"))
            this.runWhileCollided = nbt.getBoolean("runWhileCollided");
        if (nbt.hasKey("collisionListener"))
            this.collisionListener = nbt.getBoolean("collisionListener");
        if (nbt.hasKey("aabbCollisionListener"))
            this.aabbCollisionListener = nbt.getBoolean("aabbCollisionListener");
        if (nbt.hasKey("rightClickListener"))
            this.rightClickListener = nbt.getBoolean("rightClickListener");
        if (nbt.hasKey("collisionArea")) {
            NBTTagCompound n = (NBTTagCompound) nbt.getTag("collisionArea");
            this.collisionArea = NBTUtils.readAABB(n);
        }
        if (nbt.hasKey("consideredEventsConditions"))
            this.considerEventsConditions = nbt.getBoolean("consideredEventsConditions");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        int i = 0;
        NBTTagList list = new NBTTagList();
        for (LittleTriggerObject triggerObj : triggerObjs) {
            NBTTagCompound n = new NBTTagCompound();
            n.setTag(i + "", triggerObj.createNBT());
            list.appendTag(n);
            i++;
        }
        nbt.setTag("triggers", list);
        nbt.setInteger("currentTick", this.tick);
        nbt.setInteger("currentEvent", this.currentEvent);
        nbt.setBoolean("runWhileCollided", this.runWhileCollided);
        nbt.setBoolean("collisionListener", this.collisionListener);
        nbt.setBoolean("aabbCollisionListener", this.aabbCollisionListener);
        nbt.setBoolean("rightClickListener", this.rightClickListener);
        if (this.collisionArea != null)
            nbt.setTag("collisionArea", NBTUtils.writeAABB(this.collisionArea));
        nbt.setBoolean("consideredEventsConditions", this.considerEventsConditions);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World worldIn, IParentTileList parent, BlockPos pos, Entity entityIn) {
        if (worldIn.isRemote)
            return;
        if (collisionListener) {
            boolean intersected = false;
            for (LittleTile tile : parent) {
                if (tile.getBox().getBox(parent.getContext(), pos).intersects(entityIn.getEntityBoundingBox())) {
                    intersected = true;
                    break;
                }
            }
            if (intersected) {
                this.entities.clear();
                entities.add(entityIn);
                this.run = true;
            }
        }
    }
    
    @Override
    public void checkForAnimationCollision(EntityAnimation animation, HashMap<Entity, AxisAlignedBB> entities) throws CorruptedConnectionException, NotYetConnectedException {
        if (animation.world.isRemote)
            return;
        if (collisionListener) {
            this.entities.clear();
            this.entities.addAll(entities.keySet());
            this.run = true;
        }
    }
    
    @Override
    public void tick() {
        if (!this.isClient()) {
            if (this.aabbCollisionListener)
                if (this.collisionArea != null) {
                    entities.addAll(this.getWorld().getEntitiesWithinAABB(Entity.class, this.collisionArea));
                }
            if (this.run) {
                boolean hasCondition = LittleTriggerObject.hasCondition(this.triggerObjs); //Will reset the run and tick values
                boolean shouldContinue = true; //Will stop the for loop and reset the run and tick values
                boolean flag2 = false; //Will reset the run and tick values
                //for loops through all trigger conditions and events in order.
                while (this.currentEvent < this.triggerObjs.size()) {
                    LittleTriggerObject triggerObj = this.triggerObjs.get(this.currentEvent);
                    if (triggerObj instanceof LittleTriggerCondition) {
                        if (this.triggerObjs.size() > this.currentEvent + 1) {
                            LittleTriggerCondition condition = (LittleTriggerCondition) triggerObj;
                            shouldContinue = condition.conditionRunEvent();
                            if (!shouldContinue && !condition.shouldLoop) {
                                flag2 = true;
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
                //Will stop the loop if there is no entities inside the collision area.
                if (!this.rightClickListener && runWhileCollided) {
                    flag2 = this.entities.isEmpty();
                    entities.clear();
                }
                if (hasCondition && shouldContinue && !flag2)
                    this.getInput(0).updateState(new boolean[] { true });
                else
                    this.getInput(0).updateState(new boolean[] { false });
                //If there is no conditions then there is no need to loop.
                if (this.currentEvent >= triggerObjs.size() || flag2) {
                    this.run = false;
                    this.tick = 0;
                    this.currentEvent = 0;
                    for (LittleTriggerObject triggerObj : this.triggerObjs) {
                        if (triggerObj instanceof LittleTriggerCondition)
                            ((LittleTriggerCondition) triggerObj).completed = false;
                    }
                }
            }
        }
        
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (!this.isClient()) {
            if (this.rightClickListener) {
                this.run = true;
                this.entities.clear();
                this.entities.add(playerIn);
            }
        }
        return true;
    }
    
    public static class LittleTriggerBoxStructureParser extends LittleStructureGuiParser {
        public List<LittleTriggerObject> triggers = new ArrayList<LittleTriggerObject>();
        LittleTriggerObject trigger = null;
        public LittlePreviews previews;
        public boolean runWhileCollided = false;
        public boolean collisionListener = false;
        public boolean aabbCollisionListener = false;
        public boolean rightClickListener = false;
        public AxisAlignedBB collisionArea;
        public boolean consideredEventsConditions = false;
        public String collisionListenerTranslate = I18n.translateToLocal("trigger.listener.collision.name");
        public String aabbCollisionListenerTranslate = I18n.translateToLocal("trigger.listener.aabb_collision.name");
        public String rightClickTranslate = I18n.translateToLocal("trigger.listener.right_click.name");
        
        public LittleTriggerBoxStructureParser(GuiParent parent, AnimationGuiHandler handler) {
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
            LittleTriggerBoxStructureALET triggerBox = (LittleTriggerBoxStructureALET) structure;
            this.previews = previews;
            if (triggerBox != null) {
                this.triggers = triggerBox.triggerObjs;
                this.runWhileCollided = triggerBox.runWhileCollided;
                this.collisionListener = triggerBox.collisionListener;
                this.aabbCollisionListener = triggerBox.aabbCollisionListener;
                this.rightClickListener = triggerBox.rightClickListener;
                this.consideredEventsConditions = triggerBox.considerEventsConditions;
                if (triggerBox.collisionArea != null)
                    this.collisionArea = triggerBox.collisionArea;
            }
            GuiPanel triggerPanel = new GuiPanel("content", 135, 50, 159, 179);
            parent.controls.add(triggerPanel);
            GuiPanel settingsPanel = new GuiPanel("settings", 135, 0, 159, 43);
            parent.controls.add(settingsPanel);
            GuiScrollBox box = new GuiScrollBox("box", 0, 19, 127, 165);
            parent.controls.add(box);
            List<String> listeners = new ArrayList<String>();
            List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
            GuiMenuPart moveUp = new GuiMenuPart("", "Move Up", EnumPartType.Leaf);
            GuiMenuPart moveDown = new GuiMenuPart("", "Move Down", EnumPartType.Leaf);
            GuiMenuPart delete = new GuiMenuPart("", "Delete", EnumPartType.Leaf);
            listOfMenus.add(moveUp);
            listOfMenus.add(moveDown);
            listOfMenus.add(delete);
            GuiMenu menu = new GuiMenu("", 0, 0, 60, listOfMenus);
            menu.height = 100;
            GuiPopupMenu popup = new GuiPopupMenu("popup", menu, 0, 0, 0, 0);
            box.addControl(popup);
            listeners.add(collisionListenerTranslate);
            listeners.add(aabbCollisionListenerTranslate);
            listeners.add(rightClickTranslate);
            GuiComboBox listListener = (new GuiComboBox("listener", 0, 0, 127, listeners) {
                @Override
                protected GuiComboBoxExtension createBox() {
                    return new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 133 - getContentOffset() * 2, 44, lines);
                }
            });
            if (collisionListener) {
                listListener.select(collisionListenerTranslate);
                collisionControls(settingsPanel);
            } else if (aabbCollisionListener) {
                listListener.select(aabbCollisionListenerTranslate);
                aabbCollisionControls(settingsPanel);
            } else if (rightClickListener)
                listListener.select(rightClickTranslate);
            parent.addControl(listListener);
            GuiComboBoxCategory<Class<? extends LittleTriggerObject>> list = (new GuiComboBoxCategory<Class<? extends LittleTriggerObject>>("list", 0, 189, 100, LittleTriggerRegistrar.triggerables) {
                @Override
                protected GuiComboBoxExtensionCategory<Class<? extends LittleTriggerObject>> createBox() {
                    return new GuiComboBoxExtensionCategory<Class<? extends LittleTriggerObject>>(name + "extension", this, posX, posY + height, 133 - getContentOffset() * 2, 100);
                }
            });
            list.height = 19;
            parent.controls.add(list);
            GuiTriggerBoxAddButton add = new GuiTriggerBoxAddButton(this, "Add", 105, 189, 22);
            add.height = 19;
            parent.addControl(add);
            if (triggers != null && !triggers.isEmpty()) {
                for (int i = 0; i < triggers.size(); i++) {
                    box.addControl(new GuiTriggerEventButton(triggers.get(i).getName() + i, I18n.translateToLocal(triggers.get(i).getName()), 0, i * 17, 119, 12));
                }
            }
            parent.addControl(new GuiCheckBox("consider", "Make Events Conditional", 0, 208, consideredEventsConditions));
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public LittleTriggerBoxStructureALET parseStructure(LittlePreviews previews) {
            LittleTriggerBoxStructureALET structure = createStructure(LittleTriggerBoxStructureALET.class, null);
            String selected = ((GuiComboBox) parent.get("listener")).getCaption();
            if (selected.equals(collisionListenerTranslate))
                structure.collisionListener = true;
            else
                structure.collisionListener = false;
            if (selected.equals(aabbCollisionListenerTranslate))
                structure.aabbCollisionListener = true;
            else
                structure.aabbCollisionListener = false;
            if (selected.equals(rightClickTranslate))
                structure.rightClickListener = true;
            else
                structure.rightClickListener = false;
            if (this.collisionArea != null)
                structure.collisionArea = this.collisionArea;
            structure.triggerObjs = this.triggers;
            structure.runWhileCollided = this.runWhileCollided;
            structure.considerEventsConditions = this.consideredEventsConditions;
            return structure;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        protected LittleStructureType getStructureType() {
            return LittleStructureRegistry.getStructureType(LittleTriggerBoxStructureALET.class);
        }
        
        public void collisionControls(GuiPanel settingsPanel) {
            settingsPanel.addControl(new GuiCheckBox("while_collided", "Run Only While Collided", 0, 0, this.runWhileCollided));
        }
        
        public void aabbCollisionControls(GuiPanel settingsPanel) {
            settingsPanel.addControl(new GuiCheckBox("while_collided", "Run Only While Collided", 0, 0, this.runWhileCollided));
            settingsPanel.addControl(new GuiLabel("X1:", 0, 14));
            settingsPanel.addControl(new GuiLabel("X2:", 0, 29));
            settingsPanel.addControl(new GuiLabel("Y1:", 52, 14));
            settingsPanel.addControl(new GuiLabel("Y2:", 52, 29));
            settingsPanel.addControl(new GuiLabel("Z1:", 104, 14));
            settingsPanel.addControl(new GuiLabel("Z2:", 104, 29));
            settingsPanel.addControl(new GuiTextfield("x_min", "0.0", 18, 14, 30, 8).setFloatOnly());
            settingsPanel.addControl(new GuiTextfield("x_max", "0.0", 18, 29, 30, 8).setFloatOnly());
            settingsPanel.addControl(new GuiTextfield("y_min", "0.0", 70, 14, 30, 8).setFloatOnly());
            settingsPanel.addControl(new GuiTextfield("y_max", "0.0", 70, 29, 30, 8).setFloatOnly());
            settingsPanel.addControl(new GuiTextfield("z_min", "0.0", 122, 14, 30, 8).setFloatOnly());
            settingsPanel.addControl(new GuiTextfield("z_max", "0.0", 122, 29, 30, 8).setFloatOnly());
        }
        
        @CustomEventSubscribe
        public void onControlClicked(GuiControlClickEvent event) {}
        
        public void moveTriggerOnList(int distance) {
            int z = 0;
            LittleTriggerObject triggerObj = null;
            for (int i = 0; i < triggers.size(); i++) {
                triggerObj = triggers.get(i);
                String triggerName = triggerObj.getName() + triggerObj.id;
                if (triggerName.equals(trigger.getName() + trigger.id)) {
                    z = i;
                    break;
                }
            }
            if (triggerObj != null && !(z + distance <= -1) && !(z + distance >= triggers.size())) {
                Collections.swap(triggers, z, z + distance);
                updateList();
            }
        }
        
        @CustomEventSubscribe
        public void onControlChanged(GuiControlChangedEvent event) {
            if (event.source.is("while_collided"))
                this.runWhileCollided = ((GuiCheckBox) event.source).value;
            else if (event.source.is("consider"))
                this.consideredEventsConditions = ((GuiCheckBox) event.source).value;
            else if (event.source.is("listener")) {
                GuiComboBox listListener = (GuiComboBox) event.source;
                GuiPanel settingsPanel = (GuiPanel) parent.get("settings");
                settingsPanel.removeControls(" ");
                if (listListener.getCaption().equals(collisionListenerTranslate))
                    collisionControls(settingsPanel);
                else if (listListener.getCaption().equals(aabbCollisionListenerTranslate))
                    aabbCollisionControls(settingsPanel);
            } else if (event.source instanceof GuiMenuPart) {
                String caption = ((GuiMenuPart) event.source).caption;
                if (caption.equals("Move Up")) {
                    moveTriggerOnList(-1);
                } else if (caption.equals("Move Down")) {
                    moveTriggerOnList(1);
                } else if (caption.equals("Delete")) {
                    int z = 0;
                    for (int i = 0; i < triggers.size(); i++) {
                        LittleTriggerObject triggerObj = triggers.get(i);
                        String triggerName = triggerObj.getName() + triggerObj.id;
                        if (triggerName.equals(trigger.getName() + trigger.id)) {
                            z = i;
                            break;
                        }
                    }
                    triggers.remove(z);
                    updateList();
                }
            }
            if (trigger != null)
                trigger.guiChangedEvent(event.source);
        }
        
        public void updateList() {
            GuiScrollBox box = (GuiScrollBox) this.parent.get("box");
            GuiPanel panel = (GuiPanel) this.parent.get("content");
            box.removeControls("popup");
            panel.removeControls(" ");
            for (int i = 0; i < triggers.size(); i++) {
                LittleTriggerObject triggerObj = triggers.get(i);
                triggerObj.id = i;
                box.addControl(new GuiTriggerEventButton(triggerObj.getName() + i, I18n.translateToLocal(triggerObj.getName()), 0, i * 17, 119, 12));
            }
        }
        
        public void updateControls(String name) {
            for (LittleTriggerObject triggerObj : triggers) {
                String objName = triggerObj.getName() + triggerObj.id;
                if (objName.equals(name)) {
                    trigger = triggerObj;
                    break;
                }
            }
            if (trigger != null) {
                GuiPanel panel = (GuiPanel) parent.get("content");
                LittleTriggerObject.wipeControls(panel);
                trigger.createGuiControls(this.parent, this.previews);
            }
        }
        
        public class GuiTriggerEventButton extends GuiButton {
            public GuiTriggerEventButton(String name, String caption, int x, int y, int width, int height) {
                super(name, caption, x, y, width, height);
            }
            
            @Override
            public void onClicked(int x, int y, int button) {
                GuiScrollBox box = (GuiScrollBox) this.getGui().get("box");
                if (button == 0 || button == 1) {
                    for (GuiControl gui : box.controls) {
                        if (gui instanceof GuiTriggerEventButton) {
                            GuiButton b = (GuiButton) gui;
                            b.color = ColorUtils.WHITE;
                        }
                    }
                    this.color = ColorUtils.YELLOW;
                    updateControls(this.name);
                } else if (button == 1) {/*
                                         
                                         */
                }
            }
            
            @Override
            public boolean hasBorder() {
                return false;
            }
        }
        
        public class GuiTriggerBoxAddButton extends GuiButton {
            LittleTriggerBoxStructureParser parser;
            
            public GuiTriggerBoxAddButton(LittleTriggerBoxStructureParser parser, String caption, int x, int y, int width) {
                super(caption, x, y, width);
                this.parser = parser;
            }
            
            @Override
            public void onClicked(int x, int y, int button) {
                GuiScrollBox box = (GuiScrollBox) this.getGui().get("box");
                GuiComboBoxCategory<Class<? extends LittleTriggerObject>> list = (GuiComboBoxCategory<Class<? extends LittleTriggerObject>>) this.getGui().get("list");
                int i = triggers.size();
                parser.triggers.add(LittleTriggerRegistrar.getTriggerObject(list.getSelected().value, i));
                GuiTriggerEventButton bu = new GuiTriggerEventButton(list.getSelected().key + i, list.getCaption(), 0, i * 17, 119, 12);
                box.addControl(bu);
            }
        }
    }
}