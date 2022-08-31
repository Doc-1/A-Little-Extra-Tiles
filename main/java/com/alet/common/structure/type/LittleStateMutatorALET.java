package com.alet.common.structure.type;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.alet.client.gui.controls.GuiStackSelectorAllMutator;
import com.alet.client.gui.controls.GuiStackSelectorExtensionMutator;
import com.alet.common.packet.PacketUpdateMutateFromServer;
import com.alet.littletiles.common.utils.mc.ColorUtilsAlet;
import com.alet.littletiles.gui.controls.GuiColorPickerAlet;
import com.creativemd.creativecore.common.gui.container.GuiParent;
import com.creativemd.creativecore.common.gui.controls.gui.GuiButton;
import com.creativemd.creativecore.common.gui.controls.gui.GuiCheckBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiPanel;
import com.creativemd.creativecore.common.gui.controls.gui.GuiScrollBox;
import com.creativemd.creativecore.common.gui.controls.gui.GuiTextBox;
import com.creativemd.creativecore.common.gui.controls.gui.custom.GuiStackSelectorAll;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlChangedEvent;
import com.creativemd.creativecore.common.gui.event.gui.GuiControlClickEvent;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.creativecore.common.utils.mc.BlockUtils;
import com.creativemd.littletiles.client.gui.LittleSubGuiUtils.LittleBlockSelector;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents.GuiSignalEventsButton;
import com.creativemd.littletiles.common.action.LittleActionException;
import com.creativemd.littletiles.common.action.block.LittleActionActivated;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.animation.AnimationGuiHandler;
import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.structure.registry.LittleStructureGuiParser;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.LittleStructureType;
import com.creativemd.littletiles.common.structure.signal.output.InternalSignalOutput;
import com.creativemd.littletiles.common.tile.LittleTile;
import com.creativemd.littletiles.common.tile.LittleTileColored;
import com.creativemd.littletiles.common.tile.parent.IStructureTileList;
import com.creativemd.littletiles.common.tile.parent.StructureTileList;
import com.creativemd.littletiles.common.tile.preview.LittlePreviews;
import com.n247s.api.eventapi.eventsystem.CustomEventSubscribe;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LittleStateMutatorALET extends LittleStructure {
    
    public int attributeMod = 0;
    public LinkedHashMap<String, MutatorData> mutateMaterial = new LinkedHashMap<String, MutatorData>();
    public boolean activated = false;
    public boolean allowRightClick = true;
    
    public LittleStateMutatorALET(LittleStructureType type, IStructureTileList mainBlock) {
        super(type, mainBlock);
    }
    
    @Override
    protected void loadFromNBTExtra(NBTTagCompound nbt) {
        attributeMod = nbt.getInteger("attributeMod");
        if (nbt.hasKey("mutater")) {
            NBTTagList mutateList = nbt.getTagList("mutater", Constants.NBT.TAG_LIST);
            int counter = 0;
            for (int i = 0; i < mutateList.tagCount(); i += 3) {
                MutatorData dataA = new MutatorData();
                MutatorData dataB = new MutatorData();
                NBTTagList states = (NBTTagList) mutateList.get(i);
                dataA.state = NBTUtil.readBlockState(states.getCompoundTagAt(0));
                dataB.state = NBTUtil.readBlockState(states.getCompoundTagAt(1));
                NBTTagList colors = (NBTTagList) mutateList.get(i + 1);
                dataA.color = colors.getCompoundTagAt(0).getInteger("color_a");
                dataB.color = colors.getCompoundTagAt(1).getInteger("color_b");
                NBTTagList colisions = (NBTTagList) mutateList.get(i + 2);
                dataA.colision = colisions.getCompoundTagAt(0).getBoolean("colision_a");
                dataB.colision = colisions.getCompoundTagAt(1).getBoolean("colision_b");
                mutateMaterial.put("a" + counter, dataA);
                mutateMaterial.put("b" + counter, dataB);
                counter++;
            }
        }
        if (nbt.hasKey("allowRightClick")) {
            allowRightClick = nbt.getBoolean("allowRightClick");
        }
    }
    
    @Override
    protected void writeToNBTExtra(NBTTagCompound nbt) {
        nbt.setInteger("attributeMod", attributeMod);
        nbt.setBoolean("allowRightClick", allowRightClick);
        if (!mutateMaterial.isEmpty()) {
            NBTTagList mutate = new NBTTagList();
            
            for (int i = 0; i < mutateMaterial.size() / 2; i++) {
                NBTTagList mutateList = new NBTTagList();
                NBTTagList colorList = new NBTTagList();
                NBTTagList colisionList = new NBTTagList();
                NBTTagCompound colorA = new NBTTagCompound();
                colorA.setInteger("color_a", mutateMaterial.get("a" + i).color);
                NBTTagCompound colorB = new NBTTagCompound();
                colorB.setInteger("color_b", mutateMaterial.get("b" + i).color);
                NBTTagCompound colisionA = new NBTTagCompound();
                colisionA.setBoolean("colision_a", mutateMaterial.get("a" + i).colision);
                NBTTagCompound colisionB = new NBTTagCompound();
                colisionB.setBoolean("colision_b", mutateMaterial.get("b" + i).colision);
                
                mutateList.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), mutateMaterial.get("a" + i).state));
                mutateList.appendTag(NBTUtil.writeBlockState(new NBTTagCompound(), mutateMaterial.get("b" + i).state));
                colorList.appendTag(colorA);
                colorList.appendTag(colorB);
                colisionList.appendTag(colisionA);
                colisionList.appendTag(colisionB);
                mutate.appendTag(mutateList);
                mutate.appendTag(colorList);
                mutate.appendTag(colisionList);
            }
            nbt.setTag("mutater", mutate);
        }
    }
    
    @Override
    public void performInternalOutputChange(InternalSignalOutput output) {
        if (output.component.is("activate")) {
            try {
                changeMaterialState();
            } catch (CorruptedConnectionException | NotYetConnectedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void changeVisibleState() throws CorruptedConnectionException, NotYetConnectedException {
        
        for (IStructureTileList tileList : this.blocksList()) {
            for (LittleTile tile : tileList)
                tile.invisible = !tile.invisible;
            tileList.getTe().updateBlock();
            tileList.getTe().updateNeighbour();
        }
        this.updateStructure();
    }
    
    public void changeMaterialState() throws CorruptedConnectionException, NotYetConnectedException {
        for (IStructureTileList tileList : this.blocksList()) {
            tileList.getTe().updateTiles(x -> {
                StructureTileList s = x.get(tileList);
                for (LittleTile tile : tileList) {
                    
                    for (int i = 0; i < mutateMaterial.size() / 2; i++) {
                        MutatorData dataA = mutateMaterial.get("a" + i);
                        MutatorData dataB = mutateMaterial.get("b" + i);
                        int colorA = dataA.color;
                        int colorB = dataB.color;
                        
                        if (tile instanceof LittleTileColored) {
                            LittleTileColored coloredTile = (LittleTileColored) tile;
                            if (coloredTile.getBlockState().equals(dataA.state) && coloredTile.color == dataA.color) {
                                coloredTile.setBlock(dataB.state.getBlock(), dataB.state.getBlock().getMetaFromState(dataB.state));
                                LittleTile temp = LittleTileColored.setColor(tile.copy(), dataB.color);
                                s.remove(coloredTile);
                                s.add(temp);
                                
                            }
                        } else if (tile.getBlockState().equals(dataA.state)) {
                            tile.setBlock(dataB.state.getBlock(), dataB.state.getBlock().getMetaFromState(dataB.state));
                            LittleTile coloredTile = LittleTileColored.setColor(tile.copy(), dataB.color);
                            s.remove(tile);
                            s.add(coloredTile);
                            
                            break;
                        }
                    }
                    
                }
            });
        }
        if (!this.getWorld().isRemote)
            PacketHandler.sendPacketToTrackingPlayers(new PacketUpdateMutateFromServer(this.getStructureLocation()), this.getWorld(), this.getPos(), null);
    }
    
    public void changeCollisionState() throws CorruptedConnectionException, NotYetConnectedException {
        NBTTagCompound nbt = new NBTTagCompound();
        
        if (attributeMod == LittleStructureAttribute.NOCOLLISION)
            attributeMod = LittleStructureAttribute.NONE;
        else if (attributeMod == LittleStructureAttribute.NONE)
            attributeMod = LittleStructureAttribute.NOCOLLISION;
        
        this.tryAttributeChangeForBlocks();
        
        World world = getWorld();
        for (IStructureTileList list : this.blocksList()) {
            IBlockState state = world.getBlockState(list.getPos());
            world.notifyBlockUpdate(list.getPos(), state, state, 2);
        }
        
        this.mainBlock.getTe().updateBlock();
        this.mainBlock.getTe().updateNeighbour();
        this.updateStructure();
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, LittleTile tile, BlockPos pos, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ, LittleActionActivated action) throws LittleActionException {
        if (this.allowRightClick && !worldIn.isRemote)
            getOutput(0).toggle();
        return true;
    }
    
    @Override
    public int getAttribute() {
        return super.getAttribute() | attributeMod;
    }
    
    public static class LittleStateMutatorParserALET extends LittleStructureGuiParser {
        public LinkedHashMap<String, MutatorData> mutateMaterial = new LinkedHashMap<String, MutatorData>();
        
        public LittleStateMutatorParserALET(GuiParent parent, AnimationGuiHandler handler) {
            super(parent, handler);
        }
        
        @Override
        @SideOnly(Side.CLIENT)
        public void create(LittlePreviews previews, @Nullable LittleStructure structure) {
            createControls(previews, structure);
            parent.controls.add(new GuiSignalEventsButton("signal", 0, 122, previews, structure, getStructureType()));
        }
        
        @Override
        protected void createControls(LittlePreviews previews, LittleStructure structure) {
            LittleStateMutatorALET mutator = (LittleStateMutatorALET) structure;
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            
            if (player.isCreative()) {
                
                GuiScrollBox box = new GuiScrollBox("box", 0, 0, 294, 100);
                GuiCheckBox check = new GuiCheckBox("check", "Allow Right Click", 120, 110, mutator.allowRightClick);
                GuiButton add = new GuiButton("add", 224, 110, 20) {
                    @Override
                    public void onClicked(int x, int y, int button) {
                        int size = mutateMaterial.size() / 2;
                        mutateMaterial.put("a" + size, new MutatorData(Blocks.STONE.getDefaultState(), 0xFFFFFFFF, true));
                        mutateMaterial.put("b" + size, new MutatorData(Blocks.STONE.getDefaultState(), 0xFFFFFFFF, true));
                        add();
                    }
                };
                parent.controls.add(add);
                parent.controls.add(check);
                parent.controls.add(box);
                if (mutator != null && !mutator.mutateMaterial.isEmpty()) {
                    this.mutateMaterial = mutator.mutateMaterial;
                    add();
                }
            } else
                parent.controls.add(new GuiTextBox("message", "These settings are only avalible in creative mode", 0, 0, 100));
        }
        
        public void add() {
            GuiScrollBox box = (GuiScrollBox) this.parent.get("box");
            box.removeControls(" ");
            
            int size = (mutateMaterial.size() / 2);
            int k = 0;
            LinkedHashMap<String, MutatorData> newMutateMaterial = new LinkedHashMap<String, MutatorData>();
            for (Entry<String, MutatorData> entry : mutateMaterial.entrySet()) {
                newMutateMaterial.put(entry.getKey().substring(0, 1) + k, entry.getValue());
                if (entry.getKey().contains("b"))
                    k++;
            }
            mutateMaterial = newMutateMaterial;
            for (int i = 0; i < size; i++) {
                GuiMutatorPanel panel = new GuiMutatorPanel(i + "", 0, 28 * i, 242, 21);
                GuiStackSelectorAllMutator a = new GuiStackSelectorAllMutator("a" + i, 0, 0, 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true);
                GuiStackSelectorAllMutator b = new GuiStackSelectorAllMutator("b" + i, 134, 0, 80, null, new GuiStackSelectorAll.CreativeCollector(new LittleBlockSelector()), true);
                panel.controls.add(a);
                panel.controls.add(b);
                MutatorData mutatorA = mutateMaterial.get("a" + i);
                MutatorData mutatorB = mutateMaterial.get("b" + i);
                IBlockState materialA = mutatorA.state;
                IBlockState materialB = mutatorB.state;
                a.setSelectedForce(materialA.getBlock().getPickBlock(materialA, null, null, null, null));
                a.color = mutatorA.color;
                
                b.setSelectedForce(materialB.getBlock().getPickBlock(materialB, null, null, null, null));
                b.color = mutatorB.color;
                panel.controls.add(new GuiLabel("To", 111, 2));
                
                panel.refreshControls();
                box.controls.add(panel);
                box.refreshControls();
            }
            
        }
        
        @CustomEventSubscribe
        public void controlClicked(GuiControlClickEvent event) {
            if (event.button == 1 && event.source instanceof GuiMutatorPanel) {
                this.mutateMaterial.remove("a" + event.source.name);
                this.mutateMaterial.remove("b" + event.source.name);
                add();
                
            }
        }
        
        @CustomEventSubscribe
        public void controlChanged(GuiControlChangedEvent event) {
            if (event.source instanceof GuiStackSelectorAllMutator) {
                GuiStackSelectorAllMutator mutator = (GuiStackSelectorAllMutator) event.source;
                this.mutateMaterial.put(mutator.name, new MutatorData(BlockUtils.getState(mutator.getSelected()), mutator.color, true));
            }
            if (event.source instanceof GuiColorPickerAlet) {
                GuiColorPickerAlet color = (GuiColorPickerAlet) event.source;
                GuiStackSelectorAllMutator mutator = (GuiStackSelectorAllMutator) ((GuiStackSelectorExtensionMutator) event.source.parent).comboBox;
                this.mutateMaterial.put(mutator.name, new MutatorData(BlockUtils.getState(mutator.getSelected()), ColorUtilsAlet.RGBAToInt(color.color), true));
            }
        }
        
        @Override
        protected LittleStructure parseStructure(LittlePreviews previews) {
            LittleStateMutatorALET structure = createStructure(LittleStateMutatorALET.class, null);
            
            structure.mutateMaterial = this.mutateMaterial;
            structure.allowRightClick = ((GuiCheckBox) this.parent.get("check")).value;
            return structure;
        }
        
        @Override
        protected LittleStructureType getStructureType() {
            return LittleStructureRegistry.getStructureType(LittleStateMutatorALET.class);
        }
        
        public class GuiMutatorPanel extends GuiPanel {
            
            public GuiMutatorPanel(String name, int x, int y, int width, int height) {
                super(name, x, y, width, height);
            }
            
            @Override
            public void mouseReleased(int x, int y, int button) {
                if (isMouseOver(x, y) && button == 1) {
                    this.getParent().raiseEvent(new GuiControlClickEvent(this, x, y, button));
                }
            }
        }
    }
    
}
