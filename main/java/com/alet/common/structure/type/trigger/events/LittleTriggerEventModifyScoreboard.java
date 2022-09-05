package com.alet.common.structure.type.trigger.events;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.creativemd.creativecore.common.gui.CoreControl;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiComboBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextfield;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class LittleTriggerEventModifyScoreboard extends LittleTriggerEvent {
    
    public int effectPerTick = 0;
    public int value = 0;
    public String scoreName = "";
    private WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
    
    public LittleTriggerEventModifyScoreboard(int id) {
        super(id);
    }
    
    @Override
    public NBTTagCompound createNBT(NBTTagCompound nbt) {
        nbt.setInteger("value", value);
        nbt.setString("scoreName", scoreName);
        nbt.setInteger("tick", effectPerTick);
        return nbt;
    }
    
    @Override
    public LittleTriggerEvent createFromNBT(NBTTagCompound nbt) {
        this.value = nbt.getInteger("value");
        this.scoreName = nbt.getString("scoreName");
        return this;
    }
    
    @Override
    public void updateControls(GuiParent parent) {
        GuiPanel panel = (GuiPanel) parent.get("content");
        Scoreboard score = world.getScoreboard();
        Collection<ScoreObjective> objectives = score.getScoreObjectives();
        List<String> list = new ArrayList<String>();
        for (Iterator iterator = objectives.iterator(); iterator.hasNext();) {
            ScoreObjective obj = (ScoreObjective) iterator.next();
            list.add(obj.getName());
        }
        wipeControls(panel);
        panel.addControl(new GuiLabel("Type:", 0, 100));
        panel.addControl(new GuiLabel("type", "", 30, 100));
        panel.addControl(new GuiLabel("Change Value by:", 0, 118));
        panel.addControl(new GuiTextfield("value", value + "", 90, 118, 30, 10).setNumbersIncludingNegativeOnly());
        panel.addControl(new GuiLabel("For Every Tick:", 8, 137));
        panel.addControl(new GuiTextfield("tick", effectPerTick + "", 90, 137, 30, 10).setNumbersIncludingNegativeOnly());
        panel.addControl(new GuiScrollBox("bx", 0, 40, 152, 50));
        GuiComboBox cBox = new GuiComboBox("ls", 0, 20, 152, list);
        cBox.setCaption(scoreName);
        cBox.width = 158;
        panel.addControl(cBox);
        fillComboBox(cBox);
        panel.addControl(new GuiLabel("Modify Scoreboard", 0, 0));
        
    }
    
    public void fillComboBox(GuiComboBox cBox) {
        GuiPanel panel = (GuiPanel) cBox.getGui().get("panel");
        
        GuiScrollBox sBox = (GuiScrollBox) panel.get("bx");
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
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
    public void updateValues(CoreControl source) {
        if (source instanceof GuiComboBox) {
            GuiComboBox cBox = (GuiComboBox) source;
            fillComboBox(cBox);
            scoreName = cBox.getCaption();
        }
        if (source instanceof GuiTextfield) {
            GuiTextfield tField = (GuiTextfield) source;
            if (tField.name.equals("value"))
                value = Integer.parseInt(tField.text);
            if (tField.name.equals("tick"))
                effectPerTick = Integer.parseInt(tField.text.equals("") ? "0" : tField.text);
        }
    }
    
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
    
    @Override
    public boolean runEvent(HashSet<Entity> entities) {
        
        Collection<ScoreObjective> objectives = world.getScoreboard().getScoreObjectives();
        Collection<Score> scores = world.getScoreboard().getScores();
        ScoreObjective objective = null;
        Score score = null;
        
        for (ScoreObjective obj : objectives) {
            if (obj.getName().equals(scoreName)) {
                objective = obj;
                break;
            }
        }
        
        for (Entity entity : entities) {
            if (entity instanceof EntityPlayerMP)
                score = world.getScoreboard().getOrCreateScore(entity.getName(), objective);
            else
                score = world.getScoreboard().getOrCreateScore(entity.getUniqueID().toString(), objective);
            
        }
        
        if (score != null)
            if (value > 0)
                score.increaseScore(value);
            else if (value < 0)
                score.decreaseScore(Math.abs(value));
        return true;
    }
    
    @Override
    public String getName() {
        return "Modify Scoreboard";
    }
    
}
