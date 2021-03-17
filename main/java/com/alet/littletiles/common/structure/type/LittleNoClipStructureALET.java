package com.alet.littletiles.common.structure.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiAnalogeSlider;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleNoClipStructureALET extends LittleStructure {
	
	public HashSet<Entity> entities = new HashSet<>();
	
	public static List<DamageSource> sourceOfDmg = new ArrayList<DamageSource>();
	static {
		if (sourceOfDmg.isEmpty()) {
			sourceOfDmg.add(DamageSource.ANVIL);
			sourceOfDmg.add(DamageSource.CACTUS);
			sourceOfDmg.add(DamageSource.CRAMMING);
			sourceOfDmg.add(DamageSource.DRAGON_BREATH);
			sourceOfDmg.add(DamageSource.DROWN);
			sourceOfDmg.add(DamageSource.FALL);
			sourceOfDmg.add(DamageSource.FALLING_BLOCK);
			sourceOfDmg.add(DamageSource.FIREWORKS);
			sourceOfDmg.add(DamageSource.FLY_INTO_WALL);
			sourceOfDmg.add(DamageSource.GENERIC);
			sourceOfDmg.add(DamageSource.HOT_FLOOR);
			sourceOfDmg.add(DamageSource.IN_FIRE);
			sourceOfDmg.add(DamageSource.IN_WALL);
			sourceOfDmg.add(DamageSource.LAVA);
			sourceOfDmg.add(DamageSource.LIGHTNING_BOLT);
			sourceOfDmg.add(DamageSource.MAGIC);
			sourceOfDmg.add(DamageSource.ON_FIRE);
			sourceOfDmg.add(DamageSource.OUT_OF_WORLD);
			sourceOfDmg.add(DamageSource.STARVE);
			sourceOfDmg.add(DamageSource.WITHER);
		}
	}
	
	public boolean web = false;
	public boolean damage = false;
	public boolean motion = false;
	
	public float damageAmount = 0;
	public String damageType = "";
	public int damagePerTick = 0;
	private static int tick = 0;
	
	public double xMotionStrength = 0;
	public double yMotionStrength = 0;
	public double zMotionStrength = 0;
	public double forward = 0;
	
	public LittleNoClipStructureALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		damage = nbt.getBoolean("damage");
		motion = nbt.getBoolean("motion");
		
		damageAmount = nbt.getFloat("dmgAmount");
		damageType = nbt.getString("dmgType");
		damagePerTick = nbt.getInteger("dmgPerTick");
		
		xMotionStrength = nbt.getDouble("xStr");
		yMotionStrength = nbt.getDouble("yStr");
		zMotionStrength = nbt.getDouble("zStr");
		
		forward = nbt.getDouble("forward");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		nbt.setBoolean("damage", damage);
		nbt.setBoolean("motion", motion);
		
		nbt.setFloat("dmgAmount", damageAmount);
		nbt.setString("dmgType", damageType);
		nbt.setInteger("dmgPerTick", damagePerTick);
		
		nbt.setDouble("xStr", xMotionStrength);
		nbt.setDouble("yStr", yMotionStrength);
		nbt.setDouble("zStr", zMotionStrength);
		
		nbt.setDouble("forward", forward);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, IParentTileList parent, BlockPos pos, Entity entityIn) {
		if (worldIn.isRemote)
			return;
		
		boolean intersected = false;
		for (LittleTile tile : parent) {
			if (tile.getBox().getBox(parent.getContext(), pos).intersects(entityIn.getEntityBoundingBox())) {
				intersected = true;
				break;
			}
		}
		if (intersected)
			entities.add(entityIn);
		
		queueForNextTick();
	}
	
	private void tickWhileCollided() {
		if (motion)
			for (Entity entity : entities) {
				
				entity.velocityChanged = true;
				if (!entity.isSneaking()) {
					
					double totalX = 0;
					double totalY = yMotionStrength;
					double totalZ = 0;
					float rotationYawA = entity.rotationYaw;
					float rotationYawB = entity.rotationYaw;
					
					double x1 = -MathHelper.sin((entity.rotationYaw) / 180.0F * (float) Math.PI) * forward;
					double z1 = MathHelper.cos((entity.rotationYaw) / 180.0F * (float) Math.PI) * forward;
					double y1 = -MathHelper.sin((entity.rotationPitch) / 180.0F * (float) Math.PI) * 0.5;
					
					double x2 = -MathHelper.sin((270) / 180.0F * (float) Math.PI) * xMotionStrength;
					double z2 = MathHelper.cos((270) / 180.0F * (float) Math.PI) * xMotionStrength;
					
					double x3 = -MathHelper.sin((360) / 180.0F * (float) Math.PI) * zMotionStrength;
					double z3 = MathHelper.cos((360) / 180.0F * (float) Math.PI) * zMotionStrength;
					
					totalX = (forward != 0) ? x1 + x2 + x3 : x2 + x3;
					totalZ = (forward != 0) ? z1 + z2 + z3 : z2 + z3;
					totalY += (forward != 0) ? y1 : 0;
					System.out.println(x1 + " " + x2 + " " + z1 + " " + z2);
					entity.motionX = totalX;
					entity.motionY = totalY;
					entity.motionZ = totalZ;
				} else {
					entity.motionX = 0;
					entity.motionY = 0;
					entity.motionZ = 0;
				}
			}
		if (damage && tick >= damagePerTick) {
			DamageSource damageSource = DamageSource.GENERIC;
			for (DamageSource source : sourceOfDmg) {
				if (damageType.equals(source.getDamageType())) {
					damageSource = source;
					break;
				}
			}
			for (Entity entity : entities) {
				entity.attackEntityFrom(damageSource, damageAmount);
			}
			System.out.println("damage");
			tick = 0;
		}
		tick++;
	}
	
	@Override
	public boolean queueTick() {
		int players = 0;
		for (Entity entity : entities)
			if (entity instanceof EntityPlayer)
				players++;
		getInput(0).updateState(BooleanUtils.toBits(players, 4));
		getInput(1).updateState(BooleanUtils.toBits(entities.size(), 4));
		boolean wasEmpty = entities.isEmpty();
		
		tickWhileCollided();
		entities.clear();
		
		if (wasEmpty) {
			tick = 0;
		}
		return !wasEmpty;
	}
	
	public static class LittleNoClipStructureParser extends LittleStructureGuiParser {
		
		public LittleNoClipStructureParser(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void createControls(LittlePreviews previews, LittleStructure structure) {
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			
			double xStrength = 0;
			double yStrength = 0;
			double zStrength = 0;
			double forward = 0;
			boolean push = false;
			
			boolean hurt = false;
			String source = "";
			float dmgAmount = 0;
			int dmgPerTick = 0;
			
			List<String> sourceList = new ArrayList<String>();
			for (DamageSource forEachSource : ((LittleNoClipStructureALET) structure).sourceOfDmg) {
				sourceList.add(forEachSource.damageType);
			}
			if (structure instanceof LittleNoClipStructureALET) {
				push = ((LittleNoClipStructureALET) structure).motion;
				xStrength = ((LittleNoClipStructureALET) structure).xMotionStrength;
				yStrength = ((LittleNoClipStructureALET) structure).yMotionStrength;
				zStrength = ((LittleNoClipStructureALET) structure).zMotionStrength;
				forward = ((LittleNoClipStructureALET) structure).forward;
				
				hurt = ((LittleNoClipStructureALET) structure).damage;
				source = ((LittleNoClipStructureALET) structure).damageType;
				dmgAmount = ((LittleNoClipStructureALET) structure).damageAmount;
				dmgPerTick = ((LittleNoClipStructureALET) structure).damagePerTick;
			}
			
			parent.controls.add(new GuiCheckBox("motion", "push entity", 3, 1, push));
			parent.controls.add(new GuiAnalogeSlider("forward", 144, 1, 48, 10, forward, -5, 5));
			parent.controls.add(new GuiAnalogeSlider("xStr", 11, 18, 48, 10, xStrength, -5, 5));
			parent.controls.add(new GuiAnalogeSlider("yStr", 77, 18, 48, 10, yStrength, -5, 5));
			parent.controls.add(new GuiAnalogeSlider("zStr", 144, 18, 48, 10, zStrength, -5, 5));
			parent.controls.add(new GuiLabel("ford", "Forward:", 95, 2));
			parent.controls.add(new GuiLabel("x", "X:", -1, 19));
			parent.controls.add(new GuiLabel("y", "Y:", 65, 19));
			parent.controls.add(new GuiLabel("z", "Z:", 132, 19));
			parent.controls.add(new GuiPanel("box", 0, 0, 194, 30));
			
			parent.controls.add(new GuiCheckBox("hurt", "hurt entity", 3, 40, hurt));
			parent.controls.add(new GuiLabel("Damage Type:", 3, 57));
			parent.controls.add(new GuiComboBox("sources", 75, 55, 55, sourceList));
			((GuiComboBox) parent.get("sources")).select(source);
			parent.controls.add(new GuiLabel("For Every Tick:", 3, 78));
			parent.controls.add(new GuiTextfield("dmgPerTick", dmgPerTick + "", 90, 76, 40, 14).setNumbersOnly());
			
			parent.controls.add(new GuiLabel("Total Damage:", 3, 99));
			parent.controls.add(new GuiAnalogeSlider("dmgAmount", 74, 97, 56, 14, dmgAmount, 0, 20));
			
			parent.controls.add(new GuiPanel("box2", 0, 40, 194, 74));
			
			if (!player.isCreative()) {
				for (GuiControl control : parent.controls) {
					control.enabled = false;
				}
				parent.controls.add(new GuiTextBox("message", "These settings are only avalible in creative mode", 140, 45, 50));
			}
			createCreativeControls(structure);
			
		}
		
		private void createCreativeControls(LittleStructure structure) {
			
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public LittleNoClipStructureALET parseStructure(LittlePreviews previews) {
			LittleNoClipStructureALET structure = createStructure(LittleNoClipStructureALET.class, null);
			if (parent.getPlayer().isCreative()) {
				structure.damage = ((GuiCheckBox) parent.get("hurt")).value;
				structure.damageType = ((GuiComboBox) parent.get("sources")).getCaption();
				structure.damagePerTick = Integer.parseInt(((GuiTextfield) parent.get("dmgPerTick")).text);
				structure.damageAmount = (float) ((GuiAnalogeSlider) parent.get("dmgAmount")).value;
				structure.motion = ((GuiCheckBox) parent.get("motion")).value;
				structure.xMotionStrength = ((GuiAnalogeSlider) parent.get("xStr")).value;
				structure.yMotionStrength = ((GuiAnalogeSlider) parent.get("yStr")).value;
				structure.zMotionStrength = ((GuiAnalogeSlider) parent.get("zStr")).value;
				structure.forward = ((GuiAnalogeSlider) parent.get("forward")).value;
			}
			return structure;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleNoClipStructureALET.class);
		}
	}
	
}

