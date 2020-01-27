package com.ltphoto.client;

import com.MasterForge.MultiTileStructure;
import com.MasterForge.MultiTileTicking;
import com.ltphoto.CommonProxy;
import com.ltphoto.render.TapeRenderer;
import com.ltphoto.render.string.StringRenderer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class LTPhotoClient extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		MinecraftForge.EVENT_BUS.register(new TapeRenderer());

	}
}
