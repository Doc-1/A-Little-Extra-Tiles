package com.alet.common.structure.type.trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import com.alet.common.packet.PacketUpdateBreakBlock;
import com.alet.common.structure.type.ILeftClickListener;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBoxExtension;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.math.BooleanUtils;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents.GuiSignalEventsButton;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.entity.EntityAnimation;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerBoxStructureALET extends LittleStructure implements ILeftClickListener {
    
    public HashSet<Entity> entities = new HashSet<>();
    
    public boolean breakBlock = false;
    public boolean listen = true;
    public List<LittleTriggerEvent> triggers = new ArrayList<LittleTriggerEvent>();
    
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
                triggers.add(LittleTriggerEvent.getFromNBT((NBTTagCompound) n.getTag(i + "")));
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
    public void onLittleTileDestroy() throws CorruptedConnectionException, NotYetConnectedException {
        if (this.breakBlock)
            super.onLittleTileDestroy();
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
    
    @Override
    public void checkForAnimationCollision(EntityAnimation animation, HashMap<Entity, AxisAlignedBB> entities) throws CorruptedConnectionException, NotYetConnectedException {
        if (animation.world.isRemote)
            return;
        
        this.entities.addAll(entities.keySet());
        queueForNextTick();
    }
    
    private void tickWhileCollided() {
        int i = 0;
        for (LittleTriggerEvent event : triggers) {
            event.runEvent(entities);
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
            for (LittleTriggerEvent event : triggers) {
                event.tick = 0;
            }
        }
        return !wasEmpty;
    }
    
    @Override
    public void onLeftClick(EntityPlayer player) {
        this.breakBlock = LittleAction.isUsingSecondMode(player);
        PacketHandler.sendPacketToServer(new PacketUpdateBreakBlock(this.getStructureLocation()));
        
    }
    
    public static class LittleTriggerBoxStructureParser extends LittleStructureGuiParser {
        
        public List<LittleTriggerEvent> triggers = new ArrayList<LittleTriggerEvent>();
        LittleTriggerEvent trigger = null;
        
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
            
            List<String> strings = new ArrayList<>(LittleTriggerEvent.registeredEvents.keySet());
            GuiComboBox list = (new GuiComboBox("list", 0, 170, 100, strings) {
                @Override
                protected GuiComboBoxExtension createBox() {
                    return new GuiComboBoxExtension(name + "extension", this, posX, posY + height, 133 - getContentOffset() * 2, 100, lines);
                }
            });
            list.height = 19;
            parent.controls.add(list);
            
            GuiTriggerBoxAddButton add = new GuiTriggerBoxAddButton(this, "Add", 105, 170, 22);
            add.height = 19;
            parent.addControl(add);
            if (triggers != null && !triggers.isEmpty()) {
                for (int i = 0; i < triggers.size(); i++) {
                    box.addControl(new GuiTriggerEventButton(this, triggers.get(i).getName() + i, triggers.get(i).getName(), 0, i * 17, 119, 12));
                }
            }
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public LittleTriggerBoxStructureALET parseStructure(LittlePreviews previews) {
            LittleTriggerBoxStructureALET structure = createStructure(LittleTriggerBoxStructureALET.class, null);
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