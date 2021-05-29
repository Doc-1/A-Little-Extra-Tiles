package com.alet.render.tapemeasure.shape;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import com.alet.ALETConfig;
import com.alet.items.ItemTapeMeasure;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

public abstract class Shapes {
	
	Vec3d pos;
	Vec3d pos2;
	int contextSize;
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public Shapes(Vec3d p, Vec3d p2, int contextSz) {
		pos = p;
		pos2 = p2;
		contextSize = contextSz;
	}
	
	public Shapes(double x1, double y1, double z1, double x2, double y2, double z2, int contextSize) {
		this(new Vec3d(x1, y1, z1), new Vec3d(x2, y2, z2), contextSize);
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
			System.out.println(arg.get(0));
			
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
	
	protected abstract void calculateDistance(Vec3d pos, Vec3d pos2, int contextSize);
	
	public void calculateDistance() {
		calculateDistance(pos, pos2, contextSize);
	}
}
