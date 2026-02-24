package com.noodlegamer76.engine.gltf.animation.animation.controller;

import com.noodlegamer76.engine.client.renderer.gltf.RenderableMesh;
import com.noodlegamer76.engine.gltf.animation.animation.InterpolationMatrices;
import com.noodlegamer76.engine.gltf.node.Node;
import de.javagl.jgltf.model.NodeModel;
import org.joml.Matrix4f;

import java.util.*;

public class Animator {

    private AnimationState currentState;

    private final List<WeightedAnimation> activeAnimations = new ArrayList<>();
    private final Map<AnimationState, List<AnimatorTransition>> transitions = new HashMap<>();
    private final List<AnimatorTransition> anyStateTransitions = new ArrayList<>();
    private final AnimatorParameters parameters = new AnimatorParameters();
    private final List<BlendInfo> activeBlends = new ArrayList<>();
    private final List<Node> rootNodes;

    private final Map<Node, Matrix4f> animatedGlobals = new HashMap<>();

    public Animator(List<Node> rootNodes) {
        this.rootNodes = rootNodes;
    }

    public void setInitialState(AnimationState state) {
        this.currentState = state;
        AnimationPlayer player = new AnimationPlayer(state.getClip());
        activeAnimations.clear();
        activeAnimations.add(new WeightedAnimation(player, 1f));
    }

    public void addTransition(AnimationState from, AnimatorTransition transition) {
        transitions.computeIfAbsent(from, k -> new ArrayList<>())
                .add(transition);
    }

    public void addAnyStateTransition(AnimatorTransition transition) {
        anyStateTransitions.add(transition);
    }

    public void update(float delta) {

        if (activeAnimations.isEmpty())
            return;

        for (WeightedAnimation wa : activeAnimations) {
            wa.player.update(delta);
        }

        for (AnimatorTransition transition : anyStateTransitions) {
            if (transition.shouldTransition(parameters)) {
                startTransition(transition);
                break;
            }
        }

        for (AnimatorTransition transition :
                transitions.getOrDefault(currentState, List.of())) {
            if (transition.shouldTransition(parameters)) {
                startTransition(transition);
                break;
            }
        }

        activeAnimations.removeIf(wa -> !wa.player.isLooping() && wa.player.getTime() >= wa.player.getClip().getDurationSeconds() && wa.weight <= 0f);

        for (Iterator<BlendInfo> it = activeBlends.iterator(); it.hasNext();) {
            BlendInfo blend = it.next();
            blend.update(delta);
            if (blend.isFinished()) {
                it.remove();
            }
        }

        computeGlobals();
    }

    private void startTransition(AnimatorTransition transition) {
        AnimationState target = transition.getTarget();
        AnimationPlayer newPlayer = new AnimationPlayer(target.getClip());

        WeightedAnimation newWeighted = new WeightedAnimation(newPlayer, 0f);
        activeAnimations.add(newWeighted);

        float blendTime = transition.getBlendTime();
        BlendInfo blendInfo = new BlendInfo(newWeighted, blendTime);
        activeBlends.add(blendInfo);

        currentState = target;
    }

    public Matrix4f sample(Node node) {
        if (activeAnimations.isEmpty())
            return new Matrix4f().identity();

        Matrix4f blended = new Matrix4f();
        boolean first = true;

        for (WeightedAnimation wa : activeAnimations) {
            Matrix4f m = wa.sample(node);
            if (first) {
                blended.set(m);
                first = false;
            } else {
                blended = InterpolationMatrices.interpolateMatrices(blended, m, wa.weight);
            }
        }

        return blended;
    }

    public void computeGlobals() {
        if (activeAnimations.isEmpty())
            return;

        animatedGlobals.clear();
        for (Node root : rootNodes) {
            computeRecursive(root, new Matrix4f().identity());
        }
    }

    private void computeRecursive(Node node, Matrix4f parentGlobal) {
        Matrix4f local = sample(node);
        Matrix4f global = new Matrix4f(parentGlobal).mul(local);
        animatedGlobals.put(node, global);

        for (Node child : node.getChildren()) {
            computeRecursive(child, global);
        }
    }

    public Matrix4f getAnimatedGlobal(Node node) {
        return animatedGlobals.getOrDefault(node, new Matrix4f());
    }

    public boolean isCurrentClipFinished() {
        if (activeAnimations.isEmpty()) return true;
        for (WeightedAnimation wa : activeAnimations) {
            if (wa.player.getTime() < wa.player.getClip().getDurationSeconds())
                return false;
        }
        return true;
    }

    public AnimatorParameters getParameters() {
        return parameters;
    }

    public AnimationState getCurrentState() {
        return currentState;
    }

    public List<WeightedAnimation> getActiveAnimations() {
        return activeAnimations;
    }

    public List<Node> getRootNodes() {
        return rootNodes;
    }

    private static class BlendInfo {
        WeightedAnimation animation;
        float blendTime;
        float elapsed = 0f;

        public BlendInfo(WeightedAnimation animation, float blendTime) {
            this.animation = animation;
            this.blendTime = blendTime;
        }

        public void update(float delta) {
            elapsed += delta;
            float t = Math.min(elapsed / blendTime, 1f);
            animation.weight = t;
        }

        public boolean isFinished() {
            return elapsed >= blendTime;
        }

    }
}