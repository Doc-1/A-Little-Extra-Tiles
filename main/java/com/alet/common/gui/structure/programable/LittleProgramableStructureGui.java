package com.alet.common.gui.structure.programable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.alet.common.gui.controls.menu.GuiMenu;
import com.alet.common.gui.controls.menu.GuiMenuPart;
import com.alet.common.gui.controls.menu.GuiPopupMenu;
import com.alet.common.gui.controls.menu.GuiTreePart;
import com.alet.common.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.components.structures.type.programable.basic.LittleProgramableStructure;
import com.alet.components.structures.type.programable.basic.LittleTriggerObject;
import com.alet.components.structures.type.programable.basic.LittleTriggerRegistrar;
import com.alet.components.structures.type.programable.basic.activator.LittleTriggerActivator;
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
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents.GuiSignalEventsButton;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleProgramableStructureGui extends LittleStructureGuiParser {
    public List<LittleTriggerObject> triggers = new ArrayList<LittleTriggerObject>();
    LittleTriggerObject trigger = null;
    public LittlePreviews previews;
    public boolean runWhileCollided = false;
    public AxisAlignedBB collisionArea;
    public LittleTriggerActivator triggerActivator;
    public boolean consideredEventsConditions = false;
    
    public LittleProgramableStructureGui(GuiParent parent, AnimationGuiHandler handler) {
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
        LittleProgramableStructure triggerBox = (LittleProgramableStructure) structure;
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
        GuiTriggerBoxAddButton add = new GuiTriggerBoxAddButton("Add", 105, 189, 22);
        add.height = 19;
        parent.addControl(add);
        if (triggers != null && !triggers.isEmpty()) {
            for (int i = 0; i < triggers.size(); i++) {
                box.addControl(new GuiTriggerEventButton(triggers.get(i).getName() + i, I18n.translateToLocal(triggers.get(i)
                        .getName()), 0, i * 17, 119, 12));
            }
        }
        parent.addControl(new GuiCheckBox("consider", "Make Events Conditional", 0, 208, consideredEventsConditions));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public LittleProgramableStructure parseStructure(LittlePreviews previews) {
        LittleProgramableStructure structure = createStructure(LittleProgramableStructure.class, null);
        structure.triggerObjs = this.triggers;
        structure.triggerActivator = this.triggerActivator;
        structure.considerEventsConditions = this.consideredEventsConditions;
        return structure;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    protected LittleStructureType getStructureType() {
        return LittleStructureRegistry.getStructureType(LittleProgramableStructure.class);
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
        
        public GuiTriggerBoxAddButton(String caption, int x, int y, int width) {
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