package com.alet.client;

import java.awt.GraphicsEnvironment;

import com.alet.CommonProxy;
import com.alet.gui.GuiAxisIndicatorAletControl;
import com.alet.gui.SubGuiTypeWriter;
import com.alet.items.ItemTapeMeasure;
import com.alet.render.string.StringRenderer;
import com.alet.render.tapemeasure.GuiDisplayMeasurements;
import com.alet.render.tapemeasure.TapeRenderer;
import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.client.render.overlay.OverlayControl;
import com.creativemd.littletiles.client.render.overlay.OverlayRenderer.OverlayPositionType;
import com.creativemd.littletiles.server.LittleTilesServer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ALETClient extends LittleTilesServer {

	@Override
	public void loadSidePre() {
		super.loadSidePre();
		MinecraftForge.EVENT_BUS.register(new TapeRenderer());		
		MinecraftForge.EVENT_BUS.register(new ItemTapeMeasure());
	}
	
	
	@Override
	public void loadSidePost() {
		super.loadSidePost();
		LittleTilesClient.overlay.add(new OverlayControl(new GuiAxisIndicatorAletControl("axis", 20, 20), OverlayPositionType.RIGHT_CORNOR).setShouldRender(() -> TapeRenderer.inInv));
		LittleTilesClient.overlay.add(new OverlayControl(new GuiDisplayMeasurements("display", 0, 0), OverlayPositionType.RIGHT_CORNOR).setShouldRender(() -> TapeRenderer.inInv));
		
	}
	
}
