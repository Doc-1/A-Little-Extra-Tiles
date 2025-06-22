package com.alet.common.packets.tool.tapemeasure;

import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketIncrementalNBTSync extends CreativeCorePacket {
    
    public PacketIncrementalNBTSync() {
        // TODO Auto-generated constructor stub
    }
    
    NBTTagCompound deltaModified;
    NBTTagCompound deltaRemoved;
    
    public PacketIncrementalNBTSync(ItemStack stack, NBTTagCompound origin, NBTTagCompound change) {
        NBTTagCompound modified = change.copy();
        //delta = change.copy();
        parseNBT(origin, change, modified);
        //  stack.setTagCompound(modified);
        deltaModified = modified.copy();
        System.out.println(deltaModified);
    }
    
    private void parseNBT(NBTTagCompound origin, NBTTagCompound change, NBTTagCompound modified) {
        for (String key : change.getKeySet()) {
            if (!origin.hasKey(key)) {
                NBTBase changedValue = change.getTag(key);
                
            } else {
                NBTBase changedValue = change.getTag(key);
                NBTBase originValue = origin.getTag(key);
                if (!changedValue.equals(originValue)) {
                    // System.out.println(modified);
                    if (originValue instanceof NBTTagCompound) {
                        
                        parseNBT((NBTTagCompound) originValue, (NBTTagCompound) changedValue, modified.getCompoundTag(key));
                    } else if (originValue instanceof NBTTagList) {
                        parseList((NBTTagList) originValue, (NBTTagList) changedValue, (NBTTagList) modified.getTag(key),
                            modified);
                    }
                } else {
                    modified.removeTag(key);
                }
            }
        }
    }
    
    private void parseList(NBTTagList origin, NBTTagList change, NBTTagList modifiedList, NBTTagCompound modifiedTag) {
        for (int i = 0; i < change.tagCount(); i++) {
            if (origin.tagCount() < i) {
                NBTBase changedValue = change.get(i);
            } else {
                NBTBase originValue = origin.get(i);
                NBTBase changedValue = change.get(i);
                
                if (!changedValue.equals(originValue)) {
                    
                    if (originValue instanceof NBTTagCompound) {
                        parseNBT((NBTTagCompound) originValue, (NBTTagCompound) changedValue, modifiedList.getCompoundTagAt(
                            i));
                    } else if (originValue instanceof NBTTagList)
                        parseList((NBTTagList) originValue, (NBTTagList) changedValue, (NBTTagList) modifiedList.get(i),
                            modifiedTag);
                    
                }
            }
        }
    }
    
    @Override
    public void writeBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, deltaModified);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        deltaModified = ByteBufUtils.readTag(buf);
        
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        ItemStack stack = player.getHeldItemMainhand();
        NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound().copy() : new NBTTagCompound();
        
        mergeNBT(nbt, deltaModified);
        System.out.println(nbt);
        stack.setTagCompound(nbt);
    }
    
    private void mergeNBT(NBTTagCompound target, NBTTagCompound toMerge) {
        for (String key : toMerge.getKeySet()) {
            NBTBase merge = toMerge.getTag(key);
            
            if (!target.hasKey(key))
                target.setTag(key, merge.copy());
            else {
                NBTBase targetBase = target.getTag(key);
                
                if (merge instanceof NBTTagCompound && targetBase instanceof NBTTagCompound)
                    mergeNBT((NBTTagCompound) targetBase, (NBTTagCompound) merge);
                else
                    target.setTag(key, merge.copy());
                
            }
        }
    }
}
