package com.noodlegamer76.engine.event;
;
import com.mojang.blaze3d.systems.RenderSystem;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.client.glitf.util.GltfLoader;
import com.noodlegamer76.engine.core.NativeLoader;
import com.noodlegamer76.engine.network.PacketHandler;
import com.noodlegamer76.engine.physics.PhysicsEngine;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import de.javagl.jgltf.model.io.GltfModelReader;
import io.netty.util.internal.NativeLibraryLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = NoodleEngine.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void clientSetup(FMLCommonSetupEvent event) throws Exception {
        event.enqueueWork(PacketHandler::register);
        event.enqueueWork(() -> {
            PhysicsEngine.getInstance();
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            String modelPath = "gltf";
            GltfLoader.loadAllGlbModels(resourceManager, modelPath);
        });
    }
}
