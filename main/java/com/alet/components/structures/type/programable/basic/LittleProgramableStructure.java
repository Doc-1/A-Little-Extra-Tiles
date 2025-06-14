package com.alet.components.structures.type.programable.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.alet.components.structures.type.programable.basic.activator.LittleTriggerActivator;
import com.alet.components.structures.type.programable.basic.conditions.LittleTriggerCondition;
import com.alet.components.structures.type.programable.basic.events.LittleTriggerEvent;
import com.creativemd.creativecore.common.world.SubWorldServer;
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
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;

public class LittleProgramableStructure extends LittleStructure {
    public HashSet<Entity> entities = new HashSet<>();
    public List<UUID> entitiesToLoad;
    public int tick = 0;
    public int currentEvent = 0;
    public boolean run = false;
    public LittleTriggerActivator triggerActivator;
    public boolean considerEventsConditions = false;
    public List<LittleTriggerObject> triggerObjs = new ArrayList<LittleTriggerObject>();
    
    public LittleProgramableStructure(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("triggers", Constants.NBT.TAG_COMPOUND);
        int i = 0;
        triggerObjs.clear();
        for (NBTBase base : list) {
            if (base instanceof NBTTagCompound) {
                NBTTagCompound n = (NBTTagCompound) base;
                LittleTriggerObject triggerObj = LittleTriggerRegistrar.getFromNBT((NBTTagCompound) n.getTag(i + ""));
                triggerObjs.add(triggerObj);
                triggerObj.structure = this;
                
                i++;
            }
        }
        NBTTagList entityList = nbt.getTagList("entities", Constants.NBT.TAG_STRING);
        this.entitiesToLoad = new ArrayList<UUID>();
        for (int j = 0; j < entityList.tagCount(); j++) {
            entitiesToLoad.add(UUID.fromString(entityList.getStringTagAt(j)));
        }
        if (nbt.hasKey("activator")) {
            triggerActivator = (LittleTriggerActivator) LittleTriggerRegistrar.getFromNBT(nbt.getCompoundTag("activator"));
            triggerActivator.structure = this;
        }
        run = nbt.getBoolean("isRunning");
        tick = nbt.getInteger("currentTick");
        currentEvent = nbt.getInteger("currentEvent");
        if (nbt.hasKey("consideredEventsConditions"))
            this.considerEventsConditions = nbt.getBoolean("consideredEventsConditions");
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        int i = 0;
        NBTTagList list = new NBTTagList();
        for (LittleTriggerObject triggerObj : triggerObjs) {
            NBTTagCompound n = new NBTTagCompound();
            n.setTag(i + "", triggerObj.createNBT());
            list.appendTag(n);
            i++;
        }
        NBTTagList entityList = new NBTTagList();
        if (this.entities != null && !this.entities.isEmpty()) {
            for (Entity entity : this.entities) {
                entityList.appendTag(new NBTTagString(entity.getUniqueID().toString()));
            }
        }
        if (this.triggerActivator != null)
            nbt.setTag("activator", this.triggerActivator.createNBT());
        
        nbt.setBoolean("isRunning", run);
        nbt.setTag("triggers", list);
        nbt.setTag("entities", entityList);
        nbt.setInteger("currentTick", this.tick);
        nbt.setInteger("currentEvent", this.currentEvent);
        /*
        if (this.collisionArea != null)
            nbt.setTag("collisionArea", NBTUtils.writeAABB(this.collisionArea));*/
        nbt.setBoolean("consideredEventsConditions", this.considerEventsConditions);
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
            this.triggerActivator.onCollision(worldIn, entityIn);
        }
        
    }
    
    @Override
    public void checkForAnimationCollision(EntityAnimation animation, HashMap<Entity, AxisAlignedBB> entities) throws CorruptedConnectionException, NotYetConnectedException {
        if (animation.world.isRemote)
            return;
        this.triggerActivator.onCollision(animation.world, entities.keySet());
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (!this.isClient())
            this.triggerActivator.onRightClick(worldIn, tile, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ, action);
        return true;
    }
    
    @Override
    public void tick() {
        
        if (!this.isClient()) {
            World world = this.getWorld();
            if (world instanceof SubWorldServer)
                world = ((SubWorldServer) world).getRealWorld();
            WorldServer server = (WorldServer) world;
            if (this.entitiesToLoad != null) {
                for (Iterator<UUID> iterator = entitiesToLoad.iterator(); iterator.hasNext();) {
                    UUID uuid = iterator.next();
                    Entity en = server.getEntityFromUuid(uuid);
                    if (en != null) {
                        this.entities.add(en);
                        iterator.remove();
                    }
                }
                if (entitiesToLoad.isEmpty())
                    entitiesToLoad = null;
            }
            
            this.run = this.triggerActivator.shouldRun(world, entities);
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
                        if (!event && this.considerEventsConditions) {
                            this.run = false;
                            this.tick = 0;
                            this.currentEvent = 0;
                            this.entities.clear();
                            for (LittleTriggerObject triggerObj2 : this.triggerObjs)
                                if (triggerObj2 instanceof LittleTriggerCondition)
                                    ((LittleTriggerCondition) triggerObj2).completed = false;
                        }
                        
                    }
                    if (!shouldContinue)
                        break;
                    this.currentEvent++;
                    
                }
                
                this.triggerActivator.loopRules(entities, shouldContinue, flag);
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
        
    }
    
}
