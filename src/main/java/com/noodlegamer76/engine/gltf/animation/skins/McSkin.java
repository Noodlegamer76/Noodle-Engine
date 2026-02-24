package com.noodlegamer76.engine.gltf.animation.skins;

import com.noodlegamer76.engine.gltf.node.Node;

import java.util.ArrayList;
import java.util.List;

public class McSkin {
    private final List<Node> joints = new ArrayList<>();

    public List<Node> getJoints() {
        return joints;
    }

    public void addJoint(Node node) {
        joints.add(node);
    }
}
