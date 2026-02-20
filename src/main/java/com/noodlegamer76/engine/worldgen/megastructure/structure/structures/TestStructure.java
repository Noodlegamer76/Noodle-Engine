package com.noodlegamer76.engine.worldgen.megastructure.structure.structures;

import com.noodlegamer76.engine.worldgen.megastructure.Node;
import com.noodlegamer76.engine.worldgen.megastructure.structure.Structure;
import com.noodlegamer76.engine.worldgen.megastructure.structure.StructureUtils;
import com.noodlegamer76.engine.worldgen.megastructure.structure.placers.StructurePlacer;
import com.noodlegamer76.engine.worldgen.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.worldgen.megastructure.structure.context.GenVar;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TestStructure extends Structure {
    private final ResourceLocation structureTemplate;

    public TestStructure(int priority, ResourceLocation structureTemplate) {
        super(priority);
        this.structureTemplate = structureTemplate;
    }

    @Override
    public int getMaxSize() {
        return 31;
    }

    @Override
    public List<GenVar<?>> getGenVariables() {
        return List.of();
    }

    @Override
    public void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx, Node n, RandomSource random, StructureInstance instance) {
        var server = ctx.level().getServer();
        if (server == null) return;

        StructureTemplate template = server
                .getStructureManager()
                .get(structureTemplate)
                .orElse(null);

        if (template == null) return;

        int originX = random.nextIntBetweenInclusive(n.getX(), n.getX() + n.getSize());
        int originY = 100;
        int originZ = random.nextIntBetweenInclusive(n.getZ(), n.getZ() + n.getSize());

        AABB boundingBox = new AABB(
                originX,
                originY,
                originZ,
                originX + getMaxSize(),
                ctx.level().getMaxBuildHeight(),
                originZ + getMaxSize()
        );

        AABB intersection = StructureUtils.getChunkIntersection(ctx, boundingBox);

        if (intersection == null) {
            return;
        }

        StructurePlaceSettings settings = new StructurePlaceSettings();

        StructurePlacer placer = new StructurePlacer(intersection, new BlockPos(originX, originY, originZ), template, settings);
        instance.addPlacer(placer);
    }
}
