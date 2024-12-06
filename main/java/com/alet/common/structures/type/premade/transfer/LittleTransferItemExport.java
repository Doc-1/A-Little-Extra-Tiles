package com.alet.common.structures.type.premade.transfer;

import javax.vecmath.Vector3f;

import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.structure.directional.StructureDirectional;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.relative.StructureRelative;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.structure.type.LittleStorage;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class LittleTransferItemExport extends LittleStructurePremade {
    
    @StructureDirectional(color = ColorUtils.CYAN)
    public StructureRelative frame;
    
    @StructureDirectional
    public EnumFacing facing;
    
    @StructureDirectional
    public Vector3f topRight;
    
    public boolean dropped = false;
    
    int counter = 0;
    
    public LittleTransferItemExport(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {}
    
    public void dropItemFromStorage() {
        try {
            if (this.getParent() != null)
                if (this.getParent().getStructure() instanceof LittleStorage) {
                    LittleStorage storage = (LittleStorage) this.getParent().getStructure();
                    for (int i = 0; i < storage.inventorySize; i++) {
                        if (!storage.inventory.getStackInSlot(i).getItem().equals(Items.AIR)) {
                            ItemStack stack = storage.inventory.getStackInSlot(i).copy();
                            stack.setCount(1);
                            
                            double x = this.frame.getCenter().x;
                            double y = this.frame.getCenter().y - 0.5;
                            double z = this.frame.getCenter().z;
                            EntityItem item = new EntityItem(this.getWorld(), this.getStructureLocation().pos.getX() + x, this.getStructureLocation().pos
                                    .getY() + y, this.getStructureLocation().pos.getZ() + z, stack);
                            item.motionX = 0;
                            item.motionY = 0;
                            item.motionZ = 0;
                            this.getWorld().spawnEntity(item);
                            //PacketHandler.sendPacketToServer(new PacketDropItem(this.frame.getCenter(), this.getStructureLocation().pos, stack));
                            storage.inventory.decrStackSize(i, 1);
                            break;
                        }
                    }
                }
        } catch (CorruptedConnectionException | NotYetConnectedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    
    @Override
    public void performInternalOutputChange(InternalSignalOutput output) {
        if (output.component.is("drop")) {
            if (!dropped) {
                dropped = true;
                counter = 0;
            }
        }
    }
    
    @Override
    public void tick() {
        if (!this.isClient()) {
            if (!dropped) {
                counter++;
                if (counter >= 11) {
                    counter = 0;
                    dropItemFromStorage();
                    dropped = false;
                }
            }
        }
        
    }
    
}
