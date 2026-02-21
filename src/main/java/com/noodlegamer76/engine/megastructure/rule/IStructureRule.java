package com.noodlegamer76.engine.megastructure.rule;

import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

@FunctionalInterface
public interface IStructureRule {
    boolean shouldGenerate(FeaturePlaceContext<NoneFeatureConfiguration> ctx, RandomSource random, StructureExecuter structure);
}
