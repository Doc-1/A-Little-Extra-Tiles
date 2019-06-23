package com.ltphoto;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.common.items.ItemPremadeStructure;
import com.creativemd.littletiles.common.structure.premade.LittleStructurePremade;
import com.ltphoto.container.SubContainerPhotoImport;
import com.ltphoto.gui.SubGuiPhotoImport;
import com.ltphoto.structure.premade.LittlePhotoImporter;

@Mod(modid = LTPhoto.MODID, name = LTPhoto.NAME, version = LTPhoto.VERSION)
public class LTPhoto
{
    public static final String MODID = "ltphoto";
    public static final String NAME = "LT Photo Converter";
    public static final String VERSION = "1.0";
    
    public static CreativeTabs lTPhotoTab = new CreativeTabs("littletiles") {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(LittleTiles.chisel);
		}
	};
	
	public static Item premade;
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	GuiHandler.registerGuiHandler("lt-import", new CustomGuiHandler() {
			
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

		premade = new ItemPremadeStructure().setUnlocalizedName("LTPremade").setRegistryName("premade");
		
    	LittleStructurePremade.registerPremadeStructureType("photoimporter", LTPhoto.MODID, LittlePhotoImporter.class);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
    }
}
