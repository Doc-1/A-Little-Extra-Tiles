package com.alet.client.gui.controls.menu;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.event.gui.GuiControlDragEvent;
import com.alet.client.gui.event.gui.GuiControlReleaseEvent;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;

public class GuiTreePart extends GuiControl {
    
    public List<GuiTreePart> listOfParts = new ArrayList<GuiTreePart>();
    public List<String> listOfSearchKeywords = new ArrayList<String>();
    GuiTreePart branchHeldIn;
    protected boolean isOpened = false;
    boolean isRoot = false;
    int heldInID;
    public final String CAPTION;
    public String caption;
    public GuiTree tree;
    boolean flag = false;
    public int originPosY;
    public int originPosX;
    
    public int textColor = ColorUtils.WHITE;
    
    public boolean mousePressed = false;
    public boolean wasOver = false;
    public boolean counting = false;
    public int tick = 0;
    
    public int tempPosY;
    public int tempPosX;
    
    public boolean selected = false;
    public static final Style SELECTED_DISPLAY = new Style("SELECTED", new ColoredDisplayStyle(50, 50, 50), new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    public static final Style DISPLAY = new Style("DISPLAY", new ColoredDisplayStyle(240, 240, 240), new ColoredDisplayStyle(240, 240, 240), new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    
    public EnumPartType type;
    
    public GuiTreePart(GuiTreePart part) {
        this(part, part.type);
        this.posX = part.posX;
        this.posY = part.posY;
        this.originPosX = part.originPosX;
        this.originPosY = part.originPosY;
        this.tempPosX = part.tempPosX;
        this.tempPosY = part.tempPosY;
        this.tree = part.tree;
    }
    
    public GuiTreePart(GuiTreePart part, EnumPartType type) {
        this("", part.caption, type);
        this.heldInID = part.heldInID;
        this.name = part.name;
        this.posX = part.posX;
        this.posY = part.posY;
        this.originPosX = part.originPosX;
        this.originPosY = part.originPosY;
        this.tempPosX = part.tempPosX;
        this.tempPosY = part.tempPosY;
        this.tree = part.tree;
    }
    
    public GuiTreePart(String name, String caption, EnumPartType type) {
        super(name, 0, 0, GuiRenderHelper.instance.getStringWidth(caption), 8);
        this.caption = caption;
        this.CAPTION = caption;
        this.type = type;
    }
    
    public GuiTreePart(String name, int textColor, String caption, EnumPartType type) {
        super(name, 0, 0, GuiRenderHelper.instance.getStringWidth(caption), 8);
        this.caption = caption;
        this.CAPTION = caption;
        this.type = type;
        this.textColor = textColor;
    }
    
    public GuiTreePart(String caption, int textColor, EnumPartType type) {
        this("", textColor, caption, type);
    }
    
    public GuiTreePart(String caption, EnumPartType type) {
        this("", caption, type);
    }
    
    public GuiTreePart addMenu(GuiTreePart button) {
        this.listOfParts.add(button);
        button.branchHeldIn = this;
        return this;
    }
    
    public GuiTreePart getBranchThisIsIn() {
        GuiTreePart part = this.tree.listOfParts.get(this.getBranchIDThisIsIn());
        if (part.equals(this))
            return null;
        else
            return part;
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        
        if (isOpened) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 3, 0);
            helper.drawRect(2, 0, 3, (this.getTotalOpenedBranchSize() * 14) + 2, ColorUtils.WHITE);
            GlStateManager.popMatrix();
        }
        int off = this.posY - this.originPosY;
        int count = (int) Math.floor(off / 14D);
        int y = (off - (count * 14));
        if (!this.type.canHold()) {
            helper.drawRect(-12, y + 4, -4, y + 5, ColorUtils.WHITE);
        } else {
            helper.drawRect(-12, y + 4, -4, y + 5, ColorUtils.WHITE);
            helper.drawRect(-1, y, 7, y + 8, ColorUtils.WHITE);
            if (!this.type.equals(EnumPartType.Title))
                if (!this.isOpened) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0.4, 0, 0);
                    helper.drawRect(2, y + 1, 3, y + 7, ColorUtils.BLACK);
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0, 0.4, 0);
                    helper.drawRect(0, y + 3, 6, y + 4, ColorUtils.BLACK);
                    GlStateManager.popMatrix();
                } else {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0, 0.4, 0);
                    helper.drawRect(0, y + 3, 6, y + 4, ColorUtils.BLACK);
                    GlStateManager.popMatrix();
                }
            
        }
        
        GlStateManager.pushMatrix();
        if (this.type.equals(EnumPartType.Title))
            GlStateManager.translate(10, 0, 0);
        if (this.type.openable)
            GlStateManager.translate(10, 0, 0);
        helper.drawStringWithShadow(caption, 0, 0, GuiRenderHelper.instance.getStringWidth(caption), height, this.textColor);
        GlStateManager.popMatrix();
    }
    
    public void setSearchKeywords(String... keyword) {
        for (int i = 0; i < keyword.length; i++) {
            this.listOfSearchKeywords.add(keyword[i]);
        }
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
    
    public GuiTreePart nextBranch() {
        GuiTreePart part = this;
        do {
            if (part.isBranch()) {
                return part;
            }
            part = part.nextPart();
        } while (part != null);
        return null;
    }
    
    public GuiTreePart previousBranch() {
        GuiTreePart part = this;
        do {
            if (part.isBranch() && !part.equals(this)) {
                return part;
            }
            part = part.previousPart();
        } while (part != null);
        return null;
    }
    
    public boolean isInView() {
        return tree.has(this.name);
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
        return this.heldInID;
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
        if (start < tree.listOfParts.size()) {
            for (int i = start; i < tree.numberOfAllParts(); i++) {
                GuiTreePart part1 = tree.listOfParts.get(i);
                if (this.isInSameBranch(part1)) {
                    total++;
                }
            }
        }
        return total;
    }
    
    public int getTotalOpenedBranchSize() {
        int total = 0;
        for (GuiTreePart part : this.getListOfPartsUp()) {
            if (part.isInView()) {
                total++;
            }
        }
        return total;
    }
    
    public List<GuiTreePart> getListOfPartsToMove() {
        List<GuiTreePart> list = new ArrayList<GuiTreePart>();
        int start = this.getBranchSize() + Integer.parseInt(this.name) - 1;
        int end = tree.listOfParts.size();
        for (int i = start; i < end; i++) {
            GuiTreePart checkPart = tree.listOfParts.get(i);
            if (!this.isInSameBranch(checkPart))
                list.add(checkPart);
        }
        return list;
    }
    
    public List<GuiTreePart> getListOfPartsUp() {
        List<GuiTreePart> list = new ArrayList<GuiTreePart>();
        
        int start = this.getPartID() + 1;
        int end = start + this.getTotalBranchSize();
        for (int i = start; i < end; i++) {
            GuiTreePart checkPart = tree.listOfParts.get(i);
            list.add(checkPart);
        }
        return list;
    }
    
    public List<GuiTreePart> getListOfPartsDown() {
        List<GuiTreePart> list = new ArrayList<GuiTreePart>();
        
        int start = this.getPartID();
        int end = this.getRootIDThisIsIn();
        for (int i = start; i > end; i--) {
            GuiTreePart checkPart = tree.listOfParts.get(i);
            list.add(checkPart);
        }
        
        return list;
    }
    
    public List<GuiTreePart> getListOfBranchesUp() {
        List<GuiTreePart> list = new ArrayList<GuiTreePart>();
        
        int start = this.getPartID() + 1;
        int end = start + this.getTotalBranchSize();
        for (int i = start; i < end; i++) {
            GuiTreePart checkPart = tree.listOfParts.get(i);
            if (checkPart.isBranch())
                list.add(checkPart);
        }
        return list;
    }
    
    public List<GuiTreePart> getListOfBranchesDown() {
        List<GuiTreePart> list = new ArrayList<GuiTreePart>();
        GuiTreePart branch = this;
        while (branch.branchHeldIn != null) {
            branch = branch.branchHeldIn;
            list.add(branch);
        }
        return list;
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
        return !this.isInView() && this.isOpened;
    }
    
    /** @return
     *         true if the branch is closed but no longer in view. false if still in view or opened and not in view. */
    public boolean isBranchHiddenAndClosed() {
        return !this.isInView() && !this.isOpened;
    }
    
    public void onClicked(int x, int y, int mouseButton) {
        int xPos = (x - this.posX);
        int yPos = (y - this.posY);
        if ((xPos >= 2 && xPos <= 10) && (yPos >= 3 && yPos <= 10)) {
            if (this.listOfParts != null && !this.listOfParts.isEmpty()) {
                if (this.type.isOpenable()) {
                    if (!isOpened) {
                        this.isOpened = true;
                        this.openMenus();
                    } else {
                        this.isOpened = false;
                        this.closeMenus();
                    }
                    tree.updatePartsPosition();
                }
            }
        }
        tree.highlightPart(this);
        raiseEvent(new GuiControlChangedEvent(this));
    }
    
    public void setOpened(boolean opened) {
        this.isOpened = opened;
    }
    
    public boolean isOpened() {
        return this.isOpened;
    }
    
    public void openMenus() {
        for (int i = 0; i < this.listOfParts.size(); i++) {
            GuiTreePart button = this.listOfParts.get(i);
            if (!button.type.openable)
                button.width = GuiRenderHelper.instance.getStringWidth(button.caption) + 5;
            else
                button.width = GuiRenderHelper.instance.getStringWidth(button.caption) + 15;
            if (!button.isRoot) {
                tree.addControl(button);
                if (button.isBranch() && button.isOpened) {
                    button.openMenus();
                }
            }
        }
    }
    
    public void closeMenus() {
        List controls = tree.getControls();
        for (GuiTreePart button : this.listOfParts) {
            if (button.isBranch()) {
                button.closeMenus();
            }
            controls.remove(button);
        }
    }
    
    public void onDoubleClick() {
        if (this.type.equals(EnumPartType.Searched)) {
            tree.wipePartControls();
            tree.createRootControls();
            tree.openTitles();
            tree.openTo(this);
            ((GuiTextfield) tree.get("search")).text = "";
            ((GuiTextfield) tree.get("search")).cursorPosition = 0;
        }
        if (this.listOfParts != null && !this.listOfParts.isEmpty()) {
            if (this.type.isOpenable()) {
                if (!isOpened) {
                    this.isOpened = true;
                    this.openMenus();
                } else {
                    this.isOpened = false;
                    this.closeMenus();
                }
                tree.updatePartsPosition();
            }
        }
        tree.highlightPart(this);
        raiseEvent(new GuiControlChangedEvent(this));
    }
    
    @Override
    public void mouseDragged(int x, int y, int button, long time) {
        super.mouseDragged(x, y, button, time);
        if (this.selected && this.wasOver && !this.type.canHold())
            this.raiseEvent(new GuiControlDragEvent(this, x, y, button));
    }
    
    @Override
    public boolean isMouseOver(int posX, int posY) {
        boolean result = super.isMouseOver(posX, posY);
        if (result) {
            if (this.mousePressed && !this.counting) {
                this.mousePressed = false;
                this.counting = true;
            }
            if (this.counting)
                tick++;
            if (tick > 80) {
                tick = 0;
                this.counting = false;
            }
            int xPos = (posX - this.posX);
            int yPos = (posY - this.posY);
            if (this.counting && this.mousePressed && !((xPos >= 2 && xPos <= 10) && (yPos >= 2 && yPos <= 10))) {
                this.onDoubleClick();
                tick = 0;
                this.counting = false;
                this.mousePressed = false;
            }
            
        } else {
            this.counting = false;
            this.mousePressed = false;
        }
        return result;
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        if (wasOver) {
            this.wasOver = false;
            this.raiseEvent(new GuiControlReleaseEvent(this, x, y, button));
        }
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        playSound(SoundEvents.UI_BUTTON_CLICK);
        onClicked(x, y, button);
        this.mousePressed = true;
        this.wasOver = false;
        if (super.isMouseOver(x, y))
            this.wasOver = true;
        return true;
    }
    
    @Override
    public boolean hasBorder() {
        return false;
    }
    
    public enum EnumPartType {
        /** This is the first folder, or lowest point in the directory you can reach. It can hold Branches, and Leaves */
        Root(true, true),
        /** This, just like a root can hold other Branches or Leaves */
        Branch(true, true),
        /** A type of root or branch that remains open and does not close. */
        Title(false, true),
        /** This is a file. */
        Leaf(false, false),
        /** A type of Leaf that is used for when searching for a Leaf */
        Searched(false, false);
        
        private boolean openable;
        private boolean canHold;
        
        private EnumPartType(boolean openable, boolean canHold) {
            this.openable = openable;
            this.canHold = canHold;
        }
        
        public boolean isOpenable() {
            return this.openable;
        }
        
        public boolean canHold() {
            return this.canHold;
        }
    }
}