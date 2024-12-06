package com.alet.common.structures.type.premade.signal;

import javax.annotation.Nullable;

import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleCircuitTFlipFlop extends LittleStructurePremade {
    
    public boolean clock = false;
    public boolean hasChanged = false;
    
    public LittleCircuitTFlipFlop(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    public void tick() {
        if (!isClient()) {
            
            try {
                LittleSignalInput clear = (LittleSignalInput) this.children.get(0).getStructure();
                LittleSignalInput pulse = (LittleSignalInput) this.children.get(1).getStructure();
                LittleSignalInput t = (LittleSignalInput) this.children.get(2).getStructure();
                LittleSignalInput preset = (LittleSignalInput) this.children.get(3).getStructure();
                LittleSignalOutput q = (LittleSignalOutput) this.children.get(4).getStructure();
                LittleSignalOutput qNot = (LittleSignalOutput) this.children.get(5).getStructure();
                
                if (this.clock != pulse.getState()[0])
                    hasChanged = true;
                else
                    hasChanged = false;
                
                this.clock = pulse.getState()[0];
                
                if (preset.getState()[0]) {
                    if (hasChanged && pulse.getState()[0] && t.getState()[0]) {
                        q.updateState(new boolean[] { !q.getState()[0] });
                        qNot.updateState(new boolean[] { !q.getState()[0] });
                    }
                    if (!clear.getState()[0]) {
                        q.updateState(new boolean[] { false });
                        qNot.updateState(new boolean[] { !q.getState()[0] });
                    }
                }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
                
            }
            
        }
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        if (nbt.hasKey("clock"))
            clock = nbt.getBoolean("clock");
        
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setBoolean("clock", clock);
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        
        return true;
    }
    
}
