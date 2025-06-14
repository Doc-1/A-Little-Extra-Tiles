package com.alet;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alet.client.ALETClient;
import com.alet.client.registries.FunctionRegistery;
import com.alet.client.registries.NodeRegistery;
import com.alet.client.registries.SoundsRegister;
import com.alet.common.gui.container.SubContainerAnimatorWorkbench;
import com.alet.common.gui.container.SubContainerBasic;
import com.alet.common.gui.container.SubContainerFillingCabinet;
import com.alet.common.gui.container.SubContainerLittleHopper;
import com.alet.common.gui.container.SubContainerPhotoImport;
import com.alet.common.gui.container.SubContainerTypeWriter;
import com.alet.common.gui.messages.SubGuiNoBluePrintMessage;
import com.alet.common.gui.origins.SubGuiAnimatorsWorkbench;
import com.alet.common.gui.origins.SubGuiNoticeAtJoin;
import com.alet.common.gui.origins.SubGuiSignalEventsALET;
import com.alet.common.gui.structure.premade.SubGuiLittleHopper;
import com.alet.common.gui.structure.premade.SubGuiPhotoImport;
import com.alet.common.gui.structure.premade.SubGuiTypeWriter;
import com.alet.common.gui.structure.premade.filling_cabinet.SubGuiFillingCabinet;
import com.alet.common.gui.tool.SubGuiManual;
import com.alet.common.packets.PacketRegistery;
import com.alet.components.blocks.BasicBlock;
import com.alet.components.blocks.TransparentBlock;
import com.alet.components.items.ItemJumpTool;
import com.alet.components.items.ItemLittleManual;
import com.alet.components.items.ItemLittleRope;
import com.alet.components.items.ItemLittleScissors;
import com.alet.components.items.ItemTapeMeasure;
import com.alet.components.structures.connection.RopeConnection;
import com.alet.components.structures.regestries.StructureTypeRegestery;
import com.alet.components.structures.regestries.premade.PremadeStructureRegistery;
import com.alet.components.structures.type.premade.LittleAnimatorBench;
import com.alet.components.structures.type.premade.LittleFillingCabinet;
import com.alet.components.structures.type.premade.transfer.LittleTransferLittleHopper;
import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.creativecore.common.utils.math.Rotation;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.dialogs.SubGuiSignalEvents.GuiSignalEventsButton;
import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.directional.StructureDirectionalField;
import com.creativemd.littletiles.common.structure.directional.StructureDirectionalType;
import com.creativemd.littletiles.common.structure.exception.CorruptedConnectionException;
import com.creativemd.littletiles.common.structure.exception.NotYetConnectedException;
import com.creativemd.littletiles.common.tile.math.vec.LittleVec;
import com.creativemd.littletiles.common.util.grid.LittleGridContext;
import com.creativemd.littletiles.server.LittleTilesServer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ALET.MODID, name = ALET.NAME, version = ALET.VERSION, guiFactory = "com.alet.client.ALETSettings",
        dependencies = "required-after:creativecore;required-after:kirosblocks;required-after:littletiles")
@Mod.EventBusSubscriber
public class ALET {
    
    @SidedProxy(clientSide = "com.alet.client.ALETClient", serverSide = "com.alet.server.ALETServer")
    public static LittleTilesServer proxy;
    
    @Instance
    public static ALET instance;
    
    public static final String MODID = "alet";
    public static final String NAME = "A Little Extra Tiles";
    public static final String VERSION = "1.0";
    
    public static final Logger LOGGER = LogManager.getLogger(ALET.MODID);
    public static ALETConfig CONFIG;
    
    public static CreativeTabs littleCircuitTab = new CreativeTabs("alet") {
        
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(LittleTiles.wrench);
        }
        
