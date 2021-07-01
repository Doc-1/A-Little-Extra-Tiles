package com.alet.client.gui.override;

import com.alet.client.gui.controls.GuiToolTipBox;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.littletiles.client.gui.configure.SubGuiModeSelector;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

public class SubGuiOverrideConfigure implements IOverrideSubGui {
	
	@Override
	public void modifyControls(SubGui gui) {
		
		SubGuiModeSelector gridGui = (SubGuiModeSelector) gui;
		GuiComboBox contextBox = (GuiComboBox) gridGui.get("grid");
		if (contextBox != null) {
			gridGui.removeControl(gridGui.get("grid"));
			contextBox = (new GuiComboBox("grid", 128, 0, 15, LittleGridContext.getNames()) {
				
				@Override
				protected GuiComboBoxExtension createBox() {
					return new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 35 - getContentOffset() * 2, 100, lines);
				}
			});
			contextBox.select(ItemMultiTiles.currentContext.size + "");
			gridGui.controls.add(contextBox);
		}
		
		GuiToolTipBox tips = new GuiToolTipBox("tips");
		tips.addAdditionalTips("MainGui", "Will add more info soon.");
		gridGui.controls.add(tips);
		
		gridGui.refreshControls();
	}
	
}
