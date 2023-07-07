package com.alet.client.gui.controls.programmable.nodes;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.programmable.blueprints.GuiBlueprint;
import com.alet.client.gui.event.gui.GuiControlReleaseEvent;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;

public abstract class GuiNode extends GuiParent {
    
    public final static byte SENDER = 0b0001, RECEIVER = 0b0010, MODIFIABLE = 0b0100;
    private final byte ATTRIBUTES;
    public final String TITLE;
    public GuiNode senderConnection;
    public GuiNode receiverConnection;
    public List<GuiNode> senderConnections = new ArrayList<GuiNode>();
    public List<GuiNode> receiverConnections = new ArrayList<GuiNode>();
    public boolean selected = false;
    public final int color;
    
    public GuiNode(String name, String title, byte attributes) {
        super(name, 0, 0, (!title.equals("")) ? font.getStringWidth(title) + 7 : 1, 1);
        this.ATTRIBUTES = attributes;
        this.TITLE = title;
        createControls();
        color = setNodeColor();
    }
    
    public GuiBlueprint getBlueprint() {
        return (GuiBlueprint) this.parent;
    }
    
    public abstract int setNodeColor();
    
    public void createControls() {}
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        this.raiseEvent(new GuiControlReleaseEvent(this, x, y, button));
    }
    
    public boolean canConnect(GuiNode secondNode) {
        
        if (secondNode == null)
            return false;
        if (this.getParent().equals(secondNode.parent))
            return false;
        if (this.isSender() && secondNode.isSender())
            return false;
        if (this.isReceiver() && secondNode.isReceiver())
            return false;
        if (!this.isDataTypeEqual(secondNode))
            return false;
        if (this.alreadyConnectedTo(secondNode))
            return false;
        if (this.isReceiver() && this.isConnected())
            return false;
        PairList<GuiNode, GuiNode> nodeConnections1 = ((GuiBlueprint) this.parent).nodeConnections;
        if (nodeConnections1.containsKey(this))
            return false;
        return true;
    }
    
    public void connect(GuiNode secondNode) {
        PairList<GuiNode, GuiNode> nodeConnections = ((GuiBlueprint) this.parent).nodeConnections;
        
        if (secondNode.isReceiver()) {
            if (secondNode instanceof GuiNodeMethod) {
                secondNode.senderConnections.add(this);
                this.receiverConnection = secondNode;
            } else {
                secondNode.senderConnection = this;
                this.receiverConnections.add(secondNode);
            }
            
        }
        nodeConnections.add(new Pair<GuiNode, GuiNode>(this, secondNode));
    }
    
    public boolean isConnected() {
        return !this.senderConnections.isEmpty() || !this.receiverConnections.isEmpty() || this.senderConnection != null || this.receiverConnection != null;
    }
    
    public boolean alreadyConnectedTo(GuiNode secondNode) {
        if (this.senderConnection == secondNode)
            return true;
        for (GuiNode node : this.senderConnections) {
            if (node.equals(secondNode))
                return true;
        }
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
        if (this.isSender()) {
            font.drawStringWithShadow(this.TITLE, this.width - font.getStringWidth(TITLE) - 14, -1, ColorUtils.WHITE);
            xOffSet = this.width - 7;
        } else if (this.isReceiver()) {
            font.drawStringWithShadow(this.TITLE, 8, -1, ColorUtils.WHITE);
            xOffSet = 0;
        }
        
        helper.drawRect(-1 + xOffSet, -1, 6 + xOffSet, 6, color);
        if (!isConnected())
            helper.drawRect(0 + xOffSet, 0, 5 + xOffSet, 5, 0xff5a5a5a);
        if (selected)
            helper.drawRect(0 + xOffSet, 0, 5 + xOffSet, 5, 0xff000000);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean mousePressed(int x, int y, int button) {
        boolean results = super.mousePressed(x, y, button);
        GuiBlueprint blueprint = (GuiBlueprint) topControl(this.getParent().getParent().getControls(), GuiBlueprint.class);
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
        return this.getClass().equals(secondNode.getClass());
    }
    
    public boolean isSender() {
        return (this.ATTRIBUTES & SENDER) != 0;
    }
    
    public boolean isReceiver() {
        return (this.ATTRIBUTES & RECEIVER) != 0;
    }
    
    public boolean isModifiable() {
        return (this.ATTRIBUTES & MODIFIABLE) != 0;
    }
    
}
