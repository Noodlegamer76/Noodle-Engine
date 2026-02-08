package com.noodlegamer76.engine.core.component.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.core.network.GameObjectSerializers;
import com.noodlegamer76.engine.core.network.SyncedVar;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.gltf.McGltf;
import com.noodlegamer76.engine.gltf.geometry.MeshData;
import com.noodlegamer76.engine.gltf.load.ModelStorage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
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
    public void onRemoved(Level level) {
        if (level.isClientSide) {
            for (RenderableMesh mesh : meshes) {
                GlbRenderer.getBatch().remove(mesh);
            }
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
        if (meshes.isEmpty() && modelLocation.getValue() != null) {
            McGltf model = ModelStorage.getModel(modelLocation.getValue());
            if (model != null) {

                for (MeshData meshData : model.getMeshes()) {
                    RenderableMesh renderableMesh = GlbRenderer.createMesh(meshData, poseStack, packedLight);
                    meshes.add(renderableMesh);
                }
            }
        }

        for (RenderableMesh mesh : meshes) {
            mesh.setModelViewMatrix(poseStack.last().pose());
            GlbRenderer.addMesh(mesh);
        }
    }
}
