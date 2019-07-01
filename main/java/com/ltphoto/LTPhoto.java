package com.ltphoto;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.items.ItemPremadeStructure;
import com.creativemd.littletiles.common.structure.premade.LittleStructurePremade;
import com.ltphoto.config.Config;
import com.ltphoto.container.SubContainerBlock;
import com.ltphoto.container.SubContainerPhotoImport;
import com.ltphoto.gui.SubGuiBlock;
import com.ltphoto.gui.SubGuiPhotoImport;
import com.ltphoto.server.LTPhotoServer;
import com.ltphoto.structure.premade.LittlePhotoImporter;

@Mod(modid = LTPhoto.MODID, name = LTPhoto.NAME, version = LTPhoto.VERSION)
public class LTPhoto
{
	
	@SidedProxy(clientSide = "com.ltphoto.client.LTPhotoClient", serverSide = "com.ltphoto.server.LTPhotoServer")
	public static LTPhotoServer proxy;
	
    public static final String MODID = "ltphoto";
    public static final String NAME = "LT Photo Converter";
    public static final String VERSION = "1.0";
    
	
    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
    	Config.checkConfigFile();
    	GuiHandler.registerGuiHandler("test", new CustomGuiHandler() {
			
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

    }
    
    
    @EventHandler
    public void Init(FMLInitializationEvent event) {
    	LittleStructurePremade.registerPremadeStructureType("photoimporter", LTPhoto.MODID, LittlePhotoImporter.class);

    	GameRegistry.addShapedRecipe(new ResourceLocation("craft_photo_importer"), new ResourceLocation("ltphoto"),
    			LittleStructurePremade.getPremadeStack("photoimporter"), new Object[]{
    					"XZX",
    					"XYX",
    					"XXX",
    					'X', Items.IRON_INGOT,
    					'Y', LittleTiles.recipeAdvanced,
    					'Z', Items.PAPER
    	});
    }
}
