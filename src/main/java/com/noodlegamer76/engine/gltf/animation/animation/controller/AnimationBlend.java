package com.noodlegamer76.engine.gltf.animation.animation.controller;

import com.noodlegamer76.engine.gltf.animation.animation.InterpolationMatrices;
import com.noodlegamer76.engine.gltf.node.Node;
import org.joml.Matrix4f;

public class AnimationBlend {
    private AnimationPlayer from;
    private AnimationPlayer to;

    private float blendDuration;
    private float blendTime;

    public AnimationBlend(AnimationPlayer from, AnimationPlayer to, float duration) {
        this.from = from;
        this.to = to;
        this.blendDuration = duration;
        this.blendTime = 0f;
    }

    public void update(float delta) {
        blendTime += delta;
        from.update(delta);
        to.update(delta);
    }

    public Matrix4f sample(Node node) {
        float t = Math.min(blendTime / blendDuration, 1f);

        Matrix4f a = from.sample(node);
        Matrix4f b = to.sample(node);

        return InterpolationMatrices.interpolateMatrices(a, b, t);
    }

    public boolean isFinished() {
        return blendTime >= blendDuration;
    }

    public AnimationPlayer from() {
        return from;
    }

    public AnimationPlayer to() {
        return to;
    }
}
