package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public abstract class ValueNode<T extends Node<T>> extends Node<T> {

    protected ValueNode(int id, Graph graph, RegistryObject<NodeType<T>> registry, String name, String category) {
        super(id, graph, registry, name, category);
    }

    public abstract List<GenVar<?>> evaluate(Graph graph, ExecutionContext context);
}