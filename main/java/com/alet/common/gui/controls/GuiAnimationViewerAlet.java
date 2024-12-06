package com.alet.common.gui.controls;

import javax.vecmath.Vector3d;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.utils.math.SmoothValue;
import com.creativemd.creativecore.common.utils.mc.TickUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.controls.GuiAnimationViewer;
import com.creativemd.littletiles.client.world.LittleAnimationHandlerClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class GuiAnimationViewerAlet extends GuiAnimationViewer {
    
    public static final ResourceLocation GRASS_TEXTURE = Blocks.GRASS.getRegistryName();
    
    private int moveX;
    private int moveY;
    public SmoothValue tranX = new SmoothValue(200);
    public SmoothValue tranY = new SmoothValue(200);
    public SmoothValue tranZ = new SmoothValue(200);
    public boolean rightGrabbed = false;
    private int textureId = -1;
    
    public GuiAnimationViewerAlet(String name, int x, int y, int width, int height) {
        super(name, x, y, width, height);
        
    }
    
    public void moveViewPort(int x, int y) {
        moveX = x;
        moveY = y;
    }
    
    @Override
    public void mouseMove(int x, int y, int button) {
        if (grabbed) {
            rotY.set(rotY.aimed() + x - grabX);
            rotX.set(rotX.aimed() + y - grabY);
            grabX = x;
            grabY = y;
        }
        
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        if (button == 0) {
            grabbed = true;
            grabX = x;
            grabY = y;
            return true;
        }
        return false;
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        if (button == 0)
            grabbed = false;
        if (button == 1)
            rightGrabbed = false;
    }
    
    @Override
    public boolean onKeyPressed(char character, int key) {
        if (!isMouseOver())
            return false;
        double mod = 0.5D;
        if (GuiScreen.isCtrlKeyDown())
            mod = 1.5D;
        
        if (GuiScreen.isAltKeyDown())
            mod = 0.1D;
        GameSettings gSettings = Minecraft.getMinecraft().gameSettings;
        double x = tranX.current();
        double y = tranY.current();
        double z = tranZ.current();
        if (Keyboard.isKeyDown(gSettings.keyBindForward.getKeyCode())) {
            x += mod * (double) (Math.sin(-this.rotY.aimed() * ((float) Math.PI / 180F)));
            z += mod * (double) (Math.cos(this.rotY.aimed() * ((float) Math.PI / 180F)));
            //tranX.set(tranX.current() + (mod * (double) (Math.sin(-this.rotY.aimed() * ((float) Math.PI / 180F)))));
            //tranZ.set(tranZ.current() + (mod * (double) (Math.cos(this.rotY.aimed() * ((float) Math.PI / 180F)))));
        }
        if (Keyboard.isKeyDown(gSettings.keyBindBack.getKeyCode())) {
            x -= mod * (double) (Math.sin(-this.rotY.aimed() * ((float) Math.PI / 180F)));
            z -= mod * (double) (Math.cos(this.rotY.aimed() * ((float) Math.PI / 180F)));
            //tranX.set(tranX.current() - (mod * (double) (Math.sin(-this.rotY.aimed() * ((float) Math.PI / 180F)))));
            //tranZ.set(tranZ.current() - (mod * (double) (Math.cos(this.rotY.aimed() * ((float) Math.PI / 180F)))));
        }
        if (Keyboard.isKeyDown(gSettings.keyBindRight.getKeyCode())) {
            x -= mod * (double) (Math.cos(-this.rotY.aimed() * ((float) Math.PI / 180F)));
            z -= mod * (double) (Math.sin(this.rotY.aimed() * ((float) Math.PI / 180F)));
            //tranX.set(tranX.current() - (mod * (double) (Math.cos(-this.rotY.aimed() * ((float) Math.PI / 180F)))));
            //tranZ.set(tranZ.current() - (mod * (double) (Math.sin(this.rotY.aimed() * ((float) Math.PI / 180F)))));
        }
        if (Keyboard.isKeyDown(gSettings.keyBindLeft.getKeyCode())) {
            x += mod * (double) (Math.cos(-this.rotY.aimed() * ((float) Math.PI / 180F)));
            z += mod * (double) (Math.sin(this.rotY.aimed() * ((float) Math.PI / 180F)));
            //tranX.set(tranX.current() + (mod * (double) (Math.cos(-this.rotY.aimed() * ((float) Math.PI / 180F)))));
            //tranZ.set(tranZ.current() + (mod * (double) (Math.sin(this.rotY.aimed() * ((float) Math.PI / 180F)))));
        }
        if (Keyboard.isKeyDown(gSettings.keyBindSneak.getKeyCode())) {
            y += mod;
            //tranY.set(tranY.current() + mod);
        }
        if (Keyboard.isKeyDown(gSettings.keyBindJump.getKeyCode())) {
            y -= mod;
            //tranY.set(tranY.current() - mod);
        }
        tranX.set(x);
        tranY.set(y);
        tranZ.set(z);
        /*
        if (key == 17 && !GuiScreen.isShiftKeyDown()) {
            tranZ.set(tranZ.current() - (0.2D * mod));
        } else if (key == 31 && !GuiScreen.isShiftKeyDown()) {
            tranZ.set(tranZ.current() + (0.2D * mod));
        } else if (key == 32) {
            tranX.set(tranX.current() - (mod));
        } else if (key == 30) {
            tranX.set(tranX.current() + (mod));
        } else if (key == 17 && GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown()) {
            tranY.set(tranY.current() + (mod));
        } else if (key == 31 && GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown()) {
            tranY.set(tranY.current() - (mod));
        }*/
        
        return true;
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        if (animation == null)
            return;
        
        makeLightBright();
        tranX.tick();
        tranY.tick();
        tranZ.tick();
        rotX.tick();
        rotY.tick();
        distance.tick();
        
        GlStateManager.disableDepth();
        
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.translate(width / 2D, height / 2D, 0);
        
        GlStateManager.pushMatrix();
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager
                .tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int x = getPixelOffsetX();
        int y = getPixelOffsetY() - 1;
        int scale = getGuiScale();
        GlStateManager.viewport((x + moveX) * scale, (y + moveY) * scale, width * scale, height * scale);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        Project.gluPerspective(90, (float) width / (float) height, 0.05F, 16 * 16);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepth();
        GlStateManager.translate(0, 0, -distance.current());
        Vector3d rotationCenter = new Vector3d(animation.center.rotationCenter);
        rotationCenter.y -= 75;
        
        GlStateManager.translate(-min.getPosX(context), -min.getPosY(context), -min.getPosZ(context));
        
        GlStateManager.rotate((float) rotX.current(), 1, 0, 0);
        GlStateManager.rotate((float) rotY.current(), 0, 1, 0);
        GlStateManager.rotate((float) rotZ.current(), 0, 0, 1);
        GlStateManager.translate(tranX.current() - rotationCenter.x, tranY.current() - rotationCenter.y, tranZ.current() - rotationCenter.z);
        
        GlStateManager.pushMatrix();
        ResourceLocation WHITE_TEXTURE = new ResourceLocation(LittleTiles.modid, "textures/preview.png");
        mc.renderEngine.bindTexture(WHITE_TEXTURE);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        renderBox();
        GlStateManager.translate(TileEntityRendererDispatcher.staticPlayerX, TileEntityRendererDispatcher.staticPlayerY, TileEntityRendererDispatcher.staticPlayerZ);
        GlStateManager.translate(0, -75, 0);
        
        LittleAnimationHandlerClient.render.doRender(animation, 0, 0, 0, 0, TickUtils.getPartialTickTime());
        
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        
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
    
    public void renderBox() {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(0, 50, 0).color(0, 0, 0, 0).endVertex();
        bufferbuilder.pos(0, 51, 500).color(0, 0, 0, 0).endVertex();
        bufferbuilder.pos(500, 50, 500).color(0, 0, 0, 0).endVertex();
        bufferbuilder.pos(500, 51, 0).color(0, 0, 0, 0).endVertex();
        
        tessellator.draw();
    }
}
