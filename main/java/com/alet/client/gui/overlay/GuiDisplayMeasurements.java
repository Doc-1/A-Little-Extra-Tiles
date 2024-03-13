package com.alet.client.gui.overlay;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.client.render.tapemeasure.TapeRenderer;
import com.alet.common.utils.shape.tapemeasure.TapeMeasureShape;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.ColoredDisplayStyle;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.tile.math.vec.LittleVecContext;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

public class GuiDisplayMeasurements extends GuiControl {
    
    public static Style transparentStyle = new Style("Transparent", new ColoredDisplayStyle(0, 0, 0, 40), new ColoredDisplayStyle(90, 90, 90, 60), new ColoredDisplayStyle(90, 90, 90, 50), new ColoredDisplayStyle(198, 198, 198), new ColoredDisplayStyle(0, 0, 0, 100));
    protected static ScaledResolution scaledResolution;
    public static int slotID = -1;
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferbuilder = tessellator.getBuffer();
    public static Minecraft mc = Minecraft.getMinecraft();
    
    public GuiDisplayMeasurements(String name) {
        super(name, 0, 0, 100, 100);
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        EntityPlayer player = mc.player;
        scaledResolution = new ScaledResolution(mc);
        posX = 0;
        posY = 0;
        ItemStack stack = TapeRenderer.tapemeasure;
        
        if (!stack.isEmpty() && stack.hasTagCompound() && !Minecraft.getMinecraft().gameSettings.showDebugInfo) {
            GuiOverlayTextList textList = new GuiOverlayTextList("measurement", 0, 90, 140, getParent());
            textList.setStyle(transparentStyle);
            
            NBTTagCompound stackNBT = stack.getTagCompound();
            if (stackNBT.hasKey("measurements")) {
                NBTTagCompound measurements = (NBTTagCompound) stackNBT.getTag("measurements");
                for (String key : measurements.getKeySet()) {
                    NBTTagCompound measurement = (NBTTagCompound) measurements.getTag(key);
                    int index = Integer.parseInt(key);
                    Color color = ColorUtils.IntToRGBA(measurement.getInteger("color"));
                    TapeMeasureShape shape = null;
                    if (TapeRenderer.cachedMeasurements.containsKey(index))
                        shape = TapeRenderer.cachedMeasurements.get(index);
                    String title = "Measurment " + (index + 1);
                    if (index == stackNBT.getInteger("index"))
                        title = TextFormatting.UNDERLINE + title;
                    textList.addText(title, ColorUtils.WHITE);
                    shape.tryGetText(textList, ColorUtils.RGBAToInt(color));
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.scale(0.9F, 0.9F, 0.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glFlush();
            GlStateManager.enableCull();
            textList.renderControl(helper, 0, getRect());
            GlStateManager.popMatrix();
        }
        
        /*
        if (data != null) {
            
            NBTTagCompound stackNBT = stack.getTagCompound();
            NBTTagList list = new NBTTagList();
            for (int i = 0; i < 10; i++) {
                list = stackNBT.getTagList("measurement_" + i, NBT.TAG_COMPOUND);
                NBTTagCompound nbt = list.getCompoundTagAt(0);
                String shapeName = nbt.getString("shape");
                if (!shapeName.equals("")) {
                    int contextSize = ItemTapeMeasure.getContext(nbt);
                    
                    int colorInt = nbt.hasKey("shape") ? nbt.getInteger("color") : ColorUtils.WHITE;
                    LittleAbsoluteVec pos = new LittleAbsoluteVec(data.result, LittleGridContext.get(contextSize));
                    
                    Vec3d vec1 = StructureUtils.facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(),
                        contextSize, data.result.sideHit);
                    Vec3d vec2 = StructureUtils.facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(),
                        contextSize, data.result.sideHit);
                    Vec3d vec3 = StructureUtils.facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(),
                        contextSize, data.result.sideHit);
                    Vec3d vec4 = StructureUtils.facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(),
                        contextSize, data.result.sideHit);
                    
                    if (nbt.hasKey("x1"))
                        vec1 = new Vec3d(nbt.getDouble("x1"), nbt.getDouble("y1"), nbt.getDouble("z1"));
                    if (nbt.hasKey("x2"))
                        vec2 = new Vec3d(nbt.getDouble("x2"), nbt.getDouble("y2"), nbt.getDouble("z2"));
                    
                    if (nbt.hasKey("x3"))
                        vec3 = new Vec3d(nbt.getDouble("x3"), nbt.getDouble("y3"), nbt.getDouble("z3"));
                    if (nbt.hasKey("x4"))
                        vec4 = new Vec3d(nbt.getDouble("x4"), nbt.getDouble("y4"), nbt.getDouble("z4"));
                    
                    if (nbt.hasKey("x1") || nbt.hasKey("x2") || nbt.hasKey("x3") || nbt.hasKey("x4")) {
                        try {
                            List<Vec3d> listOfPoints = new ArrayList<Vec3d>();
                            listOfPoints.add(vec1);
                            listOfPoints.add(vec2);
                            listOfPoints.add(vec3);
                            listOfPoints.add(vec4);
                            TapeMeasureShape shape = TapemeasureShapeRegistar.registeredShapes.get(shapeName)
                                    .getConstructor(List.class, int.class).newInstance(listOfPoints, contextSize);
                            textList.addText("Measurment " + (i + 1), ColorUtils.WHITE);
                            shape.getText(textList, colorInt);
                        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
            
            textList.renderControl(helper, 0, getRect());
            
        }*/
        
    }
    
    /*
     * 
                        if ((nbt.hasKey("x" + index1) || nbt.hasKey("x" + index2))) {
                            
                            String contextSize = "context" + index1;
                            int color = (nbt.hasKey("color" + index1)) ? nbt.getInteger("color" + index1) : ColorUtils.WHITE;
                            
                            LittleAbsoluteVec pos = new LittleAbsoluteVec(data.result, LittleGridContext.get(Integer.parseInt(list.get(nbt.getInteger(contextSize)))));
                            double[] posEdit = ItemTapeMeasure
                                    .facingOffset(pos.getPosX(), pos.getPosY(), pos.getPosZ(), Integer.parseInt(list.get(nbt.getInteger(contextSize))), data.result.sideHit);
                            
                            String xKey1 = "x" + index1;
                            String yKey1 = "y" + index1;
                            String zKey1 = "z" + index1;
                            
                            String xKey2 = "x" + index2;
                            String yKey2 = "y" + index2;
                            String zKey2 = "z" + index2;
                            
                            double x1 = posEdit[0];
                            double y1 = posEdit[1];
                            double z1 = posEdit[2];
                            
                            double x2 = posEdit[0];
                            double y2 = posEdit[1];
                            double z2 = posEdit[2];
                            
                            if (nbt.hasKey("x" + index1)) {
                                x1 = Double.parseDouble(nbt.getString(xKey1));
                                y1 = Double.parseDouble(nbt.getString(yKey1));
                                z1 = Double.parseDouble(nbt.getString(zKey1));
                            }
                            
                            if (nbt.hasKey("x" + index2)) {
                                x2 = Double.parseDouble(nbt.getString(xKey2));
                                y2 = Double.parseDouble(nbt.getString(yKey2));
                                z2 = Double.parseDouble(nbt.getString(zKey2));
                            }
                            
                            boxes.add(new AxisAlignedBB(x1, y1, z1, x2, y2, z2));
                            
                            if (nbt.getInteger("shape" + index1) == 0) {
                                Box box = new Box(x1, y1, z1, x2, y2, z2, Integer.parseInt(list.get(nbt.getInteger(contextSize))));
                                if (((index / 2)) == nbt.getInteger("index"))
                                    textList.addText(TextFormatting.UNDERLINE + "Measurment " + ((index / 2) + 1), ColorUtils.WHITE);
                                else
                                    textList.addText("Measurment " + ((index / 2) + 1), ColorUtils.WHITE);
                                
                                textList.addText("X: " + box.xString, color);
                                textList.addText("Y: " + box.yString, color);
                                textList.addText("Z: " + box.zString, color);
                                
                            } else if (nbt.getInteger("shape" + index1) == 1) {
                                Line line = new Line(x1, y1, z1, x2, y2, z2, Integer.parseInt(list.get(nbt.getInteger(contextSize))));
                                if (((index / 2)) == nbt.getInteger("index"))
                                    textList.addText(TextFormatting.UNDERLINE + "Measurment " + ((index / 2) + 1), ColorUtils.WHITE);
                                else
                                    textList.addText("Measurment " + ((index / 2) + 1), ColorUtils.WHITE);
                                textList.addText("Line: " + line.distance, color);
                            }
                        }
     */
    
    /*** @param playerPos
     * @param playerLookVector
     * @param boxes
     *            Your list of things that player hovers over.
     * @param maxDistance
     * @return */
    public static AxisAlignedBB getBox(Vec3d playerPos, Vec3d playerLookVector, List<AxisAlignedBB> boxes, double maxDistance) {
        AxisAlignedBB result = null;
        AxisAlignedBB box = null;
        Vec3d startPos = null;
        Vec3d endPos = null;
        double distance = 0, x3, y3, z3 = 0;
        for (int i = 0, m = boxes.size(); i < m; i++) {
            box = boxes.get(i);
            endPos = playerPos.addVector(playerLookVector.x * maxDistance, playerLookVector.y * maxDistance,
                playerLookVector.z * maxDistance);
            endPos = new Vec3d(endPos.x, endPos.y, endPos.z);
            startPos = new Vec3d(playerPos.x, playerPos.y, playerPos.z);
            distance = startPos.distanceTo(endPos);
        }
        for (double j = 0; j < distance; j += 0.25) {
            x3 = startPos.x + (j / distance) * (endPos.x - startPos.x);
            y3 = startPos.y + (j / distance) * (endPos.y - startPos.y);
            z3 = startPos.z + (j / distance) * (endPos.z - startPos.z);
            Vec3d pointPos = getAccurateVec(x3, y3, z3);
            
            if (contains(box, pointPos)) {
                result = box;
                break;
            }
        }
        //box[-1555.25, 4.0, 81.0 -> -1553.75, 4.5, 81.75]
        //(-1555.5, 4.5, 81.5)
        return result;
    }
    
    public static Vec3d getAccurateVec(double x, double y, double z) {
        LittleGridContext context = LittleGridContext.get(4);
        
        x = context.toGridAccurate(x);
        y = context.toGridAccurate(y);
        z = context.toGridAccurate(z);
        
        BlockPos pos = new BlockPos((int) Math.floor(context.toVanillaGrid(x)), (int) Math.floor(context.toVanillaGrid(
            y)), (int) Math.floor(context.toVanillaGrid(z)));
        LittleVecContext contextVec = new LittleVecContext(new LittleVec((int) (x - context.toGridAccurate(pos
                .getX())), (int) (y - context.toGridAccurate(pos.getY())), (int) (z - context.toGridAccurate(pos
                        .getZ()))), context);
        
        return new Vec3d(pos.getX() + contextVec.getPosX(), pos.getY() + contextVec.getPosY(), pos.getZ() + contextVec
                .getPosZ());
    }
    
    /** Returns if the supplied Vec3D is completely inside the bounding box */
    public static boolean contains(AxisAlignedBB box, Vec3d vec) {
        if (vec.x >= box.minX && vec.x <= box.maxX) {
            if (vec.y >= box.minY && vec.y <= box.maxY) {
                return vec.z >= box.minZ && vec.z <= box.maxZ;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

/*
 String distence1 = Box.distence(new Vec3d(Double.parseDouble(nbt.getString("x0")), Double.parseDouble(nbt.getString("y0")), 
						Double.parseDouble(nbt.getString("z0"))), new Vec3d(Double.parseDouble(nbt.getString("x1")), 
						Double.parseDouble(nbt.getString("y1")), Double.parseDouble(nbt.getString("z1"))), 
						Integer.parseInt(list.get(nbt.getInteger("context0"))));
				
				textList.addText("Measurement 1:");
				textList.addText("X: "+Box.xString, ColorUtils.RED);
				textList.addText("Y: "+Box.yString, ColorUtils.RED);
				textList.addText("Z: "+Box.zString, ColorUtils.RED);
				
				String distence2 = Line.distence(new Vec3d(Double.parseDouble(nbt.getString("x2")), Double.parseDouble(nbt.getString("y2")), 
						Double.parseDouble(nbt.getString("z2"))), new Vec3d(Double.parseDouble(nbt.getString("x3")), 
						Double.parseDouble(nbt.getString("y3")), Double.parseDouble(nbt.getString("z3"))),
						Integer.parseInt(list.get(nbt.getInteger("context0"))));

				textList.addText("Measurement 2:");
				textList.addText("Distance: "+distence2, ColorUtils.GREEN);

				String distence3 = Box.distence(new Vec3d(Double.parseDouble(nbt.getString("x4")), Double.parseDouble(nbt.getString("y4")), 
						Double.parseDouble(nbt.getString("z4"))), new Vec3d(Double.parseDouble(nbt.getString("x5")), 
						Double.parseDouble(nbt.getString("y5")), Double.parseDouble(nbt.getString("z5"))),
						Integer.parseInt(list.get(nbt.getInteger("context4"))));
				
				textList.addText("Measurement 3:");
				textList.addText("X: "+Box.xString, ColorUtils.BLUE);
				textList.addText("Y: "+Box.yString, ColorUtils.BLUE);
				textList.addText("Z: "+Box.zString, ColorUtils.BLUE);

*/