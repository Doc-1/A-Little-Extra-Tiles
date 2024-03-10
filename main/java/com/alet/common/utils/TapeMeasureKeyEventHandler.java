package com.alet.common.utils;

import com.alet.client.ALETClient;
import com.alet.items.ItemTapeMeasure;
import com.creativemd.littletiles.client.LittleTilesClient;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TapeMeasureKeyEventHandler {
	
	public static final int UP = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int RIGHT = 4;
	public static final int CLEAR = 5;
	
	public int[] keyBindingPressed() {
		int[] returnB = { 0, 0 }; //returnB[0] boolean for if any key is pressed, returnB[1] int to tell which button is pressed
		while (LittleTilesClient.up.isPressed()) {
			returnB[0] = 1;
			returnB[1] = 1;
		}
		while (LittleTilesClient.down.isPressed()) {
			returnB[0] = 1;
			returnB[1] = 2;
		}
		while (LittleTilesClient.left.isPressed()) {
			returnB[0] = 1;
			returnB[1] = 3;
		}
		while (LittleTilesClient.right.isPressed()) {
			returnB[0] = 1;
			returnB[1] = 4;
		}
		while (ALETClient.clearMeasurment.isPressed()) {
			returnB[0] = 1;
			returnB[1] = 5;
		}
		return returnB;
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	@SideOnly(Side.CLIENT)
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			if (mc.player != null) {
				ItemStack itemInMainHand = mc.player.getHeldItemMainhand();
				if (itemInMainHand.getItem() instanceof ItemTapeMeasure) {
					ItemTapeMeasure tapeMeasureInMainHand = (ItemTapeMeasure) itemInMainHand.getItem();
					
					int[] data = keyBindingPressed();
					int isKeyBindingPressed = data[0];
					int getKeyBindingPressed = data[1];
					
					if (isKeyBindingPressed == 1) {
						tapeMeasureInMainHand.onKeyPress(getKeyBindingPressed, mc.player, itemInMainHand);
					}
				} else
					keyBindingPressed(); //clears memory for some reason without it if the button is spammed while tape measure is not in main hand
					                     //Once placed in main hand it runs the method that many times you pressed the button.
			}
		}
	}
	
}
