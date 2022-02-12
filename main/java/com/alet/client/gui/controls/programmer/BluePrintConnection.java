package com.alet.client.gui.controls.programmer;

import java.util.ArrayList;
import java.util.List;

import com.alet.client.gui.controls.programmer.blueprints.GuiBluePrintNode;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class BluePrintConnection<V> extends GuiControl {
    
    public static final int METHOD_SENDER_CONNECTION = 0;
    public static final int METHOD_RECEIVER_CONNECTION = 1;
    public static final int RETURN_CONNECTION = 2;
    public static final int PARAMETER_CONNECTION = 3;
    public final int connectionType;
    public String title;
    public BluePrintConnection sender;
    public List<BluePrintConnection> receiver = new ArrayList<BluePrintConnection>();
    public GuiBluePrintNode parentNode;
    public V value;
    public int nodePos;
    public boolean isSelected;
    
    public BluePrintConnection(String name, String title, GuiBluePrintNode parentNode, int nodePos, int nodeType) {
        super(name, 0, 0, 1, 1);
        this.connectionType = nodeType;
        this.nodePos = nodePos;
        this.title = title;
        this.parentNode = parentNode;
    }
    
    public void setValue(V value) {
        this.value = value;
    }
    
    public V getValue() {
        return this.value;
    }
    
    @Override
    public boolean canOverlap() {
        return false;
    }
    
    @Override
    public boolean hasBorder() {
        return false;
    }
    
    @Override
    public boolean hasBackground() {
        return false;
    }
    
    public boolean canAddConnection(BluePrintConnection connection) {
        if (this.getValue().getClass().equals(connection.getValue().getClass()))
            if ((this.connectionType == METHOD_SENDER_CONNECTION && connection.connectionType == METHOD_RECEIVER_CONNECTION))
                return true;
            else if (this.connectionType == METHOD_RECEIVER_CONNECTION && connection.connectionType == METHOD_SENDER_CONNECTION)
                return true;
            else if ((this.connectionType == RETURN_CONNECTION && connection.connectionType == PARAMETER_CONNECTION))
                return true;
            else if ((this.connectionType == PARAMETER_CONNECTION && connection.connectionType == RETURN_CONNECTION))
                return true;
            else
                return false;
        else
            return false;
    }
    
    public void addConnection(BluePrintConnection connection) {
        if ((connection.connectionType == METHOD_SENDER_CONNECTION || connection.connectionType == RETURN_CONNECTION) && (this.connectionType == METHOD_RECEIVER_CONNECTION || this.connectionType == PARAMETER_CONNECTION)) {
            connection.receiver.add(this);
            this.sender = connection;
            System.out.println(this.parentNode.name + "." + this.name + " " + connection.parentNode.name + "." + connection.name);
        } else if ((this.connectionType == METHOD_SENDER_CONNECTION || this.connectionType == RETURN_CONNECTION) && (connection.connectionType == METHOD_RECEIVER_CONNECTION || connection.connectionType == PARAMETER_CONNECTION)) {
            this.receiver.add(connection);
            connection.sender = this;
            System.out.println(this.parentNode.name + "." + this.name + " " + connection.parentNode.name + "." + connection.name);
        }
        
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        if (this.isMouseOver()) {
            this.raiseEvent(new GuiControlClickEvent(this, x, y, button));
        }
    }
    
    @Override
    protected void renderContent(GuiRenderHelper helper, Style style, int width, int height) {
        
        posY = this.nodePos > 0 ? (this.nodePos * 13) + 2 : (this.nodePos * 13);
        
        if (this.connectionType == METHOD_SENDER_CONNECTION) {
            posX = this.getParent().width - 13;
            if (this.nodePos > 0)
                font.drawStringWithShadow(this.title, -font.getStringWidth(this.title) - 6, -3, ColorUtils.WHITE);
            
        }
        if (this.connectionType == RETURN_CONNECTION) {
            posX = this.getParent().width - 13;
            font.drawStringWithShadow(this.title, -font.getStringWidth(this.title) - 6, -3, ColorUtils.WHITE);
        }
        if (this.connectionType == PARAMETER_CONNECTION) {
            posX = 0;
            font.drawStringWithShadow(this.title, 8, -3, ColorUtils.WHITE);
        }
        if (this.value instanceof Boolean)
            if (this.connectionType == METHOD_RECEIVER_CONNECTION || this.connectionType == METHOD_SENDER_CONNECTION)
                helper.drawRect(-3, -3, 4, 4, 0xffffffff);
            else
                helper.drawRect(-3, -3, 4, 4, 0xffff3333);
            
        if (this.value instanceof Boolean[]) {
            helper.drawRect(-3, -3, 4, 4, 0xff0066cc);
        }
        if (this.value instanceof Integer[]) {
            helper.drawRect(-3, -3, 4, 4, 0xffFF9933);
        }
        if (this.value instanceof Integer) {
            helper.drawRect(-3, -3, 4, 4, 0xff66ffb3);
        }
        if (!isSelected)
            helper.drawRect(-2, -2, 3, 3, 0xff5a5a5a);
        
    }
    
}
