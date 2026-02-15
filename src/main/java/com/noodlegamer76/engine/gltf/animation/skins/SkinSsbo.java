package com.noodlegamer76.engine.gltf.animation.skins;

import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableBuffer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL43;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinSsbo {
    private final int skinMatricesId;
    private final FloatBuffer skinMatrices;
    private final int startIndicesId;
    private final IntBuffer startIndices;
    private final int MAX_MATRICES = 131072;

    public SkinSsbo() {
        skinMatrices = BufferUtils.createFloatBuffer(MAX_MATRICES * 16);
        startIndices = BufferUtils.createIntBuffer(MAX_MATRICES);

        skinMatricesId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, skinMatricesId);
        GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, (long) MAX_MATRICES * 16 * Float.BYTES, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);

        startIndicesId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, startIndicesId);
        GL15.glBufferData(GL43.GL_SHADER_STORAGE_BUFFER, (long) MAX_MATRICES * Integer.BYTES, GL15.GL_DYNAMIC_DRAW);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    public void build() {
        skinMatrices.clear();
        startIndices.clear();

        List<RenderableMesh> meshes = GlbRenderer.getBatch().getMeshes();
        Map<RenderableMesh, Integer> meshStarts = new HashMap<>();

        int matrixOffset = 0;

        for (RenderableMesh mesh : meshes) {
            List<Matrix4f> joints = mesh.getJointMatrices();
            if (joints == null || joints.isEmpty()) {
                meshStarts.put(mesh, 0);
                continue;
            }

            meshStarts.put(mesh, matrixOffset);

            for (Matrix4f m : joints) {
                if (matrixOffset >= MAX_MATRICES) break;

                float[] arr = new float[16];
                m.get(arr);
                skinMatrices.put(arr);
                matrixOffset++;
            }
        }

        for (Map.Entry<McMaterial, Map<GltfVbo, List<RenderableBuffer>>> entry : GlbRenderer.getBatch().getBuffers().entrySet()) {
            for (Map.Entry<GltfVbo, List<RenderableBuffer>> buffersEntry : entry.getValue().entrySet()) {
                for (RenderableBuffer buffer : buffersEntry.getValue()) {
                    startIndices.put(meshStarts.get(buffer.getMesh()));
                }
            }
        }

        upload();
    }

    private void upload() {
        skinMatrices.flip();

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, skinMatricesId);
        GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, skinMatrices);

        startIndices.flip();

        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, startIndicesId);
        GL15.glBufferSubData(GL43.GL_SHADER_STORAGE_BUFFER, 0, startIndices);
        GL15.glBindBuffer(GL43.GL_SHADER_STORAGE_BUFFER, 0);
    }

    public void destroy() {
        GL15.glDeleteBuffers(skinMatricesId);
        GL15.glDeleteBuffers(startIndicesId);
    }


    public FloatBuffer getSkinMatrices() {
        return skinMatrices;
    }

    public int getMaxMatrices() {
        return MAX_MATRICES;
    }

    public int getSkinMatricesId() {
        return skinMatricesId;
    }

    public int getStartIndicesId() {
        return startIndicesId;
    }

    public IntBuffer getStartIndices() {
        return startIndices;
    }
}
