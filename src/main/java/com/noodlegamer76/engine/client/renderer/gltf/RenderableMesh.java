package com.noodlegamer76.engine.client.renderer.gltf;

import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import org.joml.Matrix4f;

import java.util.*;

public class RenderableMesh {
    private final List<RenderableBuffer> buffers = new ArrayList<>();
    private final MeshData meshData;
    private final McGltf gltf;
    private Matrix4f modelViewMatrix;

    public RenderableMesh(MeshData meshData) {
        this.meshData = meshData;
        this.gltf = meshData.getGltf();

    }

    public List<RenderableBuffer> getBuffers() {
        return buffers;
    }

    public MeshData getMeshData() {
        return meshData;
    }

    public void addBuffer(RenderableBuffer buffer) {
        buffers.add(buffer);
    }

    public McGltf getGltf() {
        return gltf;
    }

    public void setModelViewMatrix(Matrix4f modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
        for (RenderableBuffer buffer : buffers) {
            buffer.setModelViewMatrix(modelViewMatrix);
        }
    }

    public Matrix4f getModelViewMatrix() {
        return modelViewMatrix;
    }
}
