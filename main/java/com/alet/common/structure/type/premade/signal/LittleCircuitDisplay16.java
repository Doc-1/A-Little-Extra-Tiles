package com.alet.common.structure.type.premade.signal;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import javax.annotation.Nullable;

import com.alet.font.FontReader;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalDisplay;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.google.common.primitives.Booleans;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleCircuitDisplay16 extends LittleStructurePremade {
	public List<Boolean> imageBitsTL = new ArrayList<Boolean>();
	public List<Boolean> imageBitsTR = new ArrayList<Boolean>();
	public List<Boolean> imageBitsBL = new ArrayList<Boolean>();
	public List<Boolean> imageBitsBR = new ArrayList<Boolean>();
	public String charDisplayed = "";
	
	public LittleCircuitDisplay16(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	public void tick() {
		if (!isClient()) {
			try {
				boolean b0 = ((LittleSignalInput) this.children.get(4).getStructure()).getState()[0];
				boolean b1 = ((LittleSignalInput) this.children.get(5).getStructure()).getState()[0];
				boolean b2 = ((LittleSignalInput) this.children.get(6).getStructure()).getState()[0];
				boolean b3 = ((LittleSignalInput) this.children.get(7).getStructure()).getState()[0];
				boolean b4 = ((LittleSignalInput) this.children.get(8).getStructure()).getState()[0];
				boolean b5 = ((LittleSignalInput) this.children.get(9).getStructure()).getState()[0];
				boolean b6 = ((LittleSignalInput) this.children.get(10).getStructure()).getState()[0];
				boolean b7 = ((LittleSignalInput) this.children.get(11).getStructure()).getState()[0];
				
				boolean[] displayData = { b0, b1, b2, b3, b4, b5, b6, b7 };
				BitSet bits = new BitSet(8);
				for (int i = 0; i < 8; i++)
					bits.set(i, displayData[i]);
				LittleSignalDisplay topLeft = (LittleSignalDisplay) this.children.get(2).getStructure();
				LittleSignalDisplay topRight = (LittleSignalDisplay) this.children.get(3).getStructure();
				LittleSignalDisplay bottomLeft = (LittleSignalDisplay) this.children.get(1).getStructure();
				LittleSignalDisplay bottomRight = (LittleSignalDisplay) this.children.get(0).getStructure();
				String c = new String(bits.toByteArray());
				if (!c.equals(charDisplayed)) {
					System.out.println(bits.toByteArray());
					imageBitsTL.clear();
					imageBitsTR.clear();
					imageBitsBL.clear();
					imageBitsBR.clear();
					decodeImageToBits(c);
				}
				topLeft.getOutput(0).updateState(Booleans.toArray(imageBitsTL));
				topRight.getOutput(0).updateState(Booleans.toArray(imageBitsTR));
				bottomLeft.getOutput(0).updateState(Booleans.toArray(imageBitsBL));
				bottomRight.getOutput(0).updateState(Booleans.toArray(imageBitsBR));
				
				charDisplayed = c;
			} catch (CorruptedConnectionException | NotYetConnectedException e) {
				e.printStackTrace();
				
			}
			
		}
	}
	
	public void decodeImageToBits(String character) {
		BufferedImage image = FontReader.fontToPhoto(character, "Press Start 2P", null, 8, ColorUtils.WHITE, 0);
		for (int y = 0; y < image.getHeight(); y++)
			for (int x = 0; x < image.getWidth(); x++) {
				if ((y >= 0 && y < 4) && (x >= 0 && x < 4)) {
					imageBitsTL.add(image.getRGB(x, y) != 0 ? true : false);
				}
				if ((y >= 0 && y < 4) && (x >= 4 && x < 8)) {
					imageBitsTR.add(image.getRGB(x, y) != 0 ? true : false);
				}
				if ((y >= 4 && y < 8) && (x >= 0 && x < 4)) {
					imageBitsBL.add(image.getRGB(x, y) != 0 ? true : false);
				}
				if ((y >= 4 && y < 8) && (x >= 4 && x < 8)) {
					imageBitsBR.add(image.getRGB(x, y) != 0 ? true : false);
				}
			}
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (!worldIn.isRemote)
			LittleStructureGuiHandler.openGui("magnitude-comparator", new NBTTagCompound(), playerIn, this);
		return true;
	}
	
}
