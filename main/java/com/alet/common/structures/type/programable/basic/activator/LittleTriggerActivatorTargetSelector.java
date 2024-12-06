package com.alet.common.structures.type.programable.basic.activator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.alet.client.gui.controls.GuiWrappedTextField;
import com.alet.common.commands.sender.StructureCommandSender;
import com.alet.common.structures.type.programable.basic.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerActivatorTargetSelector extends LittleTriggerActivator {
    
    String target = "";
    
    @Override
    public void onCollision(World worldIn, Entity entityIn) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onCollision(World worldIn, Collection<Entity> entities) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onRightClick(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) {
        
    }
    
    @Override
    public void loopRules(HashSet<Entity> entities, boolean shouldContinue, boolean flag) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean shouldRun(World world, HashSet<Entity> entities) {
        StructureCommandSender sender = new StructureCommandSender(null, structure);
        try {
            String[] args = target.split(" ");
            List<Entity> list = EntitySelector.<Entity>matchEntities(sender, args[0], Entity.class);
            if (args.length >= 2) {
                List<Entity> ls = new ArrayList<Entity>();
                if (list != null)
                    for (Entity entity : list)
                        try {
                            NBTTagCompound nbttagcompound = JsonToNBT.getTagFromJson(CommandBase.buildString(args, 1));
                            if (nbttagcompound != null) {
                                NBTTagCompound nbttagcompound1 = CommandBase.entityToNBT(entity);
                                
                                if (NBTUtil.areNBTEquals(nbttagcompound, nbttagcompound1, true))
                                    ls.add(entity);
                            }
                        } catch (NBTException e) {
                            e.printStackTrace();
                        }
                entities.addAll(ls);
                return true;
            } else
                entities.addAll(list);
        } catch (CommandException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (!entities.isEmpty())
            return true;
        
        return false;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        this.target = nbt.getString("target");
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setString("target", this.target);
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        panel.addControl(new GuiWrappedTextField("target", target, 0, 0, 153, 37));
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source.is("target")) {
            GuiWrappedTextField text = (GuiWrappedTextField) source;
            this.target = text.text;
        }
        
    }
    
}
