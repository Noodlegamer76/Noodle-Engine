package com.noodlegamer76.engine.client.renderer.gltf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import org.joml.Matrix4f;

import java.util.Map;

public class GlbRenderer {
    private static final MaterialBatch BATCH = new MaterialBatch();

    public static MaterialBatch getBatch() {
        return BATCH;
    }

    public static void clear() {
        BATCH.clear();
    }

    public static void addBuffer(McMaterial material, RenderableBuffer buffer) {
        BATCH.add(material, buffer);
    }

    public static void addMesh(RenderableMesh mesh) {
        BATCH.add(mesh);
    }

    /*
    make packedLight '-1' to match world light
     */
    public static RenderableMesh createMesh(MeshData meshData, PoseStack poseStack, int packedLight) {
        RenderableMesh renderableMesh = new RenderableMesh(meshData);
        Matrix4f modelViewMatrix = new Matrix4f(poseStack.last().pose());
        for (Map.Entry<McMaterial, GltfVbo> buffers: meshData.getPrimitiveBuffers().entrySet()) {
            RenderableBuffer renderableBuffer = new RenderableBuffer(modelViewMatrix, buffers.getValue(), renderableMesh, packedLight, packedLight == -1);
            renderableMesh.addBuffer(renderableBuffer);
        }

        return renderableMesh;
    }
}
