package com.ltphoto;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
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

import com.MasterForge.MultiTile.MultiTileStructure;
import com.MasterForge.MultiTile.MultiTileStructureRegistry;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.SubGuiRecipe;
import com.creativemd.littletiles.client.gui.SubGuiRecipeAdvancedSelection;
import com.creativemd.littletiles.common.container.SubContainerRecipeAdvanced;
import com.creativemd.littletiles.common.item.ItemRecipeAdvanced;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.type.LittleBed;
import com.ltphoto.config.Config;
import com.ltphoto.config.IGCMLoader;
import com.ltphoto.container.SubContainerBlock;
import com.ltphoto.container.SubContainerPhotoImport;
import com.ltphoto.container.SubContainerTypeWriter;
import com.ltphoto.gui.SubGuiBlock;
import com.ltphoto.gui.SubGuiPhotoImport;
import com.ltphoto.gui.SubGuiTypeWriter;
import com.ltphoto.items.ItemTapeMeasure;
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
    
	public static Item tapeMeasure;
	
	
    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
    	proxy.preInit(event);
    	
    	tapeMeasure = new ItemTapeMeasure("tapemeasure");

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
		event.getRegistry().registerAll(tapeMeasure);
	}
    
    @SubscribeEvent
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(tapeMeasure);
	}
	
	private static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
	}
    
    @EventHandler
    public void Init(FMLInitializationEvent event) {
    	LittleStructurePremade.registerPremadeStructureType("photoimporter", LTPhoto.MODID, LittlePhotoImporter.class);
    	LittleStructurePremade.registerPremadeStructureType("typewriter", LTPhoto.MODID, LittleTypeWriter.class);
    	
    	LittleStructurePremade.registerPremadeStructureType("clayForge_1", LTPhoto.MODID, MultiTileStructure.class);
    	LittleStructurePremade.registerPremadeStructureType("clayForge_2", LTPhoto.MODID, MultiTileStructure.class);
    	LittleStructurePremade.registerPremadeStructureType("clayForge_3", LTPhoto.MODID, MultiTileStructure.class);
    	LittleStructurePremade.registerPremadeStructureType("clayForge_4", LTPhoto.MODID, MultiTileStructure.class);
    	LittleStructurePremade.registerPremadeStructureType("clayForge_5", LTPhoto.MODID, MultiTileStructure.class);
    	LittleStructurePremade.registerPremadeStructureType("clayForge_6", LTPhoto.MODID, MultiTileStructure.class, LittleStructureAttribute.TICKING);
    	//LittleStructurePremade.registerPremadeStructureType("clayForge_6", LTPhoto.MODID, MultiTileStructure.class);

    	//MultiTileStructureRegistry.registerPremadeStructureType("clayForge", LTPhoto.MODID, LittlePhotoImporter.class,6); 
    	MultiTileStructureRegistry.addRecipe("clayForge_1", Items.CLAY_BALL, 8);
    	MultiTileStructureRegistry.addRecipe("clayForge_2", Items.CLAY_BALL, 5);
    	MultiTileStructureRegistry.addRecipe("clayForge_3", Items.CLAY_BALL, 5);
    	MultiTileStructureRegistry.addRecipe("clayForge_4", Items.CLAY_BALL, 5);
    	MultiTileStructureRegistry.addRecipe("clayForge_5", Items.FLINT, 1);

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
