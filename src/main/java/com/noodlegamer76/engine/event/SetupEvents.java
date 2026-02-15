package com.noodlegamer76.engine.event;
;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.gltf.load.GltfLoader;
import com.noodlegamer76.engine.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = NoodleEngine.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void clientSetup(FMLCommonSetupEvent event) throws Exception {
        event.enqueueWork(PacketHandler::register);
        event.enqueueWork(() -> {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            String modelPath = "gltf";
            GltfLoader.loadAllGlbModels(resourceManager, modelPath);
        });
    }
}
