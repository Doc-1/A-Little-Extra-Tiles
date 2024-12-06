package com.alet.client.gui.origins;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.alet.client.gui.controls.Layer;
import com.alet.client.gui.controls.menu.GuiMenu;
import com.alet.client.gui.controls.menu.GuiMenuPart;
import com.alet.client.gui.controls.menu.GuiPopupMenu;
import com.alet.client.gui.controls.menu.GuiTree;
import com.alet.client.gui.controls.menu.GuiTreePart;
import com.alet.client.gui.controls.menu.GuiTreePart.EnumPartType;
import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.container.SlotControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.item.ItemLittleRecipe;
import com.creativemd.littletiles.common.structure.LittleStructure;
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
    public GuiTreePart selectedFile;
    
    public SubGuiFillingCabinet(LittleStructure structure) {
        super(382, 282);
        createTreeList();
    }
    
    @Override
    public void onTick() {
        for (int i = 0; i < controls.size(); i++) {
            GuiControl control = controls.get(i);
            control.onTick();
        }
    }
    
    @Override
    public void createControls() {
        SubContainer contain = this.container;
        
        GuiMenuPart sortBy = new GuiMenuPart("", "Sort by", EnumPartType.Branch, true);
        GuiMenuPart name = new GuiMenuPart("", "Name", EnumPartType.Leaf);
        GuiMenuPart date = new GuiMenuPart("", "Date modified", EnumPartType.Leaf);
        GuiMenuPart structureType = new GuiMenuPart("", "Structure type", EnumPartType.Leaf);
        GuiMenuPart size = new GuiMenuPart("", "Size", EnumPartType.Leaf, true);
        GuiMenuPart ascending = new GuiMenuPart("", "Ascending", EnumPartType.Leaf);
        GuiMenuPart descending = new GuiMenuPart("", "Descending", EnumPartType.Leaf);
        GuiMenuPart showFolder = new GuiMenuPart("", "Show in folder", EnumPartType.Leaf);
        GuiMenuPart newFolder = new GuiMenuPart("", "New folder", EnumPartType.Leaf);
        GuiMenuPart delete = new GuiMenuPart("", "Delete", EnumPartType.Leaf);
        GuiMenuPart rename = new GuiMenuPart("", "Rename", EnumPartType.Leaf, true);
        GuiMenuPart details = new GuiMenuPart("", "Structure details", EnumPartType.Leaf);
        
        List<GuiTreePart> listOfMenus = new ArrayList<GuiTreePart>();
        listOfMenus.add(sortBy.addMenu(name).addMenu(date).addMenu(structureType).addMenu(size).addMenu(ascending).addMenu(
            descending));
        listOfMenus.add(showFolder);
        listOfMenus.add(newFolder);
        listOfMenus.add(delete);
        listOfMenus.add(rename);
        listOfMenus.add(details);
        GuiMenu menu = new GuiMenu("", 0, 0, 500, listOfMenus);
        menu.height = 500;
        this.addControl(new GuiPopupMenu("pop", menu, 0, 0, 0, 0));
        
        createTreeList();
        GuiTree tree = new GuiTree("list", 0, 0, 210, listOfRoots, true, 0, 0, 116);
        GuiFillingCabinetScrollBox scrollBox = new GuiFillingCabinetScrollBox("scrollBox", tree, 0, 15, 210, 222);
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
                if (stack != null && (PlacementHelper.isLittleBlock(stack) || stack.getItem() instanceof ItemLittleRecipe)) {
                    try {
                        File d = new File("./little_structures");
                        if (!d.exists())
                            d.mkdir();
                        int length = text.text.length();
                        
                        File f1 = getPath(text.text);
                        if (!f1.exists())
                            f1.createNewFile();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(f1));
                        writer.write(StructureStringUtils.exportStructure(stack));
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                updateTree();
            }
        });
        addControl(new GuiButton("Import", 243, 177) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                try {
                    String file = getBlueprintFromSelectedFile();
                    if (file != null) {
                        NBTTagCompound nbt = JsonToNBT.getTagFromJson(file);
                        try {
                            LittleGridContext.get(nbt);
                            sendPacketToServer(nbt);
                        } catch (RuntimeException e) {
                            openButtonDialogDialog("Invalid grid size " + nbt.getInteger("grid"), "Ok");
                            
                        }
                    }
                } catch (NBTException e) {
                    
                }
            }
        });
    }
    
    public GuiTreePart validGrid(File file, GuiTreePart part) {
        String data = this.getBlueprintFromFile(part);
        if (data != null) {
            try {
                try {
                    NBTTagCompound nbt = JsonToNBT.getTagFromJson(data);
                    LittleGridContext.get(nbt);
                } catch (RuntimeException e) {
                    part = new GuiTreePart(file.getName(), ColorUtils.RED, EnumPartType.Leaf);
                    part.setCustomTooltip("Error: Invalid Grid Size");
                    return part;
                }
            } catch (NBTException e) {}
        }
        return part;
    }
    
    public void createTreeList() {
        File d = new File("./little_structures");
        listOfRoots.clear();
        for (File file : d.listFiles()) {
            GuiTreePart root = new GuiTreePart(file.getName(), EnumPartType.Leaf);
            root = validGrid(file, root);
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
                GuiTreePart newPart = new GuiTreePart(file.getName(), EnumPartType.Leaf);
                newPart = validGrid(file, newPart);
                part.addMenu(newPart);
            }
        }
    }
    
    public void updateTree() {
        GuiScrollBox scrollBox = (GuiScrollBox) get("scrollBox");
        GuiTree tree = (GuiTree) scrollBox.get("list");
        tree.removeControls("search");
        createTreeList();
        tree.replaceTree(listOfRoots);
    }
    
    public String getBlueprintFromSelectedFile() {
        return getBlueprintFromFile(this.selectedFile);
    }
    
    public String getBlueprintFromFile(GuiTreePart part) {
        String file = "";
        if (part != null) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(getPath(part.caption)));
                file = reader.readLine();
                reader.close();
            } catch (IOException e) {
                return null;
            }
            return file;
        } else
            return null;
    }
    
    public File getNewFolder() {
        String newFile = "New Folder";
        int x = 1;
        while (getPath(newFile).exists()) {
            x++;
            newFile = "New Folder" + " (" + x + ")";
            if (x > 100)
                break;
        }
        return getPath(newFile);
    }
    
    public File getPath(String fileName) {
        String path = "";
        GuiTreePart p = this.selectedFile;
        if (p != null) {
            int i = 0;
            while (p.getBranchThisIsIn() != null) {
                i++;
                path = "/" + p.getBranchThisIsIn().CAPTION + path;
                p = p.getBranchThisIsIn();
                if (i > 100)
                    break;
                
            }
            String end = "";
            if (this.selectedFile.type.canHold()) {
                end = "/" + this.selectedFile.caption;
                if (!fileName.equals(""))
                    end = end + "/" + fileName;
            } else if (!fileName.equals(""))
                end = "/" + fileName;
            
            return new File("./little_structures" + path + end);
        } else {
            String end = "";
            if (!fileName.equals(""))
                end = "/" + fileName;
            return new File("./little_structures" + end);
        }
    }
    
    @CustomEventSubscribe
    public void clicked(GuiControlClickEvent event) {
        if (event.source instanceof GuiPopupMenu) {
            GuiPopupMenu popup = (GuiPopupMenu) event.source;
        }
    }
    
    @CustomEventSubscribe
    public void changed(GuiControlChangedEvent event) {
        if (event.source instanceof GuiMenuPart) {
            if (((GuiMenuPart) event.source).caption.equals("Show in folder")) {
                try {
                    File f1 = getPath("");
                    Desktop.getDesktop().open(f1);
                } catch (IOException e) {}
            } else if (((GuiMenuPart) event.source).caption.equals("Move Up")) {
                File f1 = getPath("");
            } else if (((GuiMenuPart) event.source).caption.equals("Delete")) {
                if (selectedFile != null) {
                    File f1 = null;
                    if (selectedFile.type.canHold())
                        f1 = getPath("");
                    else
                        f1 = getPath(selectedFile.caption);
                    f1.delete();
                    updateTree();
                    updateSelected(null);
                }
            } else if (((GuiMenuPart) event.source).caption.equals("Rename")) {
                if (this.selectedFile.type.canHold())
                    Layer.addLayer(this, new SubGuiRenameFile(getPath(""), this));
                else
                    Layer.addLayer(this, new SubGuiRenameFile(getPath(this.selectedFile.caption), this));
            } else if (((GuiMenuPart) event.source).caption.equals("New folder")) {
                File f1 = getNewFolder();
                f1.mkdir();
                updateTree();
            } else if (((GuiMenuPart) event.source).caption.equals("Structure details")) {
                
                String file = getBlueprintFromSelectedFile();
                if (file != null) {
                    Layer.addLayer(this, new SubGuiBlueprintDetails(file, getPath(selectedFile.caption)));
                }
            }
        }
        if (event.source instanceof GuiTreePart && !(event.source instanceof GuiMenuPart)) {
            if (!((GuiTreePart) event.source).equals(this.selectedFile))
                updateSelected((GuiTreePart) event.source);
        }
    }
    
    public void updateSelected(@Nullable GuiTreePart control) {
        GuiLabel lable = (GuiLabel) get("viewing");
        GuiAnimationViewerAlet viewer = (GuiAnimationViewerAlet) get("renderer");
        this.selectedFile = control;
        if (control != null) {
            lable.setCaption(TextFormatting.BOLD + control.CAPTION);
            try {
                String file = getBlueprintFromSelectedFile();
                if (file != null) {
                    NBTTagCompound nbt = JsonToNBT.getTagFromJson(file);
                    ItemStack stack = StructureStringUtils.importStructure(nbt);
                    LittlePreviews pre = LittlePreviews.getPreview(stack, false);
                    AnimationPreview anPre = new AnimationPreview(pre);
                    if (anPre != null)
                        viewer.onLoaded(anPre);
                    this.refreshControls();
                }
            } catch (NBTException | NullPointerException e) {}
        } else {
            lable.setCaption(TextFormatting.BOLD + "None");
        }
        
    }
    
    public class GuiFillingCabinetScrollBox extends GuiScrollBox {
        GuiTree tree;
        
        public GuiFillingCabinetScrollBox(String name, GuiTree tree, int x, int y, int width, int height) {
            super(name, x, y, width, height);
            this.tree = tree;
        }
        
        public GuiControl menuOver(int x, int y, int button, ArrayList<GuiControl> controls) {
            for (GuiControl control : controls) {
                if (control instanceof GuiTreePart && control.isMouseOver(x - tree.posX, y - 20)) {
                    return control;
                }
                if (control instanceof GuiParent) {
                    GuiControl found = menuOver(x, y, button, ((GuiParent) control).controls);
                    if (found != null)
                        return found;
                }
            }
            return null;
        }
        
        @Override
        public boolean mousePressed(int x, int y, int button) {
            GuiTreePart part = (GuiTreePart) menuOver(x, y, button, this.controls);
            if (button == 0 && part == null) {
                SubGuiFillingCabinet cabinet = (SubGuiFillingCabinet) this.getParent();
                cabinet.updateSelected(part);
            }
            return super.mousePressed(x, y, button);
        }
    }
}
