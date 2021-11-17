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
		GuiTreeButton a4_0 = new GuiTreeButton("old", "ZZZZZZZZ");
		GuiTreeButton a4_1 = new GuiTreeButton("old", "OTHER1");
		GuiTreeButton a4_2 = new GuiTreeButton("old", "OTHER2");
		GuiTreeButton a4_3 = new GuiTreeButton("old", "OTHER3");
		GuiTreeButton a4 = new GuiTreeButton("old", "Wall");
		GuiTreeButton b1 = new GuiTreeButton("old", "Door");
		a3.addMenu(a3_1).addMenu(a4_1).addMenu(a4_2.addMenu(a4_3));
		a1.addMenu(a2).addMenu(a3).addMenu(a4);
		b1.addMenu(a4_0);
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
				GuiTreeButton root = listOfMenus.get(i);
				root.isRoot = true;
				root.parentIndex = -1;
				root.name = indexPos++ + "";
				root.tree = this;
				listOfAllSubTrees.add(root);
				if (root.listOfMenus != null && !root.listOfMenus.isEmpty())
					allButtons(root, i);
			}
		}
		
		public void allButtons(GuiTreeButton root, int j) {
			for (int i = 0; i < root.listOfMenus.size(); i++) {
				GuiTreeButton part = root.listOfMenus.get(i);
				part.parentIndex = Integer.parseInt(root.name);
				part.name = indexPos++ + "";
				part.tree = this;
				listOfAllSubTrees.add(part);
				if (part.listOfMenus != null && !part.listOfMenus.isEmpty()) {
					allButtons(part, j);
				}
			}
		}
		
		public void onMenuChanged() {
		}
		
		//Make a find root method. for buttons
		public void moveTreePartsDown(GuiTreeButton button) {
			int id = 0;
			int moveRoot = 0;
			int move = 0;
			boolean flag = true;
			button.getBranchRootID();
			for (GuiTreeButton part : listOfAllSubTrees) {
				if (part.opened) {
					moveRoot = part.getTotalBranchSize(true);
					move = part.getTotalBranchSize(false);
					id = part.getPartID();
				}
				if (part.isBranch() && part.opened) {
					GuiTreeButton p = listOfAllSubTrees.get(id);
					int start = part.getPartID() + move + 1;
					
					for (int i = start; i < this.listOfAllSubTrees.size(); i++) {
						GuiTreeButton p2 = listOfAllSubTrees.get(i);
						if (p2.parentIndex == part.parentIndex) {
							p2.posY = p2.originPosY + (moveRoot * 14);
							System.out.println(p2.CONSTANT_CAPTION + " " + start + " " + moveRoot + " "
							        + p2.parentIndex);
						}
					}
				}
			}
		}
		
		public void moveTreePartsUp(GuiTreeButton button) {
			int id = 0;
			int moveRoot = 0;
			int move = 0;
			boolean flag = true;
			
			int start = button.getPartID() + move + 1;
			for (int i = start; i < this.listOfAllSubTrees.size(); i++) {
				GuiTreeButton p2 = this.listOfAllSubTrees.get(i);
				
				if (this.has(p2.name)) {
					System.out.println(p2.CONSTANT_CAPTION + "  " + button.getTotalBranchSize(true));
					
					p2.posY = p2.posY - (button.getTotalBranchSize(true) * 14);
					
				}
				
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
				helper.drawRect(2, 0, 3, (this.getTotalBranchSize(true) * 14) + 2, ColorUtils.WHITE);
				GlStateManager.popMatrix();
			}
			if (!this.isBranch() && !this.isRoot) {
				int off = this.posY - this.originPosY;
				int count = (int) Math.floor(off / 14D);
				helper.drawRect(-11, (off - (count * 14)) + 4, -2, (off - (count * 14)) + 5, ColorUtils.WHITE);
			}
			helper.drawStringWithShadow(caption, 0, 0, GuiRenderHelper.instance.getStringWidth(caption), height, ColorUtils.WHITE);
		}
		
		/** @return
		 *         The ID the part has. IDs are in same order as addMenus that create the tree */
		public int getPartID() {
			return Integer.parseInt(this.name);
		}
		
		/** @return
		 *         true the part is a branch. false if it is a root. */
		public boolean isBranch() {
			return this.listOfMenus != null && !this.listOfMenus.isEmpty();
		}
		
		/** @return
		 *         If part is not root return the root it is connected to.
		 *         If part is root it will always return 0.
		 *         If failed return -2. */
		public int getBranchRootID() {
			if (!this.isRoot) {
				for (int i = this.getPartID(); i >= 0; i--) {
					if (tree.listOfAllSubTrees.get(i).isRoot)
						return tree.listOfAllSubTrees.get(i).getPartID();
				}
			} else {
				return -1;
			}
			return -2;
		}
		
		/** @return
		 *         How many parts a branch has. */
		public int getBranchSize() {
			if (isBranch())
				return this.listOfMenus.size();
			return 0;
		}
		
		/** @return
		 *         How many parts a branch has including other branches that are a part of it. */
		public int getTotalBranchSize(boolean checkForOpened) {
			int total = 0;
			for (int i = this.getPartID() + 1; i < this.getPartID() + this.getBranchSize() + 1; i++) {
				GuiTreeButton part2 = tree.listOfAllSubTrees.get(i);
				total++;
				if (checkForOpened) {
					if (part2.isBranch() && part2.opened)
						total = getTotalBranchSize(part2, total, true);
				} else {
					if (part2.isBranch())
						total = getTotalBranchSize(part2, total, false);
				}
			}
			return total;
		}
		
		private int getTotalBranchSize(GuiTreeButton part, int total, boolean checkForOpened) {
			for (int i = part.getPartID() + 1; i < part.getPartID() + part.getBranchSize() + 1; i++) {
				GuiTreeButton part2 = tree.listOfAllSubTrees.get(i);
				
				total++;
				if (checkForOpened) {
					if (part2.isBranch() && part2.opened)
						total = getTotalBranchSize(part2, total, true);
				} else {
					if (part2.isBranch())
						total = getTotalBranchSize(part2, total, false);
				}
			}
			return total;
		}
		
		/** @return
		 *         true if the branch is opened but no longer in view. false if still in view or closed and not in view. */
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
