package com.noodlegamer76.engine.gltf;

import com.noodlegamer76.engine.gltf.animation.animation.AnimationClip;
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
import org.joml.Matrix4f;

import java.nio.Buffer;
import java.util.*;

public class McGltf {
    private final ResourceLocation location;
    private final List<MeshData> meshes = new ArrayList<>();
    private final Map<McSkin, Map<Node, Matrix4f>> inverseBindMatrices = new HashMap<>();
    private final Map<String, AnimationClip> animations = new HashMap<>();
    private final List<Node> rootNodes = new ArrayList<>();

    public McGltf(ResourceLocation location) {
        this.location = location;
    }

    public void close() {
        meshes.forEach(MeshData::close);
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public List<MeshData> getMeshes() {
        return meshes;
    }

    public Map<String, AnimationClip> getAnimations() {
        return animations;
    }

    public Map<McSkin, Map<Node, Matrix4f>> getInverseBindMatrices() {
        return inverseBindMatrices;
    }

    public List<Node> getRootNodes() {
        return rootNodes;
    }
}
