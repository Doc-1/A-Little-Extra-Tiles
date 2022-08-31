package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.controls.container.SlotControlNoSync;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelector;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorExtension;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.slots.SlotPreview;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.utils.type.HashMapList;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class GuiStackSelectorExtensionMutator extends GuiStackSelectorExtension {
    
    private EntityPlayer player;
    
    public GuiStackSelectorExtensionMutator(String name, EntityPlayer player, int x, int y, int width, int height, GuiStackSelectorAllMutator comboBox) {
        super(name, player, x, y, width, height, comboBox);
        this.player = player;
    }
    
    @Override
    public void onClosed() {
        if (comboBox != null) {
            
        }
        super.onClosed();
    }
    
    @Override
    @CustomEventSubscribe
    public void onChanged(GuiControlChangedEvent event) {
        GuiStackSelectorAllMutator comboBoxMutator = (GuiStackSelectorAllMutator) comboBox;
        if (event.source.is("searchBar")) {
            search = ((GuiTextfield) event.source).text;
            reloadControls();
            refreshControls();
        } else if (event.source.is("color"))
            comboBoxMutator.color = ColorUtils.RGBAToInt(((GuiColorPickerAlet) get("color")).color);
        else if (event.source.is("noclip")) {
            comboBoxMutator.noclip = ((GuiCheckBox) get("noclip")).value;
        }
    }
    
    @Override
    public void reloadControls() {
        if (comboBox == null)
            return;
        GuiStackSelectorAllMutator comboBoxMutator = (GuiStackSelectorAllMutator) comboBox;
        HashMapList<String, ItemStack> stacks = search == null || search.equals("") ? ((GuiStackSelector) comboBoxMutator).getStacks() : new HashMapList<>();
        
        if (search != null && !search.equals("")) {
            for (Entry<String, ArrayList<ItemStack>> entry : ((GuiStackSelector) comboBoxMutator).getStacks().entrySet()) {
                for (ItemStack stack : entry.getValue()) {
                    if (GuiStackSelectorAll.contains(search, stack))
                        stacks.add(entry.getKey(), stack);
                }
            }
        }
        
        int height = 0;
        GuiTextfield textfield = null;
        if (((GuiStackSelector) comboBoxMutator).searchBar && controls.size() > 0)
            textfield = (GuiTextfield) controls.get(0);
        
        controls.clear();
        
        if (((GuiStackSelector) comboBoxMutator).searchBar) {
            height += 4;
            if (textfield == null)
                textfield = new GuiTextfield("searchBar", search == null ? "" : search, 3, height, width - 20, 10);
            controls.add(textfield);
            height += textfield.height;
            textfield.focused = true;
        }
        GuiColorPickerAlet color = new GuiColorPickerAlet(name + "color", 0, height, ColorUtils.IntToRGBA(comboBoxMutator.color), true, 0);
        controls.add(color);
        height += color.height;
        //GuiCheckBox noclip = new GuiCheckBox("noclip", "No Colision", 0, height, comboBoxMutator.noclip);
        //controls.add(noclip);
        // height += noclip.height;
        for (Entry<String, ArrayList<ItemStack>> entry : stacks.entrySet()) {
            GuiLabel label = new GuiLabel(translate(entry.getKey()), 3, height);
            label.width = width - 20;
            label.height = 14;
            controls.add(label);
            height += label.height;
            
            int SlotsPerRow = (width - 20) / 18;
            
            InventoryBasic basic = new InventoryBasic(entry.getKey(), false, entry.getValue().size());
            int i = 0;
            for (ItemStack stack : entry.getValue()) {
                basic.setInventorySlotContents(i, stack);
                
                int row = i / SlotsPerRow;
                addControl(new SlotControlNoSync(new SlotPreview(basic, i, (i - row * SlotsPerRow) * 18, height + row * 18)).getGuiControl());
                i++;
            }
            
            height += Math.ceil(i / (double) SlotsPerRow) * 18;
        }
    }
}
