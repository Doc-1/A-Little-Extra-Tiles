package com.alet.client.events;

import com.alet.components.items.ItemTapeMeasure;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class ALETTapeMeasureEventHandler {
    
    @SubscribeEvent
    public void tapeMeasurePickedupEvent(PlayerEvent.ItemPickupEvent event) {
        if (event.getStack().getItem() instanceof ItemTapeMeasure) {
            System.out.println("picked");
        }
    }
    
}
