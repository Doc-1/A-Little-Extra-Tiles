package com.alet.common.structure.type.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.alet.common.packet.PacketUpdateBreakBlock;
import com.alet.common.structure.type.ILeftClickListener;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxCategory;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtensionCategory;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.creativecore.common.utils.type.PairList;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents.GuiSignalEventsButton;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.entity.EntityAnimation;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.parent.IParentTileList;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerBoxStructureALET extends LittleStructure implements ILeftClickListener {
    
    public HashSet<Entity> entities = new HashSet<>();
    
    public boolean breakBlock = false;
    public List<LittleTriggerObject> triggers = new ArrayList<LittleTriggerObject>();
    
    public LittleTriggerBoxStructureALET(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        NBTTagList list = nbt.getTagList("triggers", Constants.NBT.TAG_COMPOUND);
        int i = 0;
        for (NBTBase base : list) {
            if (base instanceof NBTTagCompound) {
                NBTTagCompound n = (NBTTagCompound) base;
                triggers.add(LittleTriggerRegistrar.getFromNBT((NBTTagCompound) n.getTag(i + "")));
                i++;
            }
        }
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        int i = 0;
        NBTTagList list = new NBTTagList();
        for (LittleTriggerObject trigger : triggers) {
            NBTTagCompound n = new NBTTagCompound();
            n.setTag(i + "", trigger.createNBT());
            list.appendTag(n);
            i++;
        }
        nbt.setTag("triggers", list);
    }
    
    @Override
    public void onLittleTileDestroy() throws CorruptedConnectionException, NotYetConnectedException {
        if (this.breakBlock)
            super.onLittleTileDestroy();
    }
    
    @Override
    public int getAttribute() {
        return super.getAttribute();
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
    
    @Override
    public void checkForAnimationCollision(EntityAnimation animation, HashMap<Entity, AxisAlignedBB> entities) throws CorruptedConnectionException, NotYetConnectedException {
        if (animation.world.isRemote)
            return;
        
        this.entities.addAll(entities.keySet());
        queueForNextTick();
        
    }
    
    private void triggerEvents() {
        int allCompleted = 0;
        int count = 1;
        for (LittleTriggerObject event : triggers) {
            //event.tryRunEvent(entities);
            allCompleted = count;
            if (!event.complete)
                break;
            count++;
        }
        //  if (!this.isClient())
        //getInput(3).updateState(BooleanUtils.toBits(allCompleted, 16));
    }
    
    @Override
    public void tick() {
        if (!this.isClient()) {
            if (this.entities.isEmpty()) {
                // getInput(2).updateState(BooleanUtils.toBits(0, 16));
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
        
        triggerEvents();
        entities.clear();
        if (wasEmpty) {
            for (LittleTriggerObject event : triggers) {
                //event.tick = 0;
            }
        }
        return !wasEmpty;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (this.isClient()) {
            this.entities.add(playerIn);
            queueForNextTick();
            
        }
        
        entities.clear();
        return true;
    }
    
    @Override
    public void onLeftClick(EntityPlayer player) {
        if (LittleAction.isUsingSecondMode(player)) {
            this.breakBlock = true;
            PacketHandler.sendPacketToServer(new PacketUpdateBreakBlock(this.getStructureLocation()));
        } else {
            this.breakBlock = false;
            this.entities.add(player);
            queueForNextTick();
        }
        
    }
    
    public static class LittleTriggerBoxStructureParser extends LittleStructureGuiParser {
        
        public List<LittleTriggerObject> triggers = new ArrayList<LittleTriggerObject>();
        LittleTriggerObject trigger = null;
        
        public LittleTriggerBoxStructureParser(GuiParent parent, AnimationGuiHandler handler) {
            super(parent, handler);
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public void create(LittlePreviews previews, @Nullable LittleStructure structure) {
            createControls(previews, structure);
            parent.controls.add(new GuiSignalEventsButton("signal", 0, 192, previews, structure, getStructureType()));
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public void createControls(LittlePreviews previews, LittleStructure structure) {
            LittleTriggerBoxStructureALET triggerBox = (LittleTriggerBoxStructureALET) structure;
            if (triggerBox != null)
                this.triggers = triggerBox.triggers;
            
            GuiPanel panel = new GuiPanel("content", 135, 0, 159, 199);
            parent.controls.add(panel);
            
            GuiScrollBox box = new GuiScrollBox("box", 0, 0, 127, 165);
            parent.controls.add(box);
            
            List<String> strings = new ArrayList<String>();
            PairList<String, PairList<String, String>> elements;
            GuiComboBoxCategory<Class<? extends LittleTriggerObject>> list = (new GuiComboBoxCategory<Class<? extends LittleTriggerObject>>("list", 0, 170, 100, LittleTriggerRegistrar.triggerables) {
                @Override
                protected GuiComboBoxExtensionCategory<Class<? extends LittleTriggerObject>> createBox() {
                    return new GuiComboBoxExtensionCategory<Class<? extends LittleTriggerObject>>(name + "extension", this, posX, posY + height, 133 - getContentOffset() * 2, 100);
                }
            });
            list.height = 19;
            parent.controls.add(list);
            
            GuiTriggerBoxAddButton add = new GuiTriggerBoxAddButton(this, "Add", 105, 170, 22);
            add.height = 19;
            parent.addControl(add);
            if (triggers != null && !triggers.isEmpty()) {
                for (int i = 0; i < triggers.size(); i++) {
                    box.addControl(new GuiTriggerEventButton(this, triggers.get(i).getName() + i, I18n.translateToLocal(triggers.get(i).getName()), 0, i * 17, 119, 12));
                }
            }
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public LittleTriggerBoxStructureALET parseStructure(LittlePreviews previews) {
            LittleTriggerBoxStructureALET structure = createStructure(LittleTriggerBoxStructureALET.class, null);
            System.out.println(this.trigger);
            structure.triggers = this.triggers;
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