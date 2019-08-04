package com.ltphoto.client;

import com.ltphoto.CommonProxy;
import com.ltphoto.server.LTPhotoServer;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(Side.CLIENT)
public class LTPhotoClient extends CommonProxy{
	
	@Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }
	
}
