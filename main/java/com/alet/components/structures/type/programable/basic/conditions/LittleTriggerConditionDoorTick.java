package com.alet.components.structures.type.programable.basic.conditions;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.gui.controls.GuiConnectedCheckBoxes;
import com.alet.components.structures.type.programable.basic.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiItemComboBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.SubGuiRecipe.StructureHolder;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.type.door.LittleDoorBase;
import com.creativemd.littletiles.common.structure.type.door.LittleDoorBase.LittleDoorBaseType;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerConditionDoorTick extends LittleTriggerCondition {
    
    int tick = 0;
    int childID = 0;
    boolean onClose = false;
    
    public LittleTriggerConditionDoorTick(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean conditionPassed() {
        try {
            LittleStructure struct = this.structure.getChild(childID).getStructure();
            if (struct instanceof LittleDoorBase) {
                LittleDoorBase door = (LittleDoorBase) struct;
                if (door.animation == null)
                    return false;
                String s0 = door.animation.controller.toString();
                String[] s1 = s0.split(">");
                int i = -1;
                if (s1.length > 1)
                    i = Integer.parseInt(s1[1].replace("-", ""));
                boolean flag = onClose ? door.animation.controller.getCurrentState().name.equals(
                    "opened") : door.animation.controller.getCurrentState().name.equals("closed");
                if (i == tick && flag)
                    return true;
            } else
                return false;
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        tick = nbt.getInteger("door_tick");
        childID = nbt.getInteger("child_id");
        onClose = nbt.getBoolean("on_close");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setInteger("door_tick", tick);
        nbt.setInteger("child_id", childID);
        nbt.setBoolean("on_close", onClose);
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        List<StructureHolder> doorHierarchy = new ArrayList<StructureHolder>();
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        List<String> doorNames = new ArrayList<String>();
        List<Integer> childIDs = new ArrayList<Integer>();
        addPreviews(findParent(previews), doorHierarchy, stacks, doorNames, childIDs, "", null, -1, 0);
        GuiDoorComboBox combo = new GuiDoorComboBox("doorList", 0, 5, 200, doorNames, stacks, childIDs);
        int select = 0;
        for (int i = 0; i < doorHierarchy.size(); i++) {
            if (doorHierarchy.get(i).index == this.childID) {
                select = i;
                break;
            }
        }
        combo.select(select);
        panel.raiseEvent(new GuiControlChangedEvent(combo));
        panel.addControl(combo);
        panel.addControl(new GuiTextfield("door_tick", tick + "", 0, 30, 50, 10).setNumbersOnly());
        GuiConnectedCheckBoxes doorState = new GuiConnectedCheckBoxes("door_state", 0, 50).addCheckBox("on_open",
            "On Opening").addCheckBox("on_close", "On Closing");
        panel.addControl(doorState);
        if (!onClose)
            doorState.setSelected("on_open");
        else
            doorState.setSelected("on_close");
        
    }
    
    @SideOnly(Side.CLIENT)
    public LittlePreviews findParent(LittlePreviews previews) {
        LittlePreviews parentPreview = previews.getParent();
        if (parentPreview != null)
            findParent(parentPreview);
        else
            return previews;
        return parentPreview;
    }
    
    @SideOnly(Side.CLIENT)
    protected static void addPreviews(LittlePreviews previews, List<StructureHolder> hierarchy, List<ItemStack> stacks, List<String> lines, List<Integer> childIDs, String prefix, StructureHolder parent, int childId, int index) {
        StructureHolder holder = new StructureHolder(parent, childId, hierarchy.size());
        holder.previews = previews;
        holder.prefix = prefix;
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
        if (previews.getStructureType() instanceof LittleDoorBaseType) {
            stacks.add(stack);
            hierarchy.add(holder);
            lines.add(index + " " + holder.getDisplayName());
            childIDs.add(childId);
        }
        index++;
        if (previews.hasChildren()) {
            int i = 0;
            for (LittlePreviews child : previews.getChildren()) {
                addPreviews(child, hierarchy, stacks, lines, childIDs, prefix + "-", holder, i, index);
                i++;
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public String getDisplayName(LittlePreviews previews, int childId) {
        String name = previews.getStructureName();
        if (name == null)
            if (previews.hasStructure())
                name = previews.getStructureId();
            else
                name = "none";
        return childId + " " + name;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiTextfield) {
            GuiTextfield text = (GuiTextfield) source;
            if (!text.text.equals(""))
                tick = text.parseInteger();
        }
        if (source.is("doorList")) {
            GuiDoorComboBox combo = (GuiDoorComboBox) source;
            if (!combo.lines.isEmpty())
                this.childID = combo.childIDs.get(combo.index);
        }
        if (source.is("door_state")) {
            GuiConnectedCheckBoxes doorState = (GuiConnectedCheckBoxes) source;
            this.onClose = doorState.getSelected().name.equals("on_close");
        }
    }
    
    protected class GuiDoorComboBox extends GuiItemComboBox {
        List<Integer> childIDs;
        
        public GuiDoorComboBox(String name, int x, int y, int width, List<String> lines, List<ItemStack> stacks, List<Integer> childIDs) {
            super(name, x, y, width, lines, stacks);
            this.childIDs = childIDs;
        }
    }
    
}
