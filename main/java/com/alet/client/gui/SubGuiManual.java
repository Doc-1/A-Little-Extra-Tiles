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
		
		//Check if open. if open move not same branch down that many
		public void moveTreePartsDown(GuiTreePart button) {
			boolean flag = true;
			int move = button.getBranchSize();
			int start = button.getPartID() + 1;
			GuiTreePart branch = button;
			GuiTreePart part = button;
			while (branch.hasNextBranch()) {
				if (button.isInSameBranch(branch)) {
					//System.out.println("Branch " + branch.CAPTION);
					part = branch;
					
					while (part.hasNextPart()) {
						part = part.nextPart();
						if (!branch.isInSameBranch(part) && this.has(part.name) && branch.isOpened()) {
							//System.out.println("Part " + part.CAPTION + " " + branch.getBranchSize());
							part.posY = part.posY + (14 * branch.getBranchSize());
						}
					}
				}
				branch = branch.nextBranch();
			}
			
		}
		
		public void moveTreePartsUp(GuiTreePart button) {
			boolean flag = true;
			int move = button.getBranchSize();
			int start = button.getPartID() + 1;
			GuiTreePart branch = button;
			GuiTreePart part = button;
			while (branch.hasNextBranch()) {
				if (button.isInSameBranch(branch)) {
					System.out.println("Branch " + branch.CAPTION);
					part = branch;
					
					while (part.hasNextPart()) {
						part = part.nextPart();
						//System.out.println("Part " + part.CAPTION);
						if (!branch.isInSameBranch(part) && !branch.isBranchHiddenAndClosed()) {
							System.out.println("Part " + part.CAPTION + " " + branch.getBranchSize());
							part.posY = part.posY - (14 * branch.getBranchSize());
						}
					}
				}
				branch = branch.nextBranch();
			}
			
		}
		
		/*
		 * 
			boolean flag = true;
			int move = button.getTotalOpenedBranchSize();
			int start = button.getPartID() + 1;
			for (int i = start; i < this.listOfParts.size(); i++) {
				GuiTreePart p2 = this.listOfParts.get(i);
				
				if (this.has(p2.name)) {
					p2.posY = p2.posY - (move * 14);
				}
				
			}
		
		 */
		
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
		public GuiTreePart previousPart() {
			int id = this.getPartID() - 1;
			if (id <= 0)
				return null;
			return tree.listOfParts.get(id);
		}
		
		/** @return
		 *         The next part in the list. If there is no next part returns null. */
		public GuiTreePart nextPart() {
			int id = this.getPartID() + 1;
			if (id >= tree.numberOfAllParts())
				return null;
			return tree.listOfParts.get(id);
		}
		
		public boolean hasNextPart() {
			return this.nextPart() != null;
		}
		
		public boolean hasPreviousPart() {
			return this.previousPart() != null;
		}
		
		public GuiTreePart nextBranch() {
			GuiTreePart part = this;
			while (part.hasNextPart()) {
				part = part.nextPart();
				if (part.isBranch()) {
					return part;
				}
			}
			return null;
		}
		
		public boolean hasNextBranch() {
			return this.nextBranch() != null;
		}
		
		public boolean hasPreviousBranch() {
			return this.previousBranch() != null;
		}
		
		public GuiTreePart previousBranch() {
			GuiTreePart part = this;
			while (part.hasPreviousPart()) {
				part = part.previousPart();
				if (part.isBranch()) {
					return part;
				}
			}
			return null;
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
		public int getTotalBranchSize() {
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
		
		public int getTotalOpenedBranchSize() {
			int start = this.getPartID() + 1;
			GuiTreePart part0 = tree.listOfParts.get(start);
			int total = this.getTotalBranchSize();
			for (int i = start; i < start + total; i++) {
				GuiTreePart checkPart = tree.listOfParts.get(i);
				if (checkPart.isBranch() && !checkPart.isOpened()) {
					total -= checkPart.getTotalBranchSize();
				}
			}
			
			return total;
		}
		
		public boolean isInSameBranch(GuiTreePart part) {
			GuiTreePart previousPart = part;
			GuiTreePart comparePart = this;
			if (comparePart.isBranch())
				comparePart = comparePart.nextPart();
			int partBranchID = comparePart.getBranchIDThisIsIn();
			int partBranchID2 = part.getBranchIDThisIsIn();
			if (partBranchID == partBranchID2)
				return true;
			if (part.equals(this))
				return true;
			while (previousPart != null) {
				if (previousPart.isRoot())
					return false;
				int previousPartBrandID = previousPart.getBranchIDThisIsIn();
				if (partBranchID == previousPartBrandID)
					return true;
				else if (partBranchID > previousPartBrandID)
					return false;
				previousPart = previousPart.previousPart();
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
