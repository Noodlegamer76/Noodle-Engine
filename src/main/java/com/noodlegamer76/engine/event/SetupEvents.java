package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.network.PacketHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = NoodleEngine.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void clientSetup(FMLCommonSetupEvent event) throws Exception {
        event.enqueueWork(PacketHandler::register);
    }
}
