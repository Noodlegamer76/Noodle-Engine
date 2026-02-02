package com.noodlegamer76.engine.worldgen.megastructure.structure;

import com.noodlegamer76.engine.worldgen.megastructure.Node;
import com.noodlegamer76.engine.worldgen.megastructure.StructMath;
import com.noodlegamer76.engine.worldgen.megastructure.structure.structures.StructureInstance;
import com.noodlegamer76.engine.worldgen.megastructure.rule.IStructureRule;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class Structure {
    private final List<IStructureRule> rules = new ArrayList<>();
    private final int priority;

    protected Structure(int priority) {
        this.priority = priority;
    }

    public abstract int getMaxSize();

    public AABB getIntersection(FeaturePlaceContext<NoneFeatureConfiguration> ctx, AABB structure) {
        BlockPos origin = ctx.origin();
        AABB chunk = new AABB(origin, new BlockPos(origin.getX() + 16, origin.getY() + ctx.level().getMaxBuildHeight(), origin.getZ() + 16));

        return structure.intersect(chunk);
    }

    public boolean isIntersecting(FeaturePlaceContext<NoneFeatureConfiguration> ctx, AABB structure) {
        return getIntersection(ctx, structure).getSize() > 0;
    }

    public AABB getIntersection(FeaturePlaceContext<NoneFeatureConfiguration> ctx, Vector3f center) {
        BlockPos origin = ctx.origin();
        AABB chunk = new AABB(origin, new BlockPos(origin.getX() + 16, origin.getY() + ctx.level().getMaxBuildHeight(), origin.getZ() + 16));
        int size = getMaxSize() / 2;
        AABB structure = new AABB(
                center.x - size,
                center.y - size,
                center.z - size,
                center.x + size,
                center.y + size,
                center.z + size
        );

        return structure.intersect(chunk);
    }

    public abstract void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx, Node n, RandomSource random, StructureInstance instance);

    public boolean shouldGenerate(FeaturePlaceContext<NoneFeatureConfiguration> ctx, RandomSource random) {
        for (IStructureRule rule: rules) {
            if (!rule.shouldGenerate(ctx, random, this)) {
                return false;
            }
        }
        return true;
    }

    public int getNodeLevel() {
        int size = getMaxSize();
        return StructMath.getQuadTreeLevel(size);
    }

    public int getPriority() {
        return priority;
    }

    public List<IStructureRule> getRules() {
        return rules;
    }

    public void addRule(IStructureRule rule) {
        rules.add(rule);
    }
}
