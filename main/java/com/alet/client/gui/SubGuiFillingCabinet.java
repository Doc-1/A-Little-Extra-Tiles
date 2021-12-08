package com.alet.client.gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.GuiTree;
import com.alet.client.gui.controls.GuiTreePart;
import com.alet.client.gui.controls.GuiTreePart.EnumPartType;
import com.alet.client.gui.controls.Layer;
import com.alet.littletiles.gui.controls.GuiAnimationViewerAlet;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.container.SlotControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.littletiles.common.entity.AnimationPreview;
import com.creativemd.littletiles.common.item.ItemLittleRecipe;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.tile.preview.LittlePreview;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.creativemd.littletiles.common.util.converation.StructureStringUtils;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.PlacementHelper;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class SubGuiFillingCabinet extends SubGui {
	
	List<GuiTreePart> listOfRoots = new ArrayList<GuiTreePart>();
	public boolean rightClick = false;
	public String selectedFile;
	
	public SubGuiFillingCabinet(LittleStructure structure) {
		super(332, 282);
	}
	
	@Override
	public void createControls() {
		SubContainer contain = this.container;
		List<String> listOfOptions = new ArrayList<String>();
		listOfOptions.add("Rename");
		listOfOptions.add("Delete");
		GuiRightClickMenu rightClick = new GuiRightClickMenu("rightClick", listOfOptions, this.width, this.height, 80);
		rightClick.listenFor.add(GuiTreePart.class);
		addControl(rightClick);
		GuiScrollBox scrollBox = new GuiScrollBox("scrollBox", 0, 15, 160, 222);
		rightClick.localizedControl = scrollBox;
		scrollBox.addControl(new GuiTree("list", 0, 0, 150, listOfRoots, true, 0, 0, 116));
		addControl(scrollBox);
		updateTree();
		GuiAnimationViewerAlet viewer = new GuiAnimationViewerAlet("renderer", 171, 15, 155, 154);
		viewer.moveViewPort(0, 92);
		addControl(viewer);
		
		addControl(new GuiLabel("Selected:", 0, 0));
		addControl(new GuiLabel("viewing", TextFormatting.BOLD + "None", 47, 0));
		addControl(new GuiTextfield("saveName", "", 20, 246, 142, 10));
		addControl(new GuiButton("add", "Add Structure", 0, 265, 75, 10) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				SlotControl slot = (SlotControl) contain.get("input0");
				ItemStack stack = slot.slot.getStack();
				GuiTextfield text = (GuiTextfield) this.getGui().get("saveName");
				if (stack != null
				        && (PlacementHelper.isLittleBlock(stack) || stack.getItem() instanceof ItemLittleRecipe))
					try {
						File d = new File("./little_structures");
						if (!d.exists())
							d.mkdir();
						File f1 = new File("./little_structures/" + text.text + ".txt");
						if (!f1.exists())
							f1.createNewFile();
						else {
							
						}
						BufferedWriter writer = new BufferedWriter(new FileWriter("./little_structures/" + text.text
						        + ".txt"));
						writer.write(StructureStringUtils.exportStructure(stack));
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				updateTree();
			}
		});
		addControl(new GuiButton("Import", 195, 177) {
			
			@Override
			public void onClicked(int x, int y, int button) {
				try {
					NBTTagCompound nbt = JsonToNBT.getTagFromJson(getBlueprintFromFile());
					System.out.println(nbt);
					try {
						LittleGridContext.get(nbt);
						sendPacketToServer(nbt);
					} catch (RuntimeException e) {
						openButtonDialogDialog("Invalid grid size " + nbt.getInteger("grid"), "Ok");
						
					}
				} catch (NBTException e) {
					
				}
			}
		});
		GuiTree tree = (GuiTree) scrollBox.get("list");
		tree.createSearchControls();
	}
	
	public void updateTree() {
		GuiScrollBox scrollBox = (GuiScrollBox) get("scrollBox");
		GuiTree tree = (GuiTree) scrollBox.get("list");
		File d = new File("./little_structures");
		if (!tree.listOfParts.isEmpty())
			for (GuiTreePart part : tree.listOfParts) {
				tree.removeControl(part);
			}
		listOfRoots.clear();
		for (File file : d.listFiles()) {
			listOfRoots.add(new GuiTreePart(file.getName(), EnumPartType.Root));
		}
		tree.allButtons();
		tree.height = (tree.listOfParts.size() * 14) + 25;
	}
	
	public String getBlueprintFromFile() {
		String file = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./little_structures/" + selectedFile));
			file = reader.readLine();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	@Override
	public void mouseReleased(int x, int y, int button) {
		super.mouseReleased(x, y, button);
	}
	
	@CustomEventSubscribe
	public void changed(GuiControlChangedEvent event) {
		if (event.source instanceof GuiTreePart) {
			GuiScrollBox scrollBox = (GuiScrollBox) get("scrollBox");
			GuiTree tree = (GuiTree) scrollBox.get("list");
			GuiLabel lable = (GuiLabel) get("viewing");
			GuiAnimationViewerAlet viewer = (GuiAnimationViewerAlet) get("renderer");
			this.selectedFile = ((GuiTreePart) event.source).CAPTION;
			lable.setCaption(TextFormatting.BOLD + ((GuiTreePart) event.source).CAPTION);
			this.rightClick = false;
			try {
				NBTTagCompound nbt = JsonToNBT.getTagFromJson(getBlueprintFromFile());
				ItemStack stack = StructureStringUtils.importStructure(nbt);
				LittlePreviews pre = LittlePreview.getPreview(stack);
				viewer.onLoaded(new AnimationPreview(pre));
			} catch (NBTException e) {
				e.printStackTrace();
			}
			
		}
		if (event.source instanceof GuiRightClickMenu) {
			GuiRightClickMenu rightClickMenu = (GuiRightClickMenu) event.source;
			if (rightClickMenu.buttonSelected.getCaption().equals("Rename")) {
				if (rightClickMenu.controlSelected instanceof GuiTreePart) {
					GuiTreePart part = (GuiTreePart) rightClickMenu.controlSelected;
					rightClickMenu.hideOptions();
					Layer.addLayer(this, new GuiRenameFile(part.CAPTION, this));
				}
			}
			if (rightClickMenu.buttonSelected.getCaption().equals("Delete")) {
				if (rightClickMenu.controlSelected instanceof GuiTreePart) {
					GuiTreePart part = (GuiTreePart) rightClickMenu.controlSelected;
					File d = new File("./little_structures/" + part.CAPTION);
					if (d.exists()) {
						d.delete();
						updateTree();
					}
				}
			}
		}
	}
}
