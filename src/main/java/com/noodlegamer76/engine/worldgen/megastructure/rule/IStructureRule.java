package com.noodlegamer76.engine.worldgen.megastructure.rule;

import com.noodlegamer76.engine.worldgen.megastructure.structure.Structure;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public interface IStructureRule {
    boolean shouldGenerate(FeaturePlaceContext<NoneFeatureConfiguration> ctx, RandomSource random, Structure structure);
}
