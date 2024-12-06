package com.alet.components.items;

import javax.vecmath.Point3f;

import com.alet.client.ALETClient;
import com.alet.client.renders.TapeRenderer;
import com.alet.common.gui.origins.SubGuiTapeMeasure;
import com.alet.common.utils.NBTUtils;
import com.alet.common.utils.StructureUtils;
import com.alet.packets.PacketUpdateNBT;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.client.gui.configure.SubGuiConfigure;
import com.creativemd.littletiles.common.action.LittleAction;
import com.creativemd.littletiles.common.api.ILittlePlacer;
import com.creativemd.littletiles.common.container.SubContainerConfigure;
import com.creativemd.littletiles.common.item.ItemMultiTiles;
import com.creativemd.littletiles.common.tile.math.vec.LittleAbsoluteVec;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTapeMeasure extends Item implements ILittlePlacer, IItemTooltip {
    
    public static int measurementType = 0;
    
    public void clear(ItemStack stack) {
        stack.setTagCompound(new NBTTagCompound());
    }
    
    /*** @param stack
     *            The TapeMeasure the player is using
     * @param index
     *            What index the player is selected in the GUI */
    public void clear(ItemStack stack, int index, EntityPlayer player) {
        setDefaultMeasurmentNBT(stack, index);
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
    
    public static void setDefaultStackNBT(ItemStack stack) {
        NBTTagCompound nbtStack = new NBTTagCompound();
        nbtStack.setInteger("index", 0);
        nbtStack.setTag("measurements", new NBTTagCompound());
        stack.setTagCompound(nbtStack);
    }
    
    public static void setDefaultMeasurmentNBT(ItemStack stack, int index) {
        NBTTagCompound stackNBT = stack.getTagCompound();
        NBTTagCompound measurements = stackNBT.getCompoundTag("measurements");
        NBTTagCompound data = new NBTTagCompound();
        data.setInteger("context", ItemMultiTiles.currentContext.index);
        data.setString("shape", "Box");
        data.setInteger("color", ColorUtils.WHITE);
        data.setTag("positions", new NBTTagCompound());
        measurements.setTag(index + "", data);
        stackNBT.setTag("measurements", measurements);
        stack.setTagCompound(stackNBT);
        
    }
    
    @Override
    public boolean onRightClick(World world, EntityPlayer plr, ItemStack stack, PlacementPosition position, RayTraceResult result) {
        return onBlockInteract(world, plr, stack, position, result, true);
    }
    
    @Override
    public boolean onClickBlock(World world, EntityPlayer plr, ItemStack stack, PlacementPosition position, RayTraceResult result) {
        return onBlockInteract(world, plr, stack, position, result, false);
    }
    
    public boolean onBlockInteract(World world, EntityPlayer plr, ItemStack stack, PlacementPosition position, RayTraceResult result, boolean rightClick) {
        
        int index = 0;
        NBTTagCompound stackNBT = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            stackNBT = stack.getTagCompound();
            index = stackNBT.hasKey("index") ? stackNBT.getInteger("index") : 0;
        } else {
            setDefaultStackNBT(stack);
            setDefaultMeasurmentNBT(stack, 0);
            stackNBT = stack.getTagCompound();
        }
        
        LittleGridContext context = getSelectedContext(stackNBT);
        int contextSize = context.size;
        RayTraceResult res = plr.rayTrace(6.0, Minecraft.getMinecraft().getRenderPartialTicks());
        LittleAbsoluteVec absPos = new LittleAbsoluteVec(res, context);
        
        if (LittleAction.isUsingSecondMode(plr))
            position.facing = position.facing.getOpposite();
        
        Point3f posOffsetted = StructureUtils.facingOffset(absPos.getPosX(), absPos.getPosY(), absPos.getPosZ(), contextSize,
            position.facing);
        
        int additional = rightClick ? 1 : 0;
        if (GuiScreen.isCtrlKeyDown())
            additional += 2;
        NBTTagCompound data = ((NBTTagCompound) stackNBT.getTag("measurements")).getCompoundTag(index + "");
        NBTTagCompound pos = new NBTTagCompound();
        NBTTagCompound positions = new NBTTagCompound();
        if (data.hasKey("positions"))
            positions = data.getCompoundTag("positions");
        pos.setTag("pos", NBTUtils.writeDoubleArrayFrom(posOffsetted.x, posOffsetted.y, posOffsetted.z));
        pos.setString("facing", position.facing.getName());
        positions.setTag(additional + "", pos);
        data.setTag("positions", positions);
        stack.setTagCompound(stackNBT);
        PacketHandler.sendPacketToServer(new PacketUpdateNBT(stack));
        return false;
        
    }
    
    public static LittleGridContext getSelectedContext(NBTTagCompound stackNBT) {
        NBTTagCompound selected = stackNBT.getCompoundTag("measurements").getCompoundTag(stackNBT.getInteger("index") + "");
        
        return (selected.hasKey("context") && selected.getInteger("context") >= 0 && selected.getInteger(
            "context") < LittleGridContext.gridSizes.length) ? LittleGridContext.context[selected.getInteger(
                "context")] : ItemMultiTiles.currentContext;
    }
    
    public static LittleGridContext getContextAt(NBTTagCompound stackNBT, int index) {
        NBTTagCompound selected = stackNBT.getCompoundTag("measurements").getCompoundTag(index + "");
        
        return (selected.hasKey("context") && selected.getInteger("context") >= 0 && selected.getInteger(
            "context") < LittleGridContext.gridSizes.length) ? LittleGridContext.context[selected.getInteger(
                "context")] : ItemMultiTiles.currentContext;
    }
    
    @Override
    public void tick(EntityPlayer player, ItemStack stack, PlacementPosition position, RayTraceResult result) {
        NBTTagCompound stackNBT = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        
        LittleGridContext context = getSelectedContext(stackNBT);
        LittleAbsoluteVec pos = new LittleAbsoluteVec(result, context);
        if (LittleAction.isUsingSecondMode(player))
            result.sideHit = result.sideHit.getOpposite();
        Point3f posEdit = StructureUtils.facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(), context.size,
            result.sideHit);
        
        TapeRenderer.renderCursor(posEdit, context);
        if (ALETClient.clearMeasurment.isPressed())
            clear(stack, stack.getTagCompound().getInteger("index"), player);
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
                setDefaultMeasurmentNBT(stack, index);
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
    public boolean hasLittlePreview(ItemStack stack) {
        return true;
    }
    
    @Override
    public LittlePreviews getLittlePreview(ItemStack stack) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void saveLittlePreview(ItemStack stack, LittlePreviews previews) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean containsIngredients(ItemStack stack) {
        // TODO Auto-generated method stub
        return false;
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
    
}
