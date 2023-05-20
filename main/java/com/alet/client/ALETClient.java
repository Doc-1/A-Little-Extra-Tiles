package com.alet.client;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.alet.client.gui.overlay.GuiAxisIndicatorAletControl;
import com.alet.client.gui.overlay.GuiDisplayMeasurements;
import com.alet.client.render.entity.RenderLeashConnection;
import com.alet.common.command.UpdateFontsCommand;
import com.alet.common.entity.EntityRopeConnection;
import com.alet.common.event.ALETEventHandler;
import com.alet.common.util.TapeMeasureKeyEventHandler;
import com.alet.common.util.shape.DragShapeCenteredBox;
import com.alet.common.util.shape.DragShapeCenteredCylinder;
import com.alet.common.util.shape.DragShapeCenteredSphere;
import com.alet.common.util.shape.DragShapePixel;
import com.alet.common.util.shape.LittleShapeMagicWand;
import com.alet.render.tapemeasure.TapeRenderer;
import com.creativemd.creativecore.client.CreativeCoreClient;
import com.creativemd.creativecore.client.rendering.model.CreativeBlockRenderHelper;
import com.creativemd.littletiles.client.LittleTilesClient;
import com.creativemd.littletiles.client.render.overlay.OverlayControl;
import com.creativemd.littletiles.client.render.overlay.OverlayRenderer.OverlayPositionType;
import com.creativemd.littletiles.common.util.shape.ShapeRegistry;
import com.creativemd.littletiles.common.util.shape.ShapeRegistry.ShapeType;
import com.creativemd.littletiles.server.LittleTilesServer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
    public void loadSidePre() {
        RenderingRegistry.registerEntityRenderingHandler(EntityRopeConnection.class, new IRenderFactory<EntityRopeConnection>() {
            
            @Override
            public Render<? super EntityRopeConnection> createRenderFor(RenderManager manager) {
                return new RenderLeashConnection(manager);
            }
        });
    }
    
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
        ShapeRegistry.registerShape("centered_box", new DragShapeCenteredBox(), ShapeType.SHAPE);
        ShapeRegistry.registerShape("centered_cylider", new DragShapeCenteredCylinder(), ShapeType.SHAPE);
        ShapeRegistry.registerShape("centered_sphere", new DragShapeCenteredSphere(), ShapeType.SHAPE);
        ShapeRegistry.registerShape("pixel", new DragShapePixel(), ShapeType.SHAPE);
        
        //ShapeRegistry.registerShape("fill", new DragShapeFill(), ShapeType.SHAPE);
        
        ClientCommandHandler.instance.registerCommand(new UpdateFontsCommand());
        
        MinecraftForge.EVENT_BUS.register(new TapeRenderer());
        MinecraftForge.EVENT_BUS.register(new TapeMeasureKeyEventHandler());
        MinecraftForge.EVENT_BUS.register(new ALETEventHandler());
        
        LittleTilesClient.overlay.add(new OverlayControl(new GuiAxisIndicatorAletControl("axis"), OverlayPositionType.CENTER).setShouldRender(() -> TapeRenderer.inInv));
        LittleTilesClient.overlay.add(new OverlayControl(new GuiDisplayMeasurements("display"), OverlayPositionType.CENTER).setShouldRender(() -> TapeRenderer.inInv));
        
        for (Item item : renderedItems) {
            CreativeCoreClient.registerItemColorHandler(item);
        }
    }
    
    public static void registerItemRenderer(Item item) {
        for (int i = 0; i <= 18; i++) {
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }
}
