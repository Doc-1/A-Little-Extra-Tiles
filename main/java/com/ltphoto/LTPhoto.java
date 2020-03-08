package com.ltphoto;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;

import com.alet.blocks.BasicBlock;
import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.common.structure.premade.LittleStructurePremade;
import com.ltphoto.container.SubContainerBlock;
import com.ltphoto.container.SubContainerPhotoImport;
import com.ltphoto.container.SubContainerTypeWriter;
import com.ltphoto.gui.SubGuiBlock;
import com.ltphoto.gui.SubGuiPhotoImport;
import com.ltphoto.gui.SubGuiTypeWriter;
import com.ltphoto.items.ItemTapeMeasure;
import com.ltphoto.structure.premade.LittlePhotoImporter;
import com.ltphoto.structure.premade.LittleTypeWriter;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = LTPhoto.MODID, name = LTPhoto.NAME, version = LTPhoto.VERSION)
@Mod.EventBusSubscriber
public class LTPhoto {
	
	@SidedProxy(clientSide = "com.ltphoto.client.LTPhotoClient", serverSide = "com.ltphoto.server.LTPhotoServer")
	public static CommonProxy proxy;
	
	public static final String MODID = "ltphoto";
	public static final String NAME = "LT Photo Converter";
	public static final String VERSION = "1.0";
	
	public static LTPhotoConfig CONFIG;
	
	public static List<String> fontTypeNames;
	
	public static Item tapeMeasure;
	public static BasicBlock smoothOakPlank;
	public static BasicBlock smoothDarkOakPlank;
	public static BasicBlock smoothSprucePlank;
	public static BasicBlock smoothJunglePlank;
	public static BasicBlock smoothBirchPlank;
	public static BasicBlock smoothAcaciaPlank;
	
	public static BasicBlock smoothBrick;
	public static BasicBlock smoothGroutBrick;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		
		tapeMeasure = new ItemTapeMeasure("tapemeasure");
		smoothOakPlank = new BasicBlock("smoothoakplank");
		smoothDarkOakPlank = new BasicBlock("smoothdarkoakplank");
		smoothSprucePlank = new BasicBlock("smoothspruceplank");
		smoothJunglePlank = new BasicBlock("smoothjungleplank");
		smoothBirchPlank = new BasicBlock("smoothbirchplank");
		smoothAcaciaPlank = new BasicBlock("smoothacaciaplank");
		
		smoothBrick = new BasicBlock("smoothbrick");
		smoothGroutBrick = new BasicBlock("smoothgroutbrick");
		
		getFonts();
		GuiHandler.registerGuiHandler("block", new CustomGuiHandler() {
			
			@Override
			@SideOnly(Side.CLIENT)
			public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
				return new SubGuiBlock();
			}
			
			@Override
			public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
				return new SubContainerBlock(player);
			}
		});
		
		GuiHandler.registerGuiHandler("photo-import", new CustomGuiHandler() {
			
			@Override
			@SideOnly(Side.CLIENT)
			public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
				return new SubGuiPhotoImport();
			}
			
			@Override
			public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
				return new SubContainerPhotoImport(player);
			}
		});
		
		GuiHandler.registerGuiHandler("type-writter", new CustomGuiHandler() {
			
			@Override
			@SideOnly(Side.CLIENT)
			public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
				return new SubGuiTypeWriter();
			}
			
			@Override
			public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
				return new SubContainerTypeWriter(player);
			}
		});
		
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(smoothGroutBrick, smoothBrick, smoothOakPlank, smoothDarkOakPlank, smoothAcaciaPlank, smoothSprucePlank, smoothJunglePlank, smoothBirchPlank);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(tapeMeasure, new ItemBlock(smoothGroutBrick).setRegistryName(smoothGroutBrick.getRegistryName()), new ItemBlock(smoothBrick).setRegistryName(smoothBrick.getRegistryName()), new ItemBlock(smoothOakPlank).setRegistryName(smoothOakPlank.getRegistryName()), new ItemBlock(smoothDarkOakPlank).setRegistryName(smoothDarkOakPlank.getRegistryName()), new ItemBlock(smoothAcaciaPlank).setRegistryName(smoothAcaciaPlank.getRegistryName()), new ItemBlock(smoothSprucePlank).setRegistryName(smoothSprucePlank.getRegistryName()), new ItemBlock(smoothJunglePlank).setRegistryName(smoothJunglePlank.getRegistryName()), new ItemBlock(smoothBirchPlank).setRegistryName(smoothBirchPlank.getRegistryName()));
	}
	
	@SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(tapeMeasure);
		registerRender(Item.getItemFromBlock(smoothGroutBrick));
		registerRender(Item.getItemFromBlock(smoothBrick));
		registerRender(Item.getItemFromBlock(smoothOakPlank));
		registerRender(Item.getItemFromBlock(smoothDarkOakPlank));
		registerRender(Item.getItemFromBlock(smoothAcaciaPlank));
		registerRender(Item.getItemFromBlock(smoothSprucePlank));
		registerRender(Item.getItemFromBlock(smoothJunglePlank));
		registerRender(Item.getItemFromBlock(smoothBirchPlank));
	}
	
	public static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event) {
		CreativeConfigRegistry.ROOT.registerValue(MODID, CONFIG = new LTPhotoConfig());
		
		LittleStructurePremade.registerPremadeStructureType("photoimporter", LTPhoto.MODID, LittlePhotoImporter.class);
		LittleStructurePremade.registerPremadeStructureType("typewriter", LTPhoto.MODID, LittleTypeWriter.class);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
	
	public static List<String> getFonts() {
		fontTypeNames = new ArrayList<>();
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = 0; i < fonts.length; i++) {
			fontTypeNames.add(fonts[i]);
		}
		return fontTypeNames;
	}
	
}
