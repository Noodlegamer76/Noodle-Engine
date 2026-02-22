package com.noodlegamer76.engine.megastructure;

import com.noodlegamer76.engine.megastructure.structure.StructureDefinition;
import com.noodlegamer76.engine.megastructure.structure.Structures;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MegaStructureGenerator {
    public static void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        for (StructureDefinition def : Structures.getInstance(false).getDefinitions().values()) {
            generateInstance(ctx, def);
        }
    }

    private static void generateInstance(FeaturePlaceContext<NoneFeatureConfiguration> ctx, StructureDefinition definition) {
        StructureInstance instance = new StructureInstance(definition, ctx);
        instance.generate(ctx);
    }
}
