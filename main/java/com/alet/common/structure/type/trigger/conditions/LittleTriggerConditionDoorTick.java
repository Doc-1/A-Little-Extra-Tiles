package com.alet.common.structure.type.trigger.conditions;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.structure.type.trigger.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiItemComboBox;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.SubGuiRecipe.StructureHolder;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.type.door.LittleDoorBase;
import com.creativemd.littletiles.common.structure.type.door.LittleDoorBase.LittleDoorBaseType;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LittleTriggerConditionDoorTick extends LittleTriggerCondition {
    
    int tick = 0;
    
    public LittleTriggerConditionDoorTick(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean conditionPassed() {
        if (this.structure.getParent() != null)
            try {
                LittleDoorBase door = (LittleDoorBase) this.structure.getParent().getStructure();
                if (door.animation == null)
                    return false;
                String s0 = door.animation.controller.toString();
                String[] s1 = s0.split(">");
                int i = -1;
                if (s1.length > 1)
                    i = Integer.parseInt(s1[1].replace("-", ""));
                if (i == tick && door.animation.controller.getCurrentState().name.equals("closed"))
                    return true;
                
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        tick = nbt.getInteger("door_tick");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setInteger("door_tick", tick);
        return nbt;
    }
    
    @Override
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        List<StructureHolder> doorHierarchy = new ArrayList<StructureHolder>();
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        List<String> doorNames = new ArrayList<String>();
        addPreviews(findParent(previews), doorHierarchy, stacks, doorNames, "", null, -1, 0);
        panel.addControl(new GuiItemComboBox("doorList", 0, 5, 200, doorNames, stacks));
        panel.addControl(new GuiTextfield("door_tick", tick + "", 0, 30, 50, 10).setNumbersOnly());
    }
    
    public LittlePreviews findParent(LittlePreviews previews) {
        LittlePreviews parentPreview = previews.getParent();
        if (parentPreview != null)
            findParent(parentPreview);
        else
            return previews;
        return parentPreview;
    }
    
    protected static void addPreviews(LittlePreviews previews, List<StructureHolder> hierarchy, List<ItemStack> stacks, List<String> lines, String prefix, StructureHolder parent, int childId, int index) {
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
        }
        index++;
        if (previews.hasChildren()) {
            int i = 0;
            for (LittlePreviews child : previews.getChildren()) {
                addPreviews(child, hierarchy, stacks, lines, prefix + "-", holder, i, index);
                i++;
            }
        }
    }
    
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
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiTextfield) {
            GuiTextfield text = (GuiTextfield) source;
            if (!text.text.equals(""))
                tick = text.parseInteger();
        }
        
    }
    
}
