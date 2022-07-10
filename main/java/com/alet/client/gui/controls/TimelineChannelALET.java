package com.alet.client.gui.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;

public abstract class TimelineChannelALET<T> {
    
    public int index;
    public String name;
    public List<KeyControlALET<T>> controls = new ArrayList<>();
    
    public TimelineChannelALET(String name) {
        this.name = name;
    }
    
    public TimelineChannelALET addKeys(PairList<Integer, T> keys) {
        if (keys == null || keys.isEmpty()) {
            addKey(0, getDefault());
            return this;
        }
        for (Pair<Integer, T> pair : keys) {
            addKey(pair.key, pair.value);
        }
        return this;
    }
    
    public TimelineChannelALET<T> addKeyFixed(int tick, T value) {
        KeyControlALET control = this.addKey(tick, value);
        control.modifiable = false;
        return this;
    }
    
    public KeyControlALET addKey(int tick, T value) {
        value = (T) ((double[]) value).clone();
        KeyControlALET control = new KeyControlALET(this, controls.size(), tick, value);
        for (int i = 0; i < controls.size(); i++) {
            KeyControlALET other = controls.get(i);
            
            if (other.tick == tick)
                return null;
            
            if (other.tick > tick) {
                controls.add(i, control);
                return control;
            }
        }
        
        controls.add(control);
        return control;
    }
    
    public void removeKey(KeyControlALET control) {
        controls.remove(control);
    }
    
    public void movedKey(KeyControlALET control) {
        Collections.sort(controls);
    }
    
    public boolean isSpaceFor(KeyControlALET control, int tick) {
        for (int i = 0; i < controls.size(); i++) {
            int otherTick = controls.get(i).tick;
            if (otherTick == tick)
                return false;
            if (otherTick > tick)
                return true;
        }
        return true;
    }
    
    public T getValueAt(int tick) {
        if (controls.isEmpty())
            return getDefault();
        
        int higher = controls.size();
        for (int i = 0; i < controls.size(); i++) {
            int otherTick = controls.get(i).tick;
            if (otherTick == tick)
                return controls.get(i).value;
            if (otherTick > tick) {
                higher = i;
                break;
            }
        }
        
        if (higher == 0 || higher == controls.size())
            return controls.get(higher == 0 ? 0 : controls.size() - 1).value;
        
        KeyControlALET<T> before = controls.get(higher - 1);
        KeyControlALET<T> after = controls.get(higher);
        double percentage = (double) (tick - before.tick) / (after.tick - before.tick);
        return getValueAt(before, after, percentage);
    }
    
    protected abstract T getValueAt(KeyControlALET<T> before, KeyControlALET<T> after, double percentage);
    
    protected abstract T getDefault();
    
    public PairList<Integer, T> getPairs() {
        if (controls.isEmpty())
            return null;
        boolean fixed = true;
        PairList<Integer, T> list = new PairList<>();
        for (KeyControlALET<T> control : controls) {
            if (control.modifiable)
                fixed = false;
            list.add(control.tick, control.value);
        }
        if (fixed)
            return null;
        return list;
    }
    
    public static class TimelineChannelDoorData extends TimelineChannelALET<double[]> {
        
        public TimelineChannelDoorData(String name) {
            super(name);
        }
        
        @Override
        protected double[] getValueAt(KeyControlALET<double[]> before, KeyControlALET<double[]> after, double percentage) {
            double difOffX = Math.round(((after.value[0] - before.value[0]) * percentage + before.value[0]) * 100D) / 100D;
            double difOffY = Math.round(((after.value[1] - before.value[1]) * percentage + before.value[1]) * 100D) / 100D;
            double difOffZ = Math.round(((after.value[2] - before.value[2]) * percentage + before.value[2]) * 100D) / 100D;
            
            double difRotX = Math.round(((after.value[3] - before.value[3]) * percentage + before.value[3]) * 100D) / 100D;
            double difRotY = Math.round(((after.value[4] - before.value[4]) * percentage + before.value[4]) * 100D) / 100D;
            double difRotZ = Math.round(((after.value[5] - before.value[5]) * percentage + before.value[5]) * 100D) / 100D;
            
            double[] arr = { difOffX, difOffY, difOffZ, difRotX, difRotY, difRotZ };
            return arr.clone();
        }
        
        @Override
        protected double[] getDefault() {
            return new double[8];
        }
        
    }
    
}
