package com.alet.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.packet.PacketUpdateStructureFromClient;
import com.alet.common.structure.type.premade.signal.LittleMagnitudeComparator4;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.structure.LittleStructure;

import net.minecraft.nbt.NBTTagCompound;

public class SubGuiMagnitudeComparator extends SubGui {
	
	public GuiTextfield textfield;
	
	public LittleStructure structure;
	public NBTTagCompound nbt = new NBTTagCompound();
	
	public SubGuiMagnitudeComparator(LittleStructure structure) {
		super(220, 42);
		this.structure = structure;
	}
	
	@Override
	public void onOpened() {
		super.onOpened();
		LittleMagnitudeComparator4 magComparator = (LittleMagnitudeComparator4) structure;
		magComparator.writeToNBT(nbt);
		System.out.println(nbt);
		if (nbt.hasKey("logic")) {
			GuiComboBox logicBox = (GuiComboBox) get("logic");
			logicBox.select(nbt.getString("logic"));
		}
		if (nbt.hasKey("output")) {
			
			GuiComboBox bit1Box = (GuiComboBox) get("bit1");
			GuiComboBox bit2Box = (GuiComboBox) get("bit2");
			GuiComboBox bit3Box = (GuiComboBox) get("bit3");
			GuiComboBox bit4Box = (GuiComboBox) get("bit4");
			String output = nbt.getString("output");
			
			bit1Box.select(output.toCharArray()[0] + "");
			bit2Box.select(output.toCharArray()[1] + "");
			bit3Box.select(output.toCharArray()[2] + "");
			bit4Box.select(output.toCharArray()[3] + "");
		}
	}
	
	@Override
	public void onClosed() {
		GuiComboBox logicBox = (GuiComboBox) get("logic");
		
		LittleMagnitudeComparator4 magComparator = (LittleMagnitudeComparator4) structure;
		nbt.setString("logic", logicBox.getCaption());
		
		GuiComboBox bit1Box = (GuiComboBox) get("bit1");
		GuiComboBox bit2Box = (GuiComboBox) get("bit2");
		GuiComboBox bit3Box = (GuiComboBox) get("bit3");
		GuiComboBox bit4Box = (GuiComboBox) get("bit4");
		
		String output = bit1Box.getCaption() + bit2Box.getCaption() + bit3Box.getCaption() + bit4Box.getCaption();
		nbt.setString("output", output);
		PacketHandler.sendPacketToServer(new PacketUpdateStructureFromClient(magComparator.getStructureLocation(), nbt));
		
		super.onClosed();
	}
	
	@Override
	public void createControls() {
		
		List<String> bits = new ArrayList<String>();
		bits.add("0");
		bits.add("1");
		
		GuiComboBox bit1 = new GuiComboBox("bit1", 50 + 50, 23, 20, bits);
		controls.add(bit1);
		
		GuiComboBox bit2 = new GuiComboBox("bit2", 80 + 50, 23, 20, bits);
		controls.add(bit2);
		
		GuiComboBox bit3 = new GuiComboBox("bit3", 110 + 50, 23, 20, bits);
		controls.add(bit3);
		
		GuiComboBox bit4 = new GuiComboBox("bit4", 140 + 50, 23, 20, bits);
		controls.add(bit4);
		
		GuiLabel label1 = new GuiLabel("IF:", 0, 3);
		GuiLabel blue = new GuiLabel("Blue", 17, 3, ColorUtils.BLUE);
		GuiLabel red = new GuiLabel("Red", 72, 3, ColorUtils.RED);
		GuiLabel label2 = new GuiLabel("THEN:", 15, 25);
		GuiLabel label3 = new GuiLabel("OUTPUT = ", 45, 25, ColorUtils.BLACK);
		controls.add(label1);
		controls.add(blue);
		controls.add(red);
		controls.add(label2);
		controls.add(label3);
		
		List<String> logic = new ArrayList<String>();
		logic.add(">");
		logic.add("<");
		logic.add(">=");
		logic.add("<=");
		logic.add("==");
		GuiComboBox logicBox = new GuiComboBox("logic", 45, 0, 20, logic);
		controls.add(logicBox);
		
	}
}
