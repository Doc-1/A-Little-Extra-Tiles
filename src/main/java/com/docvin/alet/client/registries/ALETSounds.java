package com.docvin.alet.client.registries;

import com.docvin.alet.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ALETSounds {
    public static void registerSounds() {
        String[] listOfSounds = {"harp", "banjo", "bdrum", "bell", "bit", "click", "cow_bell", "dbas", "didgeridoo", "flute", "guitar", "icechime", "iron_xylophone", "pling", "sdrum", "xylobone", "donk4rmas_piano"};

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

    private static void registerSound(String name) {
        ResourceLocation location = new ResourceLocation(Tags.MOD_ID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(location);
        ForgeRegistries.SOUND_EVENTS.register(event);
    }
}