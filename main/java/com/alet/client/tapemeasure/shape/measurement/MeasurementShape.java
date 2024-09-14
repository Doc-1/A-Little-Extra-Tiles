package com.alet.client.tapemeasure.shape.measurement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.alet.ALETConfig;
import com.alet.client.tapemeasure.TapeRenderer;
import com.alet.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
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
    
    public boolean hasPointsNeeded(HashMap<Integer, Point3f> points) {
        for (int i = 0; i < pointsNeeded; i++) {
            if (!points.containsKey(i))
                return false;
        }
        return true;
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
    
    public static Vector3f rotateVector(Vector3f vec, Vector3f axis, double theta) {
        double x, y, z;
        double u, v, w;
        x = vec.getX();
        y = vec.getY();
        z = vec.getZ();
        u = axis.getX();
        v = axis.getY();
        w = axis.getZ();
        float xPrime = (float) (u * (u * x + v * y + w * z) * (1d - Math.cos(theta)) + x * Math.cos(
            theta) + (-w * y + v * z) * Math.sin(theta));
        float yPrime = (float) (v * (u * x + v * y + w * z) * (1d - Math.cos(theta)) + y * Math.cos(
            theta) + (w * x - u * z) * Math.sin(theta));
        float zPrime = (float) (w * (u * x + v * y + w * z) * (1d - Math.cos(theta)) + z * Math.cos(
            theta) + (-v * x + u * y) * Math.sin(theta));
        return new Vector3f(xPrime, yPrime, zPrime);
    }
    
    public static Point3f getMidPoint(Point3f point2, Point3f point1) {
        return new Point3f((point2.x + point1.x) / 2, (point2.y + point1.y) / 2, (point2.z + point1.z) / 2);
    }
    
    public static float[] getLineAngle(Point3f startPoint, Point3f endPoint) {
        float deltaX = startPoint.x - endPoint.x;
        float deltaY = startPoint.y - endPoint.y;
        float deltaZ = startPoint.z - endPoint.z;
        float yaw = (float) Math.atan2(deltaZ, deltaX);
        float pitch = (float) Math.atan2(Math.sqrt(deltaZ * deltaZ + deltaX * deltaX), deltaY);
        return new float[] { (float) Math.toDegrees(pitch), (float) Math.toDegrees(yaw) };
    }
    
    public static float[] getAngles(Point3f point1, Point3f point2, Point3f point3) {
        if (point1 != null && point2 != null && point3 != null) {
            Point3f C = point1; //is angle C
            Point3f A = point2; //is angle A
            Point3f B = point3; //is angle B
            
            double a = C.distance(B);
            double b = A.distance(C);
            double c = A.distance(B);
            
            float a2 = (float) Math.pow(a, 2);
            float b2 = (float) Math.pow(b, 2);
            float c2 = (float) Math.pow(c, 2);
            
            return new float[] { (float) Math.toDegrees(Math.acos((b2 + c2 - a2) / (2 * b * c))), (float) Math.toDegrees(Math
                    .acos((c2 + a2 - b2) / (2 * c * a))), (float) Math.toDegrees(Math.acos((a2 + b2 - c2) / (2 * a * b))) };
        }
        return new float[0];
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
    
    public static HashMap<Integer, Point3f> drawLine(Point3f p1, Point3f p2, int contextSize, float red, float green, float blue, float alpha) {
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        double conDiv = 0.5D / contextSize;
        double minX = p1.x + conDiv;
        double minY = p1.y + conDiv;
        double minZ = p1.z + conDiv;
        
        double maxX = p2.x + conDiv;
        double maxY = p2.y + conDiv;
        double maxZ = p2.z + conDiv;
        HashMap<Integer, Point3f> points = new HashMap<>();
        points.put(0, new Point3f((float) minX, (float) minY, (float) minZ));
        points.put(1, new Point3f((float) maxX, (float) maxY, (float) maxZ));
        
        bufferbuilder.pos(minX - 0.001, minY - 0.001, minZ - 0.001).color(red, green, blue, 0.0F).endVertex();
        bufferbuilder.pos(maxX - 0.001, maxY - 0.001, maxZ - 0.001).color(red, green, blue, 1.0F).endVertex();
        bufferbuilder.pos(minX - 0.001, minY - 0.001, minZ - 0.001).color(red, green, blue, 0.0F).endVertex();
        return points;
    }
    
    public static HashMap<Integer, Point3f> drawCube(Point3f point, int contextSize, float red, float green, float blue, float alpha) {
        double conDiv = 1D / contextSize;
        double minX = (point.x);
        double minY = (point.y);
        double minZ = (point.z);
        
        double maxX = (point.x + conDiv);
        double maxY = (point.y + conDiv);
        double maxZ = (point.z + conDiv);
        HashMap<Integer, Point3f> points = new HashMap<>();
        points.put(0, new Point3f((float) minX, (float) minY, (float) minZ));
        points.put(1, new Point3f((float) maxX, (float) maxY, (float) maxZ));
        drawBoundingBox(TapeRenderer.bufferbuilder, minX - 0.001, minY - 0.001, minZ - 0.001, maxX + 0.001, maxY + 0.001,
            maxZ + 0.001, red, green, blue, alpha);
        return points;
    }
    
    public static HashMap<Integer, Point3f> drawBox(Point3f point1, Point3f point2, int contextSize, float red, float green, float blue, float alpha) {
        float conDiv = (float) (1D / contextSize);
        float minX = point1.x;
        float minY = point1.y;
        float minZ = point1.z;
        
        float maxX = point2.x + conDiv;
        float maxY = point2.y + conDiv;
        float maxZ = point2.z + conDiv;
        
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
        HashMap<Integer, Point3f> points = new HashMap<>();
        points.put(0, new Point3f(minX, minY, minZ));
        points.put(1, new Point3f(maxX, minY, minZ));
        points.put(2, new Point3f(minX, minY, maxZ));
        points.put(3, new Point3f(maxX, minY, maxZ));
        
        points.put(4, new Point3f(maxX, maxY, maxZ));
        points.put(5, new Point3f(minX, maxY, maxZ));
        points.put(6, new Point3f(maxX, maxY, minZ));
        points.put(7, new Point3f(minX, maxY, minZ));
        drawBoundingBox(TapeRenderer.bufferbuilder, minX - 0.001, minY - 0.001, minZ - 0.001, maxX + 0.001, maxY + 0.001,
            maxZ + 0.001, red, green, blue, alpha);
        return points;
    }
    
    protected abstract void drawText(HashMap<Integer, Point3f> points, List<String> measurementUnits, int contextSize, int colorInt);
    
    public void tryDrawShape(HashMap<Integer, Point3f> points, LittleGridContext context, float red, float green, float blue, float alpha) {
        
        if (points != null && !points.isEmpty() && this.hasPointsNeeded(points)) {
            drawShape(points, context, red, green, blue, alpha);
        }
    }
    
    protected abstract void drawShape(HashMap<Integer, Point3f> points, LittleGridContext context, float red, float green, float blue, float alpha);
    
    public List<String> tryGetMeasurementUnits(HashMap<Integer, Point3f> points, LittleGridContext context) {
        if (points != null && points.size() >= this.pointsNeeded)
            return getMeasurementUnits(points, context);
        return new ArrayList<String>();
    }
    
    protected abstract List<String> getMeasurementUnits(HashMap<Integer, Point3f> points, LittleGridContext context);
    
    public abstract List<GuiControl> getCustomSettings(NBTTagCompound nbt, LittleGridContext context);
    
}
