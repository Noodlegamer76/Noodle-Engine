package com.noodlegamer76.engine.worldgen.megastructure.structure.placers;

import com.noodlegamer76.engine.worldgen.megastructure.structure.structures.StructureInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.AABB;

public abstract class Placer {
    private final AABB boundingBox;

    public Placer(AABB boundingBox) {
        this.boundingBox = boundingBox;
    }

    public AABB getBoundingBox() {
        return boundingBox;
    }

    public abstract void place(FeaturePlaceContext<NoneFeatureConfiguration> ctx, RandomSource random, StructureInstance instance);
}
