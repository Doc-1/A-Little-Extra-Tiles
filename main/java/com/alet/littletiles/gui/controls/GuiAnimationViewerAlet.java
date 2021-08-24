package com.alet.littletiles.gui.controls;

import javax.vecmath.Vector3d;

import org.lwjgl.util.glu.Project;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.utils.mc.TickUtils;
import com.creativemd.littletiles.client.gui.controls.GuiAnimationViewer;
import com.creativemd.littletiles.client.world.LittleAnimationHandlerClient;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class GuiAnimationViewerAlet extends GuiAnimationViewer {
	
	private int moveX;
	private int moveY;
	
	public GuiAnimationViewerAlet(String name, int x, int y, int width, int height) {
		super(name, x, y, width, height);
		
	}
	
	public void moveViewPort(int x, int y) {
		moveX = x;
		moveY = y;
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		if (animation == null)
			return;
		
		makeLightBright();
		
		rotX.tick();
		rotY.tick();
		rotZ.tick();
		distance.tick();
		
		GlStateManager.disableDepth();
		
		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		GlStateManager.translate(width / 2D, height / 2D, 0);
		
		GlStateManager.pushMatrix();
		
		//mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		//mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		
		int x = getPixelOffsetX();
		int y = getPixelOffsetY() - 1;
		int scale = getGuiScale();
		GlStateManager.viewport((x + moveX) * scale, (y + moveY) * scale, width * scale, height * scale);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		Project.gluPerspective(90, (float) width / (float) height, 0.05F, 16 * 16);
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		//GlStateManager.matrixMode(5890);
		GlStateManager.translate(0, 0, -distance.current());
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableDepth();
		
		Vector3d rotationCenter = new Vector3d(animation.center.rotationCenter);
		rotationCenter.y -= 75;
		GlStateManager.rotate((float) rotX.current(), 1, 0, 0);
		GlStateManager.rotate((float) rotY.current(), 0, 1, 0);
		GlStateManager.rotate((float) rotZ.current(), 0, 0, 1);
		
		GlStateManager.translate(-min.getPosX(context), -min.getPosY(context), -min.getPosZ(context));
		
		GlStateManager.translate(-rotationCenter.x, -rotationCenter.y, -rotationCenter.z);
		
		GlStateManager.pushMatrix();
		
		GlStateManager.translate(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerY, TileEntityRendererDispatcher.staticPlayerZ);
		GlStateManager.translate(0, -75, 0);
		
		LittleAnimationHandlerClient.render.doRender(animation, 0, 0, 0, 0, TickUtils.getPartialTickTime());
		
		GlStateManager.popMatrix();
		
		GlStateManager.matrixMode(5888);
		
		GlStateManager.popMatrix();
		
		GlStateManager.disableLighting();
		GlStateManager.cullFace(GlStateManager.CullFace.BACK);
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		GlStateManager.disableDepth();
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		
		GlStateManager.viewport(0, 0, GuiControl.mc.displayWidth, GuiControl.mc.displayHeight);
		GlStateManager.matrixMode(5889);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.loadIdentity();
		mc.entityRenderer.setupOverlayRendering();
		GlStateManager.disableDepth();
	}
}
