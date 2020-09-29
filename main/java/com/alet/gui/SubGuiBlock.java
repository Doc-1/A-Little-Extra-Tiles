package com.alet.gui;

import java.io.IOException;

import org.lwjgl.util.Color;

import com.alet.gui.controls.GuiStack;
import com.alet.photo.AtlasSpriteToPath;
import com.alet.photo.PhotoReader;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.container.SlotChangeEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.LittleSubGuiUtils;
import com.creativemd.littletiles.common.item.ItemLittleChisel;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SubGuiBlock extends SubGui {
	
	public GuiTextfield textfield;
	
	public ItemStack stack = new ItemStack(Blocks.STONE);
	
	private String facing;
	
	private GuiCheckBox up = null;
	private GuiCheckBox down = null;
	private GuiCheckBox north = null;
	private GuiCheckBox east = null;
	private GuiCheckBox south = null;
	private GuiCheckBox west = null;
	private GuiStack selector = null;
	
	@Override
	public void createControls() {
		LittlePreview preview = ItemLittleChisel.getPreview(stack);
		Color color = ColorUtils.IntToRGBA(preview.getColor());
		selector = new GuiStack("preview", 20, 26, 112, getPlayer(), LittleSubGuiUtils.getCollector(getPlayer()), true);
		
		selector.setSelectedForce(preview.getBlockStack());
		controls.add(selector);
		
		GuiComboBox contextBox = new GuiComboBox("grid", 120, 0, 15, LittleGridContext.getNames());
		contextBox.select(ItemMultiTiles.currentContext.size + "");
		controls.add(contextBox);
		
		GuiTextfield xScale = new GuiTextfield("xScale", "16", 115, 60, 20, 14);
		controls.add(xScale);
		
		GuiTextfield yScale = new GuiTextfield("yScale", "16", 145, 60, 20, 14);
		controls.add(yScale);
		
		//Controls for choosing the facing of the block to print
		controls.add(up = new GuiCheckBox("up", translate("Up"), 20, -2, false) {
			
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				up.value = true;
				down.value = false;
				north.value = false;
				east.value = false;
				south.value = false;
				west.value = false;
				
				selector.setEnumFacing("up");
				raiseEvent(new GuiControlChangedEvent(this));
				return true;
			}
		});
		
		controls.add(down = new GuiCheckBox("down", translate("Down"), 45, -2, false) {
			
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				up.value = false;
				down.value = true;
				north.value = false;
				east.value = false;
				south.value = false;
				west.value = false;
				
				selector.setEnumFacing("down");
				raiseEvent(new GuiControlChangedEvent(this));
				return true;
			}
		});
		
		controls.add(north = new GuiCheckBox("north", translate("N"), 20, 10, true) {
			
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				up.value = false;
				down.value = false;
				north.value = true;
				east.value = false;
				south.value = false;
				west.value = false;
				
				selector.setEnumFacing("north");
				raiseEvent(new GuiControlChangedEvent(this));
				return true;
			}
		});
		
		controls.add(east = new GuiCheckBox("east", translate("E"), 45, 10, false) {
			
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				up.value = false;
				down.value = false;
				north.value = false;
				east.value = true;
				south.value = false;
				west.value = false;
				
				selector.setEnumFacing("east");
				raiseEvent(new GuiControlChangedEvent(this));
				return true;
			}
		});
		
		controls.add(south = new GuiCheckBox("south", translate("S"), 70, 10, false) {
			
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				up.value = false;
				down.value = false;
				north.value = false;
				east.value = false;
				south.value = true;
				west.value = false;
				
				selector.setEnumFacing("south");
				raiseEvent(new GuiControlChangedEvent(this));
				return true;
			}
		});
		
		controls.add(west = new GuiCheckBox("west", translate("W"), 95, 10, false) {
			
			@Override
			public boolean mousePressed(int posX, int posY, int button) {
				playSound(SoundEvents.UI_BUTTON_CLICK);
				up.value = false;
				down.value = false;
				north.value = false;
				east.value = false;
				south.value = false;
				west.value = true;
				
				selector.setEnumFacing("west");
				
				raiseEvent(new GuiControlChangedEvent(this));
				return true;
			}
		});
		//End
		
		controls.add(new GuiButton("Print", 10, 60) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				AtlasSpriteToPath texturePath = new AtlasSpriteToPath(selector.texture);
				PhotoReader.printBlock();
				String path = texturePath.getPath();
				GuiComboBox contextBox = (GuiComboBox) get("grid");
				int grid = Integer.parseInt(contextBox.caption);
				
				GuiTextfield yScale = (GuiTextfield) get("yScale");
				int resizeY = Integer.parseInt(yScale.text);
				GuiTextfield xScale = (GuiTextfield) get("xScale");
				int resizeX = Integer.parseInt(xScale.text);
				PhotoReader.setScale(resizeX, resizeY);
				
				try {
					NBTTagCompound nbt = PhotoReader.photoToNBT(path, false, grid);
					sendPacketToServer(nbt);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		controls.add(new GuiButton("<--", 145, 0) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				closeGui();
				GuiHandler.openGui("photo-import", new NBTTagCompound(), getPlayer());
				
			}
			
		});
	}
	
	@CustomEventSubscribe
	public void onSlotChange(SlotChangeEvent event) {
		
	}
	
}
