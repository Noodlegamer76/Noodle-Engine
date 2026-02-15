package com.noodlegamer76.engine.gltf.animation.animation.controller;

import com.noodlegamer76.engine.gltf.animation.animation.AnimationClip;
import com.noodlegamer76.engine.gltf.animation.animation.AnimationSampler;
import com.noodlegamer76.engine.gltf.node.Node;
import org.joml.Matrix4f;

public class AnimationPlayer {
    private AnimationClip clip;
    private float time;
    private float speed = 1.0f;
    private boolean looping = true;

    public AnimationPlayer(AnimationClip clip) {
        this.clip = clip;
    }

    public void update(float delta) {
        time += delta * speed;

        if (looping) {
            time %= clip.getDurationSeconds();
        } else {
            time = Math.min(time, clip.getDurationSeconds());
        }
    }

    public Matrix4f sample(Node node) {
        return AnimationSampler.sample(clip, node, time);
    }

    public void setClip(AnimationClip clip) {
        this.clip = clip;
        this.time = 0f;
    }

    public float getTime() {
        return time;
    }

    public AnimationClip getClip() {
        return clip;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
