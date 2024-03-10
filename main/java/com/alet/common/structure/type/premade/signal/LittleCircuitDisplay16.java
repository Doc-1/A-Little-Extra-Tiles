package com.alet.common.structure.type.premade.signal;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.alet.common.utils.SignalingUtils;
import com.alet.font.FontReader;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;

import net.minecraft.nbt.NBTTagCompound;

public class LittleCircuitDisplay16 extends LittleStructurePremade {
    public List<Boolean> imageBitsTL = new ArrayList<Boolean>();
    public List<Boolean> imageBitsTR = new ArrayList<Boolean>();
    public List<Boolean> imageBitsBL = new ArrayList<Boolean>();
    public List<Boolean> imageBitsBR = new ArrayList<Boolean>();
    public String charDisplayed = "";
    
    public LittleCircuitDisplay16(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    public void tick() {
        if (!isClient()) {
            try {
                boolean[] b = ((LittleSignalInput) this.children.get(4).getStructure()).getState();
                boolean[] displayData = { b[0], b[1], b[2], b[3], b[4], b[5], b[6], b[7] };
                boolean[] colorData = { b[8], b[9], b[10], b[11] };
                boolean[] empty = { false, false, false, false };
                
                displayData = SignalingUtils.mirrorState(displayData);
                colorData = SignalingUtils.mirrorState(colorData);
                String binary = "";
                for (int i = 0; i < 8; i++) {
                    binary += displayData[i] ? "1" : "0";
                }
                LittleStructure topLeft = this.getChild(0).getStructure();
                LittleStructure topRight = this.getChild(1).getStructure();
                LittleStructure bottomLeft = this.getChild(2).getStructure();
                LittleStructure bottomRight = this.getChild(3).getStructure();
                int parseInt = Integer.parseInt(binary, 2);
                String c = (char) parseInt + "";
                if (!c.equals(charDisplayed)) {
                    imageBitsTL.clear();
                    imageBitsTR.clear();
                    imageBitsBL.clear();
                    imageBitsBR.clear();
                    decodeImageToBits(c);
                }
                int x = 0;
                for (Boolean bits : imageBitsTL) {
                    if (bits)
                        topLeft.getOutput(x).updateState(colorData);
                    else
                        topLeft.getOutput(x).updateState(empty);
                    x++;
                }
                x = 0;
                for (Boolean bits : imageBitsTR) {
                    if (bits)
                        topRight.getOutput(x).updateState(colorData);
                    else
                        topRight.getOutput(x).updateState(empty);
                    x++;
                }
                x = 0;
                for (Boolean bits : imageBitsBL) {
                    if (bits)
                        bottomLeft.getOutput(x).updateState(colorData);
                    else
                        bottomLeft.getOutput(x).updateState(empty);
                    x++;
                }
                x = 0;
                for (Boolean bits : imageBitsBR) {
                    if (bits)
                        bottomRight.getOutput(x).updateState(colorData);
                    else
                        bottomRight.getOutput(x).updateState(empty);
                    x++;
                }
                charDisplayed = c;
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
                
            }
            
        }
    }
    
    public void decodeImageToBits(String character) {
        BufferedImage image = FontReader.fontToPhoto(character, "Press Start 2P", null, 8, ColorUtils.WHITE, 0);
        for (int y = 0; y < image.getHeight(); y++)
            for (int x = 0; x < image.getWidth(); x++) {
                if ((y >= 0 && y < 4) && (x >= 0 && x < 4)) {
                    imageBitsTL.add(image.getRGB(x, y) != 0 ? true : false);
                }
                if ((y >= 0 && y < 4) && (x >= 4 && x < 8)) {
                    imageBitsTR.add(image.getRGB(x, y) != 0 ? true : false);
                }
                if ((y >= 4 && y < 8) && (x >= 0 && x < 4)) {
                    imageBitsBL.add(image.getRGB(x, y) != 0 ? true : false);
                }
                if ((y >= 4 && y < 8) && (x >= 4 && x < 8)) {
                    imageBitsBR.add(image.getRGB(x, y) != 0 ? true : false);
                }
            }
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {}
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {}
    
}
