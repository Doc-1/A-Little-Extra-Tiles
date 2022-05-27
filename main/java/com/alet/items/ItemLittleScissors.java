package com.alet.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.alet.common.entity.EntityRopeConnection;
import com.alet.common.entity.LeadConnectionData;
import com.creativemd.creativecore.common.utils.math.vec.Vec3;
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
        Vec3d look = playerIn.getPositionVector();
        double d0 = (double) Minecraft.getMinecraft().playerController.getBlockReachDistance();
        float f0 = Minecraft.getMinecraft().getRenderPartialTicks();
        Vec3d pos1 = look.addVector(d0, d0, d0);
        Vec3d pos2 = look.addVector(-d0, -d0, -d0);
        List<EntityRopeConnection> entityList = worldIn.getEntitiesWithinAABB(EntityRopeConnection.class, new AxisAlignedBB(pos1, pos2));
        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        for (EntityRopeConnection entity : entityList) {
            boxes.addAll(findBoxes(entity, entity.posX, entity.posY, entity.posZ, entity.rotationYaw, f0));
        }
        
        Vec3 start = new Vec3(playerIn.getPositionVector().x, playerIn.getPositionVector().y, playerIn.getPositionVector().z);
        Vec3 end = new Vec3(playerIn.rayTrace(d0, f0).hitVec.x, playerIn.rayTrace(d0, f0).hitVec.y, playerIn.rayTrace(d0, f0).hitVec.z);
        Vec3 mid = new Vec3(start.x - end.x, start.y - end.y, start.z - end.z);
        Vec3 point = new Vec3(0, 0, 0);
        List<AxisAlignedBB> boxes2 = new ArrayList<AxisAlignedBB>();
        for (int j = 0; j <= 24; j++) {
            float f3 = (float) j / 24.0F;
            this.bezier(point, start, mid, end, f3);
            for (AxisAlignedBB aabb : boxes) {
                if (aabb.contains(new Vec3d(point.x, point.y, point.z))) {
                    System.out.println("success");
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
    
    public List<AxisAlignedBB> findBoxes(EntityRopeConnection entity, double x, double y, double z, float entityYaw, float partialTicks) {
        //System.out.println(entity.getDataManager().get(entity.CONNECTIONS));
        
        entity.entityFollowDoor();
        World world = entity.getWorld();
        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        for (LeadConnectionData data : entity.connectionsMap) {
            for (UUID uuid : data.uuidsConnected) {
                for (Entity loadedEntity : world.loadedEntityList) {
                    if (uuid.equals(loadedEntity.getPersistentID())) {
                        data.idsConnected.add(loadedEntity.getEntityId());
                    }
                }
            }
            for (int id : data.idsConnected)
                boxes.addAll(calcBoxes(entity, world.getEntityByID(id), x, y, z, data.thickness, data.tautness, data.lightLevel, partialTicks));
        }
        return boxes;
    }
    
    protected List<AxisAlignedBB> calcBoxes(EntityRopeConnection entityConnection, Entity entity, double x, double y, double z, double thickness, double tautness, float lightLevel, float partialTicks) {
        
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
            
            Vec3 startPoint = new Vec3(x, y, z);
            Vec3 midPoint = new Vec3(x - k0, y - k1, z - k2);
            Vec3 endPoint = new Vec3(x - g0, y - g1, z - g2);
            
            //System.out.println(startPoint + " " + endPoint);
            
            Vec3 drawPointA = new Vec3(0, 0, 0);
            Vec3 drawPointB = new Vec3(0, 0, 0);
            
            List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
            double distance = 0;
            Vec3d prevVec = new Vec3d(startPoint.x, startPoint.y, startPoint.z);
            for (int j = 0; j <= 24; j += 2) {
                System.out.println(j);
                float f3 = (float) j / 24.0F;
                float f4 = (float) j + 1 / 24.0F;
                bezier(drawPointA, startPoint, midPoint, endPoint, f3);
                bezier(drawPointB, startPoint, midPoint, endPoint, f4);
                Vec3d vecA = new Vec3d(drawPointA.x, drawPointA.y, drawPointA.z);
                Vec3d vecB = new Vec3d(drawPointB.x, drawPointB.y, drawPointB.z);
                distance += vecA.distanceTo(prevVec);
                prevVec = vecA;
                AxisAlignedBB aabb = new AxisAlignedBB(drawPointA.x - thickness, drawPointA.y - thickness, drawPointA.z, drawPointB.x + thickness, drawPointB.y + thickness, drawPointB.z);
                boxes.add(aabb);
                //bufferbuilder.pos(drawPoint.x - thickness, drawPoint.y, drawPoint.z).color(red, green, blue, alpha).endVertex();
                //bufferbuilder.pos(drawPoint.x + thickness, drawPoint.y, drawPoint.z).color(red, green, blue, alpha).endVertex();
            }
            return boxes;
        }
        return null;
    }
    
    public void bezier(Vec3 pFinal, Vec3 p0, Vec3 p1, Vec3 p2, float t) {
        pFinal.x = Math.pow(1 - t, 2) * p0.x + (1 - t) * 2 * t * p1.x + t * t * p2.x;
        pFinal.y = Math.pow(1 - t, 2) * p0.y + (1 - t) * 2 * t * p1.y + t * t * p2.y;
        pFinal.z = Math.pow(1 - t, 2) * p0.z + (1 - t) * 2 * t * p1.z + t * t * p2.z;
    }
}