        @Override
        public boolean hasSearchBar() {
            return true;
        }
    }.setBackgroundImageName("item_search.png");
    
    public static List<String> fontTypeNames;
    
    public static Item littleScissors;
    public static Item littleRope;
    public static Item manual;
    public static Item tapeMeasure;
    public static Item jumpRod;
    
    //public static ContainerAdapterBlock adapter;
    
    public static BasicBlock smoothOakPlank;
    public static BasicBlock smoothDarkOakPlank;
    public static BasicBlock smoothSprucePlank;
    public static BasicBlock smoothJunglePlank;
    public static BasicBlock smoothBirchPlank;
    public static BasicBlock smoothAcaciaPlank;
    
    public static BasicBlock smoothBrick;
    public static BasicBlock smoothGroutBrick;
    
    public static TransparentBlock transparent;
    
    @EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
        manual = new ItemLittleManual("little_manual");
        littleRope = new ItemLittleRope("little_rope");
        jumpRod = new ItemJumpTool("jump_rod");
        tapeMeasure = new ItemTapeMeasure("tapemeasure");
        littleScissors = new ItemLittleScissors("little_scissor");
        //adapter = new ContainerAdapterBlock("container_converter");
        
        smoothOakPlank = new BasicBlock("smoothoakplank");
        smoothDarkOakPlank = new BasicBlock("smoothdarkoakplank");
        smoothSprucePlank = new BasicBlock("smoothspruceplank");
        smoothJunglePlank = new BasicBlock("smoothjungleplank");
        smoothBirchPlank = new BasicBlock("smoothbirchplank");
        smoothAcaciaPlank = new BasicBlock("smoothacaciaplank");
        transparent = new TransparentBlock("transparent");
        
        smoothBrick = new BasicBlock("smoothbrick");
        smoothGroutBrick = new BasicBlock("smoothgroutbrick");
        
        if (event.getSide().equals(Side.CLIENT)) {
            ALETClient.addItemToRenderTiles(jumpRod);
            createStructureFolder();
            getFonts();
            GuiHandler.registerGuiHandler("notice", new CustomGuiHandler() {
                @Override
                public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
                    return new SubGuiNoticeAtJoin();
                }
                
                @Override
                public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
                    return new SubContainerBasic(player);
                }
                
            });
        }
        StructureDirectionalType.register(RopeConnection.class, new StructureDirectionalType<RopeConnection>() {
            
            @Override
            public RopeConnection read(StructureDirectionalField field, LittleStructure structure, NBTBase nbt) {
                return new RopeConnection(structure, (NBTTagCompound) nbt);
            }
            
            @Override
            public NBTBase write(StructureDirectionalField field, RopeConnection value) {
                return value.writeToNBT(new NBTTagCompound());
            }
            
            @Override
            public RopeConnection move(StructureDirectionalField field, RopeConnection value, LittleGridContext context, LittleVec offset) {
                return value;
            }
            
            @Override
            public RopeConnection mirror(StructureDirectionalField field, RopeConnection value, LittleGridContext context, Axis axis, LittleVec doubledCenter) {
                value.mirrorConnection(axis);
                return value;
            }
            
            @Override
            public RopeConnection rotate(StructureDirectionalField field, RopeConnection value, LittleGridContext context, Rotation rotation, LittleVec doubledCenter) {
                value.rotateConnection(rotation);
                return value;
            }
            
            @Override
            public Object getDefault(StructureDirectionalField field, LittleStructure structure, Object defaultValue) {
                return defaultValue;
            }
            
        });
        
        GuiHandler.registerGuiHandler("noblue", new CustomGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
                return new SubGuiNoBluePrintMessage();
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
                return new SubContainerBasic(player);
            }
        });
        GuiHandler.registerGuiHandler("photo-import", new LittleStructureGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubGuiPhotoImport();
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubContainerPhotoImport(player);
            }
        });
        
        GuiHandler.registerGuiHandler("type-writter", new LittleStructureGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubGuiTypeWriter(structure);
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubContainerTypeWriter(player);
            }
        });
        GuiHandler.registerGuiHandler("little_hopper", new LittleStructureGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubGuiLittleHopper();
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubContainerLittleHopper(player, (LittleTransferLittleHopper) structure);
            }
        });
        GuiHandler.registerGuiHandler("manual", new CustomGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
                return new SubGuiManual();
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
                return new SubContainerBasic(player);
            }
        });
        GuiHandler.registerGuiHandler("filling_cabinet", new LittleStructureGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubGuiFillingCabinet(structure);
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubContainerFillingCabinet(player, (LittleFillingCabinet) structure);
            }
        });
        GuiHandler.registerGuiHandler("animators_workbench", new LittleStructureGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubGuiAnimatorsWorkbench(structure);
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubContainerAnimatorWorkbench(player, (LittleAnimatorBench) structure);
            }
        });
        
        GuiHandler.registerGuiHandler("signal_interface", new LittleStructureGuiHandler() {
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                try {
                    LittleStructure parent = structure.getParent().getStructure();
                    GuiSignalEventsButton button = new GuiSignalEventsButton("", 0, 0, parent.getPreviews(parent
                            .getPos()), parent, parent.type);
                    return new SubGuiSignalEventsALET(button);
                } catch (CorruptedConnectionException | NotYetConnectedException e) {}
                return null;
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubContainerBasic(player);
            }
        });
        StructureTypeRegestery.registerStructureTypes();
        proxy.loadSidePre();
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(transparent, smoothGroutBrick, smoothBrick, smoothOakPlank, smoothDarkOakPlank,
            smoothAcaciaPlank, smoothSprucePlank, smoothJunglePlank, smoothBirchPlank);
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(manual, littleScissors, littleRope, jumpRod, tapeMeasure, new ItemBlock(transparent)
                .setRegistryName(transparent.getRegistryName()), new ItemBlock(smoothGroutBrick).setRegistryName(
                    smoothGroutBrick.getRegistryName()), new ItemBlock(smoothBrick).setRegistryName(smoothBrick
                            .getRegistryName()), new ItemBlock(smoothOakPlank).setRegistryName(smoothOakPlank
                                    .getRegistryName()), new ItemBlock(smoothDarkOakPlank).setRegistryName(smoothDarkOakPlank
                                            .getRegistryName()), new ItemBlock(smoothAcaciaPlank).setRegistryName(
                                                smoothAcaciaPlank.getRegistryName()), new ItemBlock(smoothSprucePlank)
                                                        .setRegistryName(smoothSprucePlank.getRegistryName()),
            new ItemBlock(smoothJunglePlank).setRegistryName(smoothJunglePlank.getRegistryName()),
            new ItemBlock(smoothBirchPlank).setRegistryName(smoothBirchPlank.getRegistryName()));
        proxy.loadSide();
    }
    
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        registerRender(littleRope);
        registerRender(littleScissors);
        registerRender(manual);
        registerRender(tapeMeasure);
        registerRender(Item.getItemFromBlock(smoothGroutBrick));
        registerRender(Item.getItemFromBlock(smoothBrick));
        registerRender(Item.getItemFromBlock(smoothOakPlank));
        registerRender(Item.getItemFromBlock(smoothDarkOakPlank));
        registerRender(Item.getItemFromBlock(smoothAcaciaPlank));
        registerRender(Item.getItemFromBlock(smoothSprucePlank));
        registerRender(Item.getItemFromBlock(smoothJunglePlank));
        registerRender(Item.getItemFromBlock(smoothBirchPlank));
    }
    
    public static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
    
    @EventHandler
    public void Init(FMLInitializationEvent event) {
        CreativeConfigRegistry.ROOT.registerValue(MODID, CONFIG = new ALETConfig());
        PacketRegistery.registerPackets();
        SoundsRegister.registerSounds();
        PremadeStructureRegistery.registerPremadeStructures();
    }
    
    @EventHandler
    public void PostInit(FMLPostInitializationEvent event) {
        proxy.loadSidePost();
        
        if (event.getSide().equals(Side.CLIENT)) {
            NodeRegistery.registerNodes();
            FunctionRegistery.registerFunctions();
        }
    }
    
    public static void createStructureFolder() {
        File d = new File("./little_structures");
        if (!d.exists())
            d.mkdir();
    }
    
    public static List<String> getFonts() {
        fontTypeNames = new ArrayList<>();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            InputStream font = Minecraft.getMinecraft().getClass().getClassLoader().getResourceAsStream(
                "assets/alet/font/PressStart2P-Regular.ttf");
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, font));
            File d = new File("./fonts");
            if (!d.exists()) {
                d.mkdir();
                try {
                    String data = "Place any TrueTypeFont files in this folder to add them to the typewritter. \n" + "If you added any TrueTypeFont files while still in Minecraft you can run the commmand \n" + "/alet-updatefont to add the new fonts. Otherwise just launching Minecraft will gather \n" + "the new files.";
                    File f1 = new File("./fonts/README.txt");
                    if (!f1.exists())
                        f1.createNewFile();
                    
                    BufferedWriter writer = new BufferedWriter(new FileWriter("./fonts/README.txt"));
                    writer.write(data);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (File file : d.listFiles()) {
                String fileName = file.getName();
                if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                    if (fileName.substring(fileName.lastIndexOf(".") + 1).equalsIgnoreCase("ttf") || fileName.substring(
                        fileName.lastIndexOf(".") + 1).equalsIgnoreCase("otf"))
                        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, file));
            }
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        for (int i = 0; i < fonts.length; i++)
            fontTypeNames.add(fonts[i]);
        
        return fontTypeNames;
    }
    
}
