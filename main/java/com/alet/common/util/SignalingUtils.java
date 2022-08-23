package com.alet.common.util;

import java.util.Random;

import com.creativemd.creativecore.common.utils.math.BooleanUtils;

public class SignalingUtils {
    
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
        return new boolean[bandwidth];
    }
    
    public static boolean[] flipBits(boolean[] state) {
        boolean[] flip = new boolean[state.length];
        for (int i = 0; i < state.length; i++) {
            flip[i] = !state[i];
        }
        return flip;
    }
}
