package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NoodleEngine.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerTick {

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
        }
    }
}
