package com.alet.client;

import java.awt.GraphicsEnvironment;

import com.alet.CommonProxy;
import com.alet.gui.SubGuiTypeWriter;
import com.alet.items.ItemTapeMeasure;
import com.alet.render.string.StringRenderer;
import com.alet.render.tapemeasure.MeasurementRender;
import com.alet.render.tapemeasure.TapeRenderer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ALETClient extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		MinecraftForge.EVENT_BUS.register(new TapeRenderer());		
		MinecraftForge.EVENT_BUS.register(new MeasurementRender());
		MinecraftForge.EVENT_BUS.register(new ItemTapeMeasure());		
	}
}
