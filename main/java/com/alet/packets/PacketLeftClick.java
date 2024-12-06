package com.alet.packets;

import com.alet.components.structures.type.ILeftClickListener;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.littletiles.common.block.BlockTile;
import com.creativemd.littletiles.common.block.BlockTile.TEResult;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketLeftClick extends CreativeCorePacket {
    BlockPos pos;
    
    public PacketLeftClick() {
        
    }
    
    public PacketLeftClick(BlockPos pos) {
        this.pos = pos;
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        writePos(buf, pos);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        pos = readPos(buf);
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        isStructure(player.getEntityWorld(), pos, player);
    }
    
    public static boolean isStructure(World world, BlockPos pos, EntityPlayer player) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof BlockTile) {
            try {
                TEResult result = BlockTile.loadTeAndTile(world, pos, player);
                if (result.isComplete() && result.parent.isStructure() && result.parent
                        .getStructure() instanceof ILeftClickListener) {
                    ((ILeftClickListener) result.parent.getStructure()).onLeftClick(player);
                    return true;
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
            }
            
        }
        return false;
    }
}
