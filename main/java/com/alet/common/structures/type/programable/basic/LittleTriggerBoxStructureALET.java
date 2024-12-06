package com.alet.common.structures.type.programable.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.alet.client.gui.controls.menu.GuiMenu;
import com.alet.client.gui.controls.menu.GuiMenuPart;
import com.alet.client.gui.controls.menu.GuiPopupMenu;
import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.common.structures.type.programable.basic.activator.LittleTriggerActivator;
import com.alet.common.structures.type.programable.basic.conditions.LittleTriggerCondition;
import com.alet.common.structures.type.programable.basic.events.LittleTriggerEvent;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxCategory;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtensionCategory;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;
import com.creativemd.creativecore.common.world.SubWorldServer;
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
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerBoxStructureALET extends LittleStructure {
    public HashSet<Entity> entities = new HashSet<>();
    public List<UUID> entitiesToLoad;
    public int tick = 0;
    public int currentEvent = 0;
    public boolean run = false;
    public LittleTriggerActivator triggerActivator;
    public boolean considerEventsConditions = false;
    public List<LittleTriggerObject> triggerObjs = new ArrayList<LittleTriggerObject>();
    
    public LittleTriggerBoxStructureALET(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("triggers", Constants.NBT.TAG_COMPOUND);
        int i = 0;
        triggerObjs.clear();
        for (NBTBase base : list) {
            if (base instanceof NBTTagCompound) {
                NBTTagCompound n = (NBTTagCompound) base;
                LittleTriggerObject triggerObj = LittleTriggerRegistrar.getFromNBT((NBTTagCompound) n.getTag(i + ""));
                triggerObjs.add(triggerObj);
                triggerObj.structure = this;
                
                i++;
            }
        }
        NBTTagList entityList = nbt.getTagList("entities", Constants.NBT.TAG_STRING);
        this.entitiesToLoad = new ArrayList<UUID>();
        for (int j = 0; j < entityList.tagCount(); j++) {
            entitiesToLoad.add(UUID.fromString(entityList.getStringTagAt(j)));
        }
        if (nbt.hasKey("activator")) {
            triggerActivator = (LittleTriggerActivator) LittleTriggerRegistrar.getFromNBT(nbt.getCompoundTag("activator"));
            triggerActivator.structure = this;
        }
        run = nbt.getBoolean("isRunning");
        tick = nbt.getInteger("currentTick");
        currentEvent = nbt.getInteger("currentEvent");
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
        NBTTagList entityList = new NBTTagList();
        if (this.entities != null && !this.entities.isEmpty()) {
            for (Entity entity : this.entities) {
                entityList.appendTag(new NBTTagString(entity.getUniqueID().toString()));
            }
        }
        if (this.triggerActivator != null)
            nbt.setTag("activator", this.triggerActivator.createNBT());
        
        nbt.setBoolean("isRunning", run);
        nbt.setTag("triggers", list);
        nbt.setTag("entities", entityList);
        nbt.setInteger("currentTick", this.tick);
        nbt.setInteger("currentEvent", this.currentEvent);
        /*
        if (this.collisionArea != null)
            nbt.setTag("collisionArea", NBTUtils.writeAABB(this.collisionArea));*/
        nbt.setBoolean("consideredEventsConditions", this.considerEventsConditions);
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
            this.triggerActivator.onCollision(worldIn, entityIn);
        }
        
    }
    
    @Override
    public void checkForAnimationCollision(EntityAnimation animation, HashMap<Entity, AxisAlignedBB> entities) throws CorruptedConnectionException, NotYetConnectedException {
        if (animation.world.isRemote)
            return;
        this.triggerActivator.onCollision(animation.world, entities.keySet());
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (!this.isClient())
            this.triggerActivator.onRightClick(worldIn, tile, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ, action);
        return true;
    }
    
    @Override
    public void tick() {
        
        if (!this.isClient()) {
            World world = this.getWorld();
            if (world instanceof SubWorldServer)
                world = ((SubWorldServer) world).getRealWorld();
            WorldServer server = (WorldServer) world;
            if (this.entitiesToLoad != null) {
                for (Iterator<UUID> iterator = entitiesToLoad.iterator(); iterator.hasNext();) {
                    UUID uuid = iterator.next();
                    Entity en = server.getEntityFromUuid(uuid);
                    if (en != null) {
                        this.entities.add(en);
                        iterator.remove();
                    }
                }
                if (entitiesToLoad.isEmpty())
                    entitiesToLoad = null;
            }
            
            this.run = this.triggerActivator.shouldRun(world, entities);
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
                        if (!event && this.considerEventsConditions) {
                            this.run = false;
                            this.tick = 0;
                            this.currentEvent = 0;
                            this.entities.clear();
                            for (LittleTriggerObject triggerObj2 : this.triggerObjs)
                                if (triggerObj2 instanceof LittleTriggerCondition)
                                    ((LittleTriggerCondition) triggerObj2).completed = false;
                        }
                        
                    }
                    if (!shouldContinue)
                        break;
                    this.currentEvent++;
                    
                }
                
                this.triggerActivator.loopRules(entities, shouldContinue, flag);
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
        
    }
    
    public static class LittleTriggerBoxStructureParser extends LittleStructureGuiParser {
        public List<LittleTriggerObject> triggers = new ArrayList<LittleTriggerObject>();
        LittleTriggerObject trigger = null;
        public LittlePreviews previews;
        public boolean runWhileCollided = false;
        public AxisAlignedBB collisionArea;
        public LittleTriggerActivator triggerActivator;
        public boolean consideredEventsConditions = false;
        
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
                this.consideredEventsConditions = triggerBox.considerEventsConditions;
                this.triggerActivator = triggerBox.triggerActivator;
            }
            GuiPanel settingsPanel = new GuiPanel("settings", 135, 50, 159, 179);
            parent.controls.add(settingsPanel);
            GuiPanel triggerPanel = new GuiPanel("trigger", 135, 0, 159, 43);
            parent.controls.add(triggerPanel);
            GuiScrollBox box = new GuiScrollBox("box", 0, 19, 127, 165);
            parent.controls.add(box);
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
            GuiComboBoxCategory<Class<? extends LittleTriggerActivator>> listActivators = (new GuiComboBoxCategory<Class<? extends LittleTriggerActivator>>("activators", 0, 0, 127, LittleTriggerRegistrar.triggerActivators) {
                @Override
                public void setCaption(String caption) {
                    int size = -1;
                    for (PairList<String, Class<? extends LittleTriggerActivator>> pair : elements.values()) {
                        for (Pair<String, Class<? extends LittleTriggerActivator>> d : pair) {
                            size++;
                            String key = getKeyAtIndex(size);
                            if (key.equals(caption))
                                select(size);
                        }
                    }
                }
                
                public String getKeyAtIndex(int index) {
                    int currentIndex = 0;
                    if (index == -1)
                        return "";
                    for (Pair<String, PairList<String, Class<? extends LittleTriggerActivator>>> pair : elements) {
                        if (index >= currentIndex + pair.value.size())
                            currentIndex += pair.value.size();
                        else {
                            return pair.value.get(index - currentIndex).key;
                        }
                    }
                    return "";
                }
                
                @Override
                public boolean select(int index) {
                    int currentIndex = 0;
                    if (index == -1)
                        return false;
                    for (Pair<String, PairList<String, Class<? extends LittleTriggerActivator>>> pair : elements) {
                        if (index >= currentIndex + pair.value.size())
                            currentIndex += pair.value.size();
                        else {
                            caption = translate(pair.value.get(index - currentIndex).key);
                            this.index = index;
                            raiseEvent(new GuiControlChangedEvent(this));
                            return true;
                        }
                    }
                    return false;
                }
                
                @Override
                protected GuiComboBoxExtensionCategory<Class<? extends LittleTriggerActivator>> createBox() {
                    return new GuiComboBoxExtensionCategory<Class<? extends LittleTriggerActivator>>(name + "extension", this, posX, posY + height, 133 - getContentOffset() * 2, 100);
                }
            });
            if (this.triggerActivator == null)
                createActivator(listActivators.getSelected().value);
            if (this.triggerActivator != null) {
                listActivators.setCaption(triggerActivator.getName());
                updateActivatorControls();
            }
            parent.addControl(listActivators);
            GuiComboBoxCategory<Class<? extends LittleTriggerObject>> list = (new GuiComboBoxCategory<Class<? extends LittleTriggerObject>>("list", 0, 189, 100, LittleTriggerRegistrar.triggerableObjects) {
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
                    box.addControl(new GuiTriggerEventButton(triggers.get(i).getName() + i, I18n.translateToLocal(triggers
                            .get(i).getName()), 0, i * 17, 119, 12));
                }
            }
            parent.addControl(new GuiCheckBox("consider", "Make Events Conditional", 0, 208, consideredEventsConditions));
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public LittleTriggerBoxStructureALET parseStructure(LittlePreviews previews) {
            LittleTriggerBoxStructureALET structure = createStructure(LittleTriggerBoxStructureALET.class, null);
            structure.triggerObjs = this.triggers;
            structure.triggerActivator = this.triggerActivator;
            structure.considerEventsConditions = this.consideredEventsConditions;
            return structure;
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        protected LittleStructureType getStructureType() {
            return LittleStructureRegistry.getStructureType(LittleTriggerBoxStructureALET.class);
        }
        
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
        
        public void createActivator(Class<? extends LittleTriggerActivator> clazz) {
            this.triggerActivator = LittleTriggerRegistrar.getTriggerActivator(clazz);
        }
        
        public void createActivatorGui() {
            GuiPanel panel = this.triggerActivator.getPanel(parent);
            LittleTriggerObject.wipeControls(panel);
            this.triggerActivator.createGuiControls(panel, previews);
        }
        
        @CustomEventSubscribe
        public void onControlChanged(GuiControlChangedEvent event) {
            if (event.source.is("while_collided"))
                this.runWhileCollided = ((GuiCheckBox) event.source).value;
            else if (event.source.is("consider"))
                this.consideredEventsConditions = ((GuiCheckBox) event.source).value;
            else if (event.source.is("activators")) {
                GuiComboBoxCategory<Class<? extends LittleTriggerActivator>> listActivators = (GuiComboBoxCategory<Class<? extends LittleTriggerActivator>>) event.source;
                createActivator(listActivators.getSelected().value);
                createActivatorGui();
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
            if (triggerActivator != null)
                triggerActivator.guiChangedEvent(event.source);
        }
        
        public void updateList() {
            GuiScrollBox box = (GuiScrollBox) this.parent.get("box");
            GuiPanel panel = (GuiPanel) this.parent.get("settings");
            box.removeControls("popup");
            LittleTriggerObject.wipeControls(panel);
            for (int i = 0; i < triggers.size(); i++) {
                LittleTriggerObject triggerObj = triggers.get(i);
                triggerObj.id = i;
                box.addControl(new GuiTriggerEventButton(triggerObj.getName() + i, I18n.translateToLocal(triggerObj
                        .getName()), 0, i * 17, 119, 12));
            }
        }
        
        public void updateActivatorControls() {
            if (this.triggerActivator != null) {
                GuiPanel panel = triggerActivator.getPanel(parent);
                LittleTriggerObject.wipeControls(panel);
                triggerActivator.createGuiControls(panel, this.previews);
            }
        }
        
        public void updateObjectControls(String name) {
            for (LittleTriggerObject triggerObj : triggers) {
                String objName = triggerObj.getName() + triggerObj.id;
                if (objName.equals(name)) {
                    trigger = triggerObj;
                    break;
                }
            }
            if (trigger != null) {
                GuiPanel panel = trigger.getPanel(parent);
                LittleTriggerObject.wipeControls(panel);
                trigger.createGuiControls(panel, this.previews);
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
                    updateObjectControls(this.name);
                }
            }
            
            @Override
            public boolean hasBorder() {
                return false;
            }
        }
        
        public class GuiTriggerBoxAddButton extends GuiButton {
            
            public GuiTriggerBoxAddButton(LittleTriggerBoxStructureParser parser, String caption, int x, int y, int width) {
                super(caption, x, y, width);
            }
            
            @Override
            public void onClicked(int x, int y, int button) {
                GuiScrollBox box = (GuiScrollBox) this.getGui().get("box");
                GuiComboBoxCategory<Class<? extends LittleTriggerObject>> list = (GuiComboBoxCategory<Class<? extends LittleTriggerObject>>) this
                        .getGui().get("list");
                int i = triggers.size();
                triggers.add(LittleTriggerRegistrar.getTriggerObject(list.getSelected().value, i));
                GuiTriggerEventButton bu = new GuiTriggerEventButton(list.getSelected().key + i, list
                        .getCaption(), 0, i * 17, 119, 12);
                box.addControl(bu);
            }
        }
    }
}
