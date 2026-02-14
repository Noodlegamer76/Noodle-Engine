package com.noodlegamer76.engine.gltf.geometry;

import com.mojang.blaze3d.vertex.*;
import com.noodlegamer76.engine.client.ModVertexFormats;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableBuffer;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.material.MaterialProperty;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import com.noodlegamer76.engine.mixin.BufferBuilderMixin;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;
import org.lwjgl.system.MemoryUtil;

import java.util.*;

public class VBORenderer {

    public static GltfVbo render(McGltf gltf, McMaterial material, List<MeshPrimitiveModel> primitives) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.TRIANGLES, ModVertexFormats.GLB_PBR);

        GltfVbo vbo = new GltfVbo(VertexBuffer.Usage.STATIC, material);

        renderPrimitive(gltf, bufferBuilder, primitives, material, vbo);

        BufferBuilder.RenderedBuffer renderedBuffer = bufferBuilder.end();

        vbo.bind();
        vbo.upload(renderedBuffer);
        VertexBuffer.unbind();

        return vbo;
    }

    private static void renderPrimitive(McGltf gltf, BufferBuilder bb, List<MeshPrimitiveModel> primitives, McMaterial material, GltfVbo vbo) {
        Map<MeshPrimitiveModel, int[]> primitiveJointsMap = new HashMap<>();
        Map<MeshPrimitiveModel, float[]> primitiveWeightsMap = new HashMap<>();

        for (MeshPrimitiveModel primitive : primitives) {
            int[] joints = GltfAccessorUtils.getJointIndexArray(primitive.getAttributes().get("JOINTS_0"));
            float[] weights = GltfAccessorUtils.getFloatArray(primitive.getAttributes().get("WEIGHTS_0"));

            primitiveJointsMap.put(primitive, joints);
            primitiveWeightsMap.put(primitive, weights);
        }

        for (MeshPrimitiveModel primitive : primitives) {
            int[] joints = primitiveJointsMap.get(primitive);
            float[] weights = primitiveWeightsMap.get(primitive);

            int[] indices = GltfAccessorUtils.getIndexArray(primitive.getIndices());
            AccessorModel posAcc = primitive.getAttributes().get("POSITION");
            AccessorModel normAcc = primitive.getAttributes().get("NORMAL");
            float[] positions = GltfAccessorUtils.getFloatArray(posAcc);
            float[] normals = GltfAccessorUtils.getFloatArray(normAcc);

            Map<Integer, float[]> uvLayers = new HashMap<>();
            primitive.getAttributes().forEach((name, accessor) -> {
                if (name.toUpperCase().startsWith("TEXCOORD")) {
                    int layer = name.replaceAll("[^0-9]", "").isEmpty() ? 0 : Integer.parseInt(name.replaceAll("[^0-9]", ""));
                    float[] uv = GltfAccessorUtils.getFloatArray(accessor);
                    if (uv != null) uvLayers.put(layer, uv);
                }
            });

            int vertexCount = positions.length / 3;

            if (indices != null) {
                for (int idx : indices) {
                    renderVertex(idx, positions, normals, uvLayers, joints, weights, bb, material);
                }
            } else {
                for (int i = 0; i < vertexCount; i++) {
                    renderVertex(i, positions, normals, uvLayers, joints, weights, bb, material);
                }
            }
            vbo.setVertexCount(indices != null ? indices.length : vertexCount);
        }
    }

    private static void renderVertex(int i, float[] pos, float[] norm, Map<Integer, float[]> uvs,
                                     int[] rawJoints, float[] rawWeights,
                                     BufferBuilder bb, McMaterial mat) {
        float x = pos[i * 3], y = pos[i * 3 + 1], z = pos[i * 3 + 2];
        float nx = (norm != null) ? norm[i * 3] : 0f;
        float ny = (norm != null) ? norm[i * 3 + 1] : 0f;
        float nz = (norm != null) ? norm[i * 3 + 2] : 1f;

        Map<Integer, Vector2f> vertexUVs = new HashMap<>();
        uvs.forEach((layer, arr) -> {
            if (i * 2 + 1 < arr.length) vertexUVs.put(layer, new Vector2f(arr[i * 2], arr[i * 2 + 1]));
        });

        int[] joints = new int[4];
        float[] weights = new float[4];

        if (rawJoints != null && rawJoints.length > i * 4 + 3) {
            for (int j = 0; j < 4; j++) {
                joints[j] = rawJoints[i * 4 + j];
            }
        }

        if (rawWeights != null && rawWeights.length > i * 4 + 3) {
            float sum = 0;
            for (int j = 0; j < 4; j++) {
                weights[j] = rawWeights[i * 4 + j];
                sum += weights[j];
            }
            if (sum > 0f) {
                for (int j = 0; j < 4; j++) weights[j] /= sum;
            } else {
                weights[0] = 1.0f;
            }
        } else {
            weights[0] = 1.0f;
        }

        render(bb, x, y, z, nx, ny, nz, vertexUVs, joints, weights, mat);
    }

    private static void render(BufferBuilder bb, float x, float y, float z, float nx, float ny, float nz, Map<Integer, Vector2f> vertexUVs, int[] joints, float[] weights, McMaterial mat) {
        Vector2f albedoUv = getUv(vertexUVs, mat, MaterialProperty.ALBEDO_MAP);
        Vector2f normalUv = getUv(vertexUVs, mat, MaterialProperty.NORMAL_MAP);
        Vector2f metallicUv = getUv(vertexUVs, mat, MaterialProperty.METALLIC_MAP);
        Vector2f roughnessUv = getUv(vertexUVs, mat, MaterialProperty.ROUGHNESS_MAP);
        Vector2f aoUv = getUv(vertexUVs, mat, MaterialProperty.AO_MAP);
        Vector2f emissiveUv = getUv(vertexUVs, mat, MaterialProperty.EMISSIVE_MAP);

        // Position
        bb.vertex(x, y, z);

        // Color
        bb.color(255, 255, 255, 255);

        // UV0
        bb.uv(albedoUv.x, albedoUv.y);

        // Normal
        bb.normal(nx, ny, nz);

        bb.putFloat(0, normalUv.x);
        bb.putFloat(4, normalUv.y);
        bb.nextElement();

        bb.putFloat(0, metallicUv.x);
        bb.putFloat(4, metallicUv.y);
        bb.nextElement();

        bb.putFloat(0, roughnessUv.x);
        bb.putFloat(4, roughnessUv.y);
        bb.nextElement();

        bb.putFloat(0, aoUv.x);
        bb.putFloat(4, aoUv.y);
        bb.nextElement();

        bb.putFloat(0, emissiveUv.x);
        bb.putFloat(4, emissiveUv.y);
        bb.nextElement();

        float j0 = joints.length > 0 ? joints[0] : 0;
        float j1 = joints.length > 1 ? joints[1] : 0;
        float j2 = joints.length > 2 ? joints[2] : 0;
        float j3 = joints.length > 3 ? joints[3] : 0;

        float w0 = weights.length > 0 ? weights[0] : 1f;
        float w1 = weights.length > 1 ? weights[1] : 0;
        float w2 = weights.length > 2 ? weights[2] : 0;
        float w3 = weights.length > 3 ? weights[3] : 0;

        float sum = w0 + w1 + w2 + w3;
        if (sum > 0f) {
            w0 /= sum;
            w1 /= sum;
            w2 /= sum;
            w3 /= sum;
        }

        bb.putFloat(0, j0);
        bb.putFloat(4, j1);
        bb.putFloat(8, j2);
        bb.putFloat(12, j3);
        bb.nextElement();

        bb.putFloat(0, w0);
        bb.putFloat(4, w1);
        bb.putFloat(8, w2);
        bb.putFloat(12, w3);
        bb.nextElement();

        bb.endVertex();
    }

    private static Vector2f getUv(Map<Integer, Vector2f> uvs, McMaterial mat, MaterialProperty<?> prop) {
        if (!mat.hasTexCoord((MaterialProperty<ResourceLocation>) prop)) return new Vector2f(0.5f, 0.5f);
        int idx = mat.getTexCoord((MaterialProperty<ResourceLocation>) prop);
        return uvs.getOrDefault(idx, uvs.getOrDefault(0, new Vector2f(0.5f, 0.5f)));
    }

}
