package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public abstract class GuiRightClickMenu extends GuiParent {
	
	public List<String> listOfOptions = new ArrayList<String>();
	public GuiControl controlSelected;
	public GuiControl localizedControl;
	public GuiButton buttonSelected;
	public GuiRightClickMenu instance;
	Style white = new Style("selected", new ColoredDisplayStyle(50, 50, 50), new ColoredDisplayStyle(185, 185, 185), new ColoredDisplayStyle(185, 185, 185), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
	Style mouseOver = new Style("selected", new ColoredDisplayStyle(50, 50, 50), new ColoredDisplayStyle(185, 185, 185), new ColoredDisplayStyle(145, 201, 247), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
	
	public GuiRightClickMenu(String name, List<String> listOfOptions, int x, int y, int width) {
		super(name, x, y, width, 0);
		this.instance = this;
		this.listOfOptions = listOfOptions;
		this.setStyle(white);
		createControls();
	}
	
	public void createControls() {
		int i = 0;
		int width = 0;
		for (String caption : listOfOptions) {
			GuiButton button = (new GuiButton(caption, caption, 0, (i * 14) - 1) {
				@Override
				public void onClicked(int x, int y, int button) {
					if (button == 0) {
						instance.buttonSelected = this;
						this.getGui().raiseEvent(new GuiControlChangedEvent(instance));
					}
				}
				
				@Override
				public boolean canOverlap() {
					return true;
				}
				
				@Override
				public boolean hasBorder() {
					return false;
				}
				
				@Override
				protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
					if (shouldDrawTitle())
						helper.font.drawString(caption, 0, 0, ColorUtils.BLACK);
				}
			});
			button.height = 14;
			button.posX = -1;
			button.setStyle(mouseOver);
			addControl(button);
			if (width < button.width) {
				width = button.width;
			}
			i++;
		}
		for (GuiControl button : this.controls) {
			button.width = width - 2;
		}
		this.height = (i * 14) + 4;
		this.width = width + 2;
	}
	
	public abstract void buttonActiveManager(int x, int y, int button);
	
	public GuiControl getControlSelected() {
		return controlSelected;
	}
	
	@Override
	public void mouseReleased(int x, int y, int button) {
		super.mouseReleased(x, y, button);
		if (localizedControl != null && button == 1) {
			if (x > 0 && x < localizedControl.width && y > 0 && y < localizedControl.height) {
				mouseAction(x, y, button);
				this.buttonActiveManager(x, y, button);
			} else {
				this.posX = this.getGui().width + 5;
				this.posY = this.getGui().height + 5;
			}
		} else {
			mouseAction(x, y, button);
		}
	}
	
	private void mouseAction(int x, int y, int button) {
		GuiControl controlOver = getControlMouseIsOver();
		if (button == 1 && controlOver != null) {
			this.posX = (int) x;
			this.posY = (int) y;
			this.controlSelected = controlOver;
			enableOptions();
			this.buttonActiveManager(x, y, button);
		} else {
			hideOptions();
		}
	}
	
	public void hideOptions() {
		this.posX = this.getGui().width + 5;
		this.posY = this.getGui().height + 5;
		disableOptions();
	}
	
	/** disables the buttons on the menu */
	public void disableOptions() {
		for (GuiControl control : this.controls) {
			control.enabled = false;
		}
	}
	
	/** enables the buttons on the menu */
	public void enableOptions() {
		for (GuiControl control : this.controls) {
			control.enabled = true;
		}
	}
	
	/** disables the button on the menu */
	public void disableOption(String name) {
		this.get(name).enabled = false;
	}
	
	/** enables the button on the menu */
	public void enableOptions(String name) {
		this.get(name).enabled = true;
		
	}
	
	/** @return
	 *         The control that the mouse is currently over. Ignores GuiParents. */
	public GuiControl getControlMouseIsOver() {
		for (GuiControl control : this.getGui().controls) {
			if (control.isMouseOver() && control instanceof GuiParent) {
				return getControlMouseIsOver((GuiParent) control);
			}
			if (control.isMouseOver()) {
				return control;
			}
		}
		
		return null;
	}
	
	public GuiControl getControlMouseIsOver(GuiParent parent) {
		for (GuiControl control : parent.controls) {
			if (control.isMouseOver() && control instanceof GuiParent) {
				return getControlMouseIsOver((GuiParent) control);
			}
			if (control.isMouseOver()) {
				return control;
			}
		}
		return parent;
	}
	
	@Override
	public boolean canOverlap() {
		return false;
	}
}
