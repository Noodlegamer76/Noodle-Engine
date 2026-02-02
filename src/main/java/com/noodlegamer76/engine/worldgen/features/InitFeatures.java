package com.noodlegamer76.engine.worldgen.features;

import com.noodlegamer76.engine.NoodleEngine;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, NoodleEngine.MODID);

    public static final RegistryObject<MegaStructureFeature> MEGA_STRUCTURE = FEATURES.register("mega_feature",
            () -> new MegaStructureFeature(NoneFeatureConfiguration.CODEC));
}
