package com.noodlegamer76.engine.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.core.component.components.ModelRenderer;
import com.noodlegamer76.engine.physics.PhysicsEngine;
import com.noodlegamer76.engine.physics.PhysicsLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = NoodleEngine.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderLevelStage {
    private static BakedModel bakedModel;
    private static boolean initialized = false;

    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event) {
        if (!initialized) {
            BlockState blockState = Blocks.BAMBOO_BLOCK.defaultBlockState();
            ModelResourceLocation block = BlockModelShaper.stateToModelLocation(blockState);
            bakedModel = Minecraft.getInstance().getModelManager().getModel(block);
            initialized = true;
        }

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            Minecraft mc = Minecraft.getInstance();
            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
            PoseStack poseStack = event.getPoseStack();
            int packedLight = LightTexture.pack(15, 15);

            if (Minecraft.getInstance().level == null || bakedModel == null) {
                return;
            }

            poseStack.pushPose();
            poseStack.translate(-0.5, 0, -0.5);

            PoseStack.Pose pose = poseStack.last();
            PhysicsEngine physicsEngine = PhysicsEngine.getInstance();


            poseStack.popPose();
        }
    }

    public static void renderBlock(Vector3f pos) {}
}