/*
 * double totalX = 0;
				double totalY = yMotionStrength;
				double totalZ = 0;
				
				entity.velocityChanged = true;
				double x1 = -MathHelper.sin((entity.rotationYaw + 90) / 180.0F * (float) Math.PI) * zMotionStrength;
				double z1 = MathHelper.cos((entity.rotationYaw + 90) / 180.0F * (float) Math.PI) * zMotionStrength;
				
				double x2 = -MathHelper.sin((entity.rotationYaw) / 180.0F * (float) Math.PI) * xMotionStrength;
				double z2 = MathHelper.cos((entity.rotationYaw) / 180.0F * (float) Math.PI) * xMotionStrength;
				
				if (zMotionStrength != 0 && xMotionStrength == 0) {
					totalX = x1;
					totalZ = z1;
				} else if (zMotionStrength == 0 && xMotionStrength != 0) {
					totalX = x2;
					totalZ = z2;
				} else if (zMotionStrength != 0 && xMotionStrength != 0) {
					totalX = x1 + x2;
					totalZ = z1 + z2;
				} else {
					totalX = entity.motionX;
					totalZ = entity.motionZ;
				}
				
				//double x = entity.motionX + xMotionStrength;
				//double y = entity.motionY + yMotionStrength;
				//totalZ = entity.motionZ + zMotionStrength;
				
				if (entity.motionX > xMotionStrength)
					totalX = xMotionStrength;
				if (entity.motionY > yMotionStrength)
					totalY = yMotionStrength;
				if (entity.motionZ > zMotionStrength)
					totalZ = zMotionStrength;
				
				entity.setVelocity(totalX, totalY, totalZ);
				entity.moveRelative(0.1F, 0.5F, 0F, 0.1F);
				
				System.out.println(entity.motionZ);
 */
