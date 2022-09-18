package com.alet.common.event;

import com.alet.client.gui.override.HandlerSubGuiOverride;
import com.alet.common.structure.type.ILeftClickListener;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.mc.ContainerSub;
import com.creativemd.littletiles.client.event.LeftClick;
import com.creativemd.littletiles.common.block.BlockTile;
import com.creativemd.littletiles.common.block.BlockTile.TEResult;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ALETEventHandler {
    
    private static boolean guiOpened = false;
    private static int phase = 1;
    private static String guiName = "";
    private SubGui guiList;
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(ClientTickEvent event) {
        if (event.phase == Phase.START) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null) {
                if (phase == 1 && mc.player.openContainer instanceof ContainerSub) {
                    guiList = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                    HandlerSubGuiOverride.overrideGuiFrom(guiList);
                    phase = 2;
                }
                if (phase == 2 && !(mc.player.openContainer instanceof ContainerSub)) {
                    HandlerSubGuiOverride.refreshAllGuiUpdate();
                    phase = 1;
                } else if (mc.player.openContainer instanceof ContainerSub) {
                    guiList = ((ContainerSub) mc.player.openContainer).gui.getTopLayer();
                    HandlerSubGuiOverride.updateGuiFrom(guiList);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onLeftClick(LeftClick event) {
        /*
        if (event.result.typeOfHit.equals(Type.BLOCK)) {
            Block block = event.world.getBlockState(event.result.getBlockPos()).getBlock();
            isStructure(event.world, event.result.getBlockPos(), event.player);
        }*/
    }
    
    public static boolean isStructure(World world, BlockPos pos, EntityPlayer player) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof BlockTile) {
            try {
                BlockTile blockTile = (BlockTile) block;
                TEResult result = blockTile.loadTeAndTile(world, pos, player);
                if (result.isComplete() && result.parent.isStructure() && result.parent.getStructure() instanceof ILeftClickListener) {
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
