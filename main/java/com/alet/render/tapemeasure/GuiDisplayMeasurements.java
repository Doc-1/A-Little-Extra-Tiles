package com.alet.render.tapemeasure;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.alet.ALET;
import com.alet.gui.controls.overlay.GuiOverlayTextList;
import com.alet.items.ItemTapeMeasure;
import com.alet.render.tapemeasure.shape.Box;
import com.alet.render.tapemeasure.shape.Circle;
import com.alet.render.tapemeasure.shape.Line;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.Rect;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.ingredient.LittleInventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiDisplayMeasurements extends GuiControl{
	
	public static Style transparentStyle = new Style("Transparent", new ColoredDisplayStyle(0, 0, 0, 40), new ColoredDisplayStyle(90, 90, 90, 60), new ColoredDisplayStyle(90, 90, 90, 50), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));

	public GuiDisplayMeasurements(String name, int x, int y) {
		super(name, x, y, 0, 0);
	}
	
	
	@Override
	protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
		EntityPlayer player = Minecraft.getMinecraft().player;
		
		ItemStack stack = ItemStack.EMPTY;
		ItemStack ingredient = new ItemStack(ALET.tapeMeasure, 1);
		LittleInventory inventory = new LittleInventory(player);
		
		for(int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i).getItem() instanceof ItemTapeMeasure) {
				stack = inventory.get(i);
				break;
			}
		}
		if(!stack.isEmpty()  && !Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			NBTTagCompound nbt = stack.getTagCompound();
			FontRenderer fontRender = mc.fontRenderer;
			List<String> list = LittleGridContext.getNames();

			GuiOverlayTextList textList = new GuiOverlayTextList("measurement", 0, 0, 140, getParent());
			textList.setStyle(transparentStyle);
			/*
			 * GL Settings for Text
			 */
			GlStateManager.pushMatrix();
			GlStateManager.translate(-width+3, -height+84, 50);
			GlStateManager.disableCull();
			GlStateManager.scale(0.9F, 0.9F, 0.0F);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glFlush();
			GlStateManager.enableCull();
			int i = 0;
			
			if(stack.hasTagCompound()) {
				String nbtStr = nbt.toString();
				nbtStr.indexOf("x");
				for (int h = -1; (h = nbtStr.indexOf("x", h + 1)) != -1; h++) {
				    System.out.println(h);
				}
			}
			
			
			
			
			try {
				
			} catch (Exception e) {
				// TODO: handle exception
			}

			textList.renderControl(helper, 0, getRect());
			GlStateManager.popMatrix();
		}				
	}
	
	private String createTextFromBox(double x1, double y1, double z1, double x2, double y2, double z2, int contextSize) {
		Box box = new Box(new Vec3d(x1, y1, z1), new Vec3d(x2, y2, z2), contextSize);
		return box.writeDistance();
	}
	
	private String createTextFromLine(double x1, double y1, double z1, double x2, double y2, double z2, int contextSize) {
		Line line = new Line(new Vec3d(x1, y1, z1), new Vec3d(x2, y2, z2), contextSize);
		return line.writeDistance();	
	}
	
	private String createTextFromCircle(double x1, double y1, double z1, double x2, double y2, double z2, int contextSize) {
		Circle circle = new Circle(new Vec3d(x1, y1, z1), new Vec3d(x2, y2, z2), contextSize);
		return circle.writeDistance();
	}
}
/*
 String distence1 = Box.distence(new Vec3d(Double.parseDouble(nbt.getString("x0")), Double.parseDouble(nbt.getString("y0")), 
						Double.parseDouble(nbt.getString("z0"))), new Vec3d(Double.parseDouble(nbt.getString("x1")), 
						Double.parseDouble(nbt.getString("y1")), Double.parseDouble(nbt.getString("z1"))), 
						Integer.parseInt(list.get(nbt.getInteger("context0"))));
				
				textList.addText("Measurement 1:");
				textList.addText("X: "+Box.xString, ColorUtils.RED);
				textList.addText("Y: "+Box.yString, ColorUtils.RED);
				textList.addText("Z: "+Box.zString, ColorUtils.RED);
				
				String distence2 = Line.distence(new Vec3d(Double.parseDouble(nbt.getString("x2")), Double.parseDouble(nbt.getString("y2")), 
						Double.parseDouble(nbt.getString("z2"))), new Vec3d(Double.parseDouble(nbt.getString("x3")), 
						Double.parseDouble(nbt.getString("y3")), Double.parseDouble(nbt.getString("z3"))),
						Integer.parseInt(list.get(nbt.getInteger("context0"))));

				textList.addText("Measurement 2:");
				textList.addText("Distance: "+distence2, ColorUtils.GREEN);

				String distence3 = Box.distence(new Vec3d(Double.parseDouble(nbt.getString("x4")), Double.parseDouble(nbt.getString("y4")), 
						Double.parseDouble(nbt.getString("z4"))), new Vec3d(Double.parseDouble(nbt.getString("x5")), 
						Double.parseDouble(nbt.getString("y5")), Double.parseDouble(nbt.getString("z5"))),
						Integer.parseInt(list.get(nbt.getInteger("context4"))));
				
				textList.addText("Measurement 3:");
				textList.addText("X: "+Box.xString, ColorUtils.BLUE);
				textList.addText("Y: "+Box.yString, ColorUtils.BLUE);
				textList.addText("Z: "+Box.zString, ColorUtils.BLUE);

*/