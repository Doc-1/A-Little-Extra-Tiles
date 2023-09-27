package com.alet.client.gui.controls.programmable.functions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.alet.client.gui.controls.GuiDragablePanel;
import com.alet.client.gui.controls.programmable.nodes.GuiNode;
import com.alet.client.gui.controls.programmable.nodes.GuiNodeRegistar;
import com.alet.client.gui.event.gui.GuiControlReleaseEvent;
import com.alet.common.structure.type.programable.LittleProgramableStructureALET;
import com.alet.common.util.MouseUtils;
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
    
    public LittleProgramableStructureALET structure;
    
    private int mousePosX = 0;
    private int mousePosY = 0;
    public int color;
    public boolean selected = false;
    
    public final String FUNCTION_TITLE;
    public int id;
    
    public final boolean IS_SENDER;
    public final boolean IS_RECEIVER;
    public List<GuiNode> senderNodes = new ArrayList<GuiNode>();
    public List<GuiNode> receiverNodes = new ArrayList<GuiNode>();
    public List<GuiNode> nodes = new ArrayList<GuiNode>();
    public PairList<GuiNode, GuiNode> nodeConnections = new PairList<GuiNode, GuiNode>();
    public GuiNode sender;
    public GuiNode receiver;
    public GuiNode selectedNode;
    
    public GuiFunction(int id, String name, String title, int color, boolean isSender, boolean isReceiver, List<GuiNode> nodes) {
        super(name, 0, 0, 0, 0);
        this.id = id;
        this.color = color;
        this.IS_SENDER = isSender;
        this.IS_RECEIVER = isReceiver;
        setMethodNodes();
        this.nodes = nodes;
        organizeNodes(this.nodes);
        this.FUNCTION_TITLE = title;
        createControls();
        createMethodControls();
    }
    
    public GuiFunction(String name, String title, int color, boolean isSender, boolean isReceiver, List<GuiNode> nodes) {
        this(0, name, title, color, isSender, isReceiver, nodes);
    }
    
    public GuiFunction getNextBlueprint() {
        if (this.sender.receiverConnections != null && !this.sender.receiverConnections.isEmpty())
            return this.sender.receiverConnections.get(0).getBlueprint();
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
    
    protected GuiFunction clone(int id) {
        return new GuiFunction(id, this.name, FUNCTION_TITLE, this.color, this.IS_SENDER, this.IS_RECEIVER, this.nodes);
    }
    
    public NBTTagCompound serializeNBT(NBTTagCompound nbt) {
        return nbt;
    }
    
    public GuiFunction deserializeNBT(NBTTagCompound nbt) {
        return this;
    }
    
    public NBTTagCompound createNodeNBT(NBTTagCompound nbt) {
        NBTTagList l = new NBTTagList();
        List<GuiNode> combined = new ArrayList<GuiNode>();
        combined.addAll(this.receiverNodes);
        if (this.receiver != null)
            combined.add(this.receiver);
        for (GuiNode node : combined) {
            NBTTagCompound n = new NBTTagCompound();
            n.setString("node", node.name);
            NBTTagList list = new NBTTagList();
            for (GuiNode receiver : node.receiverConnections) {
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
    
    public static GuiFunction createBlueprintFromNBT(NBTTagCompound nbt) {
        /*
        try {
            GuiFunction obj = BlueprintRegistar.getClass(nbt.getString("name"), false).getConstructor(int.class).newInstance(nbt.getInteger("id"));
            obj.deserializeNBT(nbt);
            if (obj instanceof BlueprintValue)
                obj.setNodeValue(null);
            obj.posX = nbt.getInteger("posX");
            obj.posY = nbt.getInteger("5posY");
            return obj;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }*/
        return null;
    }
    
    public static GuiFunction createBlueprintFrom(String name, int id) {
        GuiFunction obj = GuiFunctionRegistar.createFunction(name, true, id);
        return obj;
    }
    
    public static NBTTagList getNodeNBTList(NBTTagCompound nbt) {
        return nbt.getTagList("nodes", NBT.TAG_COMPOUND);
    }
    
    public static PairList<GuiFunction, GuiNode> getBlueprintFromNodeNBT(List<GuiFunction> blueprints, NBTTagCompound node) {
        
        NBTTagList list = node.getTagList("connections", NBT.TAG_COMPOUND);
        PairList<GuiFunction, GuiNode> bpList = new PairList<GuiFunction, GuiNode>();
        for (NBTBase d : list) {
            if (d instanceof NBTTagCompound) {
                NBTTagCompound nbt = (NBTTagCompound) d;
                GuiFunction bp = blueprints.stream().filter(x -> x.name.equals(nbt.getString("name")) && x.id == nbt
                        .getInteger("id")).findFirst().get();
                bpList.add(new Pair<GuiFunction, GuiNode>(bp, (GuiNode) bp.get(nbt.getString("node"))));
            }
        }
        return bpList;
    }
    
    public static GuiFunction getBlueprintObjectFromNBT(List<GuiFunction> blueprints, NBTTagCompound nbt) {
        String name = nbt.getString("name");
        int id = nbt.getInteger("id");
        return blueprints.stream().filter(x -> (x.name.equals(name) && x.id == id)).findFirst().get();
    }
    
    public GuiNode getNode(String name) {
        Optional<GuiNode> node = this.nodes.stream().filter(x -> x.name.equals(name)).findFirst();
        if (node.isPresent())
            return node.get();
        return null;
    }
    
    public void organizeNodes(List<GuiNode> nodes) {
        if (!nodes.isEmpty() && nodes != null)
            for (GuiNode node : nodes) {
                if (node.IS_SENDER)
                    this.senderNodes.add(node);
                else if (node.IS_RECIEVER)
                    this.receiverNodes.add(node);
            }
        
    }
    
    public void setMethodNodes() {
        try {
            GuiNode receiver = GuiNodeRegistar.createNode(GuiNodeRegistar.FUNCTION_NODE, "method_receiver", "", false, true,
                false);
            GuiNode sender = GuiNodeRegistar.createNode(GuiNodeRegistar.FUNCTION_NODE, "method_sender", "", true, false,
                false);
            if (this.IS_RECEIVER)
                this.receiver = receiver;
            if (this.IS_SENDER)
                this.sender = sender;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public HashSet<Entity> getEntities() {
        return this.structure.entities;
    }
    
    public Entity getEntity(WorldServer world, UUID uuid) {
        return world.getEntityFromUuid(uuid);
    }
    
    private void createMethodControls() {
        if (this.IS_RECEIVER && this.IS_SENDER) {
            this.addControl(this.sender);
            this.sender.posX = this.width - this.receiver.width - 6;
            this.addControl(this.receiver);
        } else if (this.IS_RECEIVER) {
            this.addControl(this.receiver);
        } else if (this.IS_SENDER) {
            this.posX = this.width - 13;
            this.addControl(this.sender);
        }
    }
    
    private void createControls() {
        int maxSpacing = 0;
        int maxWidth = font.getStringWidth(this.FUNCTION_TITLE) + 14;
        int maxHeight = 0;
        int y = 15;
        boolean hasModifiable = false;
        for (int i = 0; i < this.receiverNodes.size(); i++) {
            GuiNode node = this.receiverNodes.get(i);
            maxSpacing = Math.max(maxSpacing, font.getStringWidth(node.TITLE)) + 20;
            if (node.IS_MODIFIABLE)
                hasModifiable = true;
            if (i > 0) {
                GuiNode n = this.receiverNodes.get(i - 1);
                node.posY = n.posY + 15;
                if (n.IS_MODIFIABLE) {
                    node.posY = (i * y) + 30;
                }
            } else if (node.IS_MODIFIABLE && this.receiverNodes.size() == 1 && this.senderNodes.size() == 1) {
                
                maxHeight = 30;
                
            } else
                node.posY = 15;
            
            maxHeight = Math.max(maxHeight, node.posY);
            this.addControl(node);
            
        }
        for (int i = 0; i < this.senderNodes.size(); i++) {
            GuiNode node = this.senderNodes.get(i);
            maxWidth = Math.max(maxWidth, maxSpacing + font.getStringWidth(node.TITLE) + 5);
            if (node.IS_MODIFIABLE)
                hasModifiable = true;
            if (i > 0) {
                GuiNode n = this.senderNodes.get(i - 1);
                node.posY = n.posY + 15;
                
                if (n.IS_MODIFIABLE) {
                    node.posY = (i * y) + 30;
                }
            } else if (node.IS_MODIFIABLE && (this.receiverNodes.size() == 1 || this.receiverNodes
                    .size() == 0) && (this.senderNodes.size() == 1 || this.senderNodes.size() == 0)) {
                node.posY = 15;
                maxHeight = 30;
            } else
                node.posY = 15;
            maxHeight = Math.max(maxHeight, node.posY);
            this.addControl(node);
        }
        int modWidth = 60;
        if (hasModifiable)
            maxWidth = Math.max(modWidth, maxWidth);
        
        this.width = maxWidth + 14;
        this.height = maxHeight + 14;
        for (GuiNode node : this.senderNodes) {
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
                this.selected = true;
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
    
    public void nodeClicked(GuiNode node, boolean selected) {
        node.selected = selected;
        if (node.selected)
            this.selectedNode = node;
        else
            this.selectedNode = null;
        for (GuiControl control : this.controls) {
            if (control instanceof GuiNode && !control.equals(node)) {
                GuiNode n = (GuiNode) control;
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
