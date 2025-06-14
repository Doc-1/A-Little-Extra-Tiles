package com.alet.components.structures.type;

import com.creativemd.cmdcam.common.packet.StartPathPacket;
import com.creativemd.cmdcam.common.utils.CamPath;
import com.creativemd.cmdcam.server.CMDCamServer;
import com.creativemd.cmdcam.server.CamCommandServer;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

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
    
}
