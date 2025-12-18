package com.docvin.alet.common.registries;

import com.docvin.alet.ALET;
import com.docvin.alet.components.items.ItemLittleManual;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Objects;

@Mod.EventBusSubscriber
public class ALETItems {
    public static final Item littleManual = new ItemLittleManual("little_manual");
    private static HashMap<String, Item> items = new HashMap<>();

    static {
        ALET.LOGGER.info("Registering :");
        addItem(littleManual);
    }

    public static void addItem(Item item) {
        String key = "";

        try {
            Objects.requireNonNull(item);
        } catch (NullPointerException e) {
            ALET.LOGGER.error("The item trying to be registered is null!", e);
        }

        try {
            key = Objects.requireNonNull(item.getRegistryName()).getPath();
        } catch (NullPointerException e) {
            ALET.LOGGER.error("Something went wrong with naming the item's registry name!", e);
        }

        if (!key.isEmpty()) {
            if (!items.containsKey(key)) {
                items.put(key, item);
            } else
                ALET.LOGGER.warn("An item with the name {} already has been added!", key);
        } else
            ALET.LOGGER.warn("The item was not named!");

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        items.forEach((key, item) -> {
            event.getRegistry().register(item);
            ALET.LOGGER.info("{} was registered", key);
        });
        items.clear();
        items = null;
    }
}
