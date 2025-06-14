package com.alet.common.gui.structure.premade.filling_cabinet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAvatarLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.signal.SubGuiDialogSignal.GuiSignalComponent;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.component.ISignalComponent;
import com.creativemd.littletiles.common.structure.signal.component.SignalComponentType;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.converation.StructureStringUtils;
import com.creativemd.littletiles.common.util.ingredient.BlockIngredient;
import com.creativemd.littletiles.common.util.ingredient.BlockIngredientEntry;
import com.creativemd.littletiles.common.util.ingredient.ColorIngredient;
import com.creativemd.littletiles.common.util.ingredient.LittleIngredient;
import com.creativemd.littletiles.common.util.ingredient.LittleIngredients;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiBlueprintDetails extends SubGui {
    LittlePreviews previews;
    File file;
    NBTTagCompound nbt;
    int black;
    int cyan;
    int magenta;
    int yellow;
    
    public SubGuiBlueprintDetails(String nbtString, File file) {
        super(180, 320);
        try {
            this.nbt = JsonToNBT.getTagFromJson(nbtString);
            ItemStack stack = StructureStringUtils.importStructure(nbt);
            this.previews = LittlePreviews.getPreview(stack, false);
            this.file = file;
        } catch (NBTException e) {
            e.printStackTrace();
        }
        
    }
    
    public void collectIngredients(GuiScrollBox scroll) {
        LittleIngredients ingredients = LittleAction.getIngredients(previews);
        int i = 0;
        for (LittleIngredient<?> ingredient : ingredients) {
            if (ingredient instanceof BlockIngredient) {
                BlockIngredient block = (BlockIngredient) ingredient;
                
                for (BlockIngredientEntry entry : block) {
                    ItemStack stack = entry.getItemStack();
                    
                    scroll.addControl(new GuiAvatarLabel("", 0, (i * 18), ColorUtils.WHITE, new AvatarItemStack(stack)));
                    scroll.addControl(new GuiLabel(BlockIngredient.printVolume(entry.value, false) + " " + stack
                            .getDisplayName(), 20, (i * 18) + 4));
                    i++;
                }
            }
            
            if (ingredient instanceof ColorIngredient) {
                ColorIngredient color = (ColorIngredient) ingredient;
                this.black = color.black;
                this.cyan = color.cyan;
                this.magenta = color.magenta;
                this.yellow = color.yellow;
            }
        }
    }
    
    @Override
    public void createControls() {
        GuiLabel structureName = new GuiLabel("Structure Name:", 0, 0, ColorUtils.WHITE);
        GuiLabel name = new GuiLabel(previews.getStructureName(), 83, 0, ColorUtils.WHITE);
        GuiLabel structureID = new GuiLabel("Structure ID:", 0, 14, ColorUtils.WHITE);
        GuiLabel id = new GuiLabel(previews.getStructureId(), 69, 14, ColorUtils.WHITE);
        GuiLabel tileCount = new GuiLabel("Tile Count:", 0, 28, ColorUtils.WHITE);
        GuiLabel tile = new GuiLabel(previews.totalSize() + "", 54, 28, ColorUtils.WHITE);
        GuiLabel childCount = new GuiLabel("Child Count:", 0, 42, ColorUtils.WHITE);
        GuiLabel child = new GuiLabel(previews.childrenCount() + "", 60, 42, ColorUtils.WHITE);
        GuiLabel hasSignal = new GuiLabel("Uses Signaling:", 0, 56, ColorUtils.WHITE);
        ComponentSearch search = new ComponentSearch(previews, previews.getStructureType());
        List<GuiSignalComponent> searching = search.search(true, true, false);
        GuiLabel signal = new GuiLabel((!searching.isEmpty()) + "", 76, 56, ColorUtils.WHITE);
        GuiLabel fileSize = new GuiLabel("File Size:", 0, 70, ColorUtils.WHITE);
        GuiLabel size = new GuiLabel((file.length() / 1000D) + " KB", 47, 70, ColorUtils.WHITE);
        GuiLabel modifyDate = new GuiLabel("Modified Date:", 0, 84, ColorUtils.WHITE);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        GuiLabel date = new GuiLabel(sdf.format(file.lastModified()), 70, 84, ColorUtils.WHITE);
        GuiLabel grid = new GuiLabel("Grid:", 0, 98);
        GuiLabel gridSize = new GuiLabel(previews.getContext() + "", 26, 98);
        GuiLabel colors = new GuiLabel("Dyes Required:", 0, 112, ColorUtils.WHITE);
        GuiLabel black = new GuiLabel("Black:", 10, 126, 0x707070);
        GuiLabel cyan = new GuiLabel("Cyan:", 10, 140, 0x00FFFF);
        GuiLabel megenta = new GuiLabel("Magenta:", 10, 154, ColorUtils.MAGENTA);
        GuiLabel yellow = new GuiLabel("Yellow:", 10, 168, ColorUtils.YELLOW);
        GuiLabel b = new GuiLabel(this.black + "", 41, 126, 0x707070);
        GuiLabel c = new GuiLabel(this.cyan + "", 39, 140, 0x00FFFF);
        GuiLabel m = new GuiLabel(this.magenta + "", 55, 154, ColorUtils.MAGENTA);
        GuiLabel y = new GuiLabel(this.yellow + "", 45, 168, ColorUtils.YELLOW);
        GuiLabel material = new GuiLabel("Material Required:", 0, 200);
        GuiScrollBox scroll = new GuiScrollBox("materialBox", 0, 214, 174, 100);
        addControl(scroll);
        collectIngredients(scroll);
        addControl(structureID);
        addControl(id);
        addControl(structureName);
        addControl(name);
        addControl(tileCount);
        addControl(tile);
        addControl(childCount);
        addControl(child);
        addControl(hasSignal);
        addControl(signal);
        addControl(fileSize);
        addControl(size);
        addControl(modifyDate);
        addControl(date);
        addControl(grid);
        addControl(gridSize);
        addControl(colors);
        addControl(black);
        addControl(cyan);
        addControl(megenta);
        addControl(yellow);
        addControl(b);
        addControl(c);
        addControl(m);
        addControl(y);
        addControl(material);
    }
    
    private static class ComponentSearch {
        
        public LittlePreviews previews;
        public LittleStructureType type;
        
        public ComponentSearch(LittlePreviews previews, LittleStructureType type) {
            this.previews = previews;
            this.type = type;
        }
        
        public List<GuiSignalComponent> search(boolean input, boolean output, boolean includeRelations) {
            List<GuiSignalComponent> list = new ArrayList<>();
            if (input)
                gatherInputs(previews, type, "", "", list, includeRelations, true);
            if (output)
                gatherOutputs(previews, type, "", "", list, includeRelations, true);
            return list;
        }
        
        protected void addInput(LittlePreviews previews, LittleStructureType type, String prefix, String totalNamePrefix, List<GuiSignalComponent> list, boolean includeRelations) {
            String longName = previews.getStructureName();
            if (type != null && type.inputs != null)
                for (int i = 0; i < type.inputs.size(); i++)
                    list.add(new GuiSignalComponent(prefix + "a" + i, totalNamePrefix, type.inputs.get(
                        i), true, false, i, longName));
                
            for (int i = 0; i < previews.childrenCount(); i++) {
                LittlePreviews child = previews.getChild(i);
                if (child == this.previews)
                    continue;
                LittleStructureType structure = child.getStructureType();
                String name = child.getStructureName();
                if (structure instanceof ISignalComponent && ((ISignalComponent) structure)
                        .getType() == SignalComponentType.INPUT)
                    list.add(
                        new GuiSignalComponent(prefix + "i" + i, totalNamePrefix + (name != null ? name : "i" + i), (ISignalComponent) structure, true, i, longName));
                else if (includeRelations)
                    gatherInputs(child, child.getStructureType(), prefix + "c" + i + ".",
                        totalNamePrefix + (name != null ? name + "." : "c" + i + "."), list, includeRelations, false);
            }
        }
        
        protected void gatherInputs(LittlePreviews previews, LittleStructureType type, String prefix, String totalNamePrefix, List<GuiSignalComponent> list, boolean includeRelations, boolean searchForParent) {
            if (previews == this.previews)
                addInput(previews, type, "", "", list, includeRelations);
            
            if (searchForParent && previews.hasParent() && includeRelations) {
                gatherInputs(previews.getParent(), previews.getParent().getStructureType(), "p." + prefix,
                    "p." + totalNamePrefix, list, includeRelations, true);
                return;
            }
            
            if (previews != this.previews)
                addInput(previews, type, prefix, totalNamePrefix, list, includeRelations);
        }
        
        protected void addOutput(LittlePreviews previews, LittleStructureType type, String prefix, String totalNamePrefix, List<GuiSignalComponent> list, boolean includeRelations) {
            String longName = previews.getStructureName();
            if (type != null && type.outputs != null)
                for (int i = 0; i < type.outputs.size(); i++)
                    list.add(new GuiSignalComponent(prefix + "b" + i, totalNamePrefix, type.outputs.get(
                        i), false, false, i, longName));
                
            for (int i = 0; i < previews.childrenCount(); i++) {
                LittlePreviews child = previews.getChild(i);
                if (child == this.previews)
                    continue;
                LittleStructureType structure = child.getStructureType();
                String name = child.getStructureName();
                if (structure instanceof ISignalComponent && ((ISignalComponent) structure)
                        .getType() == SignalComponentType.OUTPUT)
                    list.add(
                        new GuiSignalComponent(prefix + "o" + i, totalNamePrefix + (name != null ? name : "o" + i), (ISignalComponent) structure, true, i, longName));
                else if (includeRelations)
                    gatherOutputs(child, child.getStructureType(), prefix + "c" + i + ".",
                        totalNamePrefix + (name != null ? name + "." : "c" + i + "."), list, includeRelations, false);
            }
        }
        
        protected void gatherOutputs(LittlePreviews previews, LittleStructureType type, String prefix, String totalNamePrefix, List<GuiSignalComponent> list, boolean includeRelations, boolean searchForParent) {
            if (previews == this.previews)
                addOutput(previews, type, "", "", list, includeRelations);
            
            if (searchForParent && previews.hasParent() && includeRelations) {
                gatherOutputs(previews.getParent(), previews.getParent().getStructureType(), "p." + prefix,
                    "p." + totalNamePrefix, list, includeRelations, searchForParent);
                return;
            }
            
            if (previews != this.previews)
                addOutput(previews, type, prefix, totalNamePrefix, list, includeRelations);
            
        }
    }
}
