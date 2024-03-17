package com.alet.common.utils.shape.tapemeasure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.vecmath.Point3d;

import com.alet.ALETConfig;
import com.alet.client.render.tapemeasure.TapeRenderer;
import com.alet.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MeasurementShape {
    
    protected int pointsNeeded;
    protected final String shapeName;
    
    public MeasurementShape(int pointsNeeded, String shapeName) {
        this.pointsNeeded = pointsNeeded;
        this.shapeName = shapeName;
    }
    
    public String getKey() {
        return shapeName;
    }
    
    @SideOnly(Side.CLIENT)
    public String getLocalizedName() {
        return GuiControl.translateOrDefault("shape." + getKey(), getKey());
    }
    
    public static double getDistence(double pos_1, double pos_2, int contextSize) {
        LittleGridContext context = LittleGridContext.get(contextSize);
        
        double contDecimal = 1D / context.size;
        double distence = (Math.abs(pos_1 - pos_2)) + contDecimal;
        
        return distence;
    }
    
    protected static double cleanDouble(double doub) {
        String clean = String.format("%.3f", doub);
        doub = Double.parseDouble(clean);
        return doub;
    }
    
    public static double changeMesurmentType(double toChange) {
        int x = ItemTapeMeasure.measurementType;
        if (x != 0) {
            String s = ALETConfig.tapeMeasure.measurementEquation.get(x - 1);
            List<String> arg = Arrays.asList(s.split("M"));
            
            if (arg.size() == 2) {
                if (arg.get(0).equals("("))
                    return evaluate("( " + toChange + arg.get(1));
                else
                    return evaluate(toChange + arg.get(1));
            }
        }
        return 0;
    }
    
    /** Author: geeksforgeeks
     * modified to take decimal */
    public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        
        Stack<Double> values = new Stack<Double>();
        Stack<Character> ops = new Stack<Character>();
        
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;
            if (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.') {
                StringBuffer sbuf = new StringBuffer();
                
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9' || (i < tokens.length && tokens[i] == '.'))
                    sbuf.append(tokens[i++]);
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (tokens[i] == '(')
                ops.push(tokens[i]);
            else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek()))
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                ops.push(tokens[i]);
            }
        }
        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        
        return values.pop();
    }
    
    /** Author: geeksforgeeks */
    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }
    
    /** Author: geeksforgeeks */
    public static double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new UnsupportedOperationException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }
    
    private static void drawBoundingBox(BufferBuilder buffer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red, float green, float blue, float alpha) {
        buffer.pos(minX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(minX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(minX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, minY, maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, maxY, minZ).color(red, green, blue, 0.0F).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(maxX, minY, minZ).color(red, green, blue, 0.0F).endVertex();
    }
    
    public static List<Point3d> drawCube(Point3d point, int contextSize, float red, float green, float blue, float alpha) {
        double conDiv = 1D / contextSize;
        double minX = (point.x);
        double minY = (point.y);
        double minZ = (point.z);
        
        double maxX = (point.x + conDiv);
        double maxY = (point.y + conDiv);
        double maxZ = (point.z + conDiv);
        List<Point3d> points = new ArrayList<>();
        points.add(new Point3d(minX, minY, minZ));
        points.add(new Point3d(maxX, maxY, maxZ));
        drawBoundingBox(TapeRenderer.bufferbuilder, minX - 0.001, minY - 0.001, minZ - 0.001, maxX + 0.001, maxY + 0.001,
            maxZ + 0.001, red, green, blue, alpha);
        return points;
    }
    
    public static List<Point3d> drawBox(Point3d point1, Point3d point2, int contextSize, float red, float green, float blue, float alpha) {
        double conDiv = 1D / contextSize;
        double minX = point1.x;
        double minY = point1.y;
        double minZ = point1.z;
        
        double maxX = point2.x + conDiv;
        double maxY = point2.y + conDiv;
        double maxZ = point2.z + conDiv;
        
        if (minX >= maxX) {
            minX += conDiv;
            maxX -= conDiv;
        }
        if (minZ >= maxZ) {
            minZ += conDiv;
            maxZ -= conDiv;
        }
        if (minY >= maxY) {
            minY += conDiv;
            maxY -= conDiv;
        }
        List<Point3d> points = new ArrayList<>();
        points.add(new Point3d(minX, minY, minZ));
        points.add(new Point3d(maxX, maxY, maxZ));
        drawBoundingBox(TapeRenderer.bufferbuilder, minX - 0.001, minY - 0.001, minZ - 0.001, maxX + 0.001, maxY + 0.001,
            maxZ + 0.001, red, green, blue, alpha);
        return points;
    }
    
    protected abstract void drawText(List<Point3d> points, List<String> measurementUnits, int contextSize, int colorInt);
    
    public void tryDrawShape(List<Point3d> points, LittleGridContext context, float red, float green, float blue, float alpha) {
        
        if (points != null && !points.isEmpty() && points.size() >= this.pointsNeeded) {
            drawShape(points, context, red, green, blue, alpha);
        }
    }
    
    protected abstract void drawShape(List<Point3d> points, LittleGridContext context, float red, float green, float blue, float alpha);
    
    public List<String> tryGetMeasurementUnits(List<Point3d> points, LittleGridContext context) {
        if (points != null && points.size() >= this.pointsNeeded)
            return getMeasurementUnits(points, context);
        return new ArrayList<String>();
    }
    
    protected abstract List<String> getMeasurementUnits(List<Point3d> points, LittleGridContext context);
    
    public abstract List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context);
    
}
