package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.worldgen.megastructure.structure.Structures;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NoodleEngine.MODID)
public class ServerSetupEvent {

    @SubscribeEvent
    public static void serverSetup(ServerStartedEvent event) {
        Structures.getInstance().setupStructures(event.getServer());
    }
}
