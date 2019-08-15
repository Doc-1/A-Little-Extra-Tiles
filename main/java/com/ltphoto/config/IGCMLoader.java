package com.ltphoto.config;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.ConfigTab;
import com.creativemd.igcm.api.segments.BooleanSegment;
import com.creativemd.igcm.api.segments.FloatSegment;
import com.creativemd.igcm.api.segments.IntegerSegment;
import com.creativemd.igcm.api.segments.IntegerSliderSegment;
import com.creativemd.littletiles.LittleTiles;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;

public class IGCMLoader {
	
	public static void initIGCM() {
		ConfigTab.root.registerElement("ltphoto", new ConfigBranch("LTPhoto", new ItemStack(LittleTiles.screwdriver)) {
			
			@Override
			public void saveExtra(NBTTagCompound nbt) {
				
			}
			
			@Override
			public void loadExtra(NBTTagCompound nbt) {
				
			}
			
			@Override
			public boolean requiresSynchronization() {
				return true;
			}
			
			@Override
			public void onRecieveFrom(Side side) {
				Config.allowURL = (Boolean) getValue("allowURL");
				Config.maxPixelAmount = (Integer) getValue("maxPixelAmount");
				Config.maxPixelText = (Integer) getValue("maxPixelText");
				Config.colorAccuracy = (Integer) getValue("colorAccuracy");
				
			}

			@Override
			public void createChildren() {
				registerElement("allowURL", new BooleanSegment("Allow printing from URLs", true).setToolTip("If disabled users cannot print via a URL link."));
				registerElement("maxPixelAmount", new IntegerSegment("Max pixels for photo", 9604, 1, Integer.MAX_VALUE).setToolTip("This will set the max number of pixels a photo can have when printing."));
				registerElement("maxPixelText", new IntegerSegment("Max pixels for text", 250000, 1, Integer.MAX_VALUE).setToolTip("This will set the max number of pixels a text can have when printing."));
				registerElement("colorAccuracy", new IntegerSegment("Color Accuracy", 0, 0, 239).setToolTip("This will change how many colors the printer has access to. Zero is all avalible colors."));
			}
				
		});
	}
}
