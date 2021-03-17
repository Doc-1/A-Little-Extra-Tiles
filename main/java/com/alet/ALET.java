package com.alet;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alet.blocks.BasicBlock;
import com.alet.client.ALETClient;
import com.alet.common.packet.PacketUpdateNBT;
import com.alet.container.SubContainerBlock;
import com.alet.container.SubContainerPhotoImport;
import com.alet.container.SubContainerTypeWriter;
import com.alet.gui.SubGuiBlock;
import com.alet.gui.SubGuiPhotoImport;
import com.alet.gui.SubGuiTypeWriter;
import com.alet.items.ItemJumpTool;
import com.alet.items.ItemTapeMeasure;
import com.alet.littletiles.common.structure.type.LittleFixedActivatorStructureALET;
import com.alet.littletiles.common.structure.type.LittleFixedActivatorStructureALET.LittleFixedActivatorStructureParserALET;
import com.alet.littletiles.common.structure.type.LittleNoClipStructureALET;
import com.alet.littletiles.items.ItemLittleChiselAlet;
import com.alet.littletiles.items.ItemLittleGrabberAlet;
import com.alet.littletiles.items.ItemLittlePaintBrushALET;
import com.alet.structure.premade.ItemStructurePremade;
import com.alet.structure.premade.LittlePhotoImporter;
import com.alet.structure.premade.LittleTypeWriter;
import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.api.ILittleTile;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.type.LittleStorage.LittleStorageType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.server.LittleTilesServer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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

@Mod(modid = ALET.MODID, name = ALET.NAME, version = ALET.VERSION, guiFactory = "com.alet.client.ALETSettings", dependencies = "required-after:creativecore;required-after:littletiles")
@Mod.EventBusSubscriber
public class ALET {
	
	@SidedProxy(clientSide = "com.alet.client.ALETClient", serverSide = "com.alet.server.ALETServer")
	public static LittleTilesServer proxy;
	
	public static final String MODID = "alet";
	public static final String NAME = "A Little Extra Tiles";
	public static final String VERSION = "1.0";
	
	public static ALETConfig CONFIG;
	
	public static List<String> fontTypeNames;
	
	public static Item tapeMeasure;
	public static Item jumpRod;
	
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
		
		LittleTiles.chisel = new ItemLittleChiselAlet().setUnlocalizedName("LTChisel").setRegistryName("littletiles", "chisel");
		LittleTiles.colorTube = new ItemLittlePaintBrushALET().setUnlocalizedName("LTColorTube").setRegistryName("littletiles", "colorTube");
		LittleTiles.grabber = new ItemLittleGrabberAlet().setUnlocalizedName("LTGrabber").setRegistryName("littletiles", "grabber");
		
		jumpRod = new ItemJumpTool("jumprod");
		tapeMeasure = new ItemTapeMeasure("tapemeasure");
		smoothOakPlank = new BasicBlock("smoothoakplank");
		smoothDarkOakPlank = new BasicBlock("smoothdarkoakplank");
		smoothSprucePlank = new BasicBlock("smoothspruceplank");
		smoothJunglePlank = new BasicBlock("smoothjungleplank");
		smoothBirchPlank = new BasicBlock("smoothbirchplank");
		smoothAcaciaPlank = new BasicBlock("smoothacaciaplank");
		
		smoothBrick = new BasicBlock("smoothbrick");
		smoothGroutBrick = new BasicBlock("smoothgroutbrick");
		if (event.getSide().equals(Side.CLIENT)) {
			ALETClient.addItemToRenderTiles(jumpRod);
		}
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
		
