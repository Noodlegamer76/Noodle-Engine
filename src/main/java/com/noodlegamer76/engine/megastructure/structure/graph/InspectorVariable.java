package com.noodlegamer76.engine.megastructure.structure.graph;

import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import imgui.type.ImString;

public class InspectorVariable {
    private final GenVar<?> variable;
    private final Runnable renderVariable;
    private final ImString name;

    public InspectorVariable(ImString name, GenVar<?> variable, Runnable renderVariable) {
        this.variable = variable;
        this.renderVariable = renderVariable;
        this.name = name;
    }

    public void render() {
        renderVariable.run();
    }

    public GenVar<?> getVariable() {
        return variable;
    }

    public ImString getName() {
        return name;
    }
}
