package com.alet.client.gui.controls.programmer;

import java.util.ArrayList;
import java.util.List;

import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.mc.ColorUtils;

public class BluePrintNode<V> extends GuiControl {
	
	public static final int METHOD_SENDER_NODE = 0;
	public static final int METHOD_RECEIVER_NODE = 1;
	public static final int RETURN_NODE = 2;
	public static final int PARAMETER_NODE = 3;
	private final int nodeType;
	public String title;
	public List<BluePrintNode> sender = new ArrayList<BluePrintNode>();
	public List<BluePrintNode> receiver = new ArrayList<BluePrintNode>();
	public V value;
	public int nodePos;
	public boolean isSelected;
	
	public BluePrintNode(String name, String title, int nodePos, int nodeType) {
		super(name, 0, 0, 1, 1);
		this.nodeType = nodeType;
		this.nodePos = nodePos;
		this.title = title;
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
	
	public boolean has(String name) {
		for (BluePrintNode node : this.sender)
			if (name.equals(node.name))
				return true;
		return false;
	}
	
	public boolean canAddConnection(BluePrintNode connection) {
		System.out.println(this.nodeType + " " + this.sender.size() + " " + connection.nodeType + " "
		        + connection.sender.size());
		if (this.getValue().getClass().equals(connection.getValue().getClass()))
			if ((this.nodeType == METHOD_SENDER_NODE && connection.nodeType == METHOD_RECEIVER_NODE))
				return true;
			else if (this.nodeType == METHOD_RECEIVER_NODE && connection.nodeType == METHOD_SENDER_NODE)
				return true;
			else if ((this.nodeType == RETURN_NODE && connection.nodeType == PARAMETER_NODE)
			        && (this.sender.size() == 0 || has(this.name)))
				return true;
			else if ((this.nodeType == PARAMETER_NODE && connection.nodeType == RETURN_NODE)
			        && (connection.sender.size() == 0 || connection.has(connection.name)))
				return true;
			else
				return false;
		else
			return false;
	}
	
	public void addConnection(BluePrintNode connection) {
		if ((connection.nodeType == METHOD_SENDER_NODE || connection.nodeType == RETURN_NODE)
		        && (this.nodeType == METHOD_RECEIVER_NODE || this.nodeType == PARAMETER_NODE)) {
			connection.receiver.add(this);
			connection.sender.add(connection);
			this.receiver.add(this);
			this.sender.add(connection);
		} else if ((this.nodeType == METHOD_SENDER_NODE || this.nodeType == RETURN_NODE)
		        && (connection.nodeType == METHOD_RECEIVER_NODE || connection.nodeType == PARAMETER_NODE)) {
			connection.receiver.add(connection);
			connection.sender.add(this);
			this.receiver.add(connection);
			this.sender.add(this);
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
		
		if (this.nodeType == METHOD_SENDER_NODE) {
			posX = this.getParent().width - 13;
		}
		if (this.nodeType == RETURN_NODE) {
			posX = this.getParent().width - 13;
			font.drawStringWithShadow(this.title, -font.getStringWidth(this.title) - 6, -3, ColorUtils.WHITE);
		}
		if (this.nodeType == PARAMETER_NODE) {
			posX = 0;
			font.drawStringWithShadow(this.title, 8, -3, ColorUtils.WHITE);
		}
		if (this.value instanceof Boolean)
			if (this.nodeType == METHOD_RECEIVER_NODE || this.nodeType == METHOD_SENDER_NODE)
				helper.drawRect(-3, -3, 4, 4, 0xffffffff);
			else
				helper.drawRect(-3, -3, 4, 4, 0xffff3333);
			
		if (this.value instanceof Boolean[]) {
			helper.drawRect(-3, -3, 4, 4, 0xff0066cc);
		}
		if (this.value instanceof Integer) {
			helper.drawRect(-3, -3, 4, 4, 0xff66ffb3);
		}
		if (!isSelected)
			helper.drawRect(-2, -2, 3, 3, 0xff5a5a5a);
		
	}
	
}
