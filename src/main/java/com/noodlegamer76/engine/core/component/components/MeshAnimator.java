package com.noodlegamer76.engine.core.component.components;

import com.mojang.blaze3d.vertex.PoseStack;
import com.noodlegamer76.engine.client.renderer.gltf.GlbRenderer;
import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.core.network.GameObjectSerializers;
import com.noodlegamer76.engine.core.network.SyncedVar;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.gltf.animation.animation.AnimationClip;
import com.noodlegamer76.engine.gltf.animation.animation.controller.AnimationState;
import com.noodlegamer76.engine.gltf.animation.animation.controller.Animator;
import com.noodlegamer76.engine.gltf.animation.animation.controller.AnimatorTransition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MeshAnimator extends Component implements RenderableComponent {
    private final SyncedVar<Integer> meshRendererComponent = new SyncedVar<>(this, -1, GameObjectSerializers.INTEGER);
    private Animator animator;
    private List<RenderableMesh> meshes = new ArrayList<>();
    private MeshRenderer previousRenderer;

    public MeshAnimator(GameObject gameObject) {
        super(InitComponents.ANIMATOR, gameObject);
    }

    @Override
    public List<SyncedVar<?>> getSyncedData() {
        return List.of(
                meshRendererComponent
        );
    }

    public void setMeshRenderer(MeshRenderer renderer) {
        meshRendererComponent.setValue(renderer.id, true);
    }

    @Nullable
    public MeshRenderer getMeshRenderer() {
        if (meshRendererComponent.getValue() < 0) {
            return null;
        }
        Component component = gameObject.getComponentManager().getComponents().get(meshRendererComponent.getValue());
        if (component instanceof MeshRenderer meshRenderer) {
            return meshRenderer;
        }
        return null;
    }

    @Override
    public void render(GameObject entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        MeshRenderer renderer = getMeshRenderer();
        if (previousRenderer != renderer) {
            meshes.clear();
            previousRenderer = renderer;
        }
        if (meshes.isEmpty()) {
            if (renderer == null) {
                return;
            }

            meshes.addAll(renderer.getMeshes());

            if (!meshes.isEmpty()) {
                AnimationClip dance = meshes.get(0).getGltf().getAnimations().get("Miku|Miku_Dance");

                AnimationState danceState = new AnimationState("Dance", dance);

                animator = new Animator(meshes.get(0).getGltf().getRootNodes());
                animator.setInitialState(danceState);

            }
        }
        if (!meshes.isEmpty()) {
            for (RenderableMesh mesh: meshes) {
                mesh.setAnimator(animator);
            }
        }
    }
}
