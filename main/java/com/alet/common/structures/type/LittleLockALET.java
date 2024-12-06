package com.alet.common.structures.type;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.alet.client.gui.LittleItemSelector;
import com.alet.client.gui.controls.GuiFakeSlot;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.type.door.LittleDoor;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LittleLockALET extends LittleStructure {
    
    public int[] toLock;
    public boolean lockIfOpen = true;
    public ItemStack key = ItemStack.EMPTY;
    public boolean playSound = true;
    public boolean consumeKey = false;
    
    public LittleLockALET(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        if (!worldIn.isRemote)
            if (!key.isEmpty()) {
                if (heldItem.getItem().equals(key.getItem())) {
                    getOutput(0).toggle();
                    if (consumeKey)
                        heldItem.shrink(1);
                } else
                    playerIn.sendStatusMessage(new TextComponentString("Incorrect Key"), true);
            } else
                getOutput(0).toggle();
        else if (playSound) {
            SoundEvent event = new SoundEvent(new ResourceLocation("block.wooden_trapdoor.close"));
            worldIn.playSound(playerIn, pos.getX(), pos.getY(), pos.getZ(), event, SoundCategory.BLOCKS, 1.0F, 1.6F);
        }
        return true;
    }
    
    public void lockDoor(LittleDoor door) {
        if (door.disableRightClick) {
            door.disableRightClick = false;
        } else {
            door.disableRightClick = true;
        }
        door.mainBlock.getTe().updateBlock();
        door.mainBlock.getTe().updateNeighbour();
        door.updateStructure();
    }
    
    public void changeLockState() {
        try {
            for (int c : toLock) {
                LittleStructure child = this.getChild(c).getStructure();
                if (child instanceof LittleDoor) {
                    LittleDoor door = (LittleDoor) child;
                    if (!lockIfOpen) {
                        if (!door.opened)
                            lockDoor(door);
                    } else
                        lockDoor(door);
                    
                }
                updateStructure();
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        toLock = nbt.getIntArray("toLock");
        key = new ItemStack(nbt.getCompoundTag("key"));
        lockIfOpen = nbt.getBoolean("lockIfOpen");
        playSound = nbt.getBoolean("playSound");
        consumeKey = nbt.getBoolean("consumeKey");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setIntArray("toLock", toLock);
        NBTTagCompound nbtItemStack = new NBTTagCompound();
        key.writeToNBT(nbtItemStack);
        nbt.setTag("key", nbtItemStack);
        nbt.setBoolean("lockIfOpen", lockIfOpen);
        nbt.setBoolean("playSound", playSound);
        nbt.setBoolean("consumeKey", consumeKey);
    }
    
    @Override
    public void performInternalOutputChange(InternalSignalOutput output) {
        if (output.component.is("lock")) {
            changeLockState();
        }
    }
    
    public static class LittleLockParserALET extends LittleStructureGuiParser {
        
        public List<Integer> possibleChildren;
        public List<Integer> selectedChildren;
        
        public LittleLockParserALET(GuiParent parent, AnimationGuiHandler handler) {
            super(parent, handler);
        }
        
        public String getDisplayName(LittlePreviews previews, int childId) {
            String name = previews.getStructureName();
            if (name == null)
                if (previews.hasStructure())
                    name = previews.getStructureId();
                else
                    name = "none";
            return name + " " + childId;
        }
        
        @Override
        protected void createControls(LittlePreviews previews, LittleStructure structure) {
            
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            LittleLockALET lock = structure instanceof LittleLockALET ? (LittleLockALET) structure : null;
            GuiScrollBox box = new GuiScrollBox("content", 0, 0, 100, 115);
            parent.removeControl(box);
            parent.controls.add(box);
            possibleChildren = new ArrayList<>();
            int i = 0;
            int added = 0;
            for (LittlePreviews child : previews.getChildren()) {
                Class clazz = LittleStructureRegistry.getStructureClass(child.getStructureId());
                if (clazz != null && LittleDoor.class.isAssignableFrom(clazz)) {
                    box.addControl(new GuiCheckBox("" + i, getDisplayName(child, i), 0, added * 20, lock != null && ArrayUtils.contains(lock.toLock, i)));
                    possibleChildren.add(i);
                    added++;
                }
                i++;
            }
            parent.controls.add(new GuiLabel("Current Key:", 108, 45));
            parent.controls.add(new GuiButton("use", "Use Item As Key", 110, 23) {
                @Override
                public void onClicked(int x, int y, int button) {
                    GuiStackSelectorAll preview = (GuiStackSelectorAll) parent.get("preview");
                    GuiFakeSlot key = (GuiFakeSlot) parent.get("key");
                    key.updateItemStack(preview.getSelected());
                }
            });
            parent.controls.add(new GuiStackSelectorAll("preview", 110, 0, 110, player, new GuiStackSelectorAll.InventoryCollector(new LittleItemSelector()), true));
            
            parent.controls.add(new GuiFakeSlot("key", 175, 41, 18, 18));
            
            parent.controls.add(new GuiCheckBox("lockIfOpened", "Lock When Opened", 109, 62, lock != null ? lock.lockIfOpen : true));
            parent.controls.add(new GuiCheckBox("playSound", "Play Sound When Locked", 109, 76, lock != null ? lock.playSound : true));
            parent.controls.add(new GuiCheckBox("consumeKey", "Consume Key On Use", 109, 90, lock != null ? lock.consumeKey : false));
            if (!player.isCreative()) {
                GuiCheckBox consumeKey = (GuiCheckBox) parent.get("consumeKey");
                consumeKey.enabled = false;
                consumeKey.setCustomTooltip("Must Be In Creaive Mode To Use.");
            }
            
            if (lock != null && !lock.key.isEmpty()) {
                GuiStackSelectorAll preview = (GuiStackSelectorAll) parent.get("preview");
                GuiFakeSlot key = (GuiFakeSlot) parent.get("key");
                key.updateItemStack(lock.key);
            }
        }
        
        @Override
        protected LittleStructure parseStructure(LittlePreviews previews) {
            LittleLockALET structure = createStructure(LittleLockALET.class, null);
            GuiScrollBox box = (GuiScrollBox) parent.get("content");
            GuiCheckBox checkLockIfOpen = (GuiCheckBox) parent.get("lockIfOpened");
            GuiCheckBox checkPlaySound = (GuiCheckBox) parent.get("playSound");
            GuiCheckBox checkConsumeKey = (GuiCheckBox) parent.get("consumeKey");
            GuiFakeSlot selectedKey = (GuiFakeSlot) parent.get("key");
            List<Integer> toLock = new ArrayList<>();
            for (Integer integer : possibleChildren) {
                GuiCheckBox checkBox = (GuiCheckBox) box.get("" + integer);
                if (checkBox != null && checkBox.value)
                    toLock.add(integer);
            }
            structure.toLock = new int[toLock.size()];
            structure.key = selectedKey.basic.getStackInSlot(0);
            structure.lockIfOpen = checkLockIfOpen.value;
            structure.playSound = checkPlaySound.value;
            structure.consumeKey = checkConsumeKey.value;
            for (int i = 0; i < structure.toLock.length; i++)
                structure.toLock[i] = toLock.get(i);
            return structure;
        }
        
        @Override
        protected LittleStructureType getStructureType() {
            return LittleStructureRegistry.getStructureType(LittleLockALET.class);
        }
        
    }
    
}

/*
 * for (int c : fixedChildren) {
			NBTTagCompound nbt = new NBTTagCompound();
			StructureChildConnection child = this.getChild(c);
			
			Iterable<IStructureTileList> v = child.getStructure().blocksList();
			for (IStructureTileList littleTile : child.getStructure().blocksList()) {
				
				for (Pair<IParentTileList, LittleTile> tiles : littleTile.getTe().allTiles()) {
					
					tiles.value.invisible = true;
				}
				littleTile.getTe().updateBlock();
				littleTile.getTe().updateNeighbour();
				child.getStructure().updateStructure();
			}
			updateStructure();
		}
 */
