package com.noodlegamer76.engine.megastructure.structure;

import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSimulator;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;

import java.util.HashMap;
import java.util.Map;

public class StructureExecuter {
    private final Map<String, GenVar<?>> genVars = new HashMap<>();
    private final int priority;
    private final int id;
    private final String name;
    private final Graph function;
    private final int nodeLevel;

    protected StructureExecuter(int priority, String name, Graph function, int nodeLevel) {
        this.priority = priority;
        this.name = name;
        this.function = function;
        this.nodeLevel = nodeLevel;
        this.id = Structures.getInstance().nextId();
    }

    public String getName() {
        return name;
    }

    public Graph getFunction() {
        return function;
    }

    public int getNodeLevel() {
        return nodeLevel;
    }

    public int getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    public Map<String, GenVar<?>> getGenVars() {
        return genVars;
    }
}
