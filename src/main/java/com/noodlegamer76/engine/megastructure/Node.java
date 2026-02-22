package com.noodlegamer76.engine.megastructure;

import com.noodlegamer76.engine.megastructure.structure.StructureUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Objects;

public class Node {
    private final int x;
    private final int z;
    private final int level;
    private final int size;

    public Node(int worldX, int worldZ, int level) {
        Vector2f nodeOrigin = StructMath.getNodeOrigin(worldX, worldZ, level);
        this.x = (int) nodeOrigin.x;
        this.z = (int) nodeOrigin.y;
        this.level = level;
        this.size = StructMath.getSizeFromLevel(level);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node node)) return false;
        return x == node.x && z == node.z && level == node.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z, level);
    }

}
