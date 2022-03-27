package com.alet.client.gui.controls.programmer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alet.common.programmer.functions.FunctionSleep;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;

public class BlueprintExecutor {
    
    public Map<String, Function> functions;
    private String[] functionNames;
    public LittleStructure structure;
    public int pausedFor = 0;
    public boolean pause = false;
    
    public BlueprintExecutor(LittleStructure structure, Map<String, Function> functions) {
        this.structure = structure;
        this.functions = functions;
        for (Function f : this.functions.values()) {
            f.executor = this;
        }
        functionNames = (String[]) functions.keySet().toArray(new String[] {});
    }
    
    /** @param index
     * @return
     *         >0 -> a sleep has been triggered. And the number is the index to resume at.
     *         <br>
     *         0 -> is a successful run
     *         <br>
     *         -1 -> is a failure of completing the trigger
     *         <br>
     *         -2 -> is a failure of completing a function */
    public int run(Integer index) {
        updateValues();
        Function event;
        boolean flag = false;
        if (index == 0) {
            event = this.functions.get(this.functionNames[0]);
            event.run();
            flag = event.completedRun();
            index += 1;
        } else
            flag = true;
        
        if (flag)
            for (; index < this.functionNames.length; index++) {
                event = functions.get(functionNames[index]);
                event.run();
                if (event instanceof FunctionSleep) {
                    this.pause = true;
                    this.pausedFor = ((FunctionSleep) event).delay;
                    return index + 1;
                }
                if (!event.completedRun())
                    return -2;
            }
        else
            return -1;
        
        return 0;
    }
    
    public void updateValues() {
        try {
            for (Function function : this.functions.values()) {
                List<Object> values = new ArrayList<Object>();
                for (String valueName : function.values) {
                    if (valueName.equals("i0"))
                        values.add(((LittleSignalInput) structure.getChild(0).getStructure()).getState());
                    else if (valueName.equals("i1"))
                        values.add(((LittleSignalInput) structure.getChild(1).getStructure()).getState());
                    else if (valueName.equals("i2"))
                        values.add(((LittleSignalInput) structure.getChild(2).getStructure()).getState());
                    else if (valueName.equals("o10"))
                        values.add((LittleSignalOutput) structure.getChild(10).getStructure());
                    else if (valueName.equals("false"))
                        values.add(new boolean[] { false });
                    else if (valueName.equals("true"))
                        values.add(new boolean[] { true });
                    else
                        values.add(valueName);
                    
                }
                if (!values.isEmpty())
                    function.setValues(values);
            }
        } catch (CorruptedConnectionException | NotYetConnectedException e) {
            e.printStackTrace();
        }
    }
    
}
