package com.alet.client.gui.override;

import java.util.HashMap;
import java.util.List;

import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.littletiles.client.gui.SubGuiChisel;
import com.creativemd.littletiles.client.gui.SubGuiColorTube;
import com.creativemd.littletiles.client.gui.SubGuiGrabber;
import com.creativemd.littletiles.client.gui.SubGuiParticle;
import com.creativemd.littletiles.client.gui.configure.SubGuiGridSelector;
import com.creativemd.littletiles.client.gui.configure.SubGuiModeSelector;

public class HandlerSubGuiOverride {
	public static HashMap<Class<? extends SubGui>, IOverrideSubGui> guiToMod = new HashMap<Class<? extends SubGui>, IOverrideSubGui>();
	
	static {
		guiToMod.put(SubGuiChisel.class, new SubGuiOverrideChisel());
		guiToMod.put(SubGuiColorTube.class, new SubGuiOverrideColorTube());
		guiToMod.put(SubGuiGrabber.class, new SubGuiOverrideGrabber());
		guiToMod.put(SubGuiParticle.class, new SubGuiOverrideParticle());
		guiToMod.put(SubGuiColorTube.class, new SubGuiOverrideColorTube());
		guiToMod.put(SubGuiModeSelector.class, new SubGuiOverrideConfigure());
		guiToMod.put(SubGuiGridSelector.class, new SubGuiOverrideGridSelector());
		
	}
	
	public static HashMap<Class<? extends SubGui>, IOverrideSubGui> getGuiToMod() {
		return guiToMod;
	}
	
	public static void overrideGuiFrom(List<SubGui> guiList) {
		for (SubGui modGui : guiList) {
			for (Class<? extends SubGui> clazz : guiToMod.keySet()) {
				try {
					if (clazz.isInstance(modGui)) {
						IOverrideSubGui override = guiToMod.get(clazz);
						override.modifyControls(modGui);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}
