package com.noodlegamer76.engine.client.renderer.gltf;

import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import com.noodlegamer76.engine.gltf.material.McMaterial;

import javax.annotation.Nullable;
import java.util.*;

public class MaterialBatch {
    private final Map<McMaterial, Map<GltfVbo, List<RenderableBuffer>>> buffers = new LinkedHashMap<>();
    private final List<RenderableMesh> meshes = new ArrayList<>();

    public void render(McMaterial material, RenderableBuffer buffer) {
        if (!buffers.containsKey(material)) {
            buffers.put(material, new HashMap<>());
        }
        if (!buffers.get(material).containsKey(buffer.getVertexBuffer())) {
            buffers.get(material).put(buffer.getVertexBuffer(), new ArrayList<>());
        }
        buffers.get(material).get(buffer.getVertexBuffer()).add(buffer);
    }

    public void remove(RenderableBuffer buffer) {
        McMaterial material = buffer.getVertexBuffer().getMaterial();
        Map<GltfVbo, List<RenderableBuffer>> buffers = this.buffers.get(material);
        if (buffers != null && buffers.containsKey(buffer.getVertexBuffer())) {
            buffers.get(buffer.getVertexBuffer()).remove(buffer);
        }
    }

    public void remove(RenderableMesh mesh) {
        meshes.remove(mesh);
        for (RenderableBuffer buffer : mesh.getBuffers()) {
            remove(buffer);
        }
    }

    @Nullable
    public List<RenderableBuffer> getBuffers(McMaterial material, GltfVbo buffer) {
        return buffers.get(material).get(buffer);
    }

    public List<RenderableMesh> getMeshes() {
        return meshes;
    }

    public void add(RenderableMesh mesh) {
        meshes.add(mesh);
    }

    public void remove(McMaterial material) {
        buffers.remove(material);
    }

    public Map<McMaterial, Map<GltfVbo, List<RenderableBuffer>>> getBuffers() {
        return buffers;
    }

    public boolean isEmpty() {
        return buffers.isEmpty();
    }

    public void clear() {
        buffers.clear();
    }
}
