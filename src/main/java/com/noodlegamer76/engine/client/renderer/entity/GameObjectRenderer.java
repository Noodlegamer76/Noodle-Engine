package com.noodlegamer76.engine.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.components.RenderableComponent;
import com.noodlegamer76.engine.entity.GameObject;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GameObjectRenderer extends EntityRenderer<GameObject> {
    public static final ResourceLocation DIRT = ResourceLocation.withDefaultNamespace("textures/block/dirt.png");

    public GameObjectRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(GameObject entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        for (Component component: entity.getComponentManager().getComponents().values()) {
            if (component instanceof RenderableComponent renderable) {
                renderable.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
            }
        }

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(GameObject pEntity) {
        return DIRT;
    }
}
