package com.alet.common.structure.type.premade.transfer;

import java.util.HashSet;
import java.util.Iterator;

import com.alet.common.util.StructureUtils;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class LittleTransferLittleHopper extends LittleStructurePremade {
    
    @StructureDirectional(color = ColorUtils.BLUE)
    public StructureRelative input;
    
    @StructureDirectional(color = ColorUtils.ORANGE)
    public StructureRelative output;
    
    @StructureDirectional
    public EnumFacing facing;
    
    private BlockPos inputLocation;
    private BlockPos outputLocation;
    
    private LittleStructure listeningInputStructure;
    private LittleStructure listeningOutputStructure;
    
    public boolean added = false;
    public boolean dropped = false;
    public boolean trigger = false;
    
    public HashSet<Entity> entities = new HashSet<>();
    
    int counter = 0;
    
    public LittleTransferLittleHopper(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        
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
            
        if (!playerIn.isSneaking()) {
            //if (!worldIn.isRemote && this.getParent() != null)
            //  LittleStructureGuiHandler.openGui("signal_interface", new NBTTagCompound(), playerIn, this);
        }
        return true;
    }
    
    public void findConnection() {
        World worldIn = this.getWorld();
        
        LittleBox inputBox = this.input.getBox();
        LittleBox outputBox = this.output.getBox();
        if (inputLocation == null)
            this.inputLocation = new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        
        if (outputLocation == null)
            this.outputLocation = new BlockPos(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
        
        if (this.facing.equals(EnumFacing.WEST)) {
            if (inputBox.maxX >= (this.input.getContext().size + 1)) {
                inputBox.maxX = 1;
                inputBox.minX = 0;
                inputLocation = inputLocation.east();
            }
        } else if (this.facing.equals(EnumFacing.EAST)) {
            if (inputBox.minX <= -1) {
                inputBox.maxX = 32;
                inputBox.minX = 31;
                inputLocation = inputLocation.west();
            }
        } else if (this.facing.equals(EnumFacing.NORTH)) {
            if (inputBox.maxZ >= (this.input.getContext().size + 1)) {
                inputBox.maxZ = 1;
                inputBox.minZ = 0;
                inputLocation = inputLocation.south();
            }
        } else if (this.facing.equals(EnumFacing.SOUTH)) {
            if (inputBox.minZ <= -1) {
                inputBox.maxZ = 32;
                inputBox.minZ = 31;
                inputLocation = inputLocation.north();
            }
        } else if (this.facing.equals(EnumFacing.UP)) {
            if (inputBox.minY <= -1) {
                inputBox.maxY = 32;
                inputBox.minY = 31;
                inputLocation = inputLocation.down();
            }
        } else if (this.facing.equals(EnumFacing.DOWN)) {
            if (inputBox.minY >= (this.input.getContext().size + 1)) {
                inputBox.maxY = 1;
                inputBox.minY = 0;
                inputLocation = inputLocation.up();
            }
        }
        
        double outX = this.output.getCenter().x;
        double outY = this.output.getCenter().y;
        double outZ = this.output.getCenter().z;
        double inX = this.input.getCenter().x;
        double inY = this.input.getCenter().y;
        double inZ = this.input.getCenter().z;
        BlockPos structurePos = this.getStructureLocation().pos;
        Vec3d posOut = new Vec3d(structurePos.getX() + outX, structurePos.getY() + outY, structurePos.getZ() + outZ);
        Vec3d posIn = new Vec3d(structurePos.getX() + inX, structurePos.getY() + inY, structurePos.getZ() + inZ);
        try {
            System.out.println(this.getSurroundingBox().getAbsoluteBox().box.getMinVec());
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
        LittleGridContext c = input.getContext();
        int size = c.size;
        double con = 1D / size;
        double count = 0.03125;
        inputBox = this.getSurroundingBox().getAbsoluteBox().box;
        outputBox = this.getSurroundingBox().getAbsoluteBox().box;
        int inMinY = c.toGrid(c.toVanillaGrid(inputBox.minY) + (count * 14));
        int inMaxY = c.toGrid(c.toVanillaGrid(inputBox.maxY));
        inputBox.minY = inMinY;
        inputBox.maxY = inMaxY;
        
        int outMinY = c.toGrid(c.toVanillaGrid(outputBox.minY) - count);
        int outMaxY = c.toGrid(c.toVanillaGrid(outputBox.maxY) - (count * 14));
        
        int outMinX = c.toGrid(c.toVanillaGrid(outputBox.minX) + (count * 10));
        int outMinZ = c.toGrid(c.toVanillaGrid(outputBox.minZ) + (count * 10));
        
        int outMaxX = c.toGrid(c.toVanillaGrid(outputBox.maxX) - (count * 10));
        int outMaxZ = c.toGrid(c.toVanillaGrid(outputBox.maxZ) - (count * 10));
        
        outputBox.minY = outMinY;
        outputBox.maxY = outMaxY;
        outputBox.minX = outMinX;
        outputBox.minZ = outMinZ;
        outputBox.maxX = outMaxX;
        outputBox.maxZ = outMaxZ;*/
        LittleBox bo = new LittleBox(inputBox.getCenter(), 1, 2, 1);
        LittleBox ba = new LittleBox(outputBox.getCenter(), 1, 1, 1);
        LittleStructure in = StructureUtils.getStructureAt(worldIn, bo, posIn, this);
        LittleStructure out = StructureUtils.getStructureAt(worldIn, ba, posOut, this);
        System.out.println(in + " " + out);
        
        if (in instanceof LittleStorage)
            listeningInputStructure = in;
        else if (in == null)
            listeningInputStructure = null;
        if (out instanceof LittleStorage)
            listeningOutputStructure = out;
        else if (out == null)
            listeningOutputStructure = null;
        
        /*[0,15,0 -> 32,18,32] [0,3,0 -> 16,16,16]
        double xx = Math.abs(this.output.getBox().maxX + p.getX());
        double yy = Math.abs(this.output.getBox().maxY + p.getY());
        double zz = Math.abs(this.output.getBox().maxZ + p.getZ());
        double xx2 = Math.abs(this.output.getBox().minX + p.getX());
        double yy2 = Math.abs(this.output.getBox().minY + p.getY());
        double zz2 = Math.abs(this.output.getBox().minZ + p.getZ());*/
        //AxisAlignedBB aabb = new AxisAlignedBB(pos.x - con, pos.y - con, pos.z - con, pos.x + con, pos.y + con, pos.z + con);
        
        //System.out.println(xDist + " " + yDist + " " + zDist);
        //System.out.println(xx + " " + yy + " " + zz + " | " + xx2 + " " + yy2 + " " + zz2);
        //System.out.println(Math.floor(aabb.minX) + ", " + Math.floor(aabb.minY) + ", " + Math.floor(aabb.minZ) + " | " + Math.floor(aabb.maxX) + ", " + Math
        //        .floor(aabb.maxY) + ", " + Math.floor(aabb.maxZ));
        //System.out.println(aabb.minX + ", " + aabb.minY + ", " + aabb.minZ + " | " + aabb.maxX + ", " + aabb.maxY + ", " + aabb.maxZ);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        // TODO Auto-generated method stub
        
    }
    
    public void dropItemFromStorage() {
        if (this.listeningInputStructure != null) {
            
            System.out.println("drop");
            LittleStorage storage = (LittleStorage) this.listeningInputStructure;
            for (int i = 0; i < storage.inventorySize; i++) {
                if (!storage.inventory.getStackInSlot(i).getItem().equals(Items.AIR)) {
                    ItemStack stack = storage.inventory.getStackInSlot(i).copy();
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
                    storage.inventory.decrStackSize(i, 1);
                    break;
                }
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
    
    public void collectItems() {
        if (this.listeningOutputStructure != null)
            try {
                
                System.out.println("add");
                LittleNoClipStructure noclip = (LittleNoClipStructure) this.children.get(0).getStructure();
                Entity entity = null;
                Iterator<Entity> iter = noclip.entities.iterator();
                while (iter.hasNext()) {
                    entity = iter.next();
                    if (entity instanceof EntityItem) {
                        EntityItem eItem = (EntityItem) entity;
                        addItemToStorage(eItem);
                    }
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
            }
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
            findConnection();
            if (!dropped || !added)
                counter++;
            if (counter >= 11) {
                counter = 0;
                trigger = true;
            }
            if (!dropped) {
                if (trigger) {
                    dropItemFromStorage();
                    dropped = false;
                }
            }
            if (!added) {
                if (trigger) {
                    collectItems();
                    added = false;
                }
            }
            trigger = false;
            
        }
        
    }
    
}
