package com.alet.client.gui.controls.menu;

import java.util.List;

import com.creativemd.creativecore.common.gui.GuiRenderHelper;

public class GuiMenu extends GuiTree {
    
    public GuiMenu(String name, int x, int y, int width, List<GuiTreePart> listOfRoots) {
        super(name, x, y, width, listOfRoots, false, 0, 0, 0);
    }
    
    @Override
    public void createRootControls() {
        int maxWidth = 0;
        for (int i = 0; i < listOfRoots.size(); i++) {
            GuiTreePart root = listOfRoots.get(i);
            int add = 0;
            if (root.type.canHold())
                add = 15;
            maxWidth = Math.max(maxWidth, GuiRenderHelper.instance.getStringWidth(root.caption) + 6 + add);
            
        }
        for (int i = 0; i < listOfRoots.size(); i++) {
            GuiTreePart root = listOfRoots.get(i);
            root.posY = (14 * i) + 20;
            root.originPosY = new Integer(root.posY);
            root.width = maxWidth;
            addControl(root);
            root.moveControlToTop();
        }
    }
    
    @Override
    public void allButtons(GuiTreePart root, int j) {
        for (int i = 0; i < root.listOfParts.size(); i++) {
            GuiTreePart part = root.listOfParts.get(i);
            if (!part.isRoot) {
                part.posY = (14 * (i)) + root.posY;
                part.posX = root.width + root.posX;
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
    
    @Override
    public void updatePartsPosition() {}
}
