package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.alet.client.gui.controls.GuiTreePart.EnumPartType;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiTree extends GuiParent {
	
	public List<GuiTreePart> listOfRoots;
	public List<GuiTreePart> listOfParts = new ArrayList<GuiTreePart>();
	public List<GuiTreePart> listOfPartsSearched = new ArrayList<GuiTreePart>();
	public List<GuiTreePart> listOfPartsRemoved = new ArrayList<GuiTreePart>();
	public Map<GuiTreePart, GuiTreePart> mapOfRootToSubTrees = new HashMap<GuiTreePart, GuiTreePart>();
	private int indexPos = 0;
	public boolean useSearchBar;
	int searchBarX;
	int searchBarY;
	int searchBarWidth;
	
	public GuiTree(String name, int x, int y, int width, List<GuiTreePart> listOfRoots, boolean useSearchBar, int searchBarX, int searchBarY, int searchBarWidth) {
		super(name, x, y, width, 320);
		this.useSearchBar = useSearchBar;
		this.searchBarX = searchBarX;
		this.searchBarY = searchBarY;
		this.searchBarWidth = searchBarWidth;
		this.listOfRoots = listOfRoots;
		createControls();
		openTitles();
		this.height = (listOfParts.size() * 14) + 25;
	}
	
	public void createControls() {
		for (int i = 0; i < listOfRoots.size(); i++) {
			GuiTreePart root = listOfRoots.get(i);
			root.posY = (14 * i) + 20;
			root.originPosY = new Integer(root.posY);
			addControl(root);
		}
		if (useSearchBar && !has("search"))
			addControl(new GuiTextfield("search", "", searchBarX, searchBarY, searchBarWidth, 10) {
				@Override
				public boolean onKeyPressed(char character, int key) {
					boolean result = super.onKeyPressed(character, key);
					if (result) {
						displaySearchResult();
					}
					return result;
				}
			});
		allButtons();
	}
	
	public void displaySearchResult() {
		GuiTextfield searchBar = (GuiTextfield) get("search");
		
		for (GuiTreePart part : listOfPartsSearched) {
			if (controls.contains(part))
				removeControl(part);
		}
		
		listOfPartsSearched.clear();
		
		for (GuiTreePart part : listOfParts) {
			
			if (!part.isBranch()
			        && Pattern.compile(Pattern.quote(searchBar.text), Pattern.CASE_INSENSITIVE).matcher(part.CAPTION).find()
			        && !searchBar.text.equals("")) {
				listOfPartsSearched.add(new GuiTreePart(part, EnumPartType.Searched));
			}
			if (has(part.name)) {
				listOfPartsRemoved.add(part);
				removeControl(part);
			}
		}
		
		for (int i = 0; i < listOfPartsSearched.size(); i++) {
			GuiTreePart button = listOfPartsSearched.get(i);
			button.posY = (14 * i) + 20;
			button.posX = 0;
			addControl(button);
		}
		
		if (searchBar.text.equals("")) {
			for (GuiTreePart part : listOfPartsSearched) {
				if (controls.contains(part))
					removeControl(part);
			}
			for (GuiTreePart part : listOfPartsRemoved) {
				addControl(part);
			}
			listOfPartsRemoved.clear();
			listOfPartsSearched.clear();
		}
	}
	
	public void openTitles() {
		for (GuiTreePart root : listOfParts) {
			if (root.type.equals(EnumPartType.Title)) {
				root.openMenus();
				root.setState(true);
				moveTreePartsDown(root);
			}
		}
	}
	
	public void allButtons() {
		for (int i = 0; i < listOfRoots.size(); i++) {
			GuiTreePart root = listOfRoots.get(i);
			root.isRoot = true;
			root.name = indexPos++ + "";
			root.heldInRoot = Integer.parseInt(root.name);
			root.tree = this;
			listOfParts.add(root);
			if (root.listOfParts != null && !root.listOfParts.isEmpty())
				allButtons(root, i);
		}
	}
	
	public void allButtons(GuiTreePart root, int j) {
		for (int i = 0; i < root.listOfParts.size(); i++) {
			GuiTreePart part = root.listOfParts.get(i);
			if (!part.isRoot) {
				part.posY = (14 * (i + 1)) + root.posY;
				part.posX = 14 + root.posX;
				if (part.isBranch())
					part.posX = 14 + root.posX;
				if (!part.flag) {
					part.originPosX = new Integer(part.posX);
					part.originPosY = new Integer(part.posY);
					part.flag = true;
				}
			}
			part.heldInRoot = Integer.parseInt(root.name);
			part.name = indexPos++ + "";
			part.tree = this;
			listOfParts.add(part);
			if (part.listOfParts != null && !part.listOfParts.isEmpty()) {
				allButtons(part, j);
			}
		}
	}
	
	public void highlightPart(GuiTreePart partToHighlight) {
		for (GuiTreePart part : this.listOfParts) {
			part.selected = part.equals(partToHighlight);
			if (part.selected) {
				part.setStyle(part.SELECTED_DISPLAY);
			} else {
				part.setStyle(this.defaultStyle);
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
		Map<GuiTreePart, Integer> movePartBy = new HashMap<GuiTreePart, Integer>();
		for (GuiTreePart branch : this.listOfParts) {
			if (branch.isBranch()) {
				for (GuiTreePart part : branch.getListOfPartsToMove()) {
					if (movePartBy.containsKey(part)) {
						int add = movePartBy.get(part);
						movePartBy.put(part, add + branch.getBranchSize());
					} else {
						movePartBy.put(part, branch.getBranchSize());
					}
					if (!branch.isOpened() || branch.isBranchHiddenAndOpened()) {
						int add = movePartBy.get(part);
						movePartBy.put(part, add - branch.getBranchSize());
					}
				}
			}
		}
		for (Entry<GuiTreePart, Integer> entry : movePartBy.entrySet()) {
			entry.getKey().posY = entry.getKey().originPosY + (14 * entry.getValue());
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
