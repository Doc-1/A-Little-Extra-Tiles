package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.creativemd.creativecore.common.gui.container.GuiParent;

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
		this.height = (listOfParts.size() * 14) + 25;
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
		GuiTreePart branch = button;
		GuiTreePart part = button;
		do {
			if (button.isInSameBranch(branch)) {
				part = branch;
				while (part.hasNextPart()) {
					part = part.nextPart();
					if (!branch.isInSameBranch(part) && this.has(part.name) && branch.isOpened()) {
						part.posY = part.posY + (14 * branch.getBranchSize());
					}
				}
			}
			branch = branch.nextBranch();
		} while (branch != null && branch.hasNextBranch());
		
	}
	
	public void moveTreePartsUp(GuiTreePart button) {
		boolean flag = true;
		int move = button.getBranchSize();
		int start = button.getPartID() + 1;
		GuiTreePart branch = button;
		GuiTreePart part = button;
		do {
			if (button.isInSameBranch(branch)) {
				part = branch;
				while (part.hasNextPart()) {
					part = part.nextPart();
					if (!branch.isInSameBranch(part) && !branch.isBranchHiddenAndClosed()) {
						part.posY = part.posY - (14 * branch.getBranchSize());
					}
				}
			}
			branch = branch.nextBranch();
		} while (branch != null && branch.hasNextBranch());
		
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
