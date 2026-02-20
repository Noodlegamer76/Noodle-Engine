package com.noodlegamer76.engine.worldgen.megastructure.structure;

import com.google.common.collect.Lists;
import com.ibm.icu.impl.Pair;
import com.noodlegamer76.engine.mixin.accessor.StructureTemplateAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StructureUtils {

    /**
     * Places a structure template into a single chunk slice,
     * using the structure's origin as reference and only placing blocks inside the chunk bounds.
     *
     * @param level Minecraft world
     * @param template The structure template
     * @param structureOrigin The origin (corner) of the structure in world coordinates
     * @param chunkMin Minimum corner of the chunk
     * @param chunkMax Maximum corner of the chunk
     * @param settings StructurePlaceSettings (rotation, mirror, etc.)
     */
    public static void placeTemplateInChunk(ServerLevelAccessor level,
                                            StructureTemplate template,
                                            BlockPos structureOrigin,
                                            BlockPos chunkMin,
                                            BlockPos chunkMax,
                                            StructurePlaceSettings settings,
                                            RandomSource random,
                                            int flags) {
        List<StructureTemplate.Palette> palettes = ((StructureTemplateAccessor) template).noodleEngine$getPalettes();
        if (palettes.isEmpty()) return;

        List<StructureTemplate.StructureBlockInfo> blocks = settings.getRandomPalette(palettes, structureOrigin).blocks();
        if (blocks.isEmpty() && settings.isIgnoreEntities()) return;

        BoundingBox boundingBox = settings.getBoundingBox();
        List<BlockPos> liquidsToPlaceLater = Lists.newArrayList();
        List<BlockPos> liquidSources = Lists.newArrayList();
        List<Pair<BlockPos, CompoundTag>> blockEntityInfos = Lists.newArrayList();

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

        for (StructureTemplate.StructureBlockInfo info : StructureTemplate.processBlockInfos(level, structureOrigin, BlockPos.ZERO, settings, blocks, template)) {
            BlockPos worldPos = info.pos();
            if (worldPos.getX() < chunkMin.getX() || worldPos.getX() > chunkMax.getX()) continue;
            if (worldPos.getY() < level.getMinBuildHeight() || worldPos.getY() > level.getMaxBuildHeight()) continue;
            if (worldPos.getZ() < chunkMin.getZ() || worldPos.getZ() > chunkMax.getZ()) continue;

            if (boundingBox != null && !boundingBox.isInside(worldPos)) continue;

            FluidState fluidState = settings.shouldKeepLiquids() ? level.getFluidState(worldPos) : null;
            BlockState state = info.state().mirror(settings.getMirror()).rotate(settings.getRotation());

            if (info.nbt() != null) {
                BlockEntity be = level.getBlockEntity(worldPos);
                Clearable.tryClear(be);
                level.setBlock(worldPos, Blocks.BARRIER.defaultBlockState(), 20);
            }

            if (level.setBlock(worldPos, state, flags)) {
                minX = Math.min(minX, worldPos.getX());
                minY = Math.min(minY, worldPos.getY());
                minZ = Math.min(minZ, worldPos.getZ());
                maxX = Math.max(maxX, worldPos.getX());
                maxY = Math.max(maxY, worldPos.getY());
                maxZ = Math.max(maxZ, worldPos.getZ());

                if (info.nbt() != null) {
                    blockEntityInfos.add(Pair.of(worldPos, info.nbt()));
                    BlockEntity be = level.getBlockEntity(worldPos);
                    if (be instanceof RandomizableContainerBlockEntity) {
                        info.nbt().putLong("LootTableSeed", random.nextLong());
                    }
                    if (be != null) be.load(info.nbt());
                }

                if (fluidState != null) {
                    if (state.getFluidState().isSource()) {
                        liquidSources.add(worldPos);
                    } else if (state.getBlock() instanceof LiquidBlockContainer) {
                        ((LiquidBlockContainer) state.getBlock()).placeLiquid(level, worldPos, state, fluidState);
                        if (!fluidState.isSource()) liquidsToPlaceLater.add(worldPos);
                    }
                }
            }
        }

        boolean placedLiquids;
        Direction[] directions = new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

        do {
            placedLiquids = false;
            Iterator<BlockPos> iterator = liquidsToPlaceLater.iterator();
            while (iterator.hasNext()) {
                BlockPos pos = iterator.next();
                FluidState current = level.getFluidState(pos);

                for (Direction dir : directions) {
                    BlockPos neighbor = pos.relative(dir);
                    FluidState neighborFluid = level.getFluidState(neighbor);
                    if (neighborFluid.isSource() && !liquidSources.contains(neighbor)) current = neighborFluid;
                }

                if (current.isSource()) {
                    BlockState state = level.getBlockState(pos);
                    Block block = state.getBlock();
                    if (block instanceof LiquidBlockContainer) {
                        ((LiquidBlockContainer) block).placeLiquid(level, pos, state, current);
                        placedLiquids = true;
                        iterator.remove();
                    }
                }
            }
        } while (placedLiquids && !liquidsToPlaceLater.isEmpty());

        if (minX <= maxX) {
            if (!settings.getKnownShape()) {
                DiscreteVoxelShape shape = new BitSetDiscreteVoxelShape(maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
                for (Pair<BlockPos, CompoundTag> pair : blockEntityInfos) {
                    BlockPos pos = pair.first;
                    shape.fill(pos.getX() - minX, pos.getY() - minY, pos.getZ() - minZ);
                }
                StructureTemplate.updateShapeAtEdge(level, flags, shape, minX, minY, minZ);
            }

            for (Pair<BlockPos, CompoundTag> pair : blockEntityInfos) {
                BlockPos pos = pair.first;
                if (!settings.getKnownShape()) {
                    BlockState current = level.getBlockState(pos);
                    BlockState updated = Block.updateFromNeighbourShapes(current, level, pos);
                    if (current != updated) level.setBlock(pos, updated, flags & -2 | 16);
                    level.blockUpdated(pos, updated.getBlock());
                }

                if (pair.second != null) {
                    BlockEntity be = level.getBlockEntity(pos);
                    if (be != null) be.setChanged();
                }
            }
        }

        if (!settings.isIgnoreEntities()) {
            ((StructureTemplateAccessor) template).noodleEngine$addEntitiesToWorld(level, structureOrigin, settings);
        }
    }


    public static int getLongestAxis(BlockPos a, BlockPos b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        int dz = Math.abs(a.getZ() - b.getZ());

        return Math.max(dx, Math.max(dy, dz));
    }

    public static AABB getChunkIntersection(FeaturePlaceContext<NoneFeatureConfiguration> ctx, AABB structure) {
        ChunkPos cp = new ChunkPos(ctx.origin());
        int chunkMinX = cp.getMinBlockX();
        int chunkMaxX = cp.getMaxBlockX() + 1;
        int chunkMinZ = cp.getMinBlockZ();
        int chunkMaxZ = cp.getMaxBlockZ() + 1;

        AABB chunk = new AABB(chunkMinX, ctx.level().getMinBuildHeight(), chunkMinZ,
                chunkMaxX, ctx.level().getMaxBuildHeight(), chunkMaxZ);

        AABB intersection = structure.intersect(chunk);

        if (intersection.minX >= intersection.maxX ||
                intersection.minY >= intersection.maxY ||
                intersection.minZ >= intersection.maxZ) {
            return null;
        }
        return intersection;
    }
}
