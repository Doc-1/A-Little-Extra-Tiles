package com.docvin.alet.components.gui.controls.buttons;

import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class GuiConnectedCheckBoxes extends GuiParent {
    static Style background = new Style("background", new ColoredDisplayStyle(90, 90, 90, 0), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    public List<GuiCheckBox> checkBoxList = new ArrayList<GuiCheckBox>();

    public GuiConnectedCheckBoxes(String name, int x, int y) {
        super(name, x, y, 0, 0);

    }

    public GuiConnectedCheckBoxes addCheckBox(String name, String caption) {
        int count = checkBoxList.size();
        GuiCheckBox box = new GuiCheckBox(name, caption, 0, count * 14, false) {
            @Override
            public boolean mousePressed(int posX, int posY, int button) {
                playSound(SoundEvents.UI_BUTTON_CLICK);
                GuiConnectedCheckBoxes.this.setSelected(this.name);
                raiseEvent(new GuiControlChangedEvent(GuiConnectedCheckBoxes.this));
                return true;
            }
        };
        checkBoxList.add(box);
        this.addControl(box);
        this.width = Math.max(this.width, box.width) + 5;
        this.height = ((1 + count) * 14) + 5;
        setSelected(name);
        return this;
    }

    @Override
    public Style getStyle() {
        return new Style("background", new ColoredDisplayStyle(0, 0, 0), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    }

    public GuiCheckBox getSelected() {
        for (GuiCheckBox box : checkBoxList)
            if (box.value)
                return box;
        return null;
    }

    public void setSelected(String name) {
        for (GuiCheckBox box : checkBoxList) {
            box.value = box.name.equals(name);
        }
    }

    @CustomEventSubscribe
    public void onControlChanged(GuiControlChangedEvent event) {
        System.out.println(event.source.name);
        setSelected(event.source.name);
    }
}
