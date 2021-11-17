package com.alet.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.Color;

import com.alet.client.gui.controls.GuiColorablePanel;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;

public class SubGuiManual extends SubGui {
	
	public SubGuiManual() {
		super(600, 400);
	}
	
	@Override
	public void createControls() {
		GuiColorablePanel panel = new GuiColorablePanel("table", 0, 0, 200, 394, new Color(0, 0, 0), new Color(198, 198, 198));
		GuiScrollBox scrollBox = new GuiScrollBox("scrollBox", 0, 0, 194, 388);
		scrollBox.setStyle(defaultStyle);
		controls.add(panel);
		panel.controls.add(scrollBox);
		
		List<GuiTreeButton> listOfMenus = new ArrayList<GuiTreeButton>();
		GuiTreeButton a1 = new GuiTreeButton("old", "Draw Shapes");
		GuiTreeButton a2 = new GuiTreeButton("old", "Box");
		GuiTreeButton a3 = new GuiTreeButton("old", "Slice");
		GuiTreeButton a3_1 = new GuiTreeButton("old", "Corner Slice");
		GuiTreeButton a4_1 = new GuiTreeButton("old", "OTHER");
		GuiTreeButton a4 = new GuiTreeButton("old", "Wall");
		GuiTreeButton b1 = new GuiTreeButton("old", "Door");
		a3.addMenu(a3_1);
		a1.addMenu(a2).addMenu(a3).addMenu(a4);
		listOfMenus.add(a1);
		listOfMenus.add(b1);
		
		scrollBox.controls.add(new GuiTree("tree", 0, 0, 194, listOfMenus));
		
	}
	
	public class GuiTree extends GuiParent {
		
		public List<GuiTreeButton> listOfMenus;
		public List<GuiTreeButton> listOfAllSubTrees = new ArrayList<GuiTreeButton>();
		public Map<GuiTreeButton, GuiTreeButton> mapOfRootToSubTrees = new HashMap<GuiTreeButton, GuiTreeButton>();
		private int indexPos = 0;
		
		public GuiTree(String name, int x, int y, int width, List<GuiTreeButton> listOfMenus) {
			super(name, x, y, width, 320);
			this.listOfMenus = listOfMenus;
			createControls();
			allButtons();
		}
		
		public void createControls() {
			for (int i = 0; i < listOfMenus.size(); i++) {
				GuiTreeButton button = listOfMenus.get(i);
				button.posY = 14 * i;
				button.originPosY = new Integer(button.posY);
				addControl(button);
			}
		}
		
		public void allButtons() {
			for (int i = 0; i < listOfMenus.size(); i++) {
				GuiTreeButton button = listOfMenus.get(i);
				button.isRoot = true;
				button.parentIndex = indexPos;
				button.name = indexPos++ + "";
				button.tree = this;
				listOfAllSubTrees.add(button);
				if (button.listOfMenus != null && !button.listOfMenus.isEmpty())
					allButtons(button, i);
			}
		}
		
		public void allButtons(GuiTreeButton root, int j) {
			for (int i = 0; i < root.listOfMenus.size(); i++) {
				GuiTreeButton button = root.listOfMenus.get(i);
				button.parentIndex = Integer.parseInt(root.name);
				button.name = indexPos++ + "";
				button.tree = this;
				listOfAllSubTrees.add(button);
				if (button.listOfMenus != null && !button.listOfMenus.isEmpty()) {
					allButtons(button, j);
				}
			}
		}
		
		public void onMenuChanged() {
		}
		
		public void moveTreePartsDown(GuiTreeButton button) {
			int id = 0;
			int moveRoot = 0;
			int move = 0;
			for (GuiTreeButton part : listOfAllSubTrees) {
				if (part.opened) {
					moveRoot += part.getTotalBranchSize();
					move = part.getTotalBranchSize();
					id = part.getPartID();
				}
				if (!part.isRoot && part.getPartID() > (move + id)) {
					System.out.println("ID " + part.getPartID());
					System.out.println(move + id);
					part.posY = part.originPosY + (14 * (move));
				}
			}
			for (GuiTreeButton part : listOfAllSubTrees) {
				if (part.isRoot && part.getPartID() != 0)
					part.posY = part.originPosY + (14 * (moveRoot));
				
			}
		}
		
		public void moveTreePartsUp(GuiTreeButton button) {
			int id = -1;
			int move = 1;
			for (GuiTreeButton button2 : listOfAllSubTrees) {
				if (button2.opened) {
					move += button2.getTotalBranchSize();
					id = button2.getPartID();
				}
				
				if (!button2.isRoot && button2.getPartID() > move + id) {
					button2.posY = (14 * (move));
				}
			}
			for (GuiTreeButton root : listOfMenus) {
				if (root.getPartID() != 0)
					root.posY = (14 * (move));
			}
		}
		
