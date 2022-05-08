package com.alet.client.gui.controls.menu;

import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;

public interface IGuiMenu {
    static Style background = new Style("background", new ColoredDisplayStyle(185, 185, 185, 0), new ColoredDisplayStyle(185, 185, 185), new ColoredDisplayStyle(185, 185, 185), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    static Style highlighted = new Style("selected", new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    
}
