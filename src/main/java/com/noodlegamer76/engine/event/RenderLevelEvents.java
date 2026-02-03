package com.noodlegamer76.engine.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.math.Axis;
import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.client.renderer.gltf.*;
import com.noodlegamer76.engine.gltf.animation.skins.SkinSsbo;
import com.noodlegamer76.engine.gltf.geometry.GltfVbo;
import com.noodlegamer76.engine.gltf.material.McMaterial;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = NoodleEngine.MODID)
public class RenderLevelEvents {

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) return;

        MatrixSsbo matrixManager = GlbRenderer.getMatrixSsbo();
        LightUvSsbo lightUvManager = GlbRenderer.getLightUvSsbo();
        SkinSsbo skinsManager = GlbRenderer.getSkinSsbo();

        MaterialBatch batch = GlbRenderer.getBatch();

        for (RenderableMesh mesh: RenderableMeshes.getMeshes()) {
            mesh.update(event.getPartialTick());
        }

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        poseStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        //TODO: make a wrapper for ssbos instead of reusing the same code
        GlbRenderer.getMatrixSsbo().upload(poseStack);
        GlbRenderer.getLightUvSsbo().upload();
        skinsManager.build();

        GL43.glMemoryBarrier(GL43.GL_SHADER_STORAGE_BARRIER_BIT);

        for (Map.Entry<McMaterial, Map<GltfVbo, List<RenderableBuffer>>> batchEntry : batch.getBuffers().entrySet()) {
            McMaterial material = batchEntry.getKey();
            Map<GltfVbo, List<RenderableBuffer>> vbos = batchEntry.getValue();


            material.bind();

            GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 0, matrixManager.getSsboId());
            GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 1, lightUvManager.getSsboId());
            GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 2, skinsManager.getSkinMatricesId());
            GL30.glBindBufferBase(GL43.GL_SHADER_STORAGE_BUFFER, 3, skinsManager.getStartIndicesId());

            for (Map.Entry<GltfVbo, List<RenderableBuffer>> vboEntry : vbos.entrySet()) {

                GltfVbo vbo = vboEntry.getKey();
                vbo.bind();

                vbo.instanceDraw(vboEntry.getValue().size());
            }
        }

        VertexBuffer.unbind();
        poseStack.popPose();
    }
}
