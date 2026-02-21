package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.noodlegamer76.engine.megastructure.structure.graph.Graph;

public class NodeType<T extends Node> {
    private final CreateNode<T> createNode;

    public NodeType(CreateNode<T> createNode) {
        this.createNode = createNode;
    }

    public T create(int id, Graph graph) {
        return createNode.create(id, graph);
    }
}