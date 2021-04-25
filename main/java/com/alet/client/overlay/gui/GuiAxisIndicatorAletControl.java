package com.alet.client.overlay.gui;

import org.lwjgl.util.Color;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.action.LittleAction;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;

public class GuiAxisIndicatorAletControl extends GuiControl {
	
	public static Style transparentStyle = new Style("Transparent", new ColoredDisplayStyle(0, 0, 0, 40), new ColoredDisplayStyle(90, 90, 90, 60), new ColoredDisplayStyle(90, 90, 90, 50), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
	
	public GuiAxisIndicatorAletControl(String name) {
		super(name, 0, 0, 0, 0);
	}
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		
		GlStateManager.translate(width / 2D, height / 2D, 0);
		
		posX = 50;
		posY = 40;
		
		float partialTicks = mc.getRenderPartialTicks();
		Entity entity = this.mc.getRenderViewEntity();
		float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
		float yaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
		GlStateManager.rotate(pitch, -1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(-1.0F, -1.0F, -1.0F);
		
		if (!Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			{
				float direction = pitch % 180;
				
				GlStateManager.pushMatrix();
				GlStateManager.rotate(180, 0, 0, 1);
				GuiRenderHelper.instance.drawStringWithShadow("Y", -15, -50, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
				GlStateManager.popMatrix();
				
				GlStateManager.pushMatrix();
				GlStateManager.rotate(180, 1, 0, 0);
				GuiRenderHelper.instance.drawStringWithShadow("Y", 15, -50, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
				GlStateManager.popMatrix();
				
				if (direction < 45 && direction > -45) {
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180, 0, 0, 1);
					GuiRenderHelper.instance.drawStringWithShadow("X", -30, -15, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
					GlStateManager.popMatrix();
					
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180, 1, 0, 0);
					GuiRenderHelper.instance.drawStringWithShadow("X", 30, -15, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
					GlStateManager.popMatrix();
				} else {
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.rotate(90, 1, 0, 0);
					GuiRenderHelper.instance.drawStringWithShadow("X", -30, -15, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
					GlStateManager.popMatrix();
					
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.rotate(-90, 1, 0, 0);
					GuiRenderHelper.instance.drawStringWithShadow("X", -30, -15, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
					GlStateManager.popMatrix();
				}
				
				GlStateManager.pushMatrix();
				
				GlStateManager.rotate(-90, 0, 1, 0);
				
				if (direction < 45 && direction > -45) {
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180, 0, 0, 1);
					GuiRenderHelper.instance.drawStringWithShadow("Z", -30, -15, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
					GlStateManager.popMatrix();
					
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180, 1, 0, 0);
					GuiRenderHelper.instance.drawStringWithShadow("Z", 30, -15, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
					GlStateManager.popMatrix();
				} else {
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.rotate(90, 1, 0, 0);
					GuiRenderHelper.instance.drawStringWithShadow("Z", -30, -15, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
					GlStateManager.popMatrix();
					
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180, 0, 0, 1);
					GlStateManager.rotate(-90, 1, 0, 0);
					GuiRenderHelper.instance.drawStringWithShadow("Z", -30, -15, ColorUtils.RGBAToInt(new Color(255, 255, 255, 255)));
					GlStateManager.popMatrix();
				}
				
				GlStateManager.popMatrix();
			}
			
			OpenGlHelper.renderDirections(40);
		}
	}
	
}
