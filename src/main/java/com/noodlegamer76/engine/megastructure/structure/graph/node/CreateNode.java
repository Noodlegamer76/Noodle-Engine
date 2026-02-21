package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.noodlegamer76.engine.megastructure.structure.graph.Graph;

@FunctionalInterface
public interface CreateNode<T extends Node> {
    T create(int id, Graph graph);
}