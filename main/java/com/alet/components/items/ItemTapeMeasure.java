package com.alet.components.items;

import java.util.Set;
import java.util.UUID;

import javax.vecmath.Point3f;

import com.alet.client.ALETClient;
import com.alet.client.render.overlay.DrawMeasurements;
import com.alet.common.gui.tool.SubGuiTapeMeasure;
import com.alet.common.measurment.shape.LittleMeasurementRegistry;
import com.alet.common.measurment.shape.type.LittleMeasurement;
import com.alet.common.packets.tool.tapemeasure.PacketIncrementalNBTSync;
import com.alet.common.packets.tool.tapemeasure.PacketUpdateNBT;
import com.alet.common.utils.StructureUtils;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.api.ILittleTool;
import com.creativemd.littletiles.common.container.SubContainerConfigure;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.common.util.place.IMarkMode;
import com.creativemd.littletiles.common.util.place.MarkMode;
import com.creativemd.littletiles.common.util.place.PlacementPosition;
import com.creativemd.littletiles.common.util.place.PlacementPreview;
import com.creativemd.littletiles.common.util.tooltip.IItemTooltip;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTapeMeasure extends Item implements ILittleTool, IItemTooltip {
    
    public static int measurementType = 0;
    
    public void clear(ItemStack stack) {
        stack.setTagCompound(new NBTTagCompound());
    }
    
    /*** @param stack
     *            The TapeMeasure the player is using
     * @param index
     *            What index the player is selected in the GUI */
    public void clear(ItemStack stack, int index, EntityPlayer player) {
        // setDefaultMeasurmentNBT(stack, index);
        PacketHandler.sendPacketToServer(new PacketUpdateNBT(stack));
    }
    
    public ItemTapeMeasure() {
        setMax(50);
    }
    
    public ItemTapeMeasure(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setMaxStackSize(1);
        setCreativeTab(LittleTiles.littleTab);
        
    }
    
    public static void setMax(int maxMeasurements) {
        
    }
    
    public static int getMax() {
        return 50;
    }
    
    @Override
    public boolean onRightClick(World world, EntityPlayer player, ItemStack stack, PlacementPosition position, RayTraceResult result) {
        return onBlockInteract(world, player, stack, position, result, true);
    }
    
    @Override
    public boolean onClickBlock(World world, EntityPlayer player, ItemStack stack, PlacementPosition position, RayTraceResult result) {
        return onBlockInteract(world, player, stack, position, result, false);
    }
    
    /** @param stack
     *            Only provide this with the Player's main held item stack */
    public static void initNBTData(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();
        
        if (!nbt.hasUniqueId("UUID"))
            nbt.setUniqueId("UUID", UUID.randomUUID());
        
        if (!nbt.hasKey("index"))
            nbt.setInteger("index", 0);
        
        stack.setTagCompound(nbt);
    }
    
    public boolean onBlockInteract(World world, EntityPlayer plr, ItemStack stack, PlacementPosition position, RayTraceResult r, boolean rightClick) {
        initNBTData(stack);
        
        NBTTagCompound origin = stack.getTagCompound();
        int originIndex = origin.getInteger("index");
        NBTTagCompound originMeasurements = origin.hasKey("measurements") ? origin.getCompoundTag(
            "measurements") : new NBTTagCompound();
        NBTTagCompound originMeasurement = originMeasurements.hasKey(originIndex + "") ? originMeasurements.getCompoundTag(
            originIndex + "") : new NBTTagCompound();
        String originMeasurementShape = originMeasurement.hasKey("shape") ? originMeasurement.getString("shape") : "Box";
        LittleMeasurement measurement = LittleMeasurementRegistry.getMeasurementShape(originMeasurementShape);
        
        measurement.deserialize(originMeasurement);
        measurement.setColor(ColorUtils.MAGENTA);
        measurement.setGrid(4);
        LittleGridContext context = measurement.getGrid();
        int contextSize = context.size;
        RayTraceResult res = plr.rayTrace(6.0, Minecraft.getMinecraft().getRenderPartialTicks());
        LittleAbsoluteVec absRes = new LittleAbsoluteVec(res, context);
        if (LittleAction.isUsingSecondMode(plr))
            position.facing = position.facing.getOpposite();
        
        Point3f result = StructureUtils.facingOffset(absRes.getPosX(), absRes.getPosY(), absRes.getPosZ(), contextSize,
            position.facing);
        
        int pointIndex = rightClick ? 1 : 0;
        if (GuiScreen.isCtrlKeyDown())
            pointIndex += 2;
        measurement.setPoint(pointIndex, result);
        
        NBTTagCompound change = origin.copy();
        NBTTagCompound measurements = new NBTTagCompound();
        measurements.setTag("0", measurement.serialize());
        change.setTag("measurements", measurements);
        //stack.setTagCompound(change);
        System.out.println(change);
        PacketHandler.sendPacketToServer(new PacketIncrementalNBTSync(stack, origin, change));
        
        return false;
        
    }
    
    public static void mergeChanged(NBTTagCompound target, NBTTagCompound incoming) {
        Set<String> keys = incoming.getKeySet();
        
        for (String key : keys) {
            NBTBase newValue = incoming.getTag(key);
            NBTBase oldValue = target.getTag(key);
            
            if (!newValue.equals(oldValue)) {
                target.setTag(key, newValue.copy()); // Apply only if changed
            }
        }
    }
    
    @Override
    public void tick(EntityPlayer player, ItemStack stack, PlacementPosition position, RayTraceResult result) {
        
        LittleMeasurement measurement = LittleMeasurementRegistry.getMeasurementShape("Box");
        LittleGridContext context = measurement.getGrid();
        LittleAbsoluteVec pos = new LittleAbsoluteVec(result, context);
        if (LittleAction.isUsingSecondMode(player))
            result.sideHit = result.sideHit.getOpposite();
        Point3f posEdit = StructureUtils.facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(), context.size,
            result.sideHit);
        
        DrawMeasurements.renderCursor(posEdit, measurement.getColor(), context);
        
        //   if (ALETClient.clearMeasurment.isPressed())
        //  clear(stack, index, player);
    }
    
    @Override
    public void rotate(EntityPlayer player, ItemStack stack, Rotation rotation, boolean client) {
        NBTTagCompound stackNBT = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        if (stackNBT.hasKey("index")) {
            int index = stackNBT.getInteger("index");
            if (rotation == Rotation.Z_CLOCKWISE)
                index--;
            if (rotation == Rotation.Z_COUNTER_CLOCKWISE)
                index++;
            
            if (index > 9)
                index = 0;
            else if (index < 0)
                index = 9;
            stackNBT.setInteger("index", index);
            if (!stackNBT.getCompoundTag("measurements").hasKey(index + "")) {
                // setDefaultMeasurmentNBT(stack, index);
            }
            stack.setTagCompound(stackNBT);
            
        }
    }
    
    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return 0F;
    }
    
    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public SubGuiConfigure getConfigureGUI(EntityPlayer player, ItemStack stack) {
        return new SubGuiTapeMeasure(stack);
    }
    
    @Override
    public SubGuiConfigure getConfigureGUIAdvanced(EntityPlayer player, ItemStack stack) {
        
        return null;
    }
    
    @Override
    public SubContainerConfigure getConfigureContainer(EntityPlayer player, ItemStack stack) {
        return new SubContainerConfigure(player, stack);
    }
    
    @Override
    public Object[] tooltipData(ItemStack stack) {
        return new Object[] { LittleTilesClient.configure.getDisplayName(), LittleTilesClient.up
                .getDisplayName(), LittleTilesClient.down.getDisplayName(), ALETClient.clearMeasurment.getDisplayName() };
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IMarkMode onMark(EntityPlayer player, ItemStack stack, PlacementPosition position, RayTraceResult result, PlacementPreview previews) {
        return new MarkMode(player, position, previews);
    }
    
    @Override
    public void flip(EntityPlayer player, ItemStack stack, Axis axis, boolean client) {
        // TODO Auto-generated method stub
        
    }
    
}
