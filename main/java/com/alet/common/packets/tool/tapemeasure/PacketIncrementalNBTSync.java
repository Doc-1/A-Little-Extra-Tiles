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
    
    NBTTagCompound delta;
    
    public PacketIncrementalNBTSync(ItemStack stack, NBTTagCompound origin, NBTTagCompound change) {
        NBTTagCompound modified = change.copy();
        //delta = change.copy();
        parseNBT(origin, change, modified);
        //  stack.setTagCompound(modified);
        delta = modified.copy();
        System.out.println(delta);
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
        ByteBufUtils.writeTag(buf, delta);
    }
    
    @Override
    public void readBytes(ByteBuf buf) {
        delta = ByteBufUtils.readTag(buf);
        
    }
    
    @Override
    public void executeClient(EntityPlayer player) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void executeServer(EntityPlayer player) {
        ItemStack stack = player.getHeldItemMainhand();
        NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound().copy() : new NBTTagCompound();
        
        mergeNBT(nbt, delta);
        System.out.println(nbt);
        stack.setTagCompound(nbt);
    }
    
    private void mergeNBT(NBTTagCompound target, NBTTagCompound toMerge) {
        for (String key : toMerge.getKeySet()) {
            NBTBase merge = toMerge.getTag(key);
            
            if (!target.hasKey(key)) {
                // Clone the tag to avoid shared references
                target.setTag(key, merge.copy());
            } else {
                NBTBase targetBase = target.getTag(key);
                
                // Handle nested compound
                if (merge instanceof NBTTagCompound && targetBase instanceof NBTTagCompound) {
                    mergeNBT((NBTTagCompound) targetBase, (NBTTagCompound) merge);
                }
                
                // Handle list (merge by appending)
                else if (merge instanceof NBTTagList && targetBase instanceof NBTTagList) {
                    NBTTagList mergedList = new NBTTagList();
                    NBTTagList baseList = (NBTTagList) merge;
                    NBTTagList targetList = (NBTTagList) targetBase;
                    
                    for (int i = 0; i < targetList.tagCount(); i++) {
                        mergedList.appendTag(targetList.get(i).copy());
                    }
                    for (int i = 0; i < baseList.tagCount(); i++) {
                        mergedList.appendTag(baseList.get(i).copy());
                    }
                    
                    target.setTag(key, mergedList);
                }
                
                // Otherwise, override
                else {
                    target.setTag(key, merge.copy());
                }
            }
        }
    }
}
