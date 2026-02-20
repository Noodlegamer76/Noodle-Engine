package com.noodlegamer76.engine.worldgen.megastructure.structure.placers;

import com.noodlegamer76.engine.worldgen.megastructure.structure.StructureUtils;
import com.noodlegamer76.engine.worldgen.megastructure.structure.StructureInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.phys.AABB;

public class StackedStructurePlacer extends StructurePlacer {
    private final int stackCount;
    private final Direction stackDirection;

    public StackedStructurePlacer(AABB boundingBox,
                                  BlockPos structureOrigin,
                                  StructureTemplate template,
                                  StructurePlaceSettings settings,
                                  int stackCount,
                                  Direction stackDirection) {
        super(boundingBox, structureOrigin, template, settings);
        this.stackCount = stackCount;
        this.stackDirection = stackDirection;
    }

    @Override
    public void place(FeaturePlaceContext<NoneFeatureConfiguration> ctx,
                      RandomSource random,
                      StructureInstance instance) {

        BlockPos chunkMin = ctx.origin();
        BlockPos chunkMax = chunkMin.offset(15, ctx.level().getMaxBuildHeight() - 1, 15);

        StructureTemplate template = getStructureTemplate();

        BoundingBox localBox = template.getBoundingBox(getSettings(), BlockPos.ZERO);

        int sizeX = localBox.getXSpan();
        int sizeY = localBox.getYSpan();
        int sizeZ = localBox.getZSpan();

        BlockPos baseOrigin = getStructureOrigin();

        for (int i = 0; i < stackCount; i++) {

            BlockPos offset = getDirectionalOffset(stackDirection, sizeX, sizeY, sizeZ).multiply(i);
            BlockPos placementPos = baseOrigin.offset(offset);

            StructureUtils.placeTemplateInChunk(
                    ctx.level(),
                    template,
                    placementPos,
                    chunkMin,
                    chunkMax,
                    getSettings(),
                    random,
                    2
            );
        }
    }

    private BlockPos getDirectionalOffset(Direction dir, int sizeX, int sizeY, int sizeZ) {
        return switch (dir) {
            case UP -> new BlockPos(0, sizeY, 0);
            case DOWN -> new BlockPos(0, -sizeY, 0);
            case NORTH -> new BlockPos(0, 0, -sizeZ);
            case SOUTH -> new BlockPos(0, 0, sizeZ);
            case EAST -> new BlockPos(sizeX, 0, 0);
            case WEST -> new BlockPos(-sizeX, 0, 0);
        };
    }
}
