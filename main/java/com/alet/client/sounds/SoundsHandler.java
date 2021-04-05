package com.alet.client.sounds;

import com.alet.ALET;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsHandler {
	
	public static void registerSounds() {
		String[] listOfSounds = { "harp", "banjo", "bdrum", "bell", "bit", "click", "cow_bell", "dbas", "didgeridoo", "flute", "guitar", "icechime", "iron_xylophone", "pling", "sdrum", "xylobone" };
		
		for (String sounds : listOfSounds) {
			registerSound("block.note." + sounds);
			for (int i = 0; i < 5; i++)
				if (i == 0)
					registerSound("block.note." + sounds + "low");
				else
					registerSound("block.note." + sounds + "low" + i);
				
			for (int i = 0; i < 5; i++)
				if (i == 0)
					registerSound("block.note." + sounds + "high");
				else
					registerSound("block.note." + sounds + "high" + i);
				
		}
	}
	
	private static SoundEvent registerSound(String name) {
		ResourceLocation location = new ResourceLocation(ALET.MODID);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
}
