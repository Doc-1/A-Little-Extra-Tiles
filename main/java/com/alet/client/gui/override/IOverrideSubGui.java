package com.alet.client.gui.override;

import com.creativemd.creativecore.common.gui.container.SubGui;

public interface IOverrideSubGui {
	
	/** Will modify controls of gui whenever a gui is opened
	 * 
	 * @param gui */
	public abstract void modifyControls(SubGui gui);
	
}
