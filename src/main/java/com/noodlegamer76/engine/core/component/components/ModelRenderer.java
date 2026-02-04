package com.noodlegamer76.engine.core.component.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.core.network.GameObjectSerializer;
import com.noodlegamer76.engine.core.network.GameObjectSerializers;
import com.noodlegamer76.engine.core.network.SyncedVar;
import com.noodlegamer76.engine.entity.GameObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class ModelRenderer extends Component implements RenderableComponent {
    private final SyncedVar<ModelResourceLocation> modelLocation = new SyncedVar<>(this, new ModelResourceLocation("", "", ""), GameObjectSerializers.MODEL_RESOURCE_LOCATION);
    private BakedModel bakedModel;

    public ModelRenderer(GameObject gameObject) {
        super(InitComponents.MODEL_RENDERER, gameObject);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putString("model", modelLocation.getValue().toString());
    }

    @Override
    public void loadAdditional(CompoundTag tag) {
        modelLocation.setValue(GameObjectSerializers.parseModelResourceLocation(tag.getString("model")), true);
    }

    @Override
    public List<SyncedVar<?>> getSyncedData() {
        return List.of(
                modelLocation
        );
    }

    @Override
    public void onUpdated(@NotNull Level level) {
        if (level.isClientSide) {
            bakedModel = Minecraft.getInstance().getModelManager().getModel(modelLocation.getValue());
        }
    }

    public void setModel(ModelResourceLocation model) {
        modelLocation.setValue(model, true);
    }

    public ModelResourceLocation getModel() {
        return modelLocation.getValue();
    }

    @Override
    public void render(GameObject entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        if (Minecraft.getInstance().level == null || bakedModel == null) {
            return;
        }

        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(
                poseStack.last(),
                bufferSource.getBuffer(RenderType.cutout()),
                null,
                bakedModel,
                1f, 1f, 1f,
                packedLight,
                0,
                ModelData.EMPTY,
                null
        );
    }
}
