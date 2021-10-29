package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.controls.gui.timeline.GuiTimeline;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.IAnimationHandler;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.KeyControl;
import com.creativemd.creativecore.common.gui.controls.gui.timeline.TimelineChannel;

public class GuiTimelineExtra extends GuiTimeline {
	
	private List<KeyControl> selectedArray = new ArrayList<KeyControl>();
	
	public GuiTimelineExtra(String name, int x, int y, int width, int height, int duration, List<TimelineChannel> channels, IAnimationHandler handler) {
		super(name, x, y, width, height, duration, channels, handler);
	}
	
}
