package com.noodlegamer76.engine.gltf.node;

import com.noodlegamer76.engine.gltf.McGltf;
import de.javagl.jgltf.model.NodeModel;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private Node parent;
    private final List<Node> children = new ArrayList<>();
    private Matrix4f global;
    private Matrix4f local;
    private final McGltf gltf;

    public Node(Matrix4f local, McGltf gltf) {
        this.local = local;
        this.gltf = gltf;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void setGlobal(Matrix4f global) {
        this.global = global;
    }

    public Vector3f getPosition() {
        return new Vector3f(global.m30(), global.m31(), global.m32());
    }

    public Quaternionf getRotation() {
        return new Quaternionf(global.m00(), global.m01(), global.m02(), global.m03());
    }

    public Vector3f getScale() {
        return new Vector3f(global.m00(), global.m01(), global.m02());
    }

    @Nullable
    public Node getParent() {
        return parent;
    }

    public Matrix4f getGlobal() {
        return global;
    }

    public Matrix4f getLocal() {
        return local;
    }

    public McGltf getGltf() {
        return gltf;
    }
}