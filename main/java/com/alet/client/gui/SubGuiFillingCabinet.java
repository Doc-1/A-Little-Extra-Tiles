package com.alet.client.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.menu.GuiMenu;
import com.alet.client.gui.controls.menu.GuiMenuPart;
import com.alet.client.gui.controls.menu.GuiPopupMenu;
import com.alet.client.gui.controls.menu.GuiTree;
import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.container.SlotControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.item.ItemLittleRecipe;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.converation.StructureStringUtils;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.PlacementHelper;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class SubGuiFillingCabinet extends SubGui {
    
    List<GuiTreePart> listOfRoots = new ArrayList<GuiTreePart>();
    public boolean rightClick = false;
    public String selectedFile;
    
    public SubGuiFillingCabinet(LittleStructure structure) {
        super(382, 282);
        createTreeList();
    }
    
    @Override
    public void createControls() {
        SubContainer contain = this.container;
        
        /*
        file.add(new GuiMenuItem("New"));
        GuiMenuItem openRecent = new GuiMenuItem("Open-Recent");
        openRecent.add(new GuiMenuItem("Test"));
        openRecent.add(new GuiMenuItem("Test2"));
        GuiMenuItem hello = new GuiMenuItem("Hello");
        GuiMenuItem ping = new GuiMenuItem("Ping");
        ping.add(new GuiMenuItem("another one"));
        hello.add(ping);
        openRecent.add(hello);
        file.add(openRecent);
        menuBar.add(file);
        GuiMenuItem edit = new GuiMenuItem("Edit");
        edit.add(new GuiMenuItem("Transform"));
        menuBar.add(edit);
        menuBar.add(new GuiMenuItem("Help"));
        menuBar.setGuiParent(this);*/
        GuiScrollBox scrollBox = new GuiScrollBox("scrollBox", 0, 15, 210, 222);
        
        List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
        listOfMenus.add(new GuiMenuPart("New", EnumPartType.Leaf));
        listOfMenus.add(new GuiMenuPart("Save", EnumPartType.Leaf));
        listOfMenus.add(new GuiMenuPart("Recent Files", EnumPartType.Branch).addMenu(new GuiMenuPart("Build_01.txt", EnumPartType.Leaf))
                .addMenu(new GuiMenuPart("Build_02.txt", EnumPartType.Leaf)).addMenu(new GuiMenuPart("Build_03.txt", EnumPartType.Leaf))
                .addMenu(new GuiMenuPart("Build_04.txt", EnumPartType.Leaf)).addMenu(new GuiMenuPart("Build_05.txt", EnumPartType.Leaf))
                .addMenu(new GuiMenuPart("Build_06.txt", EnumPartType.Leaf)));
        listOfMenus.add(new GuiMenuPart("Edit", EnumPartType.Leaf));
        listOfMenus.add(new GuiMenuPart("Menu", EnumPartType.Branch).addMenu(new GuiMenuPart("Sub1", EnumPartType.Branch)
                .addMenu(new GuiMenuPart("Sub2", EnumPartType.Branch).addMenu(new GuiMenuPart("otherSub3", EnumPartType.Branch).addMenu(new GuiMenuPart("Hello", EnumPartType.Leaf))
                        .addMenu(new GuiMenuPart("Sub4", EnumPartType.Branch).addMenu(new GuiMenuPart("Goodbye", EnumPartType.Leaf)))))));
        GuiMenu menu = new GuiMenu("", 0, 0, 500, listOfMenus);
        menu.height = 500;
        this.addControl(new GuiPopupMenu("pop", menu, 0, 0, 0, 0));
        GuiTree tree = new GuiTree("list", 0, 0, 210, listOfRoots, true, 0, 0, 116);
        scrollBox.addControl(tree);
        tree.height = (tree.listOfParts.size() * 14) + 25;
        addControl(scrollBox);
        GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 221, 15, 155, 154);
        viewer.moveViewPort(0, 92);
        addControl(viewer);
        
        addControl(new GuiLabel("Selected:", 0, 0));
        addControl(new GuiLabel("viewing", TextFormatting.BOLD + "None", 47, 0));
        addControl(new GuiTextfield("saveName", "", 20, 246, 142, 10));
        addControl(new GuiButton("add", "Add Structure", 0, 265, 75, 10) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                SlotControl slot = (SlotControl) contain.get("input0");
                ItemStack stack = slot.slot.getStack();
                GuiTextfield text = (GuiTextfield) this.getGui().get("saveName");
                if (stack != null && (PlacementHelper.isLittleBlock(stack) || stack.getItem() instanceof ItemLittleRecipe))
                    try {
                        File d = new File("./little_structures");
                        if (!d.exists())
                            d.mkdir();
                        File f1 = new File("./little_structures/" + text.text + ".txt");
                        if (!f1.exists())
                            f1.createNewFile();
                        else {
                            
                        }
                        BufferedWriter writer = new BufferedWriter(new FileWriter("./little_structures/" + text.text + ".txt"));
                        writer.write(StructureStringUtils.exportStructure(stack));
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                updateTree();
            }
        });
        addControl(new GuiButton("Import", 243, 177) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                try {
                    NBTTagCompound nbt = JsonToNBT.getTagFromJson(getBlueprintFromFile());
                    try {
                        LittleGridContext.get(nbt);
                        sendPacketToServer(nbt);
                    } catch (RuntimeException e) {
                        openButtonDialogDialog("Invalid grid size " + nbt.getInteger("grid"), "Ok");
                        
                    }
                } catch (NBTException e) {
                    
                }
            }
        });
    }
    
    public void createTreeList() {
        File d = new File("./little_structures");
        listOfRoots.clear();
        for (File file : d.listFiles()) {
            GuiTreePart root = new GuiTreePart(file.getName(), EnumPartType.Leaf);
            if (file.isDirectory()) {
                root = new GuiTreePart(file.getName(), EnumPartType.Root);
                collectFilesInDir(file, root);
            }
            listOfRoots.add(root);
        }
    }
    
    public void collectFilesInDir(File dir, GuiTreePart part) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                GuiTreePart branch = new GuiTreePart(file.getName(), EnumPartType.Branch);
                part.addMenu(branch);
                collectFilesInDir(file, branch);
            } else if (file.isFile()) {
                part.addMenu(new GuiTreePart(file.getName(), EnumPartType.Leaf));
            }
        }
    }
    
    public void updateTree() {
        GuiScrollBox scrollBox = (GuiScrollBox) get("scrollBox");
        GuiTree tree = (GuiTree) scrollBox.get("list");
        this.createTreeList();
        tree.wipePartControls();
        tree.createRootControls();
        tree.allButtons();
        tree.openTitles();
    }
    
    public String getBlueprintFromFile() {
        String file = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./little_structures/" + selectedFile));
            file = reader.readLine();
            reader.close();
        } catch (IOException e) {
            return null;
        }
        return file;
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);
    }
    
    public File getPath(GuiTreePart part, String fileName) {
        String path = "";
        GuiTreePart p = part;
        int i = 0;
        while (p.getRootIDThisIsIn() != -1) {
            i++;
            path = "/" + p.getBranchThisIsIn().CAPTION + path;
            p = p.getBranchThisIsIn();
            if (i > 100)
                break;
        }
        
        return new File("./little_structures" + path + "/" + fileName);
    }
    
    @CustomEventSubscribe
    public void changed(GuiControlChangedEvent event) {
        if (event.source instanceof GuiTreePart) {
            GuiScrollBox scrollBox = (GuiScrollBox) get("scrollBox");
            GuiTree tree = (GuiTree) scrollBox.get("list");
            GuiLabel lable = (GuiLabel) get("viewing");
            GuiAnimationViewerAlet viewer = (GuiAnimationViewerAlet) get("renderer");
            this.selectedFile = ((GuiTreePart) event.source).CAPTION;
            lable.setCaption(TextFormatting.BOLD + ((GuiTreePart) event.source).CAPTION);
            this.rightClick = false;
            try {
                String file = getBlueprintFromFile();
                if (file != null) {
                    NBTTagCompound nbt = JsonToNBT.getTagFromJson(file);
                    ItemStack stack = StructureStringUtils.importStructure(nbt);
                    LittlePreviews pre = LittlePreview.getPreview(stack);
                    AnimationPreview anPre = new AnimationPreview(pre);
                    if (anPre != null)
                        viewer.onLoaded(anPre);
                }
            } catch (NBTException | NullPointerException e) {}
            
        }
        /*
        if (event.source instanceof GuiMenu) {
            GuiMenu rightClickMenu = (GuiMenu) event.source;
            if (rightClickMenu.buttonSelected.getCaption().equals("Rename")) {
                if (rightClickMenu.controlSelected instanceof GuiTreePart) {
                    GuiTreePart part = (GuiTreePart) rightClickMenu.controlSelected;
                    Layer.addLayer(this, new GuiRenameFile(part.CAPTION, this));
                }
            }
            if (rightClickMenu.buttonSelected.getCaption().equals("Add New Group")) {
                if (rightClickMenu.controlSelected instanceof GuiTree) {
                    File d = new File("./little_structures/" + "New Group");
                    if (!d.exists()) {
                        d.mkdir();
                    }
                    
                    Layer.addLayer(this, new GuiRenameFile("New Group", this));
                    
                }
                if (rightClickMenu.controlSelected instanceof GuiTreePart) {
                    GuiTreePart part = (GuiTreePart) rightClickMenu.controlSelected;
                    
                    File path = getPath(part, "New Group");
                    if (!path.exists()) {
                        path.mkdir();
                    }
                    
                    Layer.addLayer(this, new GuiRenameFile("New Group", this));
                }
                
            }
            if (rightClickMenu.buttonSelected.getCaption().equals("Delete")) {
                if (rightClickMenu.controlSelected instanceof GuiTreePart) {
                    GuiTreePart part = (GuiTreePart) rightClickMenu.controlSelected;
                    File path = getPath(part, part.CAPTION);
                    if (path.exists()) {
                        if (path.isDirectory())
                            FileUtils.deleteQuietly(path);
                        else
                            path.delete();
                        
                        this.updateTree();
                    }
                    
                }
            }
        }*/
    }
}
