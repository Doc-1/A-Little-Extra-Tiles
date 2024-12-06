package com.alet.components.structures.type.premade;

import javax.annotation.Nullable;

import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleTypeWriter extends LittleStructurePremade {
	
	public String font = "";
	public int color = 0xFF000000;
	public String fontSize = "48";
	public String grid = "16";
	public double rotation = 0.0;
	public boolean strikethrough;
	public boolean underline;
	public boolean bold;
	public boolean italic;
	
	public LittleTypeWriter(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		if (nbt.hasKey("font"))
			font = nbt.getString("font");
		if (nbt.hasKey("color"))
			color = nbt.getInteger("color");
		if (nbt.hasKey("fontSize"))
			fontSize = nbt.getString("fontSize");
		if (nbt.hasKey("grid"))
			grid = nbt.getString("grid");
		if (nbt.hasKey("rotation"))
			rotation = nbt.getDouble("rotation");
		if (nbt.hasKey("italic"))
			italic = nbt.getBoolean("italic");
		if (nbt.hasKey("bold"))
			bold = nbt.getBoolean("bold");
		if (nbt.hasKey("underline"))
			underline = nbt.getBoolean("underline");
		if (nbt.hasKey("strikethrough"))
			strikethrough = nbt.getBoolean("strikethrough");
		
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setString("font", font);
		nbt.setString("fontSize", fontSize);
		nbt.setInteger("color", color);
		nbt.setString("grid", grid);
		nbt.setDouble("rotation", rotation);
		nbt.setBoolean("italic", italic);
		nbt.setBoolean("bold", bold);
		nbt.setBoolean("underline", underline);
		nbt.setBoolean("strikethrough", strikethrough);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (!worldIn.isRemote)
			LittleStructureGuiHandler.openGui("type-writter", new NBTTagCompound(), playerIn, this);
		return true;
	}
	
}
