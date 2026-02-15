package com.noodlegamer76.engine.gltf.animation.animation.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnimatorParameters {
    private final Map<String, Float> floats = new HashMap<>();
    private final Map<String, Boolean> bools = new HashMap<>();
    private final Set<String> triggers = new HashSet<>();

    public void setFloat(String name, float value) {
        floats.put(name, value);
    }

    public float getFloat(String name) {
        return floats.getOrDefault(name, 0f);
    }

    public void setBool(String name, boolean value) {
        bools.put(name, value);
    }

    public boolean getBool(String name) {
        return bools.getOrDefault(name, false);
    }

    public void setTrigger(String name) {
        triggers.add(name);
    }

    public boolean consumeTrigger(String name) {
        return triggers.remove(name);
    }
}
