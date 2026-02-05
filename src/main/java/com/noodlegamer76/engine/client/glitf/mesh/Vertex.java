package com.noodlegamer76.engine.client.glitf.mesh;

import org.joml.Vector2f;

import java.util.Map;

public record Vertex(
        float x, float y, float z,
        float nx, float ny, float nz,
        Map<Integer, Vector2f> UVs,
        float[] jointIndices, float[] jointWeights
) {

}
