package com.alet.common.gui.structure;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.alet.common.gui.LittleItemSelector;
import com.alet.common.gui.controls.GuiFakeSlot;
import com.alet.components.structures.type.LittleLock;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.door.LittleDoor;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class LittleLockGui extends LittleStructureGuiParser {
    
    public List<Integer> possibleChildren;
    public List<Integer> selectedChildren;
    
    public LittleLockGui(GuiParent parent, AnimationGuiHandler handler) {
        super(parent, handler);
    }
    
    public String getDisplayName(LittlePreviews previews, int childId) {
        String name = previews.getStructureName();
        if (name == null)
            if (previews.hasStructure())
                name = previews.getStructureId();
            else
                name = "none";
        return name + " " + childId;
    }
    
    @Override
    protected void createControls(LittlePreviews previews, LittleStructure structure) {
        
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        LittleLock lock = structure instanceof LittleLock ? (LittleLock) structure : null;
        GuiScrollBox box = new GuiScrollBox("content", 0, 0, 100, 115);
        parent.removeControl(box);
        parent.controls.add(box);
        possibleChildren = new ArrayList<>();
        int i = 0;
        int added = 0;
        for (LittlePreviews child : previews.getChildren()) {
            Class clazz = LittleStructureRegistry.getStructureClass(child.getStructureId());
            if (clazz != null && LittleDoor.class.isAssignableFrom(clazz)) {
                box.addControl(new GuiCheckBox("" + i, getDisplayName(child, i), 0, added * 20, lock != null && ArrayUtils
                        .contains(lock.toLock, i)));
                possibleChildren.add(i);
                added++;
            }
            i++;
        }
        parent.controls.add(new GuiLabel("Current Key:", 108, 45));
        parent.controls.add(new GuiButton("use", "Use Item As Key", 110, 23) {
            @Override
            public void onClicked(int x, int y, int button) {
                GuiStackSelectorAll preview = (GuiStackSelectorAll) parent.get("preview");
                GuiFakeSlot key = (GuiFakeSlot) parent.get("key");
                key.updateItemStack(preview.getSelected());
            }
        });
        parent.controls.add(
            new GuiStackSelectorAll("preview", 110, 0, 110, player, new GuiStackSelectorAll.InventoryCollector(new LittleItemSelector()), true));
        
        parent.controls.add(new GuiFakeSlot("key", 175, 41, 18, 18));
        
        parent.controls.add(
            new GuiCheckBox("lockIfOpened", "Lock When Opened", 109, 62, lock != null ? lock.lockIfOpen : true));
        parent.controls.add(
            new GuiCheckBox("playSound", "Play Sound When Locked", 109, 76, lock != null ? lock.playSound : true));
        parent.controls.add(
            new GuiCheckBox("consumeKey", "Consume Key On Use", 109, 90, lock != null ? lock.consumeKey : false));
        if (!player.isCreative()) {
            GuiCheckBox consumeKey = (GuiCheckBox) parent.get("consumeKey");
            consumeKey.enabled = false;
            consumeKey.setCustomTooltip("Must Be In Creaive Mode To Use.");
        }
        
        if (lock != null && !lock.key.isEmpty()) {
            GuiStackSelectorAll preview = (GuiStackSelectorAll) parent.get("preview");
            GuiFakeSlot key = (GuiFakeSlot) parent.get("key");
            key.updateItemStack(lock.key);
        }
    }
    
    @Override
    protected LittleStructure parseStructure(LittlePreviews previews) {
        LittleLock structure = createStructure(LittleLock.class, null);
        GuiScrollBox box = (GuiScrollBox) parent.get("content");
        GuiCheckBox checkLockIfOpen = (GuiCheckBox) parent.get("lockIfOpened");
        GuiCheckBox checkPlaySound = (GuiCheckBox) parent.get("playSound");
        GuiCheckBox checkConsumeKey = (GuiCheckBox) parent.get("consumeKey");
        GuiFakeSlot selectedKey = (GuiFakeSlot) parent.get("key");
        List<Integer> toLock = new ArrayList<>();
        for (Integer integer : possibleChildren) {
            GuiCheckBox checkBox = (GuiCheckBox) box.get("" + integer);
            if (checkBox != null && checkBox.value)
                toLock.add(integer);
        }
        structure.toLock = new int[toLock.size()];
        structure.key = selectedKey.basic.getStackInSlot(0);
        structure.lockIfOpen = checkLockIfOpen.value;
        structure.playSound = checkPlaySound.value;
        structure.consumeKey = checkConsumeKey.value;
        for (int i = 0; i < structure.toLock.length; i++)
            structure.toLock[i] = toLock.get(i);
        return structure;
    }
    
    @Override
    protected LittleStructureType getStructureType() {
        return LittleStructureRegistry.getStructureType(LittleLock.class);
    }
    
}
