package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;

public class GuiTreePart extends GuiControl {
	
	public List<GuiTreePart> listOfParts = new ArrayList<GuiTreePart>();
	private boolean isOpened = false;
	boolean isRoot = false;
	int heldIn;
	public final String CAPTION;
	public String caption;
	public GuiTree tree;
	
	private boolean flag = false;
	public int originPosY;
	public int originPosX;
	
	public int tempPosY;
	public int tempPosX;
	
	public EnumPartType type;
	
	public GuiTreePart(GuiTreePart part) {
		this(part.caption, part.type);
		this.posX = part.posX;
		this.posY = part.posY;
		this.originPosX = part.originPosX;
		this.originPosY = part.originPosY;
		this.tempPosX = part.tempPosX;
		this.tempPosY = part.tempPosY;
		this.tree = part.tree;
	}
	
	public GuiTreePart(String caption, EnumPartType type) {
		super("", 0, 0, GuiRenderHelper.instance.getStringWidth(caption), 8);
		this.caption = caption;
		this.CAPTION = caption;
		this.type = type;
	}
	
	public GuiTreePart addMenu(GuiTreePart button) {
		this.listOfParts.add(button);
		if (this.listOfParts != null && !this.listOfParts.isEmpty())
			System.out.println(this.CAPTION + " " + this.type + "  " + button.CAPTION + " " + button.type);
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
			helper.drawRect(2, 0, 3, (this.getTotalOpenedBranchSize() * 14) + 2, ColorUtils.WHITE);
			GlStateManager.popMatrix();
		}
		if (!this.isRoot) {
			int off = this.posY - this.originPosY;
			int count = (int) Math.floor(off / 14D);
			if (!this.isBranch())
				helper.drawRect(-11, (off - (count * 14)) + 4, -2, (off - (count * 14)) + 5, ColorUtils.WHITE);
			else
				helper.drawRect(-15, (off - (count * 14)) + 4, -2, (off - (count * 14)) + 5, ColorUtils.WHITE);
		}
		helper.drawStringWithShadow(caption, 0, 0, GuiRenderHelper.instance.getStringWidth(caption), height, ColorUtils.WHITE);
	}
	
	/** @return
	 *         The previous part in the list. If there is no previous part returns null. */
	public GuiTreePart previousPart() {
		int id = this.getPartID() - 1;
		if (id <= 0)
			return null;
		return (GuiTreePart) tree.listOfParts.get(id);
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
		int start = this.getPartID();
		int total = this.getTotalBranchSize();
		int end = start + total;
		for (int i = start; i < end; i++) {
			GuiTreePart checkPart = tree.listOfParts.get(i);
			if (checkPart.isBranch() && !checkPart.isOpened()) {
				int f = checkPart.getTotalBranchSize();
				total -= f;
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
		raiseEvent(new GuiControlChangedEvent(this));
	}
	
	// try adding mod when it reiterates this method
	public void openMenus() {
		for (int i = 0; i < listOfParts.size(); i++) {
			GuiTreePart button = listOfParts.get(i);
			if (!button.isRoot) {
				button.posY = (14 * (i + 1)) + this.posY;
				button.posX = 14 + this.posX;
				if (button.isBranch())
					button.posX = 18 + this.posX;
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
	
	public enum EnumPartType {
		Title, Root, Branch, Leaf;
	}
}