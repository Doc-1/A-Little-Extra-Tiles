package com.alet.items;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.utils.math.vec.Vec3;
import com.creativemd.creativecore.common.utils.mc.TickUtils;
import com.creativemd.littletiles.LittleTiles;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemLittleScissors extends Item {
    
    public ItemLittleScissors(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setMaxStackSize(1);
        setCreativeTab(LittleTiles.littleTab);
        
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        Vec3d playerPos = playerIn.getPositionVector();
        double d0 = (double) Minecraft.getMinecraft().playerController.getBlockReachDistance() * 3;
        float f0 = Minecraft.getMinecraft().getRenderPartialTicks();
        Vec3d pos1 = playerPos.addVector(d0, d0, d0);
        Vec3d pos2 = playerPos.addVector(-d0, -d0, -d0);
        
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
    
    public boolean findBoxes(EntityPlayer playerIn, float partialTicks) {
        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        float partialTickTime = TickUtils.getPartialTickTime();
        Vec3d pos = playerIn.getPositionEyes(partialTickTime);
        Vec3d look = playerIn.getLook(partialTickTime);
        double d0 = (double) Minecraft.getMinecraft().playerController.getBlockReachDistance() * 3;
        Vec3d vec32 = pos.addVector(look.x * d0, look.y * d0, look.z * d0);
        /*
        for (LeadConnectionData data : entity.connectionsMap) {
            for (UUID uuid : data.uuidsConnected) {
                for (Entity loadedEntity : world.loadedEntityList) {
                    if (uuid.equals(loadedEntity.getPersistentID())) {
                        boxes.addAll(calcBoxes(entity, loadedEntity, entity.posX, entity.posY, entity.posZ, data.thickness, data.tautness, data.lightLevel, partialTicks));
                        for (int i = 0; i < boxes.size(); i++) {
                            RayTraceResult result = boxes.get(i).calculateIntercept(pos, vec32);
                            if (result != null) {
                                System.out.println("success");
                                entity.setDead();
                            }
                        }
                    }
                }
            }
        }*/
        return false;
    }
    
    protected List<AxisAlignedBB> calcBoxes(Entity entity, double x, double y, double z, double thickness, double tautness, float lightLevel, float partialTicks) {
        /*
        if (entity != null) {
            //System.out.println(entity);
            //point 0
            double p0_0 = entityConnection.prevPosX + (entityConnection.posX - entityConnection.prevPosX) * (double) partialTicks;
            double p0_1 = entityConnection.prevPosY + (entityConnection.posY - entityConnection.prevPosY) * (double) partialTicks;
            double p0_2 = entityConnection.prevPosZ + (entityConnection.posZ - entityConnection.prevPosZ) * (double) partialTicks;
            
            //point 1
            double p1_0 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
            double p1_1 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks;
            double p1_2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
            
            //End point
            double g0 = p0_0 - p1_0;
            double g1 = p0_1 - p1_1;
            double g2 = p0_2 - p1_2;
            
            if (entity instanceof EntityPlayer) {
                g1 -= 0.9;
            }
            //center point between point 0 and 1 point 2
            double p2_0 = p0_0 - (g0 * 0.5D);
            double p2_1 = p0_1 - (g1 * 0.5D) + tautness;
            double p2_2 = p0_2 - (g2 * 0.5D);
            
            //Mid point
            double k0 = p2_0 - p1_0;
            double k1 = p2_1 - p1_1;
            double k2 = p2_2 - p1_2;
            
            Vec3 startPoint1 = new Vec3(x, y, z);
            Vec3 midPoint1 = new Vec3((x - k0), (y - k1), (z - k2));
            Vec3 endPoint1 = new Vec3((x - g0), (y - g1), (z - g2));
            
            Vec3 drawPointA = new Vec3(0, 0, 0);
            Vec3 drawPointB = new Vec3(0, 0, 0);
            
            List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
            for (int j = 0; j <= 24; j++) {
                float f3 = (float) j / 24.0F;
                float f4 = (float) (j + 1) / 24.0F;
                bezier(drawPointA, startPoint1, midPoint1, endPoint1, f3);
                bezier(drawPointB, startPoint1, midPoint1, endPoint1, f4);
                double minX = Math.min(drawPointA.x - thickness, drawPointB.x - thickness);
                double minY = Math.min(drawPointA.y - thickness, drawPointB.y - thickness);
                double minZ = Math.min(drawPointA.z - thickness, drawPointB.z - thickness);
                
                double maxX = Math.max(drawPointA.x + thickness, drawPointB.x + thickness);
                double maxY = Math.max(drawPointA.y + thickness, drawPointB.y + thickness);
                double maxZ = Math.max(drawPointA.z + thickness, drawPointB.z + thickness);
                AxisAlignedBB aabb1 = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
                boxes.add(aabb1);
            }
            return boxes;
        }*/
        return null;
    }
    
    public void bezier(Vec3 pFinal, Vec3 p0, Vec3 p1, Vec3 p2, float t) {
        pFinal.x = Math.pow(1 - t, 2) * p0.x + (1 - t) * 2 * t * p1.x + t * t * p2.x;
        pFinal.y = Math.pow(1 - t, 2) * p0.y + (1 - t) * 2 * t * p1.y + t * t * p2.y;
        pFinal.z = Math.pow(1 - t, 2) * p0.z + (1 - t) * 2 * t * p1.z + t * t * p2.z;
    }
}
