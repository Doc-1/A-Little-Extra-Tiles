package com.alet.client.gui.controls.programmer;

import java.util.List;

import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;

public class BlueprintExecutor {
    
    public List<IFunction> functions;
    public LittleStructure structure;
    
    public BlueprintExecutor(LittleStructure structure, List<IFunction> functions) {
        this.structure = structure;
        this.functions = functions;
    }
    
    public void run(Integer tick) {
        if (functions.get(0).isEvent()) {
            try {
                functions.get(0).setValues(((LittleSignalInput) structure.getChild(0).getStructure()).getState());
                functions.get(0).run();
                if (functions.get(0).completedRun())
                    if (tick < functions.size()) {
                        functions.get(tick).run();
                    } else {
                        tick = 0;
                    }
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    public void execute(Integer tick) {
        if (tick < functions.size()) {
            IFunction function = functions.get(tick);
            function.run();
        } else {
            tick = 0;
        }
    }
}
