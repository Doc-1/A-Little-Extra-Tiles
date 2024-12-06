package com.alet.common.gui.origins;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.alet.ALETConfig;
import com.alet.common.gui.controls.GuiModifibleTextBox;
import com.alet.common.gui.controls.GuiModifibleTextBox.ModifierAttribute;
import com.creativemd.creativecore.CreativeCore;
import com.creativemd.creativecore.common.config.holder.ConfigKey;
import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.creativecore.common.config.holder.ICreativeConfigHolder;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.utils.mc.JsonUtils;
import com.google.gson.JsonObject;

import net.minecraftforge.fml.relauncher.Side;

public class SubGuiNoticeAtJoin extends SubGui {
    public SubGuiNoticeAtJoin() {
        super(330, 110);
    }
    
    @Override
    public void createControls() {
        GuiModifibleTextBox box = (new GuiModifibleTextBox("", ModifierAttribute.addText(1, ColorUtils.WHITE, false, 0,
            "Attention this version of A Little Extra Tiles is in ALPHA. Issues will arise, report them to " + ModifierAttribute
                    .end() + ModifierAttribute.addClickableText(
                        "https://github.com/Doc-1/A-Little-Extra-Tiles/issues") + ModifierAttribute.end() + ModifierAttribute
                                .addDefaultText(
                                    "Please note, it may take me a while to respond and resolve issues reported. In addition, any item with \"WIP\" in the name are unstable and may not fully work yet. Lastly, I do not recommend using A Little Extra Tiles on a server or world you care about. I've not encounter any world breaking bugs but it is possible.",
                                    1), false, false, false), 0, 0, 300) {
            @Override
            public void clickedOn(String text) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/Doc-1/A-Little-Extra-Tiles/issues"));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        GuiButton exit = new GuiButton("Okay", 0, 93, 24, 10) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                this.getParent().onClosed();
                
            }
        };
        GuiButton neverAgain = new GuiButton("Don't Show Again!", 35, 93, 90, 10) {
            
            @Override
            public void onClicked(int x, int y, int button) {
                ALETConfig.client.showAgain = false;
                ICreativeConfigHolder holder = CreativeConfigRegistry.ROOT;
                ArrayList<String> list = new ArrayList<String>();
                list.add("alet");
                list.add("client");
                list.add("showAgain");
                findConfigValue(holder, list);
                
                this.getGui().closeGui();
                
            }
        };
        addControl(box);
        addControl(exit);
        addControl(neverAgain);
    }
    
    public static void findConfigValue(ICreativeConfigHolder holder, ArrayList<String> path) {
        if (path.isEmpty())
            return;
        JsonObject ROOT = new JsonObject();
        JsonUtils.tryGet(ROOT, holder.path());
        for (ConfigKey keys : holder.fields()) {
            if (keys.name != path.get(0))
                continue;
            if (path.size() > 1) {
                if (keys.name == path.get(0) && keys.get() instanceof ICreativeConfigHolder) {
                    path.remove(0);
                    findConfigValue((ICreativeConfigHolder) keys.get(), path);
                }
            } else {
                keys.set(false);
                CreativeConfigRegistry.ROOT.load(false, true, JsonUtils.get(ROOT, CreativeConfigRegistry.ROOT.path()),
                    Side.CLIENT);
                CreativeCore.configHandler.save(Side.CLIENT);
            }
            
        }
    }
}
