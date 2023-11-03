package com.alet.client.gui.controls.programmable.nodes;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.programmable.functions.GuiFunction;
import com.alet.client.gui.event.gui.GuiControlReleaseEvent;
import com.alet.common.structure.type.programable.nodes.NodeRegistar;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class GuiNode extends GuiParent {
    
    //GUI Fields
    public final String TITLE;
    public final int COLOR;
    public boolean selected = false;
    
    //Connection Fields
    public GuiNode senderConnection;
    public List<GuiNode> receiverConnections = new ArrayList<GuiNode>();
    public final boolean IS_SENDER, IS_RECIEVER, IS_MODIFIABLE;
    
    public GuiNode(String name, String title, int color, boolean isSender, boolean isReciever, boolean isModifiable) throws Exception {
        super(name, 0, 0, (!title.equals("")) ? font.getStringWidth(title) + 7 : 1, 1);
        this.TITLE = title;
        this.IS_SENDER = isSender;
        this.IS_RECIEVER = isReciever;
        this.IS_MODIFIABLE = isModifiable;
        if (IS_RECIEVER && IS_SENDER)
            throw new Exception("A node cannot be both a reciever and sender");
        
        COLOR = color;
    }
    
    public GuiFunction getBlueprint() {
        return (GuiFunction) this.parent;
    }
    
    public void createControls(List<GuiControl> controls) {
        this.parent.getControls().add(controls);
        this.parent.refreshControls();
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        this.raiseEvent(new GuiControlReleaseEvent(this, x, y, button));
    }
    
    public boolean canConnect(GuiNode secondNode) {
        if (secondNode == null)
            return false;
        if (this.getParent().equals(secondNode.parent))
            return false;
        if (this.IS_SENDER && secondNode.IS_SENDER)
            return false;
        if (this.IS_RECIEVER && secondNode.IS_RECIEVER)
            return false;
        if (this.alreadyConnectedTo(secondNode))
            return false;
        if (!this.isDataTypeEqual(secondNode))
            return false;
        if (this.isConnected() && NodeRegistar.matchType(NodeRegistar.FUNCTION_NODE, this))
            return false;
        if (secondNode.isConnected() && NodeRegistar.matchType(NodeRegistar.FUNCTION_NODE, secondNode))
            return false;
        if (this.IS_RECIEVER && this.isConnected())
            return false;
        if (secondNode.IS_RECIEVER && secondNode.isConnected())
            return false;
        /*
        PairList<GuiNode, GuiNode> nodeConnections1 = ((GuiFunction) this.parent).nodeConnections;
        if (nodeConnections1.containsKey(this))
            return false;
        */
        return true;
    }
    
    public void connect(GuiNode secondNode) {
        
        if (secondNode.IS_RECIEVER) {
            secondNode.senderConnection = this;
            this.receiverConnections.add(secondNode);
        }
        /*
        PairList<GuiNode, GuiNode> nodeConnections = ((GuiFunction) this.parent).nodeConnections;
        if (this.IS_RECIEVER)
            nodeConnections.add(new Pair<GuiNode, GuiNode>(this, secondNode));
        else
            nodeConnections.add(new Pair<GuiNode, GuiNode>(secondNode, this));
        */
    }
    
    public boolean isConnected() {
        return !this.receiverConnections.isEmpty() || this.senderConnection != null;
    }
    
    public boolean alreadyConnectedTo(GuiNode secondNode) {
        if (this.senderConnection == secondNode)
            return true;
        for (GuiNode node : this.receiverConnections) {
            if (node.equals(secondNode))
                return true;
        }
        return false;
    }
    
    @Override
    protected void renderBackground(GuiRenderHelper helper, Style style) {
        super.renderBackground(helper, style);
        int xOffSet = 0;
        if (this.IS_SENDER) {
            font.drawStringWithShadow(this.TITLE, this.width - font.getStringWidth(TITLE) - 14, -1, ColorUtils.WHITE);
            xOffSet = this.width - 7;
        } else if (this.IS_RECIEVER) {
            font.drawStringWithShadow(this.TITLE, 8, -1, ColorUtils.WHITE);
            xOffSet = 0;
        }
        
        helper.drawRect(-1 + xOffSet, -1, 6 + xOffSet, 6, COLOR);
        if (!isConnected())
            helper.drawRect(0 + xOffSet, 0, 5 + xOffSet, 5, 0xff5a5a5a);
        if (selected)
            helper.drawRect(0 + xOffSet, 0, 5 + xOffSet, 5, 0xff000000);
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        boolean results = super.mousePressed(x, y, button);
        GuiFunction blueprint = (GuiFunction) topControl(this.getParent().getParent().getControls(), GuiFunction.class);
        GuiNode node = (GuiNode) topControl(blueprint.getControls(), this.getClass());
        if (node == this)
            this.raiseEvent(new GuiControlClickEvent(this, x, y, button));
        return results;
    }
    
    public GuiControl topControl(List<GuiControl> controls, Class<? extends GuiControl> search) {
        for (GuiControl control : controls) {
            if (search.isAssignableFrom(control.getClass()) && control.isMouseOver())
                return control;
        }
        return null;
    }
    
    @Override
    public boolean hasBorder() {
        return false;
    }
    
    @Override
    public boolean hasBackground() {
        return false;
    }
    
    public boolean isDataTypeEqual(GuiNode secondNode) {
        return this.name.equals(secondNode.name);
    }
    
    public GuiNode clone(String name, String title, boolean isSender, boolean isReciever, boolean isModifiable) {
        try {
            return new GuiNode(name, title, COLOR, isSender, isReciever, isModifiable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
