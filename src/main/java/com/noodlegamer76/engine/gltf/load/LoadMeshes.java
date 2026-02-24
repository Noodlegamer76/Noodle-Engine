package com.noodlegamer76.engine.gltf.load;

import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.McGltfLoader;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.geometry.VBORenderer;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import de.javagl.jgltf.model.MaterialModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.MeshPrimitiveModel;

import java.util.List;
import java.util.Map;

public class LoadMeshes {
    public static void loadMeshes(McGltfLoader gltf) {
        for (MeshModel mesh : gltf.getModel().getMeshModels()) {
            MeshData meshData = new MeshData(gltf.getResult());

            for (MeshPrimitiveModel primitive: mesh.getMeshPrimitiveModels()) {
                MaterialModel material = primitive.getMaterialModel();
                McMaterial mcMat = gltf.getMaterialData().getMaterialModelToMaterial().get(material);
                meshData.addPrimitive(mcMat, primitive);
            }

            gltf.addMesh(meshData, mesh);
        }

        loadPrimitives(gltf);
    }

    public static void loadPrimitives(McGltfLoader gltf) {
        for (MeshData mesh : gltf.getResult().getMeshes()) {
            Map<McMaterial, List<MeshPrimitiveModel>> primitives = mesh.getPrimitives();
            for (McMaterial material: primitives.keySet()) {
                GltfVbo buffer = VBORenderer.render(gltf, material, primitives.get(material));

                mesh.addPrimitiveBuffer(material, buffer);
            };
        }
    }
}
