package com.alet.common.structure.type.premade.transfer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import com.alet.client.container.SubContainerLittleHopper;
import com.alet.common.util.StructureUtils;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.utils.mc.InventoryUtils;
import com.creativemd.creativecore.common.utils.type.HashMapList;
import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.directional.StructureDirectional;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.type.LittleNoClipStructure;
import com.creativemd.littletiles.common.structure.type.LittleStorage;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.math.box.LittleBox;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LittleTransferLittleHopper extends LittleStructurePremade {
    
    @StructureDirectional(color = ColorUtils.BLUE)
    public StructureRelative input;
    
    @StructureDirectional(color = ColorUtils.ORANGE)
    public StructureRelative output;
    
    @StructureDirectional
    public EnumFacing facing;
    
    private LittleStructure listeningInputStructure;
    private LittleStructure listeningOutputStructure;
    
    public boolean added = false;
    public boolean dropped = false;
    public boolean trigger = false;
    
    private boolean hasInputNeigborCache = false;
    private boolean hasOutputNeigborCache = false;
    
    public HashSet<Entity> entities = new HashSet<>();
    
    int counter = 0;
    
    private List<SubContainerLittleHopper> openContainers = new ArrayList<SubContainerLittleHopper>();
    
    public InventoryBasic inventory = new InventoryBasic("basic", false, 5);
    
    public LittleTransferLittleHopper(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    public void neighbourChanged() {
        if (this.isClient())
            return;
        listeningInputStructure = null;
        listeningOutputStructure = null;
        hasInputNeigborCache = false;
        hasOutputNeigborCache = false;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (playerIn.isSneaking()) {
            if (listeningOutputStructure != null && listeningInputStructure != null) {
                playerIn.sendStatusMessage(new TextComponentString("Input/Output succesfully found."), true);
            } else if (listeningOutputStructure == null && listeningInputStructure == null) {
                playerIn.sendStatusMessage(new TextComponentString("Input/Output failed to be found."), true);
            } else if (listeningOutputStructure == null) {
                if (listeningInputStructure != null)
                    playerIn.sendStatusMessage(new TextComponentString("Input succesfully found."), true);
                else
                    playerIn.sendStatusMessage(new TextComponentString("Input failed to be found."), true);
            } else if (listeningInputStructure == null)
                if (listeningOutputStructure != null)
                    playerIn.sendStatusMessage(new TextComponentString("Output succesfully found."), true);
                else
                    playerIn.sendStatusMessage(new TextComponentString("Output failed to be found."), true);
        } else if (!worldIn.isRemote && !hasPlayerOpened(playerIn))
            LittleStructureGuiHandler.openGui("little_hopper", new NBTTagCompound(), playerIn, this);
        return true;
    }
    
    public LittleStructure findConnection(StructureRelative searchArea) {
        World worldIn = this.getWorld();
        
        LittleBox foundBox = searchArea.getBox().copy();
        double searchX = searchArea.getCenter().x;
        double searchY = searchArea.getCenter().y;
        double searchZ = searchArea.getCenter().z;
        BlockPos structurePos = this.getStructureLocation().pos;
        BlockPos posSearch = new BlockPos(structurePos.getX() + searchX, structurePos.getY() + searchY, structurePos.getZ() + searchZ);
        HashMapList<BlockPos, LittleBox> boxesSearch = new HashMapList<BlockPos, LittleBox>();
        foundBox.split(searchArea.getContext(), structurePos, boxesSearch, null);
        for (Entry<BlockPos, ArrayList<LittleBox>> b : boxesSearch.entrySet())
            if (b.getKey().equals(posSearch))
                foundBox = b.getValue().get(0);
        return StructureUtils.getStructureAt(worldIn, foundBox, posSearch, searchArea.getContext(), this, LittleStorage.class);
        
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        if (nbt.hasKey("inventory"))
            inventory = InventoryUtils.loadInventoryBasic(nbt.getCompoundTag("inventory"));
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        if (inventory != null)
            nbt.setTag("inventory", InventoryUtils.saveInventoryBasic(inventory));
    }
    
    public void hopperToStorage() {
        LittleStorage storage = (LittleStorage) this.listeningOutputStructure;
        for (int i = 0; i < 5; i++) {
            
            ItemStack stack = inventory.getStackInSlot(i).copy();
            if (roomFor(stack) > 0) {
                inventory.decrStackSize(i, 1);
                stack.setCount(1);
                storage.inventory.addItem(stack);
                
                if (!stack.getItem().equals(Items.AIR))
                    break;
            }
        }
    }
    
    public void storageToHopper() {
        LittleStorage storage = (LittleStorage) this.listeningInputStructure;
        for (int i = 0; i < storage.inventorySize; i++) {
            
            ItemStack stack = storage.inventory.getStackInSlot(i).copy();
            if (roomFor(stack) > 0) {
                storage.inventory.decrStackSize(i, 1);
                stack.setCount(1);
                this.inventory.addItem(stack);
                
                if (!stack.getItem().equals(Items.AIR))
                    break;
            }
        }
    }
    
    public void itemToHopper(List<EntityItem> entityList) {
        if (!entityList.isEmpty()) {
            for (EntityItem entityItem : entityList) {
                ItemStack stack = entityItem.getItem().copy();
                if (roomFor(stack) > 0) {
                    stack = inventory.addItem(entityItem.getItem());
                    if (stack.isEmpty()) {
                        entityItem.setItem(stack);
                        entityItem.setDead();
                    } else if (stack.getCount() < 64) {
                        entityItem.setItem(stack);
                    }
                    break;
                }
            }
            
            entityList.clear();
        }
    }
    
    public byte roomFor(ItemStack item) {
        byte count = 0;
        for (int i = 0; i < 5; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (stack.getItem().equals(Items.AIR))
                count++;
            else if ((stack.getCount() < 64)) {
                if (item.getItem().equals(stack.getItem()) && item.getMetadata() == stack.getMetadata())
                    count++;
            }
        }
        return count;
    }
    
    public void dropItemFromStorage() {
        for (int i = 0; i < 5; i++) {
            if (!this.inventory.getStackInSlot(i).getItem().equals(Items.AIR)) {
                ItemStack stack = inventory.getStackInSlot(i).copy();
                stack.setCount(1);
                
                double x = this.output.getCenter().x;
                double y = this.output.getCenter().y - 0.5;
                double z = this.output.getCenter().z;
                EntityItem item = new EntityItem(this
                        .getWorld(), this.getStructureLocation().pos.getX() + x, this.getStructureLocation().pos.getY() + y, this.getStructureLocation().pos.getZ() + z, stack);
                item.motionX = 0;
                item.motionY = 0;
                item.motionZ = 0;
                this.getWorld().spawnEntity(item);
                if (this.isStillAvailable())
                    this.inventory.decrStackSize(i, 1);
                break;
            }
        }
        
    }
    
    public void addItemToStorage(EntityItem itemToAdd) {
        LittleStorage storage = (LittleStorage) this.listeningOutputStructure;
        for (int i = 0; i < storage.inventorySize; i++) {
            if (storage.inventory.getStackInSlot(i).getItem().equals(Items.AIR) || storage.inventory.getStackInSlot(i).getItem().equals(itemToAdd.getItem().getItem())) {
                ItemStack stack = storage.inventory.addItem(itemToAdd.getItem());
                if (stack.isEmpty()) {
                    itemToAdd.setItem(stack);
                    itemToAdd.setDead();
                } else if (stack.getCount() < 64) {
                    itemToAdd.setItem(stack);
                }
                break;
            }
        }
        
    }
    
    public List<EntityItem> collectItems() {
        try {
            LittleNoClipStructure noclip = (LittleNoClipStructure) this.children.get(0).getStructure();
            return this.getWorld().getEntitiesWithinAABB(EntityItem.class, noclip.getSurroundingBox().getAABB());
            
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
        return new ArrayList<EntityItem>();
    }
    
    @Override
    public void performInternalOutputChange(InternalSignalOutput output) {
        if (output.component.is("drop")) {
            if (!dropped) {
                dropped = true;
                counter = 0;
            }
        } else if (output.component.is("add")) {
            if (!added) {
                added = true;
                counter = 0;
            }
        }
    }
    
    @Override
    public void tick() {
        if (!this.isClient()) {
            if (!dropped || !added)
                counter++;
            if (counter >= 11) {
                counter = 0;
                trigger = true;
            }
            if (trigger) {
                if (!hasInputNeigborCache) {
                    this.listeningInputStructure = this.findConnection(this.input);
                    hasInputNeigborCache = true;
                }
                if (!hasOutputNeigborCache) {
                    this.listeningOutputStructure = this.findConnection(this.output);
                    hasOutputNeigborCache = true;
                }
                if (!added) {
                    if (this.listeningOutputStructure != null) {
                        hopperToStorage();
                    } else
                        dropItemFromStorage();
                    added = false;
                }
                if (!dropped) {
                    this.itemToHopper(collectItems());
                    if (this.listeningInputStructure != null) {
                        storageToHopper();
                    }
                    dropped = false;
                }
            }
            
            trigger = false;
            
        }
        
    }
    
    public boolean hasPlayerOpened(EntityPlayer player) {
        for (SubContainerLittleHopper container : openContainers)
            if (container.getPlayer() == player)
                return true;
        return false;
    }
    
    public void openContainer(SubContainerLittleHopper container) {
        openContainers.add(container);
    }
    
    public void closeContainer(SubContainerLittleHopper container) {
        openContainers.remove(container);
    }
    
}
