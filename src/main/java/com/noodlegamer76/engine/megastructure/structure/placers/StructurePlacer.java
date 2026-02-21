package com.noodlegamer76.engine.megastructure.structure.placers;

import com.noodlegamer76.engine.megastructure.structure.StructureUtils;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

public class StructurePlacer extends Placer {
    private final StructureTemplate structureTemplate;
    private final BlockPos structureOrigin;
    private final StructurePlaceSettings settings;

    public StructurePlacer(AABB boundingBox, BlockPos structureOrigin, StructureTemplate structureTemplate, StructurePlaceSettings settings) {
        super(boundingBox);
        this.structureTemplate = structureTemplate;
        this.structureOrigin = structureOrigin;
        this.settings = settings;
    }

    public StructureTemplate getStructureTemplate() {
        return structureTemplate;
    }

    public BlockPos getStructureOrigin() {
        return structureOrigin;
    }

    public StructurePlaceSettings getSettings() {
        return settings;
    }

    @Override
    public void place(FeaturePlaceContext<NoneFeatureConfiguration> ctx,
                      RandomSource random,
                      StructureInstance instance) {
        BlockPos chunkMin = ctx.origin();
        BlockPos chunkMax = chunkMin.offset(15, ctx.level().getMaxBuildHeight() - 1, 15);

        StructureUtils.placeTemplateInChunk(ctx.level(), structureTemplate, structureOrigin, chunkMin, chunkMax, settings, random, 2);
    }
}
