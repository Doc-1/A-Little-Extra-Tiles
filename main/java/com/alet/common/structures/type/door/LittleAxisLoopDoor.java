package com.alet.common.structures.type.door;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiStateButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTabStateButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.type.HashMapList;
import com.creativemd.creativecore.common.utils.type.UUIDSupplier;
import com.creativemd.creativecore.common.world.SubWorld;
import com.creativemd.littletiles.client.gui.controls.GuiTileViewer;
import com.creativemd.littletiles.client.render.world.LittleRenderChunkSuppilier;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.entity.DoorController;
import com.creativemd.littletiles.common.entity.EntityAnimation;
import com.creativemd.littletiles.common.packet.LittleAnimationControllerPacket;
import com.creativemd.littletiles.common.packet.LittleAnimationDataPacket;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureAbsolute;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.type.door.LittleAxisDoor;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittleAbsolutePreviews;
import com.creativemd.littletiles.common.tileentity.TileEntityLittleTiles;
import com.creativemd.littletiles.common.util.place.Placement;
import com.creativemd.littletiles.common.util.place.PlacementHelper;
import com.creativemd.littletiles.common.util.place.PlacementMode;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleAxisLoopDoor extends LittleAxisDoor {
	
	public LittleAxisLoopDoor(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
		
		boolean result = super.onBlockActivated(worldIn, tile, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ, action);
		
		return result;
	}
	
	@Override
	public EntityAnimation openDoor(EntityPlayer player, UUIDSupplier uuid, boolean tickOnce) throws LittleActionException {
		if (isAnimated()) {
			NBTTagCompound n = ((DoorController) animation.controller).writeToNBT(new NBTTagCompound());
			n.setInteger("duration", 0);
			animation.controller = ((DoorController) animation.controller).parseController(animation, n);
			System.out.println();
			((DoorController) animation.controller).activate();
			if (tickOnce)
				animation.onUpdateForReal();
			PacketHandler.sendPacketToTrackingPlayers(new LittleAnimationControllerPacket(animation), animation, null);
			return animation;
		}
		
		LittleAbsolutePreviews previews = getDoorPreviews();
		World world = getWorld();
		SubWorld fakeWorld = SubWorld.createFakeWorld(world);
		if (world.isRemote)
			fakeWorld.renderChunkSupplier = new LittleRenderChunkSuppilier();
		Placement placement = new Placement(player, PlacementHelper.getAbsolutePreviews(fakeWorld, previews, previews.pos, PlacementMode.all)).setIgnoreWorldBoundaries(false);
		StructureAbsolute absolute = getAbsoluteAxis();
		
		HashMapList<BlockPos, IStructureTileList> blocks = collectAllBlocksListSameWorld();
		
		EntityAnimation animation = place(getWorld(), fakeWorld, player, placement, uuid, absolute, tickOnce);
		
		System.out.println(((DoorController) animation.controller).writeToNBT(new NBTTagCompound()));
		boolean sendUpdate = !world.isRemote;
		EntityAnimation topAnimation = world instanceof WorldServer ? null : (EntityAnimation) fakeWorld.getTopEntity();
		
		for (Entry<BlockPos, ArrayList<IStructureTileList>> entry : blocks.entrySet()) {
			if (entry.getValue().isEmpty())
				continue;
			TileEntityLittleTiles te = entry.getValue().get(0).getTe();
			te.updateTiles((x) -> {
				for (IStructureTileList list : entry.getValue())
					x.get(list).remove();
			});
			if (sendUpdate)
				if (topAnimation == null)
					((WorldServer) world).getPlayerChunkMap().markBlockForUpdate(te.getPos());
				else
					PacketHandler.sendPacketToTrackingPlayers(new LittleAnimationDataPacket(topAnimation), topAnimation, null);
				
		}
		
		return animation;
		
	}
	
	public static class LittleAxisLoopDoorParser extends LittleAxisDoorParser {
		
		public LittleAxisLoopDoorParser(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public LittleAxisDoor parseStructure() {
			LittleAxisLoopDoor door = createStructure(LittleAxisLoopDoor.class, null);
			GuiTileViewer viewer = (GuiTileViewer) parent.get("tileviewer");
			door.axisCenter = new StructureRelative(viewer.getBox(), viewer.getAxisContext());
			door.axis = viewer.getAxis();
			
			GuiPanel typePanel = (GuiPanel) parent.get("typePanel");
			door.doorRotation = createRotation(((GuiTabStateButton) parent.get("doorRotation")).getState());
			if (door.doorRotation instanceof FixedRotation) {
				FixedRotation doorRotation = (FixedRotation) door.doorRotation;
				float degree;
				try {
					degree = Float.parseFloat(((GuiTextfield) parent.get("degree")).text);
				} catch (NumberFormatException e) {
					degree = 0;
				}
				doorRotation.degree = degree;
			} else if (door.doorRotation instanceof DirectionRotation) {
				DirectionRotation doorRotation = (DirectionRotation) door.doorRotation;
				doorRotation.clockwise = ((GuiStateButton) parent.get("direction")).getState() == 0;
			}
			
			return door;
		}
		
	}
}
