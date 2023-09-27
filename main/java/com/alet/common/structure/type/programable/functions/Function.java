package com.alet.common.structure.type.programable.functions;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.common.structure.type.programable.LittleProgramableStructureALET;
import com.alet.common.structure.type.programable.nodes.values.NodeFunction;
import com.alet.common.structure.type.programable.nodes.values.NodeValue;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Function {
    
    //Executing structure
    public LittleProgramableStructureALET structure;
    public int id;
    
    //Nodes Fields
    public NodeFunction methodSender;
    public NodeFunction methodReciever;
    public List<NodeValue> senderNodes = new ArrayList<>();
    public List<NodeValue> recieverNodes = new ArrayList<>();
    public final boolean IS_METHOD_SENDER;
    public final boolean IS_METHOD_RECIEVER;
    
    //GUI Fields
    private final String NAME;
    private final int COLOR;
    protected final static int ACTIVATOR_COLOR = 0xFFFFFF;
    protected final static int EVENT_COLOR = 0xFFFFFF;
    protected final static int CONDITION_COLOR = 0xFFFFFF;
    protected final static int GETTER_COLOR = 0xFFFFFF;
    protected final static int VALUE_COLOR = 0xFFFFFF;
    protected final static int CAST_COLOR = 0xFFFFFF;
    protected final static int FLOW_COLOR = 0xFFFFFF;
    
    public Function(String name, int color, boolean sender, boolean reciever) {
        this.NAME = name;
        this.COLOR = color;
        this.IS_METHOD_SENDER = sender;
        this.IS_METHOD_RECIEVER = reciever;
        setMethodNodes();
        setFunctionNodes();
    }
    
    private void setMethodNodes() {
        if (IS_METHOD_SENDER)
            this.methodSender = new NodeFunction("method_sender", "", true, false, false);
        if (IS_METHOD_RECIEVER)
            this.methodReciever = new NodeFunction("method_reciever", "", false, true, false);
    }
    
    @SideOnly(Side.CLIENT)
    public List<GuiNode> setGuiNodes() {
        List<GuiNode> nodes = new ArrayList<>();
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
    
    public abstract NBTTagCompound createNBT();
}
