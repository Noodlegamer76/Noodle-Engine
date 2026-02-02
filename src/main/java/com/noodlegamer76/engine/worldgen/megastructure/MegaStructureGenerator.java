package com.noodlegamer76.engine.worldgen.megastructure;

import com.noodlegamer76.engine.worldgen.megastructure.structure.StructureDefinition;
import com.noodlegamer76.engine.worldgen.megastructure.structure.Structures;
import com.noodlegamer76.engine.worldgen.megastructure.structure.structures.StructureInstance;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MegaStructureGenerator {
    public static void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        for (StructureDefinition definition: Structures.STRUCTURES) {
            StructureInstance instance = new StructureInstance(definition);
            instance.generate(ctx);
        }
    }
}
