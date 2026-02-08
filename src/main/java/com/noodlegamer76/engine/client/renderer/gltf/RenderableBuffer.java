package com.noodlegamer76.engine.client.renderer.gltf;

import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import org.joml.Matrix4f;

public class RenderableBuffer {
    private Matrix4f modelViewMatrix;
    private int packedLight;
    private final GltfVbo vertexBuffer;
    private boolean useLevelLight;
    private final RenderableMesh mesh;

    public RenderableBuffer(Matrix4f modelViewMatrix, GltfVbo vertexBuffer, RenderableMesh mesh, int packedLight, boolean useLevelLight) {
        this.modelViewMatrix = modelViewMatrix;
        this.vertexBuffer = vertexBuffer;
        this.packedLight = packedLight;
        this.useLevelLight = useLevelLight;
        this.mesh = mesh;
    }

    public GltfVbo getVertexBuffer() {
        return vertexBuffer;
    }

    public int getPackedLight() {
        return packedLight;
    }

    public void setPackedLight(int packedLight) {
        this.packedLight = packedLight;
    }

    public boolean isUseLevelLight() {
        return useLevelLight;
    }

    public void setUseLevelLight(boolean useLevelLight) {
        this.useLevelLight = useLevelLight;
    }

    public RenderableMesh getMesh() {
        return mesh;
    }

    public Matrix4f getModelViewMatrix() {
        return modelViewMatrix;
    }

    public void setModelViewMatrix(Matrix4f modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
    }
}
