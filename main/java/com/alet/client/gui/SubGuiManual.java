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
		
		List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
		GuiTreePart a1 = new GuiTreePart("old", "Draw Shapes");
		GuiTreePart a2 = new GuiTreePart("old", "Box");
		GuiTreePart a3 = new GuiTreePart("old", "Slice");
		GuiTreePart a3_1 = new GuiTreePart("old", "Corner Slice");
		GuiTreePart a4_0 = new GuiTreePart("old", "ZZZZZZZZ");
		GuiTreePart a4_1 = new GuiTreePart("old", "OTHER1");
		GuiTreePart a4_2 = new GuiTreePart("old", "OTHER2");
		GuiTreePart a4_3 = new GuiTreePart("old", "OTHER3");
		GuiTreePart aa = new GuiTreePart("old", "11111111");
		GuiTreePart aaa = new GuiTreePart("old", "222222222");
		GuiTreePart aab = new GuiTreePart("old", "sadasdfwe");
		GuiTreePart a4 = new GuiTreePart("old", "Wall");
		GuiTreePart b1 = new GuiTreePart("old", "Door");
		a3.addMenu(a3_1).addMenu(a4_1).addMenu(a4_2.addMenu(a4_3));
		a1.addMenu(a2).addMenu(a3).addMenu(a4).addMenu(aa.addMenu(aab));
		b1.addMenu(a4_0).addMenu(aaa);
		listOfMenus.add(a1);
		listOfMenus.add(b1);
		
		scrollBox.controls.add(new GuiTree("tree", 0, 0, 194, listOfMenus));
		
	}
	
	public class GuiTree extends GuiParent {
		
		public List<GuiTreePart> listOfRoots;
		public List<GuiTreePart> listOfParts = new ArrayList<GuiTreePart>();
		public Map<GuiTreePart, GuiTreePart> mapOfRootToSubTrees = new HashMap<GuiTreePart, GuiTreePart>();
		private int indexPos = 0;
		
		public GuiTree(String name, int x, int y, int width, List<GuiTreePart> listOfRoots) {
			super(name, x, y, width, 320);
			this.listOfRoots = listOfRoots;
			createControls();
			allButtons();
		}
		
		public void createControls() {
			for (int i = 0; i < listOfRoots.size(); i++) {
				GuiTreePart button = listOfRoots.get(i);
				button.posY = 14 * i;
				button.originPosY = new Integer(button.posY);
				addControl(button);
			}
		}
		
		public void allButtons() {
			for (int i = 0; i < listOfRoots.size(); i++) {
				GuiTreePart root = listOfRoots.get(i);
				root.isRoot = true;
				root.name = indexPos++ + "";
				root.heldIn = Integer.parseInt(root.name);
				root.tree = this;
				listOfParts.add(root);
				if (root.listOfParts != null && !root.listOfParts.isEmpty())
					allButtons(root, i);
			}
		}
		
		public void allButtons(GuiTreePart root, int j) {
			for (int i = 0; i < root.listOfParts.size(); i++) {
				GuiTreePart part = root.listOfParts.get(i);
				part.heldIn = Integer.parseInt(root.name);
				part.name = indexPos++ + "";
				part.tree = this;
				listOfParts.add(part);
				if (part.listOfParts != null && !part.listOfParts.isEmpty()) {
					allButtons(part, j);
				}
			}
		}
		
		public int numberOfAllParts() {
			return this.listOfParts.size();
		}
		
		public void moveTreePartsDown(GuiTreePart button) {
			boolean flag = true;
			int move = button.getBranchSize();
			int start = button.getPartID() + 1;
			for (int i = start; i < this.listOfParts.size(); i++) {
				GuiTreePart p2 = this.listOfParts.get(i);
				
				if (this.has(p2.name) && !button.isInSameBranch(p2)) {
					p2.posY = p2.posY + (move * 14);
				}
				
			}
			
			//int x = button.getTotalBranchSize(false);
			//System.out.println(button.getBranchSize());
			
			/*
			boolean flag = true;
			int end = this.listOfAllSubTrees.size();
			int move = 0;
			for (int i = button.getPartID(); i < end; i++) {
				GuiTreeButton p = this.listOfAllSubTrees.get(i);
				
				if (this.has(p.name) && p.opened) {
					int start = p.getPartID() + p.getTotalBranchSize(false) + 1;
					move = p.getTotalBranchSize(true);
					for (int j = start; j < end; j++) {
						GuiTreeButton p2 = this.listOfAllSubTrees.get(j);
						if (this.has(p2.name)) {
							System.out.println(p2.CONSTANT_CAPTION + " " + move);
							p2.posY += (move * 14);
						}
					}
					//
					
				}
				
			}*/
			/*
			int moveRoot = 0;
			int move = 0;
			boolean flag = true;
			button.getBranchRootID();
			for (GuiTreeButton part : listOfAllSubTrees) {
				if (part.opened) {
					
				}
				if (part.isBranch() && part.opened) {
					
				}
			}
			*/
			/*
			int id = 0;
			int moveRoot = 0;
			int move = 0;
			boolean flag = true;
			
			int start = button.getPartID() + button.getTotalBranchSize(false) + 1;
			for (int i = start; i < this.listOfAllSubTrees.size(); i++) {
				GuiTreeButton p2 = this.listOfAllSubTrees.get(i);
				if (this.has(p2.name)) {
					//System.out.println(p2.CONSTANT_CAPTION + " " + i + "  " + button.getTotalBranchSize(true) * 14);
					p2.posY = p2.posY + (button.getTotalBranchSize(true) * 14);
				}
			} 
			  for (int i = 1; i < this.listOfAllSubTrees.size(); i++) {
			  GuiTreeButton p3 = this.listOfAllSubTrees.get(i);
			  if (p3.opened) {
			  	start = p3.getPartID() + p3.getTotalBranchSize(false) + 1;
			  	System.out.println(p3.CONSTANT_CAPTION + " " + start + " " + p3.getBranchSize());
			  }
			  }*/
		}
		
		public void moveTreePartsUp(GuiTreePart button) {
			boolean flag = true;
			int move = button.getBranchSize();
			int start = button.getPartID() + 1;
			for (int i = start; i < this.listOfParts.size(); i++) {
				GuiTreePart p2 = this.listOfParts.get(i);
				
				if (this.has(p2.name)) {
					
					p2.posY = p2.posY - (move * 14);
					
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
	
	public class GuiTreePart extends GuiControl {
		
		public List<GuiTreePart> listOfParts = new ArrayList<GuiTreePart>();
		private boolean isOpened = false;
		private boolean isRoot = false;
		private int heldIn;
		public final String CAPTION;
		public String caption;
		public GuiTree tree;
		
		private boolean flag = false;
		public int originPosY;
		public int originPosX;
		
		public GuiTreePart(String name, String caption) {
			super(name, 0, 0, GuiRenderHelper.instance.getStringWidth(caption), 8);
			this.caption = caption;
			this.CAPTION = caption;
		}
		
		public GuiTreePart addMenu(GuiTreePart button) {
			this.listOfParts.add(button);
			if (this.listOfParts != null && !this.listOfParts.isEmpty())
				if (!caption.contains("+")) {
					this.caption = "+ " + caption;
					this.width = GuiRenderHelper.instance.getStringWidth(caption) + 8;
				}
			return this;
		}
		
		@Override
		protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
			
			if (isOpened) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(0, 3, 0);
				//helper.drawRect(2, 0, 3, (this.getTotalBranchSize(true) * 14) + 2, ColorUtils.WHITE);
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
		 *         The previous part in the list. If there is no previous part returns null. */
		public GuiTreePart getPreviousPart() {
			int id = this.getPartID() - 1;
			if (id < 0)
				return null;
			return tree.listOfParts.get(id);
		}
		
		/** @return
		 *         The next part in the list. If there is no next part returns null. */
		public GuiTreePart getNextPart() {
			int id = this.getPartID() + 1;
			if (id > tree.numberOfAllParts())
				return null;
			return tree.listOfParts.get(id);
		}
		
		/** @return
		 *         The ID the part has. IDs are in same order as addMenus that create the tree */
		public int getPartID() {
			return Integer.parseInt(this.name);
		}
		
		/** @return
		 *         true the part is a branch. false if it is a root or leaf. */
		public boolean isBranch() {
			return this.listOfParts != null && !this.listOfParts.isEmpty();
		}
		
		public int getBranchIDThisIsIn() {
			return this.heldIn;
		}
		
		public boolean isOpened() {
			return this.isOpened;
		}
		
		public boolean isRoot() {
			return this.isRoot;
		}
		
		/** @return
		 *         If part is not root return the root it is connected to.
		 *         If part is root it will always return 0.
		 *         If failed return -2. */
		public int getRootIDThisIsIn() {
			if (!this.isRoot) {
				for (int i = this.getPartID(); i >= 0; i--) {
					if (tree.listOfParts.get(i).isRoot)
						return tree.listOfParts.get(i).getPartID();
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
				return this.listOfParts.size();
			return 0;
		}
		
		/** @return
		 *         How many parts a branch has including other branches that are a part of it. */
		public int getTotalBranchSize(boolean checkForOpened) {
			int total = 0;
			int start = this.getPartID() + 1;
			GuiTreePart part0 = tree.listOfParts.get(start);
			for (int i = start; i < tree.numberOfAllParts(); i++) {
				GuiTreePart part1 = tree.listOfParts.get(i);
				if (this.isInSameBranch(part1)) {
					total++;
				}
			}
			return total;
		}
		
		private int getTotalBranchSize(GuiTreePart part, int total, boolean checkForOpened) {
			for (int i = part.getPartID() + 1; i < part.getPartID() + part.getBranchSize() + 1; i++) {
				GuiTreePart part2 = tree.listOfParts.get(i);
				total++;
				if (checkForOpened) {
					if (part2.isBranch() && part2.isOpened)
						total = getTotalBranchSize(part2, total, true);
				} else {
					if (part2.isBranch())
						
						total = getTotalBranchSize(part2, total, false);
				}
				System.out.println(part2.CAPTION + "  " + total);
			}
			return total;
		}
		
		public boolean isInSameBranch(GuiTreePart part) {
			GuiTreePart previousPart = part;
			GuiTreePart comparePart = this;
			if (comparePart.isBranch())
				comparePart = comparePart.getNextPart();
			int partBranchID = comparePart.getBranchIDThisIsIn();
			int partBranchID2 = part.getBranchIDThisIsIn();
			if (partBranchID == partBranchID2)
				return true;
			
			while (previousPart != null) {
				if (previousPart.isRoot())
					return false;
				int previousPartBrandID = previousPart.getBranchIDThisIsIn();
				if (partBranchID == previousPartBrandID)
					return true;
				else if (partBranchID > previousPartBrandID)
					return false;
				previousPart = previousPart.getPreviousPart();
			}
			return false;
		}
		
		/** @return
		 *         true if the branch is opened but no longer in view. false if still in view or closed and not in view. */
		public boolean isBranchHiddenAndOpened() {
			return !tree.has(this.name) && this.isOpened;
		}
		
		/** @return
		 *         true if the branch is closed but no longer in view. false if still in view or opened and not in view. */
		public boolean isBranchHiddenAndClosed() {
			return !tree.has(this.name) && !this.isOpened;
		}
		
		public void onClicked(int x, int y, int mouseButton) {
			if (this.listOfParts != null && !this.listOfParts.isEmpty()) {
				if (!isOpened) {
					isOpened = true;
					this.caption = "- " + this.CAPTION;
					this.openMenus();
					tree.moveTreePartsDown(this);
				} else {
					isOpened = false;
					this.caption = "+ " + this.CAPTION;
					this.closeMenus();
					tree.moveTreePartsUp(this);
				}
			}
		}
		
		// try adding mod when it reiterates this method
		public void openMenus() {
			for (int i = 0; i < listOfParts.size(); i++) {
				GuiTreePart button = listOfParts.get(i);
				if (!button.isRoot) {
					button.posY = (14 * (i + 1)) + this.posY;
					button.posX = 14 + this.posX;
					if (!button.flag) {
						button.originPosX = new Integer(button.posX);
						button.originPosY = new Integer(button.posY);
						button.flag = true;
					}
					tree.addControl(button);
					if (button.isBranch() && button.isOpened) {
						button.openMenus();
					}
				}
			}
		}
		
		public void closeMenus() {
			List controls = tree.getControls();
			for (GuiTreePart button : listOfParts) {
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
