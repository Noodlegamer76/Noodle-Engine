package com.noodlegamer76.engine.gltf.animation.animation.controller;

import java.util.function.Predicate;

public class AnimatorTransition {
    private final AnimationState target;
    private final float blendTime;
    private final Predicate<AnimatorParameters> condition;

    public AnimatorTransition(AnimationState target, float blendTime, Predicate<AnimatorParameters> condition) {
        this.target = target;
        this.blendTime = blendTime;
        this.condition = condition;
    }

    public boolean shouldTransition(AnimatorParameters params) {
        return condition.test(params);
    }

    public AnimationState getTarget() {
        return target;
    }

    public float getBlendTime() {
        return blendTime;
    }
}
