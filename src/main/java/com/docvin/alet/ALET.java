package com.docvin.alet;

import com.creativemd.littletiles.LittleTiles;
import com.docvin.alet.client.eventhandler.SubGuiEventHandler;
import com.docvin.alet.common.gui.override.SubGuiBluePrintOverride;
import com.docvin.alet.common.registries.ALETGuis;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class ALET {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.Instance
    public static ALET instance;

    public static CreativeTabs littleCircuitTab = new CreativeTabs("alet") {

        @Override
        @MethodsReturnNonnullByDefault
        public ItemStack createIcon() {
            return new ItemStack(LittleTiles.wrench);
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }
    }.setBackgroundImageName("item_search.png");


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Beginning to load {}, version {}", Tags.MOD_NAME, Tags.VERSION);
        ALETGuis.registerGuis();
        MinecraftForge.EVENT_BUS.register(new SubGuiEventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        new SubGuiBluePrintOverride();

    }

}
