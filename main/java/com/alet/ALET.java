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

import com.alet.client.ALETClient;
import com.alet.client.container.SubContainerAnimatorWorkbench;
import com.alet.client.container.SubContainerBasic;
import com.alet.client.container.SubContainerBlock;
import com.alet.client.container.SubContainerFillingCabinet;
import com.alet.client.container.SubContainerPhotoImport;
import com.alet.client.container.SubContainerTypeWriter;
import com.alet.client.gui.SubGuiAnimatorsWorkbench;
import com.alet.client.gui.SubGuiBlock;
import com.alet.client.gui.SubGuiFillingCabinet;
import com.alet.client.gui.SubGuiMagnitudeComparator;
import com.alet.client.gui.SubGuiManual;
import com.alet.client.gui.SubGuiMicroProcessor;
import com.alet.client.gui.SubGuiPhotoImport;
import com.alet.client.gui.SubGuiTypeWriter;
import com.alet.client.gui.message.SubGuiNoBluePrintMessage;
import com.alet.client.sounds.SoundsHandler;
import com.alet.common.blocks.BasicBlock;
import com.alet.common.blocks.TransparentBlock;
import com.alet.common.entity.EntityLeadConnection;
import com.alet.common.packet.PacketConnectLead;
import com.alet.common.packet.PacketDropItem;
import com.alet.common.packet.PacketGetServerCams;
import com.alet.common.packet.PacketSendGuiToClient;
import com.alet.common.packet.PacketSendServerCams;
import com.alet.common.packet.PacketSendSound;
import com.alet.common.packet.PacketUpdateBreakBlock;
import com.alet.common.packet.PacketUpdateMutateFromServer;
import com.alet.common.packet.PacketUpdateNBT;
import com.alet.common.packet.PacketUpdateStructureFromClient;
import com.alet.common.structure.type.LittleAlwaysOnLight;
import com.alet.common.structure.type.LittleAlwaysOnLight.LittleAlwaysOnLightStructureParser;
import com.alet.common.structure.type.LittleCamPlayerALET;
import com.alet.common.structure.type.LittleGuiLinkerALET;
import com.alet.common.structure.type.LittleLeadConnectionALET;
import com.alet.common.structure.type.LittleLockALET;
import com.alet.common.structure.type.LittleMusicComposerALET;
import com.alet.common.structure.type.LittleStateMutatorALET;
import com.alet.common.structure.type.premade.LittleAdjustableFixedStructure;
import com.alet.common.structure.type.premade.LittleAnimatorBench;
import com.alet.common.structure.type.premade.LittleFillingCabinet;
import com.alet.common.structure.type.premade.LittlePhotoImporter;
import com.alet.common.structure.type.premade.LittleTypeWriter;
import com.alet.common.structure.type.premade.PickupItemPremade;
import com.alet.common.structure.type.premade.signal.LittleCircuitClock;
import com.alet.common.structure.type.premade.signal.LittleCircuitClockAdvanced;
import com.alet.common.structure.type.premade.signal.LittleCircuitColoredDisplay16;
import com.alet.common.structure.type.premade.signal.LittleCircuitConverterFourToOne;
import com.alet.common.structure.type.premade.signal.LittleCircuitConverterFourToSixteen;
import com.alet.common.structure.type.premade.signal.LittleCircuitConverterOneToFour;
import com.alet.common.structure.type.premade.signal.LittleCircuitConverterSixteenToFour;
import com.alet.common.structure.type.premade.signal.LittleCircuitDisplay16;
import com.alet.common.structure.type.premade.signal.LittleCircuitMicroprocessor;
import com.alet.common.structure.type.premade.signal.LittleCircuitRandomNumberOne;
import com.alet.common.structure.type.premade.signal.LittleCircuitTFlipFlop;
import com.alet.common.structure.type.premade.signal.LittleMagnitudeComparator;
import com.alet.common.structure.type.premade.signal.LittlePremadeSignalInputQuickOne;
import com.alet.common.structure.type.premade.signal.LittlePremadeSignalOutputQuickOne;
import com.alet.common.structure.type.premade.signal.LittleSignalColoredDisplay;
import com.alet.common.structure.type.premade.signal.LittleSignalInputQuick;
import com.alet.common.structure.type.premade.signal.LittleSignalInputQuick.LittleStructureTypeInputQuick;
import com.alet.common.structure.type.premade.signal.LittleSignalOutputQuick;
import com.alet.common.structure.type.premade.signal.LittleSignalOutputQuick.LittleStructureTypeOutputQuick;
import com.alet.common.structure.type.premade.signal.LittleStructureTypeCircuit;
import com.alet.common.structure.type.premade.transfer.LittleTransferItemExport;
import com.alet.common.structure.type.premade.transfer.LittleTransferItemImport;
import com.alet.common.structure.type.trigger.LittleTriggerBoxStructureALET;
import com.alet.items.ItemJumpTool;
import com.alet.items.ItemLittleManual;
import com.alet.items.ItemLittleRope;
import com.alet.items.ItemTapeMeasure;
import com.alet.items.premade.ItemPremadePlaceable;
import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.creativecore.common.gui.container.SubContainer;
import com.creativemd.creativecore.common.gui.container.SubGui;
import com.creativemd.creativecore.common.gui.opener.CustomGuiHandler;
import com.creativemd.creativecore.common.gui.opener.GuiHandler;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.littletiles.LittleTiles;
import com.creativemd.littletiles.client.gui.handler.LittleStructureGuiHandler;
import com.creativemd.littletiles.common.structure.LittleStructure;
import com.creativemd.littletiles.common.structure.attribute.LittleStructureAttribute;
import com.creativemd.littletiles.common.structure.registry.LittleStructureRegistry;
import com.creativemd.littletiles.common.structure.registry.StructureIngredientRule.StructureIngredientScalerVolume;
import com.creativemd.littletiles.common.structure.signal.logic.SignalMode;
import com.creativemd.littletiles.common.structure.type.premade.LittleStructurePremade;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalCable;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalCable.LittleStructureTypeCable;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalInput.LittleStructureTypeInput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput;
import com.creativemd.littletiles.common.structure.type.premade.signal.LittleSignalOutput.LittleStructureTypeOutput;
import com.creativemd.littletiles.common.util.ingredient.StackIngredient;
import com.creativemd.littletiles.server.LittleTilesServer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ALET.MODID, name = ALET.NAME, version = ALET.VERSION, guiFactory = "com.alet.client.ALETSettings",
        dependencies = "required-after:creativecore;required-after:littletiles")
