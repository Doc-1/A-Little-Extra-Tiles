package com.alet.common.structures.type.programable.basic.conditions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.alet.common.packets.PacketGetServerScoreboard;
import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleTriggerConditionScoreboard extends LittleTriggerCondition {
    
    public int value = 0;
    public String scoreName = "";
    public int operation = 0;
    private WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
    
    public LittleTriggerConditionScoreboard(int id) {
        super(id);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public boolean conditionPassed() {
        for (Entity entity : this.getEntities()) {
            Scoreboard scoreBoard = world.getScoreboard();
            for (Score sc : scoreBoard.getScores()) {
                if (sc.getObjective().getName().equals(scoreName)) {
                    switch (operation) {
                        case 0:
                            if (sc.getScorePoints() == value)
                                return true;
                            break;
                        case 1:
                            if (sc.getScorePoints() < value)
                                return true;
                            break;
                        case 2:
                            if (sc.getScorePoints() > value)
                                return true;
                            break;
                        case 3:
                            if (sc.getScorePoints() <= value)
                                return true;
                            break;
                        case 4:
                            if (sc.getScorePoints() >= value)
                                return true;
                            break;
                        default:
                            break;
                    }
                }
            }
            
        }
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
    public LittleTriggerCondition deserializeNBT(NBTTagCompound nbt) {
        this.value = nbt.getInteger("value");
        this.operation = nbt.getInteger("operation");
        this.scoreName = nbt.getString("scoreName");
        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createGuiControls(GuiPanel panel, LittlePreviews previews) {
        PacketHandler.sendPacketToServer(new PacketGetServerScoreboard());
        panel.getGui().getPlayer();
        Scoreboard score = world.getScoreboard();
        Collection<ScoreObjective> objectives = score.getScoreObjectives();
        List<String> list = new ArrayList<String>();
        for (Iterator<ScoreObjective> iterator = objectives.iterator(); iterator.hasNext();) {
            ScoreObjective obj = iterator.next();
            list.add(obj.getName());
        }
        panel.addControl(new GuiLabel("Type:", 0, 100));
        panel.addControl(new GuiLabel("type", "", 30, 100));
        panel.addControl(new GuiLabel("If Value is", 0, 118));
        panel.addControl(new GuiTextfield("value", value + "", 86, 118, 30, 10).setNumbersIncludingNegativeOnly());
        panel.addControl(new GuiScrollBox("bx", 0, 40, 152, 50));
        GuiComboBox cBox = new GuiComboBox("ls", 0, 20, 152, list);
        cBox.setCaption(scoreName);
        cBox.width = 158;
        panel.addControl(cBox);
        fillComboBox(cBox);
        panel.addControl(new GuiLabel("Is Scoreboard", 0, 0));
        List<String> operations = new ArrayList<String>();
        operations.add("=");
        operations.add("<");
        operations.add(">");
        operations.add(">=");
        operations.add("<=");
        GuiComboBox op = (new GuiComboBox("operation", 58, 116, 20, operations) {
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
    
    @SideOnly(Side.CLIENT)
    public void fillComboBox(GuiComboBox cBox) {
        GuiPanel panel = (GuiPanel) cBox.getGui().get("panel");
        
        GuiScrollBox sBox = (GuiScrollBox) panel.get("bx");
        Scoreboard score = world.getScoreboard();
        GuiLabel label = null;
        
        wipeControls(sBox);
        int i = 0;
        String caption = cBox.getCaption();
        for (Score sc : score.getScores()) {
            String s = sc.getPlayerName().replaceAll("-", "");
            if (!sc.getObjective().getName().equals(caption)) {
                continue;
            }
            if (s.length() == 32) {
                String m = s.substring(0, 16);
                String l = s.substring(16, 32);
                long most = new BigInteger(m, 16).longValue();
                long least = new BigInteger(l, 16).longValue();
                UUID uuid = new UUID(most, least);
                Entity entity = world.getEntityFromUuid(uuid);
                if (entity.getCustomNameTag() != null && !entity.getCustomNameTag().equals("")) {
                    label = shortenName(entity.getCustomNameTag(), i);
                    label.setCustomTooltip(sc.getPlayerName());
                } else {
                    label = shortenName(sc.getPlayerName(), i);
                }
            } else {
                label = shortenName(sc.getPlayerName(), i);
            }
            GuiLabel value = new GuiLabel("" + sc.getScorePoints(), 100, i * 15);
            GuiLabel type = (GuiLabel) panel.get("type");
            type.setCaption(sc.getObjective().getCriteria().getName());
            sBox.addControl(label);
            sBox.addControl(value);
            i++;
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void guiChangedEvent(CoreControl source) {
        if (source instanceof GuiComboBox) {
            GuiComboBox cBox = (GuiComboBox) source;
            if (cBox.is("ls")) {
                fillComboBox(cBox);
                scoreName = cBox.getCaption();
            } else if (cBox.is("operation")) {
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
            if (tField.name.equals("value"))
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
