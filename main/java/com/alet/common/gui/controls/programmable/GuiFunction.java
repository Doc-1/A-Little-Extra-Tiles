package com.alet.common.gui.controls.programmable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.alet.client.registries.FunctionRegistery;
import com.alet.client.registries.NodeRegistery;
import com.alet.common.gui.controls.GuiDragablePanel;
import com.alet.common.gui.events.GuiControlReleaseEvent;
import com.alet.common.utils.MouseUtils;
import com.creativemd.creativecore.common.gui.GuiControl;
import com.creativemd.creativecore.common.gui.GuiRenderHelper;
import com.creativemd.creativecore.common.gui.client.style.Style;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.utils.type.Pair;
import com.creativemd.creativecore.common.utils.type.PairList;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants.NBT;

public class GuiFunction extends GuiParent {
    
    //GUI Fields
    private int mousePosX = 0;
    private int mousePosY = 0;
    public int color;
    public boolean selected = false;
    public GuiNodeValue selectedNode;
    
    //Identification Fields
    public final String FUNCTION_TITLE;
    public int id;
    
    //Nodes Fields
    public GuiNodeValue methodSender;
    public GuiNodeValue methodReceiver;
    public List<GuiNodeValue> senderNodes = new ArrayList<GuiNodeValue>();
    public List<GuiNodeValue> receiverNodes = new ArrayList<GuiNodeValue>();
    public final boolean IS_METHOD_SENDER;
    public final boolean IS_METHOD_RECIEVER;
    
    public GuiFunction(String title, String name, int id, int color, boolean isSender, boolean isReceiver, List<GuiNodeValue> nodes) {
        super(name, 0, 0, 0, 0);
        this.id = id;
        this.color = color;
        this.IS_METHOD_SENDER = isSender;
        this.IS_METHOD_RECIEVER = isReceiver;
        setMethodNodes();
        organizeNodes(nodes);
        this.FUNCTION_TITLE = title;
        createControls();
        createMethodControls();
    }
    
    public GuiFunction(String name, String title, int color, boolean isSender, boolean isReceiver, List<GuiNodeValue> nodes) {
        this(title, name, 0, color, isSender, isReceiver, nodes);
    }
    
    public GuiFunction getNextBlueprint() {
        if (this.methodSender.receiverConnections != null && !this.methodSender.receiverConnections.isEmpty())
            return ((GuiNodeValue) this.methodSender.receiverConnections.get(0)).getBlueprint();
        else
            return null;
    }
    