		@Override
		public boolean hasBackground() {
			return false;
		}
		
		@Override
		public boolean hasBorder() {
			return false;
		}
		
	}
	
	public class GuiTreeButton extends GuiControl {
		
		public List<GuiTreeButton> listOfMenus = new ArrayList<GuiTreeButton>();
		public boolean opened = false;
		public boolean isRoot = false;
		public int parentIndex = 1;
		public final String CONSTANT_CAPTION;
		public String caption;
		public GuiTree tree;
		
		private boolean flag = false;
		public int originPosY;
		public int originPosX;
		
		public GuiTreeButton(String name, String caption) {
			super(name, 0, 0, GuiRenderHelper.instance.getStringWidth(caption), 8);
			this.caption = caption;
			this.CONSTANT_CAPTION = caption;
		}
		
		public GuiTreeButton addMenu(GuiTreeButton button) {
			this.listOfMenus.add(button);
			if (this.listOfMenus != null && !this.listOfMenus.isEmpty())
				if (!caption.contains("+")) {
					this.caption = "+ " + caption;
				}
			return this;
		}
		
		@Override
		protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
			
			if (opened) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, 3, 0);
				//helper.drawRect(2, 0, 3, (this.getTotalBranchSize() * 14) + 2, ColorUtils.WHITE);
				GlStateManager.popMatrix();
			}
			if (!this.isBranch() && !this.isRoot) {
				int off = this.posY - this.originPosY;
				int count = (int) Math.floor(off / 14D);
				helper.drawRect(-11, (off - (count * 14)) + 4, -2, (off - (count * 14)) + 5, ColorUtils.WHITE);
			}
			helper.drawStringWithShadow(caption, 0, 0, GuiRenderHelper.instance.getStringWidth(caption), height, ColorUtils.WHITE);
		}
		
		public int getPartID() {
			return Integer.parseInt(this.name);
		}
		
		public boolean isBranch() {
			return this.listOfMenus != null && !this.listOfMenus.isEmpty();
		}
		
		public int getBranchSize() {
			if (isBranch())
				return this.listOfMenus.size();
			return 0;
		}
		
		public int getTotalBranchSize() {
			int total = 0;
			for (int i = this.getPartID() + 1; i < this.getPartID() + this.getBranchSize() + 1; i++) {
				GuiTreeButton part2 = tree.listOfAllSubTrees.get(i);
				total++;
				if (part2.isBranch() && part2.opened) {
					total = getTotalBranchSize(part2, total);
				}
			}
			return total;
		}
		
		private int getTotalBranchSize(GuiTreeButton part, int total) {
			for (int i = part.getPartID() + 1; i < part.getPartID() + part.getBranchSize() + 1; i++) {
				GuiTreeButton part2 = tree.listOfAllSubTrees.get(i);
				if (part2.isBranch() && part2.opened) {
					total++;
					getTotalBranchSize(part2, total);
				}
			}
			return total;
		}
		
		public boolean isBranchHidden() {
			return !tree.has(this.name) && this.opened;
		}
		
		public void onClicked(int x, int y, int mouseButton) {
			if (this.listOfMenus != null && !this.listOfMenus.isEmpty()) {
				if (!opened) {
					opened = true;
					this.caption = "- " + this.CONSTANT_CAPTION;
					this.width = GuiRenderHelper.instance.getStringWidth(caption) + 6;
					this.openMenus();
					tree.moveTreePartsDown(this);
				} else {
					opened = false;
					this.caption = "+ " + this.CONSTANT_CAPTION;
					this.width = GuiRenderHelper.instance.getStringWidth(caption) + 6;
					this.closeMenus();
					tree.moveTreePartsUp(this);
				}
			}
		}
		
		public void openMenus() {
			for (int i = 0; i < listOfMenus.size(); i++) {
				GuiTreeButton button = listOfMenus.get(i);
				if (!button.isRoot) {
					button.posY = (14 * (i + 1)) + this.posY;
					button.posX = 14 + this.posX;
					if (!button.flag) {
						button.originPosX = new Integer(button.posX);
						button.originPosY = new Integer(button.posY);
						button.flag = true;
					}
					tree.addControl(button);
					if (button.isBranch() && button.opened) {
						button.openMenus();
					}
				}
			}
		}
		
		public void closeMenus() {
			List controls = tree.getControls();
			for (GuiTreeButton button : listOfMenus) {
				if (button.isBranch()) {
					button.closeMenus();
				}
				controls.remove(button);
			}
		}
		
		@Override
		public boolean mousePressed(int x, int y, int button) {
			playSound(SoundEvents.UI_BUTTON_CLICK);
			onClicked(x, y, button);
			return true;
		}
		
		@Override
		public boolean hasBackground() {
			return true;
		}
		
		@Override
		public boolean hasBorder() {
			return false;
		}
	}
}