		GuiHandler.registerGuiHandler("photo-import", new LittleStructureGuiHandler() {
			
			@Override
			@SideOnly(Side.CLIENT)
			public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
				return new SubGuiPhotoImport();
			}
			
			@Override
			public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
				return new SubContainerPhotoImport(player);
			}
		});
		
		GuiHandler.registerGuiHandler("type-writter", new LittleStructureGuiHandler() {
			
			@Override
			@SideOnly(Side.CLIENT)
			public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
				return new SubGuiTypeWriter();
			}
			
			@Override
			public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
				return new SubContainerTypeWriter(player);
			}
		});
		
		GuiHandler.registerGuiHandler("grabberAlet", new CustomGuiHandler() {
			
			@Override
			@SideOnly(Side.CLIENT)
			public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
				ItemStack stack = player.getHeldItemMainhand();
				return ItemLittleGrabberAlet.getMode(stack).getGui(player, stack, ((ILittleTile) stack.getItem()).getPositionContext(stack));
			}
			
			@Override
			public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
				ItemStack stack = player.getHeldItemMainhand();
				return ItemLittleGrabberAlet.getMode(stack).getContainer(player, stack);
			}
		});
		LittleStructureRegistry.registerStructureType("advanced_noclip", "simple", LittleNoClipStructureALET.class, LittleStructureAttribute.NOCOLLISION | LittleStructureAttribute.COLLISION_LISTENER, LittleNoClipStructureALET.LittleNoClipStructureParser.class).addInput("players", 4).addInput("entities", 4);
		//LittleStructureRegistry.registerStructureType("test", "simple", LittleFixedActivatorStructureALET.class, LittleStructureAttribute.NONE, LittleFixedActivatorStructureALET.LittleFixedActivatorStructureParserALET.class);
		LittleStructureRegistry.registerStructureType(new LittleStorageType("adv_storage", "simple", LittleFixedActivatorStructureALET.class, LittleStructureAttribute.NONE).addInput("accessed", 1).addInput("filled", 16), LittleFixedActivatorStructureParserALET.class);
		proxy.loadSidePre();
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(smoothGroutBrick, smoothBrick, smoothOakPlank, smoothDarkOakPlank, smoothAcaciaPlank, smoothSprucePlank, smoothJunglePlank, smoothBirchPlank);
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(jumpRod, tapeMeasure, new ItemBlock(smoothGroutBrick).setRegistryName(smoothGroutBrick.getRegistryName()), new ItemBlock(smoothBrick).setRegistryName(smoothBrick.getRegistryName()), new ItemBlock(smoothOakPlank).setRegistryName(smoothOakPlank.getRegistryName()), new ItemBlock(smoothDarkOakPlank).setRegistryName(smoothDarkOakPlank.getRegistryName()), new ItemBlock(smoothAcaciaPlank).setRegistryName(smoothAcaciaPlank.getRegistryName()), new ItemBlock(smoothSprucePlank).setRegistryName(smoothSprucePlank.getRegistryName()), new ItemBlock(smoothJunglePlank).setRegistryName(smoothJunglePlank.getRegistryName()), new ItemBlock(smoothBirchPlank).setRegistryName(smoothBirchPlank.getRegistryName()));
		proxy.loadSide();
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
		CreativeCorePacket.registerPacket(PacketUpdateNBT.class);
		CreativeConfigRegistry.ROOT.registerValue(MODID, CONFIG = new ALETConfig());
		
		LittleStructurePremade.registerPremadeStructureType("photoimporter", ALET.MODID, LittlePhotoImporter.class);
		LittleStructurePremade.registerPremadeStructureType("typewriter", ALET.MODID, LittleTypeWriter.class);
		LittleStructurePremade.registerPremadeStructureType("jump_rod", ALET.MODID, ItemStructurePremade.class);
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event) {
		proxy.loadSidePost();
	}
	
	public static List<String> getFonts() {
		fontTypeNames = new ArrayList<>();
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		try {
			File d = new File("./fonts");
			if (!d.exists()) {
				d.mkdir();
				try {
					String data = "Place any TrueTypeFont files in this folder to add them to the typewritter. \n" + "If you added any TrueTypeFont files while still in Minecraft you can run the commmand \n" + "/alet-updatefont to add the new fonts. Otherwise just launching Minecraft will gather \n" + "the new files.";
					File f1 = new File("./fonts/README.txt");
					if (!f1.exists())
						f1.createNewFile();
					
					BufferedWriter writer = new BufferedWriter(new FileWriter("./fonts/README.txt"));
					writer.write(data);
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			for (File file : d.listFiles()) {
				String fileName = file.getName();
				if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
					if (fileName.substring(fileName.lastIndexOf(".") + 1).equals("ttf") || fileName.substring(fileName.lastIndexOf(".") + 1).equals("otf"))
						ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, file));
			}
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		
		for (int i = 0; i < fonts.length; i++) {
			fontTypeNames.add(fonts[i]);
		}
		return fontTypeNames;
	}
	
}