@Mod.EventBusSubscriber
public class ALET {
    
    @SidedProxy(clientSide = "com.alet.client.ALETClient", serverSide = "com.alet.server.ALETServer")
    public static LittleTilesServer proxy;
    
    @Instance
    public static ALET instance;
    
    public static final String MODID = "alet";
    public static final String NAME = "A Little Extra Tiles";
    public static final String VERSION = "1.0";
    
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
    public static List<String> sounds;
    
    public static Item littleRope;
    public static Item manual;
    public static Item tapeMeasure;
    public static Item jumpRod;
    
    public static Item Hz10Clock;
    public static Item Hz10ClockAdv;
    public static Item Convert1to4;
    public static Item Convert4to1;
    public static Item Convert4to16;
    public static Item Convert16to4;
    
    public static Item magCompare1;
    public static Item magCompare4;
    public static Item magCompare16;
    
    public static Item randomGen1Bit;
    public static Item randomGen4Bit;
    public static Item randomGen16Bit;
    
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
        
        magCompare1 = new ItemPremadePlaceable("magnitude_comparator_1", true);
        magCompare4 = new ItemPremadePlaceable("magnitude_comparator_4", true);
        magCompare16 = new ItemPremadePlaceable("magnitude_comparator_16", true);
        
        if (event.getSide().equals(Side.CLIENT)) {
            ALETClient.addItemToRenderTiles(jumpRod);
            createStructureFolder();
            getFonts();
        }
        
        registerEntity();
        
