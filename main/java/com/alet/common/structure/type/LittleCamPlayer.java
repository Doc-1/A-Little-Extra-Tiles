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
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
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
	
	public String camToPlay = "";
	public boolean playerIsCamera = true;
	public int duration = 0;
	public int loop = 0;
	
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
		CamPath path = CMDCamServer.getPath(world, camToPlay);
		if (path != null) {
			if (CamCommandServer.StringToDuration(this.duration + "") != 0) {
				if (!path.isRunning()) {
					if (playerIsCamera)
						path.mode = "default";
					else
						path.mode = "outside";
					long duration = CamCommandServer.StringToDuration(this.duration + "");
					path.duration = duration;
					path.loop = loop;
				} else
					for (EntityPlayer player : world.playerEntities)
						player.sendMessage(new TextComponentString("Path '" + "new" + "' could not be found!"));
					
				PacketHandler.sendPacketToAllPlayers(new StartPathPacket(path));
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		if (playerIn.isCreative())
			playCam(worldIn);
		return true;
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		camToPlay = nbt.getString("cam");
		playerIsCamera = nbt.getBoolean("playerIsCam");
		duration = nbt.getInteger("duration");
		loop = nbt.getInteger("loop");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setString("cam", camToPlay);
		nbt.setBoolean("playerIsCam", playerIsCamera);
		nbt.setInteger("duration", duration);
		nbt.setInteger("loop", loop);
	}
	
	public static class LittleCamPlayerParserALET extends LittleStructureGuiParser {
		
		public LittleCamPlayerParserALET(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		protected void createControls(LittlePreviews previews, LittleStructure structure) {
			
			LittleCamPlayer camPlayer = structure instanceof LittleCamPlayer ? (LittleCamPlayer) structure : null;
			if (camPlayer != null)
				PacketHandler.sendPacketToServer(new PacketGetServerCams(parent.getID(), camPlayer.camToPlay));
			else
				PacketHandler.sendPacketToServer(new PacketGetServerCams(parent.getID()));
			
			List<String> list = new ArrayList<String>();
			GuiComboBox box = new GuiComboBox("cameras", 0, 0, 100, list);
			
			box.enabled = false;
			parent.controls.add(box);
			parent.controls.add(new GuiCheckBox("plyrCam", "Player Is Camera", 0, 22, camPlayer == null ? true : camPlayer.playerIsCamera));
			parent.controls.add(new GuiLabel("Duration: ", 0, 40));
			parent.controls.add(new GuiLabel("Loop: ", 0, 60));
			parent.controls.add(new GuiTextfield("duration", camPlayer == null ? "0" : camPlayer.duration + "", 50, 40, 20, 10));
			parent.controls.add(new GuiTextfield("loop", camPlayer == null ? "0" : camPlayer.loop + "", 50, 60, 20, 10));
			
			parent.controls.add(new GuiTextBox("text", "Use the /cam-server add command to add a new path to the drop down menu. Duration cannot be zero. It will not play.", 110, 0, 82));
			
		}
		
		@Override
		protected LittleStructure parseStructure(LittlePreviews previews) {
			LittleCamPlayer structure = createStructure(LittleCamPlayer.class, null);
			GuiComboBox cameras = (GuiComboBox) parent.get("cameras");
			GuiCheckBox plyrCam = (GuiCheckBox) parent.get("plyrCam");
			GuiTextfield duration = (GuiTextfield) parent.get("duration");
			GuiTextfield loop = (GuiTextfield) parent.get("loop");
			structure.camToPlay = cameras.getCaption();
			structure.playerIsCamera = plyrCam.value;
			structure.duration = Integer.parseInt(duration.text);
			structure.loop = Integer.parseInt(loop.text);
			return structure;
		}
		
		@Override
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleCamPlayer.class);
		}
		
	}
	
}
