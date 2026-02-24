package com.noodlegamer76.engine.gltf;

import com.noodlegamer76.engine.gltf.animation.skins.LoadSkins;
import com.noodlegamer76.engine.gltf.animation.skins.McSkin;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.load.*;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.AccessorModel;
import de.javagl.jgltf.model.MeshModel;
import de.javagl.jgltf.model.NodeModel;
import de.javagl.jgltf.model.SkinModel;
import de.javagl.jgltf.model.impl.DefaultGltfModel;
import net.minecraft.resources.ResourceLocation;

import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

public class McGltfLoader {
    private final McGltf result;
    private final ResourceLocation location;
    private final DefaultGltfModel model;
    private final Map<AccessorModel, Buffer> accessorBuffers = new HashMap<>();
    private final ImageData imageData = new ImageData();
    private final MaterialData materialData = new MaterialData();
    private final Map<NodeModel, Node> nodeModelToNode = new HashMap<>();
    private final Map<MeshModel, MeshData> meshModelToMeshData = new HashMap<>();
    private final Map<SkinModel, McSkin> skinModelToSkin = new HashMap<>();


    public McGltfLoader(DefaultGltfModel model, ResourceLocation location) {
        this.model = model;
        this.location = location;
        this.result = new McGltf(location);
    }

    public void setup() {
        LoadBuffers.loadBuffers(this);
        LoadImages.loadImages(this);
        LoadTextures.loadTextures(this);
        LoadMaterials.loadMaterials(this);
        LoadMeshes.loadMeshes(this);
        LoadNodes.loadNodes(this);
        LoadSkins.loadSkins(this);
        LoadAnimations.loadAnimations(this);
    }

    public DefaultGltfModel getModel() {
        return model;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public Map<AccessorModel, Buffer> getAccessorBuffers() {
        return accessorBuffers;
    }

    public void addAccessorBuffer(AccessorModel accessorModel, Buffer buffer) {
        accessorBuffers.put(accessorModel, buffer);
    }

    public McGltf getResult() {
        return result;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public MaterialData getMaterialData() {
        return materialData;
    }

    public Map<NodeModel, Node> getNodeModelToNode() {
        return nodeModelToNode;
    }

    public void addNodeModelToNode(NodeModel nodeModel, Node node) {
        nodeModelToNode.put(nodeModel, node);
    }

    public void addMesh(MeshData mesh, MeshModel meshModel) {
        getResult().getMeshes().add(mesh);
        meshModelToMeshData.put(meshModel, mesh);
    }

    public Map<MeshModel, MeshData> getMeshModelToMeshData() {
        return meshModelToMeshData;
    }

    public Map<SkinModel, McSkin> getSkinModelToSkin() {
        return skinModelToSkin;
    }

    public void addSkin(SkinModel skin, McSkin mcSkin) {
        skinModelToSkin.put(skin, mcSkin);
    }
}
