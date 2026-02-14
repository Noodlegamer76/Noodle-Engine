package com.noodlegamer76.engine.client.renderer.gltf;

import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.animation.animation.SingleAnimationPlayer;
import com.noodlegamer76.engine.gltf.animation.skins.LoadSkins;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.NodeModel;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.*;

public class RenderableMesh {
    private final List<RenderableBuffer> buffers = new ArrayList<>();
    private final MeshData meshData;
    private final McGltf gltf;
    private List<Matrix4f> jointMatrices;
    private List<NodeModel> joints = new ArrayList<>();
    private Matrix4f modelMatrix;
    private SingleAnimationPlayer animationPlayer;

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

    public void buildJoints() {
        if (meshData.getSkin() == null) return;

        this.joints = new ArrayList<>(meshData.getSkin().getJoints());
        LoadSkins.getSkinGlobal(this);
    }

    public void update(float partialTick) {
        if (animationPlayer != null) {
            animationPlayer.update(partialTick / 20 / 2);
        }
        buildJoints();
    }

    public void setAnimationPlayer(SingleAnimationPlayer animationPlayer) {
        this.animationPlayer = animationPlayer;
    }

    public McGltf getGltf() {
        return gltf;
    }

    public void setJointMatrices(List<Matrix4f> jointMatrices) {
        this.jointMatrices = jointMatrices;
    }

    @Nullable
    public List<Matrix4f> getJointMatrices() {
        return jointMatrices;
    }

    public List<NodeModel> getJoints() {
        return joints;
    }

    public void setModelMatrix(Matrix4f modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }

    public SingleAnimationPlayer getAnimationPlayer() {
        return animationPlayer;
    }
}
