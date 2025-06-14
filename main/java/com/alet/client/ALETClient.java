package com.alet.client;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.alet.client.events.ALETEventHandler;
import com.alet.client.events.ALETTapeMeasureEventHandler;
import com.alet.client.render.overlay.DrawMeasurements;
import com.alet.common.placement.shape.type.LittleShapeCenteredBox;
import com.alet.common.placement.shape.type.LittleShapeCenteredCylinder;
import com.alet.common.placement.shape.type.LittleShapeCenteredSphere;
import com.alet.common.placement.shape.type.LittleShapeMagicWand;
import com.alet.common.placement.shape.type.LittleShapePixel;
import com.alet.server.commands.UpdateFontsCommand;
import com.creativemd.creativecore.client.CreativeCoreClient;
import com.creativemd.creativecore.client.rendering.model.CreativeBlockRenderHelper;
import com.creativemd.littletiles.common.util.shape.ShapeRegistry;
import com.creativemd.littletiles.common.util.shape.ShapeRegistry.ShapeType;
import com.creativemd.littletiles.server.LittleTilesServer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ALETClient extends LittleTilesServer {
    
    public static KeyBinding clearMeasurment;
    Minecraft mc = Minecraft.getMinecraft();
    private static ArrayList<Item> renderedItems = new ArrayList<Item>();
    
    @SideOnly(Side.CLIENT)
    public static void addItemToRenderTiles(Item... items) {
        for (Item item : items) {
            renderedItems.add(item);
        }
    }
    
    @Override
    public void loadSidePre() {}
    
    @Override
    public void loadSide() {
        
        for (Item item : renderedItems) {
            if (item.getHasSubtypes()) {
                registerItemRenderer(item);
                CreativeBlockRenderHelper.registerCreativeRenderedItem(item);
            } else {
                CreativeCoreClient.registerItemRenderer(item);
                CreativeBlockRenderHelper.registerCreativeRenderedItem(item);
            }
        }
    }
    
    @Override
    public void loadSidePost() {
        clearMeasurment = new KeyBinding("Clear Measurement", net.minecraftforge.client.settings.KeyConflictContext.UNIVERSAL, KeyModifier.ALT, Keyboard.KEY_C, "key.categories.littletiles");
        ClientRegistry.registerKeyBinding(clearMeasurment);
        ShapeRegistry.registerShape("magic_wand", new LittleShapeMagicWand(), ShapeType.SELECTOR);
        ShapeRegistry.registerShape("centered_box", new LittleShapeCenteredBox(), ShapeType.SHAPE);
        ShapeRegistry.registerShape("centered_cylider", new LittleShapeCenteredCylinder(), ShapeType.SHAPE);
        ShapeRegistry.registerShape("centered_sphere", new LittleShapeCenteredSphere(), ShapeType.SHAPE);
        ShapeRegistry.registerShape("pixel", new LittleShapePixel(), ShapeType.SHAPE);
        
        //ShapeRegistry.registerShape("fill", new DragShapeFill(), ShapeType.SHAPE);
        
        ClientCommandHandler.instance.registerCommand(new UpdateFontsCommand());
        
        MinecraftForge.EVENT_BUS.register(new DrawMeasurements());
        //MinecraftForge.EVENT_BUS.register(new TapeMeasureKeyEventHandler());
        MinecraftForge.EVENT_BUS.register(new ALETEventHandler());
        MinecraftForge.EVENT_BUS.register(new ALETTapeMeasureEventHandler());
        
        // LittleTilesClient.overlay.add(new OverlayControl(new GuiAxisIndicatorAletControl("axis"), OverlayPositionType.CENTER)
        //       .setShouldRender(() -> TapeRenderer.slotID != -1));
        //LittleTilesClient.overlay.add(new OverlayControl(new GuiDisplayMeasurements("display"), OverlayPositionType.CENTER)
        //        .setShouldRender(() -> !TapeRenderer.tapemeasure.isEmpty()));
        
        for (Item item : renderedItems) {
            CreativeCoreClient.registerItemColorHandler(item);
        }
    }
    
    public static void registerItemRenderer(Item item) {
        for (int i = 0; i <= 18; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item
                    .getRegistryName(), "inventory"));
        }
    }
}
