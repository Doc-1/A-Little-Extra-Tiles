package com.alet.client.gui.controls.programmer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;

public class BlueprintExecutor {
    
    public Map<String, Function> functions;
    public LittleStructure structure;
    public String pastFunction;
    
    public BlueprintExecutor(LittleStructure structure, Map<String, Function> functions) {
        this.structure = structure;
        this.functions = functions;
    }
    
    public void run() {
        
        updateValues();
        String[] arr = (String[]) functions.keySet().toArray(new String[] {});
        if (functions.get(arr[0]).isEvent()) {
            Function event = functions.get(arr[0]);
            pastFunction = arr[0];
            event.run();
            if (event.completedRun()) {
                String next = event.nextFunction;
                int b = 0;
                while (!next.equals("")) {
                    b++;
                    Function nextFunction = functions.get(next);
                    nextFunction.run();
                    if (nextFunction.completedRun())
                        next = nextFunction.nextFunction;
                    if (b == 10)
                        break;
                }
            }
        }
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
