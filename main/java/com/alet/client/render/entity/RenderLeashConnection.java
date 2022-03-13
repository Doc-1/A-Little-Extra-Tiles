package com.alet.client.render.entity;

import java.util.UUID;

import org.lwjgl.util.Color;

import com.alet.ALET;
import com.alet.common.entity.EntityLeadConnection;
import com.alet.common.entity.LeadConnectionData;
import com.creativemd.creativecore.common.utils.math.vec.Vec3;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.client.render.overlay.PreviewRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RenderLeashConnection extends Render<EntityLeadConnection> {
	
	private static final ResourceLocation CHAIN_TEXTURES = new ResourceLocation(ALET.MODID, "textures/test_1.png");
	private final ModelLeashKnot leashKnotModel = new ModelLeashKnot();
	private final World world;
	
	public RenderLeashConnection(RenderManager renderManagerIn) {
		super(renderManagerIn);
		world = Minecraft.getMinecraft().world;
	}
	
	@Override
	public boolean shouldRender(EntityLeadConnection entityConnection, ICamera camera, double camX, double camY, double camZ) {
		AxisAlignedBB axisalignedbb = entityConnection.getRenderBoundingBox().grow(10.5D);
		return true;
		//return entityConnection.isInRangeToRender3d(camX, camY, camZ) && (entityConnection.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(axisalignedbb));
	}
	
	/** Renders the desired {@code T} type Entity. */
	@Override
	public void doRender(EntityLeadConnection entity, double x, double y, double z, float entityYaw, float partialTicks) {
		//System.out.println(entity.getDataManager().get(entity.CONNECTIONS));
		
		entity.entityFollowDoor();
		World world = entity.getWorld();
		for (LeadConnectionData data : entity.connectionsMap) {
			for (UUID uuid : data.uuidsConnected) {
				for (Entity loadedEntity : world.loadedEntityList) {
					if (uuid.equals(loadedEntity.getPersistentID())) {
						data.idsConnected.add(loadedEntity.getEntityId());
					}
				}
			}
			for (int id : data.idsConnected)
				renderLeash(entity, world.getEntityByID(id), x, y, z, data.color, data.thickness, data.tautness, partialTicks);
		}
		
		/*
		 * 
			NBTTagCompound nbt = (NBTTagCompound) nb;
			long L = nbt.getLong("L");
			long M = nbt.getLong("M");
			UUID uuid = new UUID(M, L);
			for (Entity en : world.loadedEntityList) {
				if (uuid.equals(en.getPersistentID())) {
					int color = nbt.getInteger("color");
					double thickness = nbt.getDouble("thickness");
					double tautness = nbt.getDouble("tautness");
					renderLeash(entity, world.getEntityByID(en.getEntityId()), x, y, z, color, thickness, tautness, partialTicks);
					break;
				}
			}
		 */
		
		/*
		//System.out.println(nbt.getTagList("c", Constants.NBT.TAG_COMPOUND));
		if (entity.data.connectIDs != null) {
			for (int id : entity.data.connectIDs) {
				Entity en = entity.getWorld().getEntityByID(id);
				renderLeash(entity, en, x, y, z, entityYaw, partialTicks);
			}
		}
		*/
		/*
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		GlStateManager.translate((float) x, (float) y, (float) z);
		float f = 0.0625F;
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		GlStateManager.enableAlpha();
		this.bindEntityTexture(entity);
		
		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}
		
		this.leashKnotModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}
		
		GlStateManager.popMatrix();*/
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	private double interpolateValue(double start, double end, double pct) {
		return start + (end - start) * pct;
	}
	
	public void bezier(Vec3 pFinal, Vec3 p0, Vec3 p1, Vec3 p2, float t) {
		pFinal.x = Math.pow(1 - t, 2) * p0.x + (1 - t) * 2 * t * p1.x + t * t * p2.x;
		pFinal.y = Math.pow(1 - t, 2) * p0.y + (1 - t) * 2 * t * p1.y + t * t * p2.y;
		pFinal.z = Math.pow(1 - t, 2) * p0.z + (1 - t) * 2 * t * p1.z + t * t * p2.z;
	}
	
	protected void renderLeash(EntityLeadConnection entityConnection, Entity entity, double x, double y, double z, int color, double thickness, double tautness, float partialTicks) {
		
		if (entity != null) {
			//System.out.println(entity);
			//point 0
			double p0_0 = entityConnection.prevPosX
			        + (entityConnection.posX - entityConnection.prevPosX) * (double) partialTicks;
			double p0_1 = entityConnection.prevPosY
			        + (entityConnection.posY - entityConnection.prevPosY) * (double) partialTicks;
			double p0_2 = entityConnection.prevPosZ
			        + (entityConnection.posZ - entityConnection.prevPosZ) * (double) partialTicks;
			
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
			
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			Vec3 drawPoint = new Vec3(0, 0, 0);
			Color c = ColorUtils.IntToRGBA(color);
			float red = c.getRed() / 255F;
			float green = c.getGreen() / 255F;
			float blue = c.getBlue() / 255F;
			float alpha = c.getAlpha() / 255F;
			GlStateManager.enableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			
			GlStateManager.color(red, green, blue, alpha);
			PreviewRenderer.mc.renderEngine.bindTexture(PreviewRenderer.WHITE_TEXTURE);
			GlStateManager.enableAlpha();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
			
			double distance = 0;
			Vec3d prevVec = new Vec3d(startPoint.x, startPoint.y, startPoint.z);
			
			for (int j = 0; j <= 24; ++j) {
				
				float f3 = (float) j / 24.0F;
				bezier(drawPoint, startPoint, midPoint, endPoint, f3);
				Vec3d vec = new Vec3d(drawPoint.x, drawPoint.y, drawPoint.z);
				distance += vec.distanceTo(prevVec);
				prevVec = vec;
				bufferbuilder.pos(drawPoint.x
				        - thickness, drawPoint.y, drawPoint.z).color(red, green, blue, alpha).endVertex();
				bufferbuilder.pos(drawPoint.x
				        + thickness, drawPoint.y, drawPoint.z).color(red, green, blue, alpha).endVertex();
			}
			//System.out.println(distance);
			tessellator.draw();
			bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
			
			for (int k = 0; k <= 24; ++k) {
				
				float f3 = (float) k / 24.0F;
				
				bezier(drawPoint, startPoint, midPoint, endPoint, f3);
				bufferbuilder.pos(drawPoint.x, drawPoint.y, drawPoint.z
				        - thickness).color(red, green, blue, alpha).endVertex();
				bufferbuilder.pos(drawPoint.x, drawPoint.y, drawPoint.z
				        + thickness).color(red, green, blue, alpha).endVertex();
			}
			
			tessellator.draw();
			bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
			
			for (int k = 0; k <= 24; ++k) {
				
				float f3 = (float) k / 24.0F;
				
				bezier(drawPoint, startPoint, midPoint, endPoint, f3);
				bufferbuilder.pos(drawPoint.x, drawPoint.y
				        - thickness, drawPoint.z).color(red, green, blue, alpha).endVertex();
				bufferbuilder.pos(drawPoint.x, drawPoint.y
				        + thickness, drawPoint.z).color(red, green, blue, alpha).endVertex();
			}
			
			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.disableAlpha();
			GlStateManager.enableCull();
		}
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityLeadConnection entity) {
		return CHAIN_TEXTURES;
	}
}

/*
 * 
		if (entity != null) {
			y = y - (1.6D) * 0.78D;
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			double d0 = this.interpolateValue((double) entity.prevRotationYaw, (double) entity.rotationYaw, (double) (partialTicks * 0.5F)) * 0.01745329238474369D;
			double d1 = this.interpolateValue((double) entity.prevRotationPitch, 0, (double) (partialTicks * 0.5F)) * 0.01745329238474369D;
			double d2 = Math.cos(d0) - 0.85D;
			double d3 = Math.sin(d0) - 0.18D;
			double d4 = Math.sin(d1) - 3.24D;
			double d5 = Math.cos(d1);
			double d6 = this.interpolateValue(entity.prevPosX, entity.posX, (double) partialTicks) - d2 * 0.7D - d3 * 0.5D * d5;
			double d7 = this.interpolateValue(entity.prevPosY + (double) entity.getEyeHeight() * 0.7D, entity.posY + (double) entity.getEyeHeight() * 0.7D, (double) partialTicks) - d4 * 0.5D - 0.25D;
			double d8 = this.interpolateValue(entity.prevPosZ, entity.posZ, (double) partialTicks) - d3 * 0.7D + d2 * 0.5D * d5;
			if (entity instanceof EntityPlayer) {
				d6 -= 0.8D;
				d7 -= 0.6D;
				d8 += 0.3D;
			}
			double d9 = this.interpolateValue((double) entityConnection.prevRenderYawOffset, (double) 0, (double) partialTicks) * 0.01745329238474369D + (Math.PI / 2D);
			d2 = Math.cos(d9) * (double) entityConnection.width * 0.4D;
			d3 = Math.sin(d9) * (double) entityConnection.width * 0.4D;
			double d10 = this.interpolateValue(entityConnection.prevPosX, entityConnection.posX, (double) partialTicks) + d2;
			double d11 = this.interpolateValue(entityConnection.prevPosY, entityConnection.posY, (double) partialTicks);
			double d12 = this.interpolateValue(entityConnection.prevPosZ, entityConnection.posZ, (double) partialTicks) + d3;
			x = x + d2;
			z = z + d3 - 0.21D;
			double d13 = (double) ((float) (d6 - d10));
			double d14 = (double) ((float) (d7 - d11));
			double d15 = (double) ((float) (d8 - d12));
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
			
			for (int j = 0; j <= 24; ++j) {
				float f = 0.5F;
				float f1 = 0.4F;
				float f2 = 0.3F;
				
				if (j % 2 == 0) {
					f *= 0.7F;
					f1 *= 0.7F;
					f2 *= 0.7F;
				}
				
				float f3 = (float) j / 24.0F;
				bufferbuilder.pos(x + d13 * (double) f3 + 0.0D, y + d14 * (double) (f3 * f3 + f3) * 0.5D + (double) ((24.0F - (float) j) / 18.0F + 0.125F), z + d15 * (double) f3).color(f, f1, f2, 1.0F).endVertex();
				bufferbuilder.pos(x + d13 * (double) f3 + 0.025D, y + d14 * (double) (f3 * f3 + f3) * 0.5D + (double) ((24.0F - (float) j) / 18.0F + 0.125F) + 0.025D, z + d15 * (double) f3).color(f, f1, f2, 1.0F).endVertex();
			}
			
			tessellator.draw();
			bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
			
			for (int k = 0; k <= 24; ++k) {
				float f4 = 0.5F;
				float f5 = 0.4F;
				float f6 = 0.3F;
				
				if (k % 2 == 0) {
					f4 *= 0.7F;
					f5 *= 0.7F;
					f6 *= 0.7F;
				}
				
				float f7 = (float) k / 24.0F;
				bufferbuilder.pos(x + d13 * (double) f7 + 0.0D, y + d14 * (double) (f7 * f7 + f7) * 0.5D + (double) ((24.0F - (float) k) / 18.0F + 0.125F) + 0.025D, z + d15 * (double) f7).color(f4, f5, f6, 1.0F).endVertex();
				bufferbuilder.pos(x + d13 * (double) f7 + 0.025D, y + d14 * (double) (f7 * f7 + f7) * 0.5D + (double) ((24.0F - (float) k) / 18.0F + 0.125F), z + d15 * (double) f7 + 0.025D).color(f4, f5, f6, 1.0F).endVertex();
			}
			
			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			GlStateManager.enableCull();
		}
 */
