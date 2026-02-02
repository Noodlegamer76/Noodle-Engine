package com.noodlegamer76.engine.worldgen.megastructure;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.joml.Vector2f;

public class Node {
    private final int x;
    private final int z;
    private final int level;
    private final int size;

    public Node(int x, int z, int level) {
        Vector2f origin = StructMath.getNodeOrigin(x, z, level);
        this.x = (int) origin.x;
        this.z = (int) origin.y;
        this.level = level;
        size = StructMath.getSizeFromLevel(level);
    }

    public RandomSource getRandom(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return StructMath.getNodeRandom(this, context);
    }

    public int getLevel() {
        return level;
    }

    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
