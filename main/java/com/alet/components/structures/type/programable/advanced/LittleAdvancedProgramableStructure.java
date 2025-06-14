package com.alet.components.structures.type.programable.advanced;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.alet.components.structures.type.programable.advanced.Function.FunctionType;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.entity.EntityAnimation;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LittleAdvancedProgramableStructure extends LittleStructure {
    public HashSet<Entity> entities = new HashSet<>();
    public List<UUID> entitiesToLoad;
    public int tick = 0;
    public Function resumeFrom;
    public boolean run = false;
    private HashMap<FunctionType, Function> functions = new HashMap<>();
    
    public LittleAdvancedProgramableStructure(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {}
    
    public void organizeBlueprint() {
        
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
        if (intersected) {
            //this.triggerActivator.onCollision(worldIn, entityIn);
        }
        
    }
    
    @Override
    public void checkForAnimationCollision(EntityAnimation animation, HashMap<Entity, AxisAlignedBB> entities) throws CorruptedConnectionException, NotYetConnectedException {
        if (animation.world.isRemote)
            return;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        
        return true;
    }
    
    @Override
    public void tick() {
        
    }

    public HashMap<FunctionType, Function> getFunctions() {
        return functions;
    }
    
    /*
    @Override
    public void tick() {
        
        if (!this.isClient()) {
            World world = this.getWorld();
            if (world instanceof SubWorldServer)
                world = ((SubWorldServer) world).getRealWorld();
            WorldServer server = (WorldServer) world;
            if (this.entitiesToLoad != null) {
                for (Iterator<UUID> iterator = entitiesToLoad.iterator(); iterator.hasNext();) {
                    UUID uuid = (UUID) iterator.next();
                    Entity en = server.getEntityFromUuid(uuid);
                    if (en != null) {
                        this.entities.add(en);
                        iterator.remove();
                    }
                }
                if (entitiesToLoad.isEmpty())
                    entitiesToLoad = null;
            }
            
            //this.run = this.triggerActivator.shouldRun(world, entities);
            if (!this.entities.isEmpty() && this.run) {
                boolean hasCondition = LittleTriggerObject.hasCondition(this.triggerObjs); //Will reset the run and tick values
                boolean shouldContinue = true; //Will stop the for loop and reset the run and tick values
                boolean flag = false; //Will reset the run and tick values
                //for loops through all trigger conditions and events in order.
                while (this.currentEvent < this.triggerObjs.size()) {
                    LittleTriggerObject triggerObj = this.triggerObjs.get(this.currentEvent);
                    if (triggerObj instanceof LittleTriggerCondition) {
                        if (this.triggerObjs.size() > this.currentEvent) {
                            LittleTriggerCondition condition = (LittleTriggerCondition) triggerObj;
                            shouldContinue = condition.conditionRunEvent();
                            if (!shouldContinue && !condition.shouldLoop) {
                                flag = true;
                                this.entities.clear();
                            }
                        }
                    } else if (shouldContinue && triggerObj instanceof LittleTriggerEvent) {
                        LittleTriggerEvent triggerEvent = (LittleTriggerEvent) triggerObj;
                        boolean event = triggerEvent.runEvent();
                        if (this.considerEventsConditions)
                            shouldContinue = event;
                    }
                    if (!shouldContinue)
                        break;
                    this.currentEvent++;
                    
                }
                //this.triggerActivator.loopRules(entities, shouldContinue, flag);
                if (hasCondition && shouldContinue && !flag)
                    this.getInput(0).updateState(new boolean[] { true });
                else
                    this.getInput(0).updateState(new boolean[] { false });
                //If there is no conditions then there is no need to loop.
                if (this.currentEvent >= triggerObjs.size() || flag) {
                    this.run = false;
                    this.tick = 0;
                    this.currentEvent = 0;
                    this.entities.clear();
                    for (LittleTriggerObject triggerObj : this.triggerObjs) {
                        if (triggerObj instanceof LittleTriggerCondition)
                            ((LittleTriggerCondition) triggerObj).completed = false;
                    }
                }
            }
        }
        
    }*/
    
}
