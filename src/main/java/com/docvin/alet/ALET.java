package com.docvin.alet;

import com.ALETConfig;
import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.littletiles.LittleTiles;
import com.docvin.alet.client.eventhandler.SubGuiEventHandler;
import com.docvin.alet.client.registries.ALETSounds;
import com.docvin.alet.common.gui.override.OverrideBluePrintGui;
import com.docvin.alet.common.registries.ALETGuis;
import com.docvin.alet.common.registries.ALETPackets;
import com.docvin.alet.common.registries.ALETStructureTypes;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies = "required-after:creativecore;required-after:littletiles")
@Mod.EventBusSubscriber
public class ALET {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.Instance
    public static ALET instance;

    public static ALETConfig CONFIG;
    public static CreativeTabs littleCircuitTab = new CreativeTabs("assets/alet") {

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
    public void init(FMLInitializationEvent event) {
        CreativeConfigRegistry.ROOT.registerValue(Tags.MOD_ID, CONFIG = new ALETConfig());
        ALETPackets.registerPackets();
        ALETSounds.registerSounds();
        ALETStructureTypes.registerStructureTypes();

    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Beginning to load {}, version {}", Tags.MOD_NAME, Tags.VERSION);
        ALETGuis.registerGuis();
        MinecraftForge.EVENT_BUS.register(new SubGuiEventHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        new OverrideBluePrintGui();

    }

}
