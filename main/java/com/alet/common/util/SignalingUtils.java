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
}
