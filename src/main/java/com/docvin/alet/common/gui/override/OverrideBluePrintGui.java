package com.docvin.alet.common.gui.override;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiItemComboBox;
import com.creativemd.littletiles.client.gui.SubGuiRecipe;
import com.creativemd.littletiles.client.gui.controls.GuiAnimationViewer;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.docvin.alet.components.gui.controls.hierarchy.tree.GuiTree;
import com.docvin.alet.components.gui.controls.hierarchy.tree.GuiTreeDataNode;
import com.docvin.alet.components.gui.controls.hierarchy.tree.GuiTreeNode;
import net.minecraft.client.Minecraft;

public class OverrideBluePrintGui extends OverrideSubGui<SubGuiRecipe> {


    public OverrideBluePrintGui() {
        super(SubGuiRecipe.class);
    }

    protected static String getDisplayName(LittlePreviews previews) {
        String name = previews.getStructureName();
        if (name == null) {
            if (previews.hasStructure()) {
                name = previews.getStructureId();
            } else {
                name = "none";
            }
        }

        return name;
    }

    @Override
    public void onScreenResized(SubGuiRecipe gui, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
//        mc.gameSettings.guiScale = 1;
//        gui.width = width - 10;
//        gui.height = height - 10;
//        if (gui.container != null)
//            gui.container.container.gui.setGuiSize(gui.width, gui.height);
        GuiPanel panel = (GuiPanel) gui.controls.get(8);
        GuiControl save = gui.get("save");
        GuiAnimationViewer viewer = (GuiAnimationViewer) gui.getControls().get(4);
        save.posY = gui.height - 30;
        gui.get("name").posY = gui.height - 30;
        gui.get("clear").posY = gui.height - 30;

        panel.posX = 100;
        panel.height = save.posY - 34;
        panel.width = 350;
        viewer.posX = panel.posX + panel.width + 5;
        viewer.width = gui.width - (panel.width + panel.posX) - 12;
        viewer.height = panel.height;

        GuiControl play = gui.get("play");
        GuiControl pause = gui.get("pause");
        GuiControl stop = gui.get("stop");

        play.posX = panel.posX + panel.width + 55;
        pause.posX = play.posX + play.width + 2;
        stop.posX = pause.posX + pause.width + 2;

        play.posY = save.posY;
        pause.posY = play.posY;
        stop.posY = pause.posY;

        GuiTree tree = (GuiTree) gui.controls.get(11);
        tree.height = panel.height + 20;
    }

    @Override
    public void overrideGui(SubGuiRecipe gui) {
//        Minecraft mc = Minecraft.getMinecraft();
//        mc.gameSettings.guiScale = 1;
//        ScaledResolution res = new ScaledResolution(mc);
//        gui.width = res.getScaledWidth() - 10;
//        gui.height = res.getScaledHeight() - 10;


        GuiControl types = gui.controls.get(0);
        GuiControl clear = gui.controls.get(1);
        GuiControl tilesCount = gui.controls.get(2);
        GuiItemComboBox hierarchy = (GuiItemComboBox) gui.controls.get(3);
        hierarchy.name = "";
        GuiAnimationViewer viewer = (GuiAnimationViewer) gui.controls.get(4);
        GuiControl play = gui.controls.get(5);
        GuiControl pause = gui.controls.get(6);
        GuiControl stop = gui.controls.get(7);
        GuiControl panel = gui.controls.get(8);
        GuiControl save = gui.controls.get(9);
        GuiControl name = gui.controls.get(10);

        viewer.posX = 600;

        hierarchy.setEnabled(false);
        hierarchy.setVisible(false);

        GuiTree tree = new GuiTree("hierarchy", 0, 0);
        tree.width = 200;
        gui.addControl(tree);

        GuiTreeDataNode<Integer> node = new GuiTreeDataNode<>(getDisplayName(gui.previews), getDisplayName(gui.previews));
        node.setAction((subGui1, guiControl) -> {
            gui.savePreview();
            gui.loadStack(gui.hierarchy.get(0));
        });
        node.setValue(0);
        this.addPreviews(node, gui.previews);
        tree.addItem(node);

        name.posY = gui.height - 30;
        clear.posY = name.posY;
        save.posY = clear.posY;

        panel.posX = tree.posX + tree.width + 4;
        panel.height = save.posY - 34;
        panel.width = 350;

        tree.height = panel.height + 30;


        play.posX = panel.posX + panel.width + 55;
        pause.posX = play.posX + play.width + 2;
        stop.posX = pause.posX + pause.width + 2;

        play.posY = save.posY;
        pause.posY = play.posY;
        stop.posY = pause.posY;

        types.posX = tree.width + 4;
        types.width = panel.width;

    }

    protected void addPreviews(GuiTreeNode parentNode, LittlePreviews previews) {
        if (previews.hasChildren()) {
            for (LittlePreviews child : previews.getChildren()) {
                GuiTreeDataNode<SubGuiRecipe.StructureHolder> childNode = new GuiTreeDataNode<>(getDisplayName(child), getDisplayName(child));
                childNode.setAction((subGui, guiControl) -> {
                    if (subGui instanceof SubGuiRecipe) {
                        SubGuiRecipe gui = (SubGuiRecipe) subGui;
                        for (SubGuiRecipe.StructureHolder holder : gui.hierarchy) {
                            System.out.println(childNode.getValue() + " " + holder.previews);
                            if (childNode.getValue().equals(holder)) {
                                gui.savePreview();
                                gui.loadStack(holder);
                                break;
                            }
                        }
                    }
                });
                for (SubGuiRecipe.StructureHolder holder : this.subGui.hierarchy) {
                    if (child.equals(holder.previews)) {
                        childNode.setValue(holder);
                        break;
                    }
                }
                parentNode.addItem(childNode);
                addPreviews(childNode, child);
            }
        }
    }
}