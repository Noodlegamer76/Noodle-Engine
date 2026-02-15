package com.noodlegamer76.engine.gltf.animation.animation.controller;

import org.joml.Matrix4f;
import com.noodlegamer76.engine.gltf.node.Node;

public class WeightedAnimation {
    public AnimationPlayer player;
    public float weight;

    public WeightedAnimation(AnimationPlayer player, float weight) {
        this.player = player;
        this.weight = weight;
    }

    public Matrix4f sample(Node node) {
        return player.sample(node);
    }
}