    public NBTTagCompound createNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("id", id);
        nbt.setInteger("posY", posY);
        nbt.setInteger("posX", posX);
        nbt.setString("name", this.name);
        return this.serializeNBT(this.createNodeNBT(nbt));
    }
    
    public GuiFunction clone(int id) {
        return new GuiFunction(FUNCTION_TITLE, this.name, id, this.color, this.IS_METHOD_SENDER, this.IS_METHOD_RECIEVER, this
                .getNodes());
    }
    
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        return nbt;
    }
    
    public GuiFunction deserializeNBT(NBTTagCompound nbt) {
        return this;
    }
    
    public NBTTagCompound createNodeNBT(NBTTagCompound nbt) {
        NBTTagList l = new NBTTagList();
        List<GuiNodeValue> combined = new ArrayList<GuiNodeValue>();
        combined.addAll(this.receiverNodes);
        if (this.methodReceiver != null)
            combined.add(this.methodReceiver);
        for (GuiNodeValue node : combined) {
            NBTTagCompound n = new NBTTagCompound();
            n.setString("node", node.name);
            NBTTagList list = new NBTTagList();
            List<GuiNodeValue> s = node.receiverConnections;
            for (GuiNodeValue receiver : s) {
                NBTTagCompound nb = new NBTTagCompound();
                nb.setString("node", receiver.name);
                nb.setString("name", receiver.getParent().name);
                nb.setInteger("id", ((GuiFunction) receiver.getParent()).id);
                list.appendTag(nb);
            }
            if (node.senderConnection != null) {
                NBTTagCompound nb = new NBTTagCompound();
                nb.setString("node", node.senderConnection.name);
                nb.setString("name", node.senderConnection.getParent().name);
                nb.setInteger("id", ((GuiFunction) node.senderConnection.getParent()).id);
                list.appendTag(nb);
            }
            n.setTag("connections", list);
            l.appendTag(n);
        }
        nbt.setTag("nodes", l);
        return nbt;
    }
    
    public static GuiFunction createFunctionFrom(String name, int id) {
        GuiFunction obj = FunctionRegistery.createFunctionGui(name, true, id);
        return obj;
    }
    
    public static NBTTagList getNodeNBTList(NBTTagCompound nbt) {
        return nbt.getTagList("nodes", NBT.TAG_COMPOUND);
    }
    
    public static PairList<GuiFunction, GuiNodeValue> getFunctionFromNodeNBT(List<GuiFunction> blueprints, NBTTagCompound node) {
        
        NBTTagList list = node.getTagList("connections", NBT.TAG_COMPOUND);
        PairList<GuiFunction, GuiNodeValue> bpList = new PairList<GuiFunction, GuiNodeValue>();
        for (NBTBase d : list) {
            if (d instanceof NBTTagCompound) {
                NBTTagCompound nbt = (NBTTagCompound) d;
                GuiFunction bp = blueprints.stream().filter(x -> x.name.equals(nbt.getString("name")) && x.id == nbt
                        .getInteger("id")).findFirst().get();
                bpList.add(new Pair<GuiFunction, GuiNodeValue>(bp, (GuiNodeValue) bp.get(nbt.getString("node"))));
            }
        }
        return bpList;
    }
    
    public static GuiFunction getFunctionObjectFromNBT(List<GuiFunction> blueprints, NBTTagCompound nbt) {
        String name = nbt.getString("name");
        int id = nbt.getInteger("id");
        return blueprints.stream().filter(x -> (x.name.equals(name) && x.id == id)).findFirst().get();
    }
    
    public List<GuiNodeValue> getNodes() {
        List<GuiNodeValue> nodes = new ArrayList<>();
        nodes.addAll(this.receiverNodes);
        nodes.addAll(this.senderNodes);
        return nodes;
    }
    
    public GuiNodeValue getNode(String name) {
        Optional<GuiNodeValue> node = this.getNodes().stream().filter(x -> x.name.equals(name)).findFirst();
        if (node.isPresent())
            return node.get();
        return null;
    }
    
    public void organizeNodes(List<GuiNodeValue> nodes) {
        if (!nodes.isEmpty() && nodes != null)
            for (GuiNodeValue node : nodes) {
                if (node.isSender())
                    this.senderNodes.add(node);
                else if (node.isReciever())
                    this.receiverNodes.add(node);
            }
    }
    
    public void setMethodNodes() {
        try {
            if (this.IS_METHOD_RECIEVER)
                this.methodReceiver = NodeRegistery.createNode(NodeRegistery.FUNCTION_NODE, "method_receiver", "", false)
                        .setSender(false);
            if (this.IS_METHOD_SENDER)
                this.methodSender = NodeRegistery.createNode(NodeRegistery.FUNCTION_NODE, "method_sender", "", false)
                        .setSender(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Entity getEntity(WorldServer world, UUID uuid) {
        return world.getEntityFromUuid(uuid);
    }
    
    private void createMethodControls() {
        if (this.IS_METHOD_RECIEVER && this.IS_METHOD_SENDER) {
            this.addControl(this.methodSender);
            this.methodSender.posX = this.width - this.methodReceiver.width - 6;
            this.addControl(this.methodReceiver);
        } else if (this.IS_METHOD_RECIEVER) {
            this.addControl(this.methodReceiver);
        } else if (this.IS_METHOD_SENDER) {
            this.methodSender.posX = this.width - 13;
            this.addControl(this.methodSender);
        }
    }
    
    private void createControls() {
        int maxSpacing = 0;
        int maxWidth = font.getStringWidth(this.FUNCTION_TITLE) + 14;
        int maxHeight = 14;
        int y = 15;
        boolean hasModifiable = false;
        for (int i = 0; i < this.receiverNodes.size(); i++) {
            GuiNodeValue node = this.receiverNodes.get(i);
            maxSpacing = Math.max(maxSpacing, font.getStringWidth(node.TITLE)) + 20;
            if (node.IS_MODIFIABLE)
                hasModifiable = true;
            if (i > 0) {
                GuiNodeValue n = this.receiverNodes.get(i - 1);
                node.posY = n.posY + 15;
                if (n.IS_MODIFIABLE)
                    node.posY = (i * y) + 30;
            } else if (node.IS_MODIFIABLE && this.receiverNodes.size() == 1 && this.senderNodes.size() == 1)
                maxHeight = 30;
            else
                node.posY = 15;
            maxHeight = Math.max(maxHeight, node.posY + 15);
            this.addControl(node);
        }
        for (int i = 0; i < this.senderNodes.size(); i++) {
            GuiNodeValue node = this.senderNodes.get(i);
            maxWidth = Math.max(maxWidth, maxSpacing + font.getStringWidth(node.TITLE) + 5);
            if (node.IS_MODIFIABLE)
                hasModifiable = true;
            if (i > 0) {
                GuiNodeValue n = this.senderNodes.get(i - 1);
                node.posY = n.posY + 15;
                if (n.IS_MODIFIABLE)
                    node.posY = (i * y) + 30;
            } else if (node.IS_MODIFIABLE && (this.receiverNodes.size() == 1 || this.receiverNodes
                    .size() == 0) && (this.senderNodes.size() == 1 || this.senderNodes.size() == 0)) {
                node.posY = 15;
                maxHeight = 30;
            } else
                node.posY = 15;
            maxHeight = Math.max(maxHeight, node.posY);
            this.addControl(node);
        }
        /*  int modWidth = 60;
        if (hasModifiable)
            maxWidth = Math.max(modWidth, maxWidth);*/
        
        this.width = maxWidth + 14;
        this.height = maxHeight;
        for (GuiNodeValue node : this.senderNodes) {
            node.posX = this.width - node.width - 6;
        }
    }
    
    @Override
    public boolean mousePressed(int x, int y, int button) {
        boolean result = super.mousePressed(x, y, button);
        
        if (this.controlOver(x, y, button, controls) == null) {
            MouseUtils.setCursor("move");
            this.mousePosX = (int) this.getMousePos().x;
            this.mousePosY = (int) this.getMousePos().y;
            
            if (this.isMouseOver()) {
                this.raiseEvent(new GuiControlClickEvent(this, x, y, button));
            }
        }
        return result;
    }
    
    @Override
    public void mouseReleased(int x, int y, int button) {
        super.mouseReleased(x, y, button);
        MouseUtils.resetCursor();
        this.raiseEvent(new GuiControlReleaseEvent(this, x, y, button));
        this.selected = false;
    }
    
    @Override
    public void mouseDragged(int x, int y, int button, long time) {
        if (selected) {
            this.posX = x - this.mousePosX;
            this.posY = y - this.mousePosY;
            
            int left = this.posX + 1;
            int top = this.posY + 1;
            int right = this.posX + this.width + 5;
            int bottom = this.posY + this.height + 5;
            int guiWidth = this.getParent().width;
            int guiHeight = this.getParent().height;
            if (this.getParent() instanceof GuiDragablePanel) {
                GuiDragablePanel gui = (GuiDragablePanel) this.getParent();
                guiWidth = gui.maxWidth;
                guiHeight = gui.maxHeight;
            }
            if (left < 0)
                this.posX = -2;
            if (right > guiWidth)
                this.posX = guiWidth - this.width - 4;
            if (top < 0)
                this.posY = -2;
            if (bottom > guiHeight)
                this.posY = guiHeight - this.height - 4;
        }
    }
    
    public void nodeClicked(GuiNodeValue node, boolean selected) {
        node.selected = selected;
        if (node.selected)
            this.selectedNode = node;
        else
            this.selectedNode = null;
        for (GuiControl control : this.controls) {
            if (control instanceof GuiNodeValue && !control.equals(node)) {
                GuiNodeValue n = (GuiNodeValue) control;
                n.selected = false;
            }
            
        }
    }
    
    public GuiControl controlOver(int x, int y, int button, ArrayList<GuiControl> controls) {
        for (GuiControl control : controls) {
            if (control.isMouseOver()) {
                return control;
            }
            if (control instanceof GuiParent) {
                GuiControl found = controlOver(x, y, button, ((GuiParent) control).controls);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
    
    @Override
    protected void renderBackground(GuiRenderHelper helper, Style style) {
        super.renderBackground(helper, style);
        helper.drawRect(0, 12, width - 1, 13, 0xff000000);
        font.drawString(this.FUNCTION_TITLE, ((width / 2) - font.getStringWidth(this.FUNCTION_TITLE) / 2), 3, this.color,
            true);
    }
}
