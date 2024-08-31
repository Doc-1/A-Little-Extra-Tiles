package com.alet.common.structure.type.programable.basic.activator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.alet.common.packet.PacketGetServerScoreboard;
import com.alet.common.structure.type.programable.basic.LittleTriggerObject;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerActivatorIsScoreboard extends LittleTriggerActivator {
    public int value = 0;
    public String scoreName = "";
    public int operation = 0;
    World world = Minecraft.getMinecraft().world;
    
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
    
    @Override
    public void loopRules(HashSet<Entity> entities, boolean shouldContinue, boolean flag) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean shouldRun(World world, HashSet<Entity> entities) {
        System.out.println(world.getEntities(EntityLiving.class, null));
        return false;
    }
    
    @Override
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        nbt.setInteger("value", value);
        nbt.setInteger("operation", operation);
        nbt.setString("scoreName", scoreName);
        return nbt;
    }
    
    @Override
    public LittleTriggerObject deserializeNBT(NBTTagCompound nbt) {
        this.value = nbt.getInteger("value");
        this.operation = nbt.getInteger("operation");
        this.scoreName = nbt.getString("scoreName");
        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        Scoreboard score = world.getScoreboard();
        PacketHandler.sendPacketToServer(new PacketGetServerScoreboard(""));
        Collection<ScoreObjective> objectives = score.getScoreObjectives();
        List<String> list = new ArrayList<String>();
        for (Iterator<ScoreObjective> iterator = objectives.iterator(); iterator.hasNext();) {
            ScoreObjective obj = iterator.next();
            list.add(obj.getName());
        }
        panel.addControl(new GuiLabel("Scoreboard:", 0, 2));
        panel.addControl(new GuiLabel("If Value is", 0, 24));
        panel.addControl(new GuiTextfield("value", value + "", 86, 24, 60, 10).setNumbersIncludingNegativeOnly());
        GuiComboBox cBox = new GuiComboBox("ls", 68, 0, 0, list);
        cBox.setCaption(scoreName);
        cBox.width = 90;
        panel.addControl(cBox);
        List<String> operations = new ArrayList<String>();
        operations.add("=");
        operations.add("<");
        operations.add(">");
        operations.add(">=");
        operations.add("<=");
        GuiComboBox op = (new GuiComboBox("operation", 58, 22, 20, operations) {
            @Override
            public void setCaption(String caption) {
                this.caption = caption;
            }
        });
        String cap = "";
        switch (operation) {
            case 0:
                cap = "=";
                break;
            case 1:
                cap = "<";
                break;
            case 2:
                cap = ">";
                break;
            case 3:
                cap = "<=";
                break;
            case 4:
                cap = ">=";
                break;
            default:
                break;
        }
        op.setCaption(cap);
        panel.addControl(op);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiComboBox) {
            GuiComboBox cBox = (GuiComboBox) source;
            if (cBox.is("ls"))
                scoreName = cBox.getCaption();
            else if (cBox.is("operation")) {
                switch (cBox.getCaption()) {
                    case "=":
                        this.operation = 0;
                        break;
                    case "<":
                        this.operation = 1;
                        break;
                    case ">":
                        this.operation = 2;
                        break;
                    case "<=":
                        this.operation = 3;
                        break;
                    case ">=":
                        this.operation = 4;
                        break;
                    default:
                        break;
                }
            }
        }
        if (source instanceof GuiTextfield) {
            GuiTextfield tField = (GuiTextfield) source;
            if (tField.name.equals("value") && !tField.text.isEmpty())
                value = Integer.parseInt(tField.text);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public GuiLabel shortenName(String name, int y) {
        GuiLabel label = new GuiLabel(name, 0, y * 15);
        String fullName = new String(name);
        if (name.length() > 10) {
            name = name.substring(0, 10);
            label.setCaption(name + "...");
            label.setCustomTooltip(fullName);
        }
        
        return label;
    }
}