        GuiHandler.registerGuiHandler("block", new CustomGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt) {
                return new SubGuiBlock();
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt) {
                return new SubContainerBlock(player);
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
        
        GuiHandler.registerGuiHandler("magnitude-comparator", new LittleStructureGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubGuiMagnitudeComparator(structure);
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubContainerBasic(player);
            }
        });
        GuiHandler.registerGuiHandler("programmer", new LittleStructureGuiHandler() {
            
            @Override
            @SideOnly(Side.CLIENT)
            public SubGui getGui(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubGuiMicroProcessor((LittleCircuitMicroprocessor) structure);
            }
            
            @Override
            public SubContainer getContainer(EntityPlayer player, NBTTagCompound nbt, LittleStructure structure) {
                return new SubContainerBasic(player);
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
        LittleStructureRegistry
                .registerStructureType("allows_on_light", "simple", LittleAlwaysOnLight.class, LittleStructureAttribute.LIGHT_EMITTER, LittleAlwaysOnLightStructureParser.class)
                .addIngredient(new StructureIngredientScalerVolume(8), () -> new StackIngredient(new ItemStack(Items.GLOWSTONE_DUST)));
        LittleStructureRegistry
                .registerStructureType("tigger_box", "advance", LittleTriggerBoxStructureALET.class, LittleStructureAttribute.NOCOLLISION | LittleStructureAttribute.COLLISION_LISTENER, LittleTriggerBoxStructureALET.LittleTriggerBoxStructureParser.class)
                .addInput("players", 4).addInput("entities", 4).addOutput("listen", 1, SignalMode.TOGGLE).addInput("left click", 1);
        LittleStructureRegistry.registerStructureType("door_lock", "door", LittleLockALET.class, LittleStructureAttribute.NONE, LittleLockALET.LittleLockParserALET.class)
                .addOutput("lock", 1, SignalMode.TOGGLE, true);
        LittleStructureRegistry
                .registerStructureType("state_activator", "advance", LittleStateMutatorALET.class, LittleStructureAttribute.NONE, LittleStateMutatorALET.LittleStateMutatorParserALET.class)
                .addOutput("activate", 1, SignalMode.TOGGLE, true);
        LittleStructureRegistry
                .registerStructureType("music_composer", "sound", LittleMusicComposerALET.class, LittleStructureAttribute.TICKING, LittleMusicComposerALET.LittleMusicComposerParserALET.class)
                .addOutput("play", 1, SignalMode.TOGGLE).addInput("finished", 1);
        LittleStructureRegistry
                .registerStructureType("lead_connection", "simple", LittleLeadConnectionALET.class, LittleStructureAttribute.NONE, LittleLeadConnectionALET.LittleLeadConnectionParserALET.class);
        LittleStructureRegistry
                .registerStructureType("gui_linker", "advance", LittleGuiLinkerALET.class, LittleStructureAttribute.NONE, LittleGuiLinkerALET.LittleGuiLinkerParserALET.class);
        
        //LittleStructureRegistry.registerStructureType(new LittleAxisDoorType("loop_door", "door", LittleAxisLoopDoor.class, LittleStructureAttribute.NONE).addOutput("state", 1, SignalMode.TOGGLE), LittleAxisLoopDoorParser.class);
        if (Loader.isModLoaded("cmdcam"))
            LittleStructureRegistry
                    .registerStructureType("cam_player", "advance", LittleCamPlayerALET.class, LittleStructureAttribute.TICKING, LittleCamPlayerALET.LittleCamPlayerParserALET.class)
                    .addOutput("play", 1, SignalMode.TOGGLE);
        
        proxy.loadSidePre();
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry()
                .registerAll(transparent, smoothGroutBrick, smoothBrick, smoothOakPlank, smoothDarkOakPlank, smoothAcaciaPlank, smoothSprucePlank, smoothJunglePlank, smoothBirchPlank);
    }
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(magCompare1, magCompare4, magCompare16, manual, littleRope, jumpRod, tapeMeasure, new ItemBlock(transparent)
                .setRegistryName(transparent.getRegistryName()), new ItemBlock(smoothGroutBrick).setRegistryName(smoothGroutBrick.getRegistryName()), new ItemBlock(smoothBrick)
                        .setRegistryName(smoothBrick.getRegistryName()), new ItemBlock(smoothOakPlank)
                                .setRegistryName(smoothOakPlank.getRegistryName()), new ItemBlock(smoothDarkOakPlank)
                                        .setRegistryName(smoothDarkOakPlank.getRegistryName()), new ItemBlock(smoothAcaciaPlank)
                                                .setRegistryName(smoothAcaciaPlank.getRegistryName()), new ItemBlock(smoothSprucePlank)
                                                        .setRegistryName(smoothSprucePlank.getRegistryName()), new ItemBlock(smoothJunglePlank).setRegistryName(smoothJunglePlank
                                                                .getRegistryName()), new ItemBlock(smoothBirchPlank).setRegistryName(smoothBirchPlank.getRegistryName()));
        proxy.loadSide();
    }
    
    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        registerRender(littleRope);
        registerRender(manual);
        registerRender(magCompare1);
        registerRender(magCompare4);
        registerRender(magCompare16);
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
        CreativeCorePacket.registerPacket(PacketUpdateNBT.class);
        CreativeCorePacket.registerPacket(PacketSendSound.class);
        CreativeCorePacket.registerPacket(PacketUpdateStructureFromClient.class);
        CreativeCorePacket.registerPacket(PacketSendGuiToClient.class);
        CreativeCorePacket.registerPacket(PacketDropItem.class);
        CreativeCorePacket.registerPacket(PacketConnectLead.class);
        CreativeCorePacket.registerPacket(PacketUpdateBreakBlock.class);
        CreativeCorePacket.registerPacket(PacketUpdateMutateFromServer.class);
        
        if (Loader.isModLoaded("cmdcam")) {
            CreativeCorePacket.registerPacket(PacketSendServerCams.class);
            CreativeCorePacket.registerPacket(PacketGetServerCams.class);
        }
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeInput("single_input17", "premade", LittleSignalInput.class, LittleStructureAttribute.EXTRA_RENDERING, ALET.MODID, 17));
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeOutput("single_output17", "premade", LittleSignalOutput.class, LittleStructureAttribute.EXTRA_RENDERING, ALET.MODID, 17));
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCable("single_cable17", "premade", LittleSignalCable.class, LittleStructureAttribute.EXTRA_RENDERING, ALET.MODID, 17));
        
        LittleStructurePremade.registerPremadeStructureType("signal_color_display_16", ALET.MODID, LittleSignalColoredDisplay.class, LittleStructureAttribute.TICK_RENDERING)
                .addOutput("pixel0", 4, SignalMode.EQUAL, true).addOutput("pixel1", 4, SignalMode.EQUAL, true).addOutput("pixel2", 4, SignalMode.EQUAL, true)
                .addOutput("pixel3", 4, SignalMode.EQUAL, true).addOutput("pixel4", 4, SignalMode.EQUAL, true).addOutput("pixel5", 4, SignalMode.EQUAL, true)
                .addOutput("pixel6", 4, SignalMode.EQUAL, true).addOutput("pixel7", 4, SignalMode.EQUAL, true).addOutput("pixel8", 4, SignalMode.EQUAL, true)
                .addOutput("pixel9", 4, SignalMode.EQUAL, true).addOutput("pixel10", 4, SignalMode.EQUAL, true).addOutput("pixel11", 4, SignalMode.EQUAL, true)
                .addOutput("pixel12", 4, SignalMode.EQUAL, true).addOutput("pixel13", 4, SignalMode.EQUAL, true).addOutput("pixel14", 4, SignalMode.EQUAL, true)
                .addOutput("pixel15", 4, SignalMode.EQUAL, true);
        
        CreativeConfigRegistry.ROOT.registerValue(MODID, CONFIG = new ALETConfig());
        LittleStructurePremade.registerPremadeStructureType("item_export", ALET.MODID, LittleTransferItemExport.class, LittleStructureAttribute.TICKING).setNotSnapToGrid()
                .addOutput("drop", 16, SignalMode.EQUAL, true);
        LittleStructurePremade.registerPremadeStructureType("item_import", ALET.MODID, LittleTransferItemImport.class, LittleStructureAttribute.TICKING).setNotSnapToGrid()
                .addOutput("block", 16, SignalMode.EQUAL, true);
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("clock_simple", ALET.MODID, LittleCircuitClock.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("microprocessor_10_1bit", ALET.MODID, LittleCircuitMicroprocessor.class, LittleStructureAttribute.PREMADE, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("clock_advanced", ALET.MODID, LittleCircuitClockAdvanced.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().setNotShowCreativeTab();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("display_16", ALET.MODID, LittleCircuitDisplay16.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("colored_display_16", ALET.MODID, LittleCircuitColoredDisplay16.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("1_16Bit_To_4_4Bits", ALET.MODID, LittleCircuitConverterSixteenToFour.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().setNotShowCreativeTab();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("4_4Bits_To_1_16Bit", ALET.MODID, LittleCircuitConverterFourToSixteen.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().setNotShowCreativeTab();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("1_4Bit_To_4_1Bits", ALET.MODID, LittleCircuitConverterFourToOne.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().setNotShowCreativeTab();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("magnitude_comparator_1", ALET.MODID, LittleMagnitudeComparator.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().setNotShowCreativeTab();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("magnitude_comparator_4", ALET.MODID, LittleMagnitudeComparator.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().setNotShowCreativeTab();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("magnitude_comparator_16", ALET.MODID, LittleMagnitudeComparator.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().setNotShowCreativeTab();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("4_1Bits_To_1_4Bit", ALET.MODID, LittleCircuitConverterOneToFour.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().setNotShowCreativeTab();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("random_generator_1bit", ALET.MODID, LittleCircuitRandomNumberOne.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().setNotShowCreativeTab();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("t_flip_flop", ALET.MODID, LittleCircuitTFlipFlop.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("premade_quick_input1", ALET.MODID, LittlePremadeSignalInputQuickOne.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid().addOutput("SignalMode", 1, SignalMode.PULSE);
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeCircuit("premade_quick_output1", ALET.MODID, LittlePremadeSignalOutputQuickOne.class, LittleStructureAttribute.TICKING, ALET.MODID))
                .setNotSnapToGrid();
        
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeOutputQuick("signal_quick_output1", "premade", LittleSignalOutputQuick.class, LittleStructureAttribute.EXTRA_RENDERING, ALET.MODID, 1))
                .setNotSnapToGrid();
        LittleStructurePremade
                .registerPremadeStructureType(new LittleStructureTypeInputQuick("signal_quick_input1", "premade", LittleSignalInputQuick.class, LittleStructureAttribute.EXTRA_RENDERING, ALET.MODID, 1))
                .setNotSnapToGrid();
        
        LittleStructurePremade.registerPremadeStructureType("photoimporter", ALET.MODID, LittlePhotoImporter.class);
        LittleStructurePremade.registerPremadeStructureType("animator_workbench", ALET.MODID, LittleAnimatorBench.class);
        
        LittleStructurePremade.registerPremadeStructureType("typewriter", ALET.MODID, LittleTypeWriter.class);
        LittleStructurePremade.registerPremadeStructureType("jump_rod", ALET.MODID, PickupItemPremade.class).setNotShowCreativeTab();
        LittleStructurePremade.registerPremadeStructureType("adjustable", ALET.MODID, LittleAdjustableFixedStructure.class).setNotShowCreativeTab();
        LittleStructurePremade.registerPremadeStructureType("filling_cabinet", ALET.MODID, LittleFillingCabinet.class, LittleStructureAttribute.TICKING).addInput("accessed", 1);
        
        SoundsHandler.registerSounds();
        sounds = new ArrayList<>();
        for (ResourceLocation location : SoundEvent.REGISTRY.getKeys())
            sounds.add(location.toString());
    }
    
    @EventHandler
    public void PostInit(FMLPostInitializationEvent event) {
        proxy.loadSidePost();
    }
    
    public static void createStructureFolder() {
        File d = new File("./little_structures");
        if (!d.exists())
            d.mkdir();
    }
    
    public static List<String> getFonts() {
        fontTypeNames = new ArrayList<>();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ResourceLocation pressStart2P = new ResourceLocation(ALET.MODID, "font/PressStart2P-Regular.ttf");
        try {
            InputStream font = Minecraft.getMinecraft().getClass().getClassLoader().getResourceAsStream("assets/alet/font/PressStart2P-Regular.ttf");
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
                    if (fileName.substring(fileName.lastIndexOf(".") + 1).equalsIgnoreCase("ttf") || fileName.substring(fileName.lastIndexOf(".") + 1).equalsIgnoreCase("otf"))
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
    
    public static void registerEntity() {
        registerEntity("littleleash", EntityLeadConnection.class, 1, 250, 250, true);
    }
    
    public static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int updateFrequence, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, name), entity, name, id, instance, range, updateFrequence, sendsVelocityUpdates);
        
    }
    
}
