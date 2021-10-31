package com.alet.common.structure.type.trigger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerBoxStructureALET extends LittleStructure {
	
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
	
	private static int tick = 0;
	public boolean listen = true;
	public List<LittleTriggerEvent> triggers = new ArrayList<LittleTriggerEvent>();
	
	public LittleTriggerBoxStructureALET(LittleStructureType type, IStructureTileList mainBlock) {
		super(type, mainBlock);
	}
	
	@Override
	protected void loadFromNBTExtra(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("triggers", Constants.NBT.TAG_COMPOUND);
		//System.out.println(nbt);
		int i = 0;
		for (NBTBase base : list) {
			if (base instanceof NBTTagCompound) {
				NBTTagCompound n = (NBTTagCompound) base;
				triggers.add(LittleTriggerEvent.createFromNBT((NBTTagCompound) n.getTag(i + "")));
				i++;
			}
			
		}
		listen = nbt.getBoolean("listening");
	}
	
	@Override
	protected void writeToNBTExtra(NBTTagCompound nbt) {
		int i = 0;
		NBTTagList list = new NBTTagList();
		for (LittleTriggerEvent trigger : triggers) {
			NBTTagCompound n = new NBTTagCompound();
			n.setTag(i + "", trigger.createNBT());
			list.appendTag(n);
			
			i++;
		}
		nbt.setTag("triggers", list);
		nbt.setBoolean("listening", listen);
	}
	
	@Override
	public void performInternalOutputChange(InternalSignalOutput output) {
		if (output.component.is("listen")) {
			listen = !listen;
		}
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, IParentTileList parent, BlockPos pos, Entity entityIn) {
		if (worldIn.isRemote)
			return;
		
		//System.out.println(worldIn.getMinecraftServer().getCommandManager().executeCommand(entityIn, "say h"));
		boolean intersected = false;
		if (listen) {
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
	}
	
	private void tickWhileCollided() {
		for (LittleTriggerEvent trig : triggers) {
			switch (trig.name) {
			case "Modify Motion":
				LittleTriggerModifyMotion motion = (LittleTriggerModifyMotion) trig;
				for (Entity entity : entities) {
					
					entity.velocityChanged = true;
					
					double totalX = 0;
					double totalY = motion.yStrength;
					double totalZ = 0;
					float rotationYawA = entity.rotationYaw;
					float rotationYawB = entity.rotationYaw;
					
					double x1 = -MathHelper.sin((entity.rotationYaw) / 180.0F * (float) Math.PI) * motion.forward;
					double z1 = MathHelper.cos((entity.rotationYaw) / 180.0F * (float) Math.PI) * motion.forward;
					double y1 = -MathHelper.sin((entity.rotationPitch) / 180.0F * (float) Math.PI) * 0.5;
					
					double x2 = -MathHelper.sin((270) / 180.0F * (float) Math.PI) * motion.xStrength;
					double z2 = MathHelper.cos((270) / 180.0F * (float) Math.PI) * motion.xStrength;
					
					double x3 = -MathHelper.sin((360) / 180.0F * (float) Math.PI) * motion.zStrength;
					double z3 = MathHelper.cos((360) / 180.0F * (float) Math.PI) * motion.zStrength;
					
					totalX = (motion.forward != 0) ? x1 + x2 + x3 : x2 + x3;
					totalZ = (motion.forward != 0) ? z1 + z2 + z3 : z2 + z3;
					totalY += (motion.forward != 0) ? y1 : 0;
					//System.out.println(x1 + " " + x2 + " " + z1 + " " + z2);
					entity.motionX += totalX;
					entity.motionY += totalY;
					entity.motionZ += totalZ;
					entity.fallDistance = 0;
				}
				break;
			case "Modify Health":
				LittleTriggerModifyHealth health = (LittleTriggerModifyHealth) trig;
				if (health.effectPerTick <= tick) {
					if (health.harm) {
						DamageSource damageSource = DamageSource.GENERIC;
						for (DamageSource source : sourceOfDmg) {
							if (health.damageType.equals(source.getDamageType())) {
								damageSource = source;
								break;
							}
						}
						for (Entity entity : entities) {
							entity.attackEntityFrom(damageSource, health.damageAmount);
						}
					}
					if (health.heal) {
						for (Entity entity : entities) {
							if (entity instanceof EntityLivingBase) {
								EntityLivingBase living = (EntityLivingBase) entity;
								living.heal(health.healAmount);
							}
						}
					}
					tick = 0;
				}
				
				tick++;
				System.out.println(tick);
				break;
			default:
				break;
			}
		}
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
	
	public static class LittleTriggerBoxStructureParser extends LittleStructureGuiParser {
		
		public List<LittleTriggerEvent> triggers = new ArrayList<LittleTriggerEvent>();
		LittleTriggerEvent trigger = null;
		
		public LittleTriggerBoxStructureParser(GuiParent parent, AnimationGuiHandler handler) {
			super(parent, handler);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void createControls(LittlePreviews previews, LittleStructure structure) {
			LittleTriggerBoxStructureALET triggerBox = (LittleTriggerBoxStructureALET) structure;
			this.triggers = triggerBox.triggers;
			
			GuiPanel panel = new GuiPanel("content", 135, 0, 159, 199);
			parent.controls.add(panel);
			
			GuiScrollBox box = new GuiScrollBox("box", 0, 0, 127, 165);
			parent.controls.add(box);
			
			List<String> strings = new ArrayList<>();
			strings.add("Modify Motion");
			strings.add("Modify Inventory");
			strings.add("Modify Health");
			strings.add("Modify NBT");
			strings.add("Modify Potion Effect");
			strings.add("Modify Scoreboard");
			strings.add("Execute Command");
			strings.add("Send Signal");
			GuiComboBox list = (new GuiComboBox("list", 0, 170, 100, strings) {
				
				@Override
				protected GuiComboBoxExtension createBox() {
					return new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 133 - getContentOffset() * 2, 100, lines);
				}
			});
			list.height = 19;
			parent.controls.add(list);
			
			GuiTriggerBoxAddButton2 add = new GuiTriggerBoxAddButton2(this, "Add", 105, 170, 22);
			add.height = 19;
			parent.addControl(add);
			
			if (triggers != null && !triggers.isEmpty()) {
				for (int i = 0; i < triggers.size(); i++)
					box.addControl(new GuiTriggerBoxAddButton(this, triggers.get(i).name + i, triggers.get(i).name, 0, i * 17, 119, 12));
			}
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public LittleTriggerBoxStructureALET parseStructure(LittlePreviews previews) {
			LittleTriggerBoxStructureALET structure = createStructure(LittleTriggerBoxStructureALET.class, null);
			
			structure.triggers = this.triggers;
			
			/*
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
			*/
			return structure;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		protected LittleStructureType getStructureType() {
			return LittleStructureRegistry.getStructureType(LittleTriggerBoxStructureALET.class);
		}
		
		@CustomEventSubscribe
		public void onControlChanged(GuiControlChangedEvent event) {
			if (trigger != null)
				trigger.updateValues(event.source);
		}
	}
	
}
/*
 * 
 * EntityPlayerSP player = Minecraft.getMinecraft().player;
			
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
			for (DamageSource forEachSource : ((LittleTiggerBoxStructureALET) structure).sourceOfDmg) {
				sourceList.add(forEachSource.damageType);
			}
			if (structure instanceof LittleTiggerBoxStructureALET) {
				push = ((LittleTiggerBoxStructureALET) structure).motion;
				xStrength = ((LittleTiggerBoxStructureALET) structure).xMotionStrength;
				yStrength = ((LittleTiggerBoxStructureALET) structure).yMotionStrength;
				zStrength = ((LittleTiggerBoxStructureALET) structure).zMotionStrength;
				forward = ((LittleTiggerBoxStructureALET) structure).forward;
				
				hurt = ((LittleTiggerBoxStructureALET) structure).damage;
				source = ((LittleTiggerBoxStructureALET) structure).damageType;
				dmgAmount = ((LittleTiggerBoxStructureALET) structure).damageAmount;
				dmgPerTick = ((LittleTiggerBoxStructureALET) structure).damagePerTick;
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
 */

/*
 * if (motion)
			for (Entity entity : entities) {
				
				entity.velocityChanged = true;
				
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
				entity.fallDistance = 0;
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
 */

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
