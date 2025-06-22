package com.alet.common.measurment.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.alet.ALET;
import com.alet.common.Measurements;
import com.alet.common.measurment.shape.type.LittleMeasurement;
import com.alet.common.packets.tool.tapemeasure.PacketRecordMeasurements;
import com.creativemd.creativecore.common.packet.PacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleMeasurementsList {
    
    private final List<LittleMeasurement> measurementsList = new ArrayList<LittleMeasurement>();
    private final HashMap<UUID, LittleMeasurement> uuidToMeasurementsMap = new HashMap<UUID, LittleMeasurement>();
    private static MinecraftServer mcServ;
    
    private static int tick = 0;
    
    public static NBTTagCompound serialize(Measurements measurements) {
        if (measurements == null)
            return new NBTTagCompound();
        NBTTagCompound nbtStack = new NBTTagCompound();
        return nbtStack;
    }
    
    public static void deserialize(ItemStack tapeMeasureStack) {
        if (tapeMeasureStack.hasTagCompound()) {}
    }
    
    public static void recordMeasurements(EntityPlayer player) {
        for (int x = 0; x <= player.inventory.getSizeInventory(); x++) {
            ItemStack stack = player.inventory.getStackInSlot(x);
            
            if (stack.getItem().equals(ALET.tapeMeasure)) {
                
            }
        }
    }
    
    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void checkInventories(ServerTickEvent event) {
        if (event.phase.equals(Phase.START)) {
            if (tick <= 100) {
                if (mcServ == null)
                    mcServ = FMLCommonHandler.instance().getMinecraftServerInstance();
                for (EntityPlayerMP player : mcServ.getPlayerList().getPlayers())
                    for (int x = 0; x <= player.inventory.getSizeInventory(); x++) {
                        ItemStack stack = player.inventory.getStackInSlot(x);
                        if (stack != null && stack.getItem() == ALET.tapeMeasure) {
                            PacketHandler.sendPacketToPlayer(new PacketRecordMeasurements(), player);
                            break;
                        }
                    }
                tick = 0;
            }
            tick++;
        }
    }
    
}
