package com.noodlegamer76.engine.core.component.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.client.glitf.rendering.RenderableModel;
import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.core.network.GameObjectSerializers;
import com.noodlegamer76.engine.core.network.SyncedVar;
import com.noodlegamer76.engine.entity.GameObject;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Vector3f;

import java.util.List;

public class MeshRenderer extends Component implements RenderableComponent {
    private final SyncedVar<ResourceLocation> modelLocation = new SyncedVar<>(this, ResourceLocation.fromNamespaceAndPath("", ""), GameObjectSerializers.RESOURCE_LOCATION);
    private RenderableModel model;

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

    public RenderableModel getModel() {
        return model;
    }

    public ResourceLocation getModelLocation() {
        return modelLocation.getValue();
    }

    public void setModelLocation(ResourceLocation modelLocation) {
        this.modelLocation.setValue(modelLocation, true);
    }

    @Override
    public void render(GameObject entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (model == null || modelLocation.getValue() == null) {
            model = new RenderableModel(modelLocation.getValue());
        }

        model.setActiveAnimation(0);
        poseStack.pushPose();

        poseStack.scale(0.075f / 5, 0.075f / 5, 0.075f / 5);

        model.renderSingleModel(poseStack, partialTick, packedLight);
        poseStack.popPose();
    }
}
