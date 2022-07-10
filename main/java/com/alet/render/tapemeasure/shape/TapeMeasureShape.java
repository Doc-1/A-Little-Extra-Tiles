package com.alet.render.tapemeasure.shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;

import com.alet.ALETConfig;
import com.alet.client.gui.overlay.controls.GuiOverlayTextList;
import com.alet.items.ItemTapeMeasure;
import com.alet.tiles.SelectLittleTile;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public abstract class TapeMeasureShape {
    
    private List<Vec3d> listOfPoints = new ArrayList<Vec3d>();
    private int contextSize;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static LinkedHashMap<String, Class<? extends TapeMeasureShape>> registeredShapes = new LinkedHashMap<String, Class<? extends TapeMeasureShape>>();
    
    public TapeMeasureShape(List<Vec3d> listOfPoints, int contextSz) {
        this.listOfPoints = listOfPoints;
        contextSize = contextSz;
    }
    
    public TapeMeasureShape() {
        
    }
    
    static {
        registeredShapes.put("Box", Box.class);
        registeredShapes.put("Line", Line.class);
        registeredShapes.put("Compass", Compass.class);
    }
    
    public static void registerShape(String name, Class<? extends TapeMeasureShape> shape) throws Exception {
        if (!registeredShapes.containsKey(name))
            registeredShapes.put(name, shape);
        else
            throw new Exception(name + " already exists");
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
    
    public abstract void getText(GuiOverlayTextList textList, int colorInt);
    
    public abstract void drawShape(List<SelectLittleTile> listOfTilePos, int contextSize, float red, float green, float blue, float alpha);
    
    protected abstract void calculateDistance(List<Vec3d> listOfPoints, int contextSize);
    
    public void calculateDistance() {
        calculateDistance(listOfPoints, contextSize);
    }
}
