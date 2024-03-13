package com.alet.items;

import java.util.List;

import javax.annotation.Nullable;

import com.alet.client.ALETClient;
import com.alet.client.gui.SubGuiTapeMeasure;
import com.alet.client.render.tapemeasure.TapeRenderer;
import com.alet.common.packet.PacketUpdateNBT;
import com.alet.common.utils.NBTUtils;
import com.alet.common.utils.StructureUtils;
import com.alet.common.utils.TapeMeasureKeyEventHandler;
import com.alet.tiles.SelectLittleTile;
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
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTapeMeasure extends Item implements ILittlePlacer, IItemTooltip {
    
    public static int measurementType = 0;
    
    public void clear(ItemStack stack) {
        writeNBTData(stack, new NBTTagCompound());
    }
    
    /*** @param stack
     *            The TapeMeasure the player is using
     * @param index
     *            What index the player is selected in the GUI */
    public void clear(ItemStack stack, int index, EntityPlayer player) {
        NBTTagCompound nbt = stack.getTagCompound();
        
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
    
    public NBTTagCompound getNBTData(ItemStack stack) {
        return stack.getTagCompound();
    }
    
    public void writeNBTData(ItemStack stack, NBTTagCompound nbt) {
        stack.setTagCompound(nbt);
    }
    
    public static void setMax(int maxMeasurements) {
        
    }
    
    public static int getMax() {
        return 50;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (worldIn != null && !stack.hasTagCompound()) {
            NBTTagCompound stackNBT = new NBTTagCompound();
            NBTTagCompound measurementList = new NBTTagCompound();
            NBTTagCompound data = new NBTTagCompound();
            data.setInteger("context", ItemMultiTiles.currentContext.index);
            data.setString("shape", "Box");
            data.setInteger("color", ColorUtils.WHITE);
            measurementList.setTag("0", data);
            stackNBT.setTag("measurements", measurementList);
            stackNBT.setInteger("index", 0);
            stack.setTagCompound(stackNBT);
        }
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
        NBTTagCompound nbt = new NBTTagCompound();
        if (stack.hasTagCompound()) {
            stackNBT = getNBTData(stack);
            index = stackNBT.hasKey("index") ? stackNBT.getInteger("index") : 0;
        }
        
        LittleGridContext context = getContext(nbt);
        int contextSize = context.size;
        RayTraceResult res = plr.rayTrace(6.0, Minecraft.getMinecraft().getRenderPartialTicks());
        LittleAbsoluteVec absPos = new LittleAbsoluteVec(res, context);
        
        if (LittleAction.isUsingSecondMode(plr))
            position.facing = position.facing.getOpposite();
        
        Vec3d posOffsetted = StructureUtils.facingOffset(absPos.getPosX(), absPos.getPosY(), absPos.getPosZ(), contextSize,
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
        writeNBTData(stack, stackNBT);
        PacketHandler.sendPacketToServer(new PacketUpdateNBT(stack));
        return false;
        
    }
    
    public static LittleGridContext getContext(NBTTagCompound nbt) {
        return (nbt.hasKey("context") && nbt.getInteger("context") > 0 && nbt.getInteger(
            "context") < LittleGridContext.gridSizes.length) ? LittleGridContext.context[nbt.getInteger(
                "context")] : ItemMultiTiles.currentContext;
    }
    
    public class PosData {
        public SelectLittleTile tilePosMin;
        public SelectLittleTile tilePosMax;
        public SelectLittleTile tilePosCursor;
        public RayTraceResult result;
        
        public PosData(SelectLittleTile posMin, SelectLittleTile posMax, SelectLittleTile posCursor, RayTraceResult res) {
            tilePosMin = posMin;
            tilePosMax = posMax;
            tilePosCursor = posCursor;
            result = res;
        }
        
        public PosData(SelectLittleTile pos, SelectLittleTile posCursor, RayTraceResult res) {
            tilePosMin = pos;
            tilePosMax = pos;
            tilePosCursor = posCursor;
            result = res;
        }
    }
    
    @Override
    public void tick(EntityPlayer player, ItemStack stack, PlacementPosition position, RayTraceResult result) {
        NBTTagCompound stackNBT = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
        
        if (stackNBT.hasKey("index")) {
            int index = stackNBT.getInteger("index");
            NBTTagCompound nbt = new NBTTagCompound();
            if (stackNBT.hasKey("measurement_" + index)) {
                NBTTagList l = stackNBT.getTagList("measurement_" + index, NBT.TAG_COMPOUND);
                nbt = l.getCompoundTagAt(0);
            }
            
            LittleGridContext context = ItemTapeMeasure.getContext(nbt);
            LittleAbsoluteVec pos = new LittleAbsoluteVec(result, context);
            Vec3d posEdit = StructureUtils.facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(), context.size,
                result.sideHit);
            
            TapeRenderer.renderCursor(posEdit, context);
        }
    }
    
    public void onKeyPress(int pressedKey, EntityPlayer player, ItemStack stack) {
        if (pressedKey == TapeMeasureKeyEventHandler.CLEAR) {
            clear(stack, stack.getTagCompound().getInteger("index"), player);
        }
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
                NBTTagCompound measurementList = stackNBT.getCompoundTag("measurements");
                NBTTagCompound data = new NBTTagCompound();
                data.setInteger("context", ItemMultiTiles.currentContext.index);
                data.setString("shape", "Box");
                data.setInteger("color", ColorUtils.WHITE);
                measurementList.setTag(index + "", data);
                stackNBT.setTag("measurements", measurementList);
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
