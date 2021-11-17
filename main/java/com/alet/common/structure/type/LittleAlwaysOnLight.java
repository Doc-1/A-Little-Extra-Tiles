package com.alet.common.structure.type;

import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiSteppedSlider;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleAlwaysOnLight extends LittleStructure {
	
	public int level;
	
	public LittleAlwaysOnLight(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		level = nbt.getInteger("level");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setInteger("level", level);
	}
	
	@Override
	public int getLightValue(BlockPos pos) {
		return level;
	}
	
	@Override
	public int getAttribute() {
		return super.getAttribute() | LittleStructureAttribute.EMISSIVE;
	}
	
	public static class LittleAlwaysOnLightStructureParser extends LittleStructureGuiParser {
		
		public LittleAlwaysOnLightStructureParser(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void createControls(LittlePreviews previews, LittleStructure structure) {
			parent.addControl(new GuiSteppedSlider("level", 0, 0, 100, 12, structure instanceof LittleAlwaysOnLight ? ((LittleAlwaysOnLight) structure).level : 15, 0, 15));
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public LittleAlwaysOnLight parseStructure(LittlePreviews previews) {
			LittleAlwaysOnLight structure = createStructure(LittleAlwaysOnLight.class, null);
			GuiSteppedSlider slider = (GuiSteppedSlider) parent.get("level");
			structure.level = (int) slider.value;
			return structure;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleAlwaysOnLight.class);
		}
	}
	
}
