package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data;

import com.google.gson.JsonObject;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ValueNode;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import imgui.ImGui;
import imgui.type.ImString;

import java.util.List;

public class GetLocalVariable extends  ValueNode<GetLocalVariable> {
    private final ImString variableName = new ImString(256);
    private final GenVar<Object> result = new GenVar<>(Object.class, Object.class, false, "Value");

    public GetLocalVariable(int id, Graph graph) {
        super(id, graph, InitNodes.GET_LOCAL_VAR, "Get Local Variable", "Data");
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        GenVar<?> var = context.getLocalVar(variableName.get());
        if (var != null) {
            result.setValue(var.getValue());
        }
        return List.of(result);
    }

    @Override
    protected void renderContents() {
        ImGui.setNextItemWidth(150f);
        ImGui.inputText("Name##" + getId(), variableName);
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Object.class, "Value"));
    }

    @Override
    public JsonObject saveData() {
        JsonObject data = super.saveData();
        data.addProperty("variableName", variableName.get());
        return data;
    }

    @Override
    public void loadData(JsonObject data) {
        variableName.set(data.get("variableName").getAsString());
    }
}