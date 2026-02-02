package com.noodlegamer76.engine;

import com.mojang.logging.LogUtils;
import com.noodlegamer76.engine.core.NativeLoader;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.entity.InitEntities;
import com.noodlegamer76.engine.item.InitItems;
import com.noodlegamer76.engine.worldgen.features.InitFeatures;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(NoodleEngine.MODID)
public class NoodleEngine {
    public static final String MODID = "noodle_engine";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NoodleEngine(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        new NativeLoader();

        InitComponents.COMPONENT_TYPES.register(modEventBus);
        InitEntities.ENTITY_TYPES.register(modEventBus);
        InitItems.ITEMS.register(modEventBus);
        InitFeatures.FEATURES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
