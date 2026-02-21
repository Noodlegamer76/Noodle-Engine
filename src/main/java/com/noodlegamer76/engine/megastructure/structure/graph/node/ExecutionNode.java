package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public abstract class ExecutionNode<T extends Node<T>> extends Node<T> {

    protected ExecutionNode(int id, Graph graph, RegistryObject<NodeType<T>> registry, String name, String category) {
        super(id, graph, registry, name, category);
    }

    public abstract void execute(Graph graph, ExecutionContext context);
}