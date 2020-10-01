package com.alet.client;

import java.awt.GraphicsEnvironment;

import org.lwjgl.input.Keyboard;

import com.alet.CommonProxy;
import com.alet.common.util.TapeMeasureKeyEventHandler;
import com.alet.common.util.shape.DragShapeTriangle;
import com.alet.gui.GuiAxisIndicatorAletControl;
import com.alet.gui.GuiDisplayMeasurements;
import com.alet.gui.SubGuiTypeWriter;
import com.alet.items.ItemTapeMeasure;
import com.alet.render.string.StringRenderer;
import com.alet.render.tapemeasure.TapeRenderer;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.client.render.overlay.OverlayControl;
import com.creativemd.littletiles.client.render.overlay.OverlayRenderer.OverlayPositionType;
import com.creativemd.littletiles.common.util.shape.DragShape;
import com.creativemd.littletiles.common.util.shape.DragShapeBox;
import com.creativemd.littletiles.common.util.shape.SelectShape;
import com.creativemd.littletiles.common.util.shape.SelectShape.DragSelectShape;
import com.creativemd.littletiles.server.LittleTilesServer;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ALETClient extends LittleTilesServer {

	public static final DragShape triangle = new DragShapeTriangle();
	public static final DragSelectShape triangleSelect = new DragSelectShape(triangle);

	public static KeyBinding clearMeasurment;

	@Override
	public void loadSidePre() {
		DragShape.registerDragShape(triangle);
		SelectShape.registerShape(triangleSelect);
	}
	
	@Override
	public void loadSidePost() {
		clearMeasurment = new KeyBinding("Clear Measurement", net.minecraftforge.client.settings.KeyConflictContext.UNIVERSAL, KeyModifier.ALT, Keyboard.KEY_C, "key.categories.littletiles");
		ClientRegistry.registerKeyBinding(clearMeasurment);
		
		MinecraftForge.EVENT_BUS.register(new TapeRenderer());		
		MinecraftForge.EVENT_BUS.register(new TapeMeasureKeyEventHandler());
		
		LittleTilesClient.overlay.add(new OverlayControl(new GuiAxisIndicatorAletControl("axis"), OverlayPositionType.CENTER).setShouldRender(() -> TapeRenderer.inInv));
		LittleTilesClient.overlay.add(new OverlayControl(new GuiDisplayMeasurements("display"), OverlayPositionType.CENTER).setShouldRender(() -> TapeRenderer.inInv));
	}
	
}
