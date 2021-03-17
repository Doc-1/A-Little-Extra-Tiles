package com.alet.littletiles.gui.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.util.Color;

import com.alet.littletiles.common.utils.mc.ColorUtilsAlet.ColorPart;
import com.creativemd.creativecore.CreativeCore;
import com.creativemd.creativecore.CreativeCoreConfig.ColorPalette;
import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.creativecore.common.config.sync.ConfigurationChangePacket;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.DisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButtonHold;
import com.creativemd.creativecore.common.gui.controls.gui.GuiColorPicker;
import com.creativemd.creativecore.common.gui.controls.gui.GuiColorPlate;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;

public class GuiColorPickerAlet extends GuiColorPicker {
	
	public Color color;
	
	public GuiColorPickerPalette palette;
	
	public GuiColoredSteppedSliderAlet sliderR;
	public GuiColoredSteppedSliderAlet sliderG;
	public GuiColoredSteppedSliderAlet sliderB;
	public GuiColoredSteppedSliderAlet sliderS;
	private double oldShaderValue = 0;
	private boolean hasAlpha;
	
	/** @param name
	 * @param x
	 * @param y
	 * @param color
	 * @param hasAlpha
	 * @param alphaMin */
	public GuiColorPickerAlet(String name, int x, int y, Color color, boolean hasAlpha, int alphaMin) {
		super(name, x, y, color, hasAlpha, alphaMin);
		for (GuiControl control : this.controls) {
			removeControls(control.name);
		}
		this.hasAlpha = hasAlpha;
		this.width = 129 + getContentOffset() * 2;
		this.height = 55 + getContentOffset() * 2;
		marginWidth = 0;
		this.color = color;
		setStyle(Style.emptyStyle);
		
		addControl(new GuiButtonHold("r-", "<", 0, 0, 1, 5) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				onColorChanged();
				sliderR.setValue(sliderR.value - 1);
				updateShadeSlider();
			}
			
		});
		addControl(new GuiButtonHold("r+", ">", 98, 0, 1, 5) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				onColorChanged();
				sliderR.setValue(sliderR.value + 1);
				updateShadeSlider();
			}
			
		});
		
		addControl(new GuiButtonHold("g-", "<", 0, 10, 1, 5) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				onColorChanged();
				sliderG.setValue(sliderG.value - 1);
				updateShadeSlider();
			}
			
		});
		addControl(new GuiButtonHold("g+", ">", 98, 10, 1, 5) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				onColorChanged();
				sliderG.setValue(sliderG.value + 1);
				updateShadeSlider();
			}
			
		});
		
		addControl(new GuiButtonHold("b-", "<", 0, 20, 1, 5) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				onColorChanged();
				sliderB.setValue(sliderB.value - 1);
				updateShadeSlider();
			}
			
		});
		addControl(new GuiButtonHold("b+", ">", 98, 20, 1, 5) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				onColorChanged();
				sliderB.setValue(sliderB.value + 1);
				updateShadeSlider();
			}
			
		});
		
		addControl(new GuiButtonHold("s-", "<", 0, 40, 1, 5) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				onColorChanged();
				if (!isMin(sliderR.value, sliderG.value, sliderB.value)) {
					sliderR.setValue(sliderR.value - 1);
					sliderG.setValue(sliderG.value - 1);
					sliderB.setValue(sliderB.value - 1);
					sliderS.setValue(sliderS.value - 1);
					updateShadeSlider();
				}
			}
			
		});
		addControl(new GuiButtonHold("s+", ">", 98, 40, 1, 5) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				onColorChanged();
				if (!isMax(sliderR.value, sliderG.value, sliderB.value)) {
					sliderR.setValue(sliderR.value + 1);
					sliderG.setValue(sliderG.value + 1);
					sliderB.setValue(sliderB.value + 1);
					sliderS.setValue(sliderS.value + 1);
					updateShadeSlider();
				}
			}
			
		});
		
		if (hasAlpha) {
			
			addControl(new GuiButtonHold("a-", "<", 0, 30, 1, 5) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					onColorChanged();
					GuiColoredSteppedSliderAlet slider = (GuiColoredSteppedSliderAlet) get("a");
					slider.setValue(slider.value - 1);
				}
				
			});
			addControl(new GuiButtonHold("a+", ">", 98, 30, 1, 5) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					onColorChanged();
					GuiColoredSteppedSliderAlet slider = (GuiColoredSteppedSliderAlet) get("a");
					slider.setValue(slider.value + 1);
				}
				
			});
		} else
			color.setAlpha(255);
		
		//Red Slider
		sliderR = (new GuiColoredSteppedSliderAlet("r", 8, 0, 84, 5, this, ColorPart.RED) {
			@Override
			public void mouseMove(int posX, int posY, int button) {
				super.mouseMove(posX, posY, button);
				if (grabbedSlider) {
					updateShadeSlider();
				}
			}
			
			@Override
			public boolean mouseScrolled(int x, int y, int scrolled) {
				super.mouseScrolled(x, y, scrolled);
				updateShadeSlider();
				return true;
			}
		});
		addControl(sliderR.setStyle(defaultStyle));
		
		//Green Slider
		sliderG = (new GuiColoredSteppedSliderAlet("g", 8, 10, 84, 5, this, ColorPart.GREEN) {
			@Override
			public void mouseMove(int posX, int posY, int button) {
				super.mouseMove(posX, posY, button);
				if (grabbedSlider) {
					updateShadeSlider();
				}
			}
			
			@Override
			public boolean mouseScrolled(int x, int y, int scrolled) {
				super.mouseScrolled(x, y, scrolled);
				updateShadeSlider();
				return true;
			}
		});
		addControl(sliderG.setStyle(defaultStyle));
		
		//Blue Slider
		sliderB = (new GuiColoredSteppedSliderAlet("b", 8, 20, 84, 5, this, ColorPart.BLUE) {
			@Override
			public void mouseMove(int posX, int posY, int button) {
				super.mouseMove(posX, posY, button);
				if (grabbedSlider) {
					updateShadeSlider();
				}
			}
			
			@Override
			public boolean mouseScrolled(int x, int y, int scrolled) {
				super.mouseScrolled(x, y, scrolled);
				updateShadeSlider();
				return true;
			}
		});
		addControl(sliderB.setStyle(defaultStyle));
		
		//Alpha Slider
		if (hasAlpha) {
			GuiColoredSteppedSliderAlet alpha = new GuiColoredSteppedSliderAlet("a", 8, 30, 84, 5, this, ColorPart.ALPHA);
			alpha.minValue = alphaMin;
			addControl(alpha.setStyle(defaultStyle));
		}
		
		//Shader Slider
		sliderS = (new GuiColoredSteppedSliderAlet("s", 8, 40, 84, 5, this, ColorPart.SHADE) {
			@Override
			public void mouseMove(int posX, int posY, int button) {
				oldShaderValue = this.value;
				super.mouseMove(posX, posY, button);
				
				if (grabbedSlider) {
					double difference = this.value - oldShaderValue;
					sliderR.setValue(sliderR.value + difference);
					sliderG.setValue(sliderG.value + difference);
					sliderB.setValue(sliderB.value + difference);
					oldShaderValue = value;
				}
			}
			
			@Override
			public boolean mouseScrolled(int x, int y, int scrolled) {
				oldShaderValue = this.value;
				super.mouseScrolled(x, y, scrolled);
				double difference = this.value - oldShaderValue;
				
				sliderR.setValue(sliderR.value + difference);
				sliderG.setValue(sliderG.value + difference);
				sliderB.setValue(sliderB.value + difference);
				oldShaderValue = value;
				
				return true;
			}
		});
		updateShadeSlider();
		addControl(sliderS.setStyle(defaultStyle));
		
		addControl(new GuiColorPlate("plate", 107, 2, 20, 20, color).setStyle(defaultStyle));
		addControl(new GuiButton("more", 105, 28) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				if (palette != null)
					closePalette();
				else
					openPalette();
			}
		});
	}
	
	public void setColor(Color color) {
		this.color.setColor(color);
		((GuiColoredSteppedSliderAlet) get("r")).value = color.getRed();
		((GuiColoredSteppedSliderAlet) get("g")).value = color.getGreen();
		((GuiColoredSteppedSliderAlet) get("b")).value = color.getBlue();
		if (hasAlpha)
			((GuiColoredSteppedSliderAlet) get("a")).value = color.getAlpha();
	}
	
	public void onColorChanged() {
		if (palette != null)
			palette.onChanged();
		raiseEvent(new GuiControlChangedEvent(this));
	}
	
	public void openPalette() {
		palette = new GuiColorPickerPalette(name + "palette", this, posX, posY + height, width - getContentOffset() * 2, 100);
		getGui().controls.add(palette);
		
		palette.parent = getGui();
		palette.moveControlToTop();
		palette.onOpened();
		getGui().refreshControls();
		palette.rotation = rotation;
		palette.posX = getPixelOffsetX() - getGui().getPixelOffsetX() - getContentOffset();
		palette.posY = getPixelOffsetY() - getGui().getPixelOffsetY() - getContentOffset() + height;
		
		if (palette.posY + palette.height > getParent().height && this.posY >= palette.height)
			palette.posY -= this.height + palette.height;
	}
	
	public void closePalette() {
		if (palette != null) {
			palette.savePalette();
			getGui().controls.remove(palette);
			removeListener(palette);
			palette = null;
		}
	}
	
	private static final DisplayStyle SELECTED = new ColoredDisplayStyle(ColorUtils.YELLOW);
	
	public static class GuiColorPickerPalette extends GuiParent {
		
		public final GuiColorPickerAlet picker;
		public GuiColorPickerPaletteEntry selected;
		public GuiButton add;
		
		public List<GuiColorPickerPaletteEntry> entries;
		
		public GuiColorPickerPalette(String name, GuiColorPickerAlet picker, int x, int y, int width, int height) {
			super(name, x, y, width, height);
			this.picker = picker;
			List<String> lines = new ArrayList<>();
			CreativeCore.configHandler.loadClientFields();
			for (ColorPalette palette : CreativeCore.config.palette)
				lines.add(palette.name);
			addControl(new GuiComboBox("palette", 0, 0, width - getContentOffset() * 2, lines));
			this.add = new GuiButton("+", 0, 0, 10, 10) {
				
				@Override
				public void onClicked(int x, int y, int button) {
					GuiColorPickerPaletteEntry entry = new GuiColorPickerPaletteEntry(GuiColorPickerPalette.this, "" + entries.size(), 0, 0, 10, 10, new Color(picker.color));
					entries.add(entry);
					addControl(entry);
					refreshEntries();
				}
			};
			addControl(add);
			addListener(this);
			loadPalette();
		}
		
		public void comboChanged(GuiControlChangedEvent event) {
			if (event.source.is("palette"))
				loadPalette();
		}
		
		@Override
		public void onClosed() {
			super.onClosed();
			savePalette();
		}
		
		public void loadPalette() {
			GuiComboBox box = (GuiComboBox) get("palette");
			removeControls("palette", "+");
			entries = new ArrayList<>();
			if (box.index != -1) {
				ColorPalette palette = CreativeCore.config.palette.get(box.index);
				for (int i = 0; i < palette.colors.size(); i++) {
					GuiColorPickerPaletteEntry entry = new GuiColorPickerPaletteEntry(this, "" + i, 0, 0, 10, 10, ColorUtils.IntToRGBA(palette.colors.get(i)));
					entries.add(entry);
					addControl(entry);
				}
				
			}
			refreshEntries();
		}
		
		public void savePalette() {
			GuiComboBox box = (GuiComboBox) get("palette");
			
			if (box.index != -1) {
				ColorPalette palette = CreativeCore.config.palette.get(box.index);
				palette.colors = new ArrayList<>();
				for (GuiColorPickerPaletteEntry entry : entries)
					palette.colors.add(ColorUtils.RGBAToInt(entry.getColor()));
				String[] path = new String[] { "creativecore", "palette" };
				if (CreativeCore.configHandler.isSynchronizedWithServer("creativecore.palette"))
					PacketHandler.sendPacketToServer(new ConfigurationChangePacket(path, CreativeConfigRegistry.ROOT.followPath(path).save(true, false, Side.SERVER)));
				else
					CreativeCore.configHandler.save(Side.CLIENT);
			}
		}
		
		public void refreshEntries() {
			int offsetY = 21;
			int size = 18;
			int perRow = (width - 5) / size;
			for (int i = 0; i < entries.size(); i++) {
				int row = i / perRow;
				int col = i % perRow;
				GuiColorPickerPaletteEntry entry = entries.get(i);
				entry.posX = col * size;
				entry.posY = offsetY + row * size;
			}
			int row = entries.size() / perRow;
			int col = entries.size() % perRow;
			add.posX = col * size;
			add.posY = offsetY + row * size;
		}
		
		@Override
		public void onLoseFocus() {
			if (!picker.isMouseOver() && !isMouseOver())
				picker.closePalette();
		}
		
		public void onChanged() {
			if (selected != null)
				selected.setColor(new Color(picker.color));
		}
		
		@Override
		public boolean canOverlap() {
			return true;
		}
		
	}
	
	public static class GuiColorPickerPaletteEntry extends GuiColorPlate {
		
		public final GuiColorPickerPalette palette;
		
		public GuiColorPickerPaletteEntry(GuiColorPickerPalette palette, String name, int x, int y, int width, int height, Color color) {
			super(name, x, y, width, height, color);
			this.palette = palette;
		}
		
		@Override
		public DisplayStyle getBorderDisplay(DisplayStyle display) {
			if (palette.selected == this)
				return SELECTED;
			return super.getBorderDisplay(display);
		}
		
		@Override
		public boolean mousePressed(int x, int y, int button) {
			if (button == 1) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				palette.entries.remove(this);
				palette.removeControl(this);
				if (palette.selected == this)
					palette.selected = null;
				palette.refreshEntries();
				return true;
			}
			palette.selected = this;
			palette.picker.setColor(this.getColor());
			playSound(SoundEvents.UI_BUTTON_CLICK);
			return true;
		}
		
		@Override
		public void onLoseFocus() {
			if (palette.selected == this)
				palette.selected = null;
		}
	}
	
	public double getShadeLimit(double r, double g, double b) {
		List<Double> rgb = new ArrayList<Double>();
		rgb.add(r);
		rgb.add(g);
		rgb.add(b);
		
		double min = Collections.min(rgb);
		double max = Collections.max(rgb);
		double a = 255 - max;
		
		return min + a;
	}
	
	public double getMinColor(double r, double g, double b) {
		List<Double> rgb = new ArrayList<Double>();
		rgb.add(r);
		rgb.add(g);
		rgb.add(b);
		return Collections.min(rgb);
	}
	
	public double getMaxColor(double r, double g, double b) {
		List<Double> rgb = new ArrayList<Double>();
		rgb.add(r);
		rgb.add(g);
		rgb.add(b);
		return Collections.max(rgb);
	}
	
	public boolean isMin(double r, double g, double b) {
		List<Double> rgb = new ArrayList<Double>();
		rgb.add(r);
		rgb.add(g);
		rgb.add(b);
		double min = Collections.min(rgb);
		if (min == 0) {
			return true;
		}
		return false;
	}
	
	public boolean isMax(double r, double g, double b) {
		List<Double> rgb = new ArrayList<Double>();
		rgb.add(r);
		rgb.add(g);
		rgb.add(b);
		double max = Collections.max(rgb);
		if (max == 255) {
			return true;
		}
		return false;
	}
	
	public void updateShadeSlider() {
		sliderS.maxValue = getShadeLimit(sliderR.value, sliderG.value, sliderB.value);
		sliderS.value = getMinColor(sliderR.value, sliderG.value, sliderB.value);
		oldShaderValue = sliderS.value;
	}
}
