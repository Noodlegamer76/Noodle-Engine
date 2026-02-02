package com.noodlegamer76.engine.worldgen.megastructure.structure.structures.dungeon;

import com.noodlegamer76.engine.worldgen.megastructure.Node;
import com.noodlegamer76.engine.worldgen.megastructure.structure.Structure;
import com.noodlegamer76.engine.worldgen.megastructure.structure.placers.BlockPlacer;
import com.noodlegamer76.engine.worldgen.megastructure.structure.placers.SurfaceBlockPlacer;
import com.noodlegamer76.engine.worldgen.megastructure.structure.structures.StructureInstance;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SurfaceLayerStructure extends Structure {
    private final int xSize;
    private final int zSize;
    private final BlockState state;
    private final Heightmap.Types heightmapType;

    public SurfaceLayerStructure(int priority, int xSize, int zSize, BlockState state, Heightmap.Types heightmapType) {
        super(priority);
        this.xSize = xSize;
        this.zSize = zSize;
        this.state = state;
        this.heightmapType = heightmapType;
    }

    @Override
    public int getMaxSize() {
        return Math.max(xSize, zSize);
    }

    @Override
    public void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx, Node n, RandomSource random, StructureInstance instance) {
        int originX = random.nextIntBetweenInclusive(n.getX(), n.getX() + n.getSize());
        int originZ = random.nextIntBetweenInclusive(n.getZ(), n.getZ() + n.getSize());

        AABB boundingBox = new AABB(
                originX - (double) xSize / 2,
                ctx.level().getMinBuildHeight(),
                originZ - (double) zSize / 2,
                originX + (double) xSize / 2,
                ctx.level().getMaxBuildHeight(),
                originZ + (double) zSize / 2
        );

        boundingBox = getIntersection(ctx, boundingBox);

        SurfaceBlockPlacer placer = new SurfaceBlockPlacer(boundingBox, state, heightmapType);
        instance.addPlacer(placer);
    }
}
