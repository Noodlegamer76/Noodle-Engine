package com.noodlegamer76.engine.gltf.animation.animation.controller;

import com.noodlegamer76.engine.gltf.animation.animation.AnimationClip;

public class AnimationState {
    private final String name;
    private final AnimationClip clip;

    public AnimationState(String name, AnimationClip clip) {
        this.name = name;
        this.clip = clip;
    }

    public String getName() {
        return name;
    }

    public AnimationClip getClip() {
        return clip;
    }
}
