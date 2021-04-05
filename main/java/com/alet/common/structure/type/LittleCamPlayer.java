package com.alet.common.structure.type;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.packet.PacketGetServerCams;
import com.creativemd.cmdcam.common.packet.StartPathPacket;
import com.creativemd.cmdcam.common.utils.CamPath;
import com.creativemd.cmdcam.server.CMDCamServer;
import com.creativemd.cmdcam.server.CamCommandServer;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LittleCamPlayer extends LittleStructure {
	
	public static String[] camIDs;
	public int camToPlay;
	
	public LittleCamPlayer(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void performInternalOutputChange(InternalSignalOutput output) {
		if (output.component.is("play"))
			playCam(output.getStructureWorld());
	}
	
	public void playCam(World world) {
		if (world.isRemote)
			return;
		CamPath path = CMDCamServer.getPath(world, "new");
		if (path != null)
			if (!path.isRunning()) {
				path.mode = "outside";
				long duration = CamCommandServer.StringToDuration("10");
				path.duration = duration;
				path.currentLoop = 0;
			} else
				for (EntityPlayer player : world.playerEntities)
					player.sendMessage(new TextComponentString("Path '" + "new" + "' could not be found!"));
				
		PacketHandler.sendPacketToAllPlayers(new StartPathPacket(path));
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		playCam(worldIn);
		return true;
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}
	
	public static class LittleLockParserALET extends LittleStructureGuiParser {
		
		public List<Integer> possibleChildren;
		public List<Integer> selectedChildren;
		
		public LittleLockParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		public String getDisplayName(LittlePreviews previews, int childId) {
			String name = previews.getStructureName();
			if (name == null)
				if (previews.hasStructure())
					name = previews.getStructureId();
				else
					name = "none";
			return name + " " + childId;
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			
			PacketHandler.sendPacketToServer(new PacketGetServerCams());
			GuiScrollBox box = new GuiScrollBox("content", 0, 0, 100, 115);
			parent.controls.add(box);
			LittleLockALET lock = structure instanceof LittleLockALET ? (LittleLockALET) structure : null;
			possibleChildren = new ArrayList<>();
			LittleCamPlayer camPlayer = structure instanceof LittleCamPlayer ? (LittleCamPlayer) structure : null;
			
			if (LittleCamPlayer.camIDs != null) {
				for (int i = 0; i < LittleCamPlayer.camIDs.length; i++) {
					box.addControl(new GuiCheckBox("" + i, LittleCamPlayer.camIDs[i], 0, i * 20, camPlayer != null && camPlayer.camToPlay == i));
				}
				
			}
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleCamPlayer structure = createStructure(LittleCamPlayer.class, null);
			GuiScrollBox box = (GuiScrollBox) parent.get("content");
			List<Integer> toPlay = new ArrayList<>();
			for (Integer integer : possibleChildren) {
				GuiCheckBox checkBox = (GuiCheckBox) box.get("" + integer);
				if (checkBox != null && checkBox.value)
					toPlay.add(integer);
			}
			for (int i = 0; i < toPlay.size(); i++)
				structure.camToPlay = toPlay.get(i);
			return structure;
		}
		
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleCamPlayer.class);
		}
		
	}
	
}
