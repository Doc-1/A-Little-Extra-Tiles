package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.alet.client.gui.controls.GuiTreePart.EnumPartType;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

public class GuiTree extends GuiParent {
    
    public List<GuiTreePart> listOfRoots;
    public List<GuiTreePart> listOfParts = new ArrayList<GuiTreePart>();
    public List<GuiTreePart> listOfPartsSearched = new ArrayList<GuiTreePart>();
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
        createRootControls();
        createSearchControls();
        allButtons();
        openTitles();
        this.height = (listOfParts.size() * 14) + 25;
    }
    
    public void addToTree(GuiTreePart part) {
        this.listOfRoots.add(part);
        createRootControls();
        createSearchControls();
        allButtons();
        openTitles();
        this.height = (listOfParts.size() * 14) + 25;
    }
    
    public void replaceTree(List<GuiTreePart> listOfRoots) {
        this.listOfRoots = listOfRoots;
        createRootControls();
        createSearchControls();
        allButtons();
        openTitles();
        this.height = (listOfParts.size() * 14) + 25;
    }
    
    public void createRootControls() {
        for (int i = 0; i < listOfRoots.size(); i++) {
            GuiTreePart root = listOfRoots.get(i);
            root.posY = (14 * i) + 20;
            root.originPosY = new Integer(root.posY);
            addControl(root);
        }
    }
    
    public void createSearchControls() {
        if (useSearchBar && !has("search"))
            addControl(new GuiTextfield("search", "", searchBarX, searchBarY, searchBarWidth, 10) {
                @Override
                public boolean onKeyPressed(char character, int key) {
                    boolean result = super.onKeyPressed(character, key);
                    if (result) {
                        GuiTextfield searchBar = (GuiTextfield) get("search");
                        wipePartControls();
                        if (!searchBar.text.equals("")) {
                            setSearchedParts(searchBar.text);
                            createSearchResultControls();
                        } else {
                            createRootControls();
                            openTitles();
                        }
                    }
                    return result;
                }
            });
    }
    
    /** Opens all Titles. */
    public void openTitles() {
        for (GuiTreePart part : listOfParts) {
            if (part.type.equals(EnumPartType.Title)) {
                part.openMenus();
                part.setOpened(true);
                updatePartsPosition();
            }
        }
    }
    
    public void setSearchedParts(String search) {
        listOfPartsSearched.clear();
        for (GuiTreePart part : listOfParts) {
            if (!part.isBranch()) {
                if (Pattern.compile(Pattern.quote(search), Pattern.CASE_INSENSITIVE).matcher(part.CAPTION).find()) {
                    listOfPartsSearched.add(new GuiTreePart(part, EnumPartType.Searched));
                }
                for (String keyword : part.listOfSearchKeywords) {
                    if (Pattern.compile(Pattern.quote(search), Pattern.CASE_INSENSITIVE).matcher(keyword).find()) {
                        GuiTreePart searchedPart = new GuiTreePart(part, EnumPartType.Searched);
                        searchedPart.caption = searchedPart.CAPTION + ": " + keyword;
                        searchedPart.width = GuiRenderHelper.instance.getStringWidth(searchedPart.caption) + searchedPart.getContentOffset() * 2;
                        listOfPartsSearched.add(searchedPart);
                    }
                }
            }
            
        }
    }
    
    public void createSearchResultControls() {
        if (this.listOfPartsSearched != null && !this.listOfPartsSearched.isEmpty())
            for (int i = 0; i < listOfPartsSearched.size(); i++) {
                GuiTreePart part = listOfPartsSearched.get(i);
                part.posY = (14 * i) + 20;
                part.posX = 0;
                part.originPosY = new Integer(part.posY);
                addControl(part);
            }
        
    }
    
    public void wipePartControls() {
        removeControls("search");
    }
    
    public void openTo(GuiTreePart check) {
        do {
            GuiTreePart heldInPart = this.listOfParts.get(check.heldInID);
            GuiTreePart part = this.listOfParts.get(Integer.parseInt(check.name));
            if (heldInPart.type.isOpenable()) {
                heldInPart.openMenus();
                heldInPart.setOpened(true);
                heldInPart.caption = "- " + heldInPart.CAPTION;
                this.updatePartsPosition();
            }
            this.highlightPart(part);
            check = check.previousBranch();
        } while (check != null);
    }
    
    public void allButtons() {
        for (int i = 0; i < listOfRoots.size(); i++) {
            GuiTreePart root = listOfRoots.get(i);
            root.isRoot = true;
            root.name = indexPos++ + "";
            root.heldInID = Integer.parseInt(root.name);
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
            part.heldInID = Integer.parseInt(root.name);
            part.name = indexPos++ + "";
            part.tree = this;
            listOfParts.add(part);
            if (part.listOfParts != null && !part.listOfParts.isEmpty()) {
                allButtons(part, j);
            }
        }
    }
    
    /** Highlights the a part. It will un-highlight any other parts that maybe selected.
     * 
     * @param partToHighlight
     *            This is the part it highlights. */
    public void highlightPart(GuiTreePart partToHighlight) {
        
        if (!partToHighlight.type.equals(EnumPartType.Searched)) {
            for (GuiTreePart part : this.listOfParts) {
                part.selected = part.equals(partToHighlight);
                if (part.selected) {
                    part.setStyle(part.SELECTED_DISPLAY);
                } else {
                    part.setStyle(this.defaultStyle);
                }
            }
        } else if (this.listOfPartsSearched != null && !this.listOfPartsSearched.isEmpty()) {
            for (GuiTreePart part : this.listOfPartsSearched) {
                part.selected = part.equals(partToHighlight);
                if (part.selected) {
                    part.setStyle(part.SELECTED_DISPLAY);
                } else {
                    part.setStyle(this.defaultStyle);
                }
            }
            
        }
    }
    
    public int numberOfAllParts() {
        return this.listOfParts.size();
    }
    
    /** Moves parts to their new position after a branch or root is opened. */
    public void updatePartsPosition() {
        boolean flag = true;
        int i = 0;
        Map<GuiTreePart, Integer> movePartBy = new HashMap<GuiTreePart, Integer>();
        for (GuiTreePart branch : this.listOfParts) {
            i++;
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