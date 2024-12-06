package com.alet.components.structures.type.programable.advanced;

import java.util.ArrayList;
import java.util.List;

import com.alet.common.gui.controls.programmable.GuiFunction;
import com.alet.common.gui.controls.programmable.GuiNodeValue;
import com.alet.components.structures.type.programable.advanced.nodes.values.NodeFunction;
import com.alet.components.structures.type.programable.advanced.nodes.values.NodeValue;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Function {
    
    //Executing structure
    public LittleProgramableStructureALET structure;
    
    //Identification Field
    private final String NAME;
    public int id;
    
    //Nodes Fields
    public NodeFunction methodSender;
    public NodeFunction methodReciever;
    public List<NodeValue> senderNodes = new ArrayList<>();
    public List<NodeValue> recieverNodes = new ArrayList<>();
    private boolean isMethodSender;
    private boolean isMethodReciever;
    
    //GUI Fields
    private final int COLOR;
    protected final static int ACTIVATOR_COLOR = 0xFFFFFF;
    protected final static int EVENT_COLOR = 0xFFFFFF;
    protected final static int CONDITION_COLOR = 0xFFFFFF;
    protected final static int GETTER_COLOR = 0xFFFFFF;
    protected final static int VALUE_COLOR = 0xFFFFFF;
    protected final static int CAST_COLOR = 0xFFFFFF;
    protected final static int FLOW_COLOR = 0xFFFFFF;
    
    public Function(String name, int id, int color) {
        this.NAME = name;
        this.id = id;
        this.COLOR = color;
        this.isMethodReciever = false;
        this.isMethodSender = false;
        setMethodNodes();
        setFunctionNodes();
    }
    
    public Function setAsSender() {
        this.isMethodSender = true;
        return this;
    }
    
    public Function setAsReciever() {
        this.isMethodReciever = true;
        return this;
    }
    
    public static Function guiToFunction(GuiFunction guiFunction) {
        return null;
    }
    
    private void setMethodNodes() {
        if (this.isMethodSender())
            this.methodSender = new NodeFunction("method_sender", "", false);
        if (this.isMethodReciever())
            this.methodReciever = new NodeFunction("method_reciever", "", false);
    }
    
    @SideOnly(Side.CLIENT)
    public List<GuiNodeValue> setGuiNodes() {
        List<GuiNodeValue> nodes = new ArrayList<>();
        try {
            for (NodeValue node : senderNodes) {
                nodes.add(node.getGuiNode());
            }
            for (NodeValue node : recieverNodes) {
                nodes.add(node.getGuiNode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodes;
    }
    
    public abstract void setFunctionNodes();
    
    @SideOnly(Side.SERVER)
    public abstract void setValues();
    
    @SideOnly(Side.SERVER)
    public Function getNextFunction() {
        return methodReciever.getNodeValue();
    }
    
    public String getName() {
        return NAME;
    }
    
    public int getColor() {
        return COLOR;
    }
    
    public boolean isMethodReciever() {
        return isMethodReciever;
    }
    
    public boolean isMethodSender() {
        return isMethodSender;
    }
    
    public enum FunctionType {
        ACTIVATOR,
        EVENTS,
        CONDITION,
        FLOW,
        GETTER;
    }
}
