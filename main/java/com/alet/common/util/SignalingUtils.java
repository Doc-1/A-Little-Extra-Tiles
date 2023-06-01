package com.alet.common.util;

import java.util.Random;

import com.creativemd.creativecore.common.utils.math.BooleanUtils;

public class SignalingUtils {
    
    private static final boolean[] FALSE_4 = new boolean[4];
    private static final boolean[] FALSE_16 = new boolean[16];
    private static final boolean[] FALSE_32 = new boolean[32];
    
    public static boolean[] stringToBool(String binary) {
        if (!binary.equals("")) {
            boolean[] bool = new boolean[binary.length()];
            char[] c = binary.toCharArray();
            for (int i = 0; i < binary.length(); i++)
                if (c[i] == '0')
                    bool[i] = false;
                else if (c[i] == '1')
                    bool[i] = true;
                
            return bool;
        }
        return null;
    }
    
    public static boolean[] randState(int bandwidth) {
        int max = (int) Math.pow(bandwidth, 2) + 1;
        Random random = new Random();
        int rand = random.nextInt(max);
        return BooleanUtils.toBits(rand, bandwidth);
        
    }
    
    public static boolean[] randState(int min, int max, int bandwidth) {
        if (min > max) {
            return allFalse(bandwidth);
        } else {
            Random random = new Random();
            int rand = random.nextInt(max - min + 1) + min;
            return BooleanUtils.toBits(rand, bandwidth);
        }
        
    }
    
    public static int boolToInt(boolean[] state) {
        if (state.length > 32)
            throw new RuntimeException("Cannot convert more than 32 bits to an integer");
        int n = 0;
        for (int i = state.length - 1; i >= 0; i--) {
            n = (n << 1) | (state[i] ? 1 : 0);
        }
        return n;
    }
    
    public static boolean[] convertBandwidth(boolean[] state, int bandwidth) {
        boolean[] newState = new boolean[bandwidth];
        for (int i = 0; i < bandwidth; i++) {
            if (state.length <= i)
                break;
            newState[i] = state[i];
        }
        return newState;
        
    }
    
    public static boolean[] mirrorState(boolean[] state) {
        boolean[] mirror = new boolean[state.length];
        int k = 0;
        for (int i = state.length - 1; i >= 0; i--) {
            mirror[k] = state[i];
            k++;
        }
        return mirror;
    }
    
    public static boolean[] allFalse(int bandwidth) {
        if (bandwidth == 4)
            return FALSE_4;
        else if (bandwidth == 16)
            return FALSE_16;
        else if (bandwidth == 32)
            return FALSE_32;
        else {
            new Exception("Bandwidth is a invalid number. Valid numbers are: 4, 16, and 32");
            return BooleanUtils.SINGLE_FALSE;
        }
    }
    
    public static boolean[] flipBits(boolean[] state) {
        boolean[] flip = new boolean[state.length];
        for (int i = 0; i < state.length; i++) {
            flip[i] = !state[i];
        }
        return flip;
    }
    
}
