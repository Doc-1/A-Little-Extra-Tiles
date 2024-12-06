package com.alet.common.gui.origins;

import java.io.File;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.util.text.TextFormatting;

public class SubGuiRenameFile extends SubGui {
    
    public File file;
    public SubGuiFillingCabinet instance;
    
    public SubGuiRenameFile(File file, SubGuiFillingCabinet instance) {
        super(200, 42);
        this.file = file;
        this.instance = instance;
    }
    
    @Override
    public void createControls() {
        addControl(new GuiTextfield("filename", file.getName(), 0, 0, 194, 14));
        addControl(new GuiButton("save", "Save", 144, 22, 50, 14) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                if (file.exists()) {
                    GuiTextfield fileName = (GuiTextfield) this.getGui().get("filename");
                    File f = new File(file.getParent() + "\\" + fileName.text);
                    file.renameTo(f);
                    instance.createTreeList();
                    instance.updateTree();
                    
                    GuiLabel lable = (GuiLabel) instance.get("viewing");
                    lable.setCaption(TextFormatting.BOLD + fileName.text);
                    this.getGui().closeGui();
                }
            }
        });
        addControl(new GuiButton("cancel", "Cancel", 0, 22, 50, 14) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                this.getGui().closeGui();
            }
        });
    }
    
}
