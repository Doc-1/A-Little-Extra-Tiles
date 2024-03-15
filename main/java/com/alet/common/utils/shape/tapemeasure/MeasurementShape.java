package com.alet.common.utils.shape.tapemeasure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import javax.vecmath.Point3d;

import com.alet.ALETConfig;
import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.items.ItemTapeMeasure;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

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
    
    public void tryGetText(GuiOverlayTextList textList, List<Point3d> points, LittleGridContext context, int colorInt) {
        List<String> measurementUnits = tryGetMeasurementUnits(points, context);
        if (!measurementUnits.isEmpty())
            getText(textList, measurementUnits, colorInt);
    }
    
    protected abstract void getText(GuiOverlayTextList textList, List<String> measurementUnits, int colorInt);
    
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
