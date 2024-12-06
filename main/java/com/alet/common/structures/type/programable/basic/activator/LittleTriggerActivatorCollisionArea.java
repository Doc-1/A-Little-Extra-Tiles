package com.alet.common.structures.type.programable.basic.activator;

import java.util.Collection;
import java.util.HashSet;

import com.alet.common.structures.type.programable.basic.LittleTriggerObject;
import com.alet.common.utils.NBTUtils;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerActivatorCollisionArea extends LittleTriggerActivator {
    
    private boolean runWhileCollided = false;
    private double x_min = 0;
    private double y_min = 0;
    private double z_min = 0;
    
    private double x_max = 0;
    private double y_max = 0;
    private double z_max = 0;
    private AxisAlignedBB collisionArea = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("collisionArea")) {
            NBTTagCompound n = (NBTTagCompound) nbt.getTag("collisionArea");
            this.collisionArea = NBTUtils.readAABB(n);
            this.x_min = this.collisionArea.minX;
            this.y_min = this.collisionArea.minY;
            this.z_min = this.collisionArea.minZ;
            this.x_max = this.collisionArea.maxX;
            this.y_max = this.collisionArea.maxY;
            this.z_max = this.collisionArea.maxZ;
        }
        return this;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setTag("collisionArea", NBTUtils.writeAABB(this.collisionArea));
        return nbt;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        panel.addControl(new GuiCheckBox("while_collided", "Run Only While Collided", 0, 0, runWhileCollided));
        panel.addControl(new GuiLabel("X1:", 0, 14));
        panel.addControl(new GuiLabel("X2:", 0, 29));
        panel.addControl(new GuiLabel("Y1:", 52, 14));
        panel.addControl(new GuiLabel("Y2:", 52, 29));
        panel.addControl(new GuiLabel("Z1:", 104, 14));
        panel.addControl(new GuiLabel("Z2:", 104, 29));
        panel.addControl(new GuiTextfield("x_min", collisionArea != null ? collisionArea.minX + "" : "", 18, 14, 30, 8)
                .setFloatOnly());
        panel.addControl(new GuiTextfield("x_max", collisionArea != null ? collisionArea.maxX + "" : "", 18, 29, 30, 8)
                .setFloatOnly());
        panel.addControl(new GuiTextfield("y_min", collisionArea != null ? collisionArea.minY + "" : "", 70, 14, 30, 8)
                .setFloatOnly());
        panel.addControl(new GuiTextfield("y_max", collisionArea != null ? collisionArea.maxY + "" : "", 70, 29, 30, 8)
                .setFloatOnly());
        panel.addControl(new GuiTextfield("z_min", collisionArea != null ? collisionArea.minZ + "" : "", 122, 14, 30, 8)
                .setFloatOnly());
        panel.addControl(new GuiTextfield("z_max", collisionArea != null ? collisionArea.maxZ + "" : "", 122, 29, 30, 8)
                .setFloatOnly());
        
    }
    
    /*
    GuiTextfield text = (GuiTextfield) event.source;
    
    }*/
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source.is("x_min", "x_max", "y_min", "y_max", "z_min", "z_max")) {
            GuiTextfield text = (GuiTextfield) source;
            System.out.println(this.collisionArea);
            double value = Double.parseDouble(text.text);
            if (text.is("x_min"))
                this.x_min = value;
            else if (text.is("x_max"))
                this.x_max = value;
            else if (text.is("y_min"))
                this.y_min = value;
            else if (text.is("y_max"))
                this.y_max = value;
            else if (text.is("z_min"))
                this.z_min = value;
            else if (text.is("z_max"))
                this.z_max = value;
            this.collisionArea = new AxisAlignedBB(x_min, y_min, z_min, x_max, y_max, z_max);
        }
        
    }
    
    @Override
    public boolean shouldRun(World world, HashSet<Entity> entities) {
        if (!world.loadedEntityList.isEmpty()) {
            entities.clear();
            entities.addAll(world.getEntitiesWithinAABB(Entity.class, this.collisionArea));
            if (!entities.isEmpty())
                return true;
        }
        return false;
    }
    
    @Override
    public void loopRules(HashSet<Entity> entities, boolean shouldContinue, boolean flag) {
        // TODO Auto-generated method stub
        
    }
    
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
        // TODO Auto-generated method stub
        
    }
}
