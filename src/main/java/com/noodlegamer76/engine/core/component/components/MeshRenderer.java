package com.noodlegamer76.engine.core.component.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableBuffer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.core.network.GameObjectSerializers;
import com.noodlegamer76.engine.core.network.SyncedVar;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.animation.animation.AnimationClip;
import com.noodlegamer76.engine.gltf.animation.animation.SingleAnimationPlayer;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.load.ModelStorage;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeshRenderer extends Component implements RenderableComponent {
    private final SyncedVar<ResourceLocation> modelLocation = new SyncedVar<>(this, ResourceLocation.fromNamespaceAndPath("", ""), GameObjectSerializers.RESOURCE_LOCATION);
    private final List<RenderableMesh> meshes = new ArrayList<>();

    public MeshRenderer(GameObject gameObject) {
        super(InitComponents.MESH_RENDERER, gameObject);
    }

    @Override
    public List<SyncedVar<?>> getSyncedData() {
        return List.of(
                modelLocation
        );
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putString("model", modelLocation.getValue().toString());
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        modelLocation.setValue(ResourceLocation.parse(tag.getString("model")), true);
    }

    public List<RenderableMesh> getMeshes() {
        return meshes;
    }

    @Override
    public void onUpdated(Level level) {
        if (level.isClientSide) {
            updateModel();
        }
    }

    @Override
    public void onRemoved(Level level) {
        for (RenderableMesh mesh : meshes) {
            GlbRenderer.remove(mesh);
        }
    }

    public void updateModel() {
        McGltf model = ModelStorage.getModel(modelLocation.getValue());
        if (model == null) {
            return;
        }
        else {
            for (RenderableMesh mesh : meshes) {
                GlbRenderer.remove(mesh);
            }
        }
        for (MeshData meshData : model.getMeshes()) {

            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();

            Vector3f translation = gameObject.getPosition(Minecraft.getInstance().getPartialTick()).toVector3f();
            Quaternionf rotation = gameObject.getRotation();
            Vector3f scale = new Vector3f(gameObject.getScale());
            poseStack.translate(translation.x, translation.y, translation.z);
            poseStack.mulPose(rotation);
            poseStack.scale(scale.x, scale.y, scale.z);

            RenderableMesh mesh = GlbRenderer.addInstance(meshData, poseStack, -1);
            meshes.add(mesh);

            poseStack.popPose();
        }
    }

    public ResourceLocation getModelLocation() {
        return modelLocation.getValue();
    }

    public void setModelLocation(ResourceLocation modelLocation) {
        this.modelLocation.setValue(modelLocation, true);
    }

    @Override
    public void render(GameObject entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        for (RenderableMesh mesh: meshes) {
            if (mesh.getAnimationPlayer() == null) {
                //mesh.setAnimationPlayer(new SingleAnimationPlayer(mesh, (AnimationClip) Arrays.stream(mesh.getGltf().getAnimations().values().toArray()).toArray()[0]));
            }
        }
    }
}
