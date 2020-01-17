package com.ltphoto;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.Loader;

import java.awt.Color;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;

import com.MasterForge.DummyStructure;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.blocks.ItemBlockColored;
import com.creativemd.littletiles.common.blocks.ItemBlockFlowingLava;
import com.creativemd.littletiles.common.blocks.ItemBlockFlowingWater;
import com.creativemd.littletiles.common.blocks.ItemBlockTransparentColored;
import com.creativemd.littletiles.common.items.ItemBlockTiles;
import com.creativemd.littletiles.common.items.ItemHammer;
import com.creativemd.littletiles.common.items.ItemPremadeStructure;
import com.creativemd.littletiles.common.structure.premade.LittleStructurePremade;
import com.ltphoto.config.Config;
import com.ltphoto.config.IGCMLoader;
import com.ltphoto.container.SubContainerBlock;
import com.ltphoto.container.SubContainerPhotoImport;
import com.ltphoto.container.SubContainerTypeWriter;
import com.ltphoto.gui.SubGuiBlock;
import com.ltphoto.gui.SubGuiPhotoImport;
import com.ltphoto.gui.SubGuiTypeWriter;
import com.ltphoto.items.TapeMeasure;
import com.ltphoto.server.LTPhotoServer;
import com.ltphoto.structure.premade.LittlePhotoImporter;
import com.ltphoto.structure.premade.LittleTypeWriter;

@Mod(modid = LTPhoto.MODID, name = LTPhoto.NAME, version = LTPhoto.VERSION)
@Mod.EventBusSubscriber
public class LTPhoto
{
	
	@SidedProxy(clientSide = "com.ltphoto.client.LTPhotoClient", serverSide = "com.ltphoto.server.LTPhotoServer")
	public static CommonProxy proxy;
	
    public static final String MODID = "ltphoto";
    public static final String NAME = "LT Photo Converter";
    public static final String VERSION = "1.0";
    
	public static Item tapeMessure;
	
	
    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
    	proxy.preInit(event);
    	
    	tapeMessure = new TapeMeasure("tapemessure");

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
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(tapeMessure);
	}
    
    @EventHandler
    public void Init(FMLInitializationEvent event) {
    	LittleStructurePremade.registerPremadeStructureType("photoimporter", LTPhoto.MODID, LittlePhotoImporter.class);
    	LittleStructurePremade.registerPremadeStructureType("typewriter", LTPhoto.MODID, LittleTypeWriter.class);

    	
    	LittleStructurePremade.registerPremadeStructureType("clayForge_1", LTPhoto.MODID, DummyStructure.class);
    	LittleStructurePremade.registerPremadeStructureType("clayForge_2", LTPhoto.MODID, LittleTypeWriter.class);

    	GameRegistry.addShapedRecipe(new ResourceLocation("craft_photo_importer"), new ResourceLocation("ltphoto"),
    			LittleStructurePremade.getPremadeStack("photoimporter"), new Object[]{
    					"XXX",
    					"XZX",
    					"XYX",
    					'X', Items.IRON_INGOT,
    					'Y', LittleTiles.recipeAdvanced,
    					'Z', Blocks.GLASS_PANE,
    	});
    	
    	GameRegistry.addShapedRecipe(new ResourceLocation("craft_typewriter"), new ResourceLocation("ltphoto"),
    			LittleStructurePremade.getPremadeStack("typewriter"), new Object[]{
    					" X ",
    					"XXX",
    					"XYX",
    					'X', Items.IRON_INGOT,
    					'Y', LittleTiles.recipeAdvanced,
    	});
    	
    	if (Loader.isModLoaded("igcm"))
			IGCMLoader.initIGCM();
    }
    
    @EventHandler
    public void PostInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    }
}
