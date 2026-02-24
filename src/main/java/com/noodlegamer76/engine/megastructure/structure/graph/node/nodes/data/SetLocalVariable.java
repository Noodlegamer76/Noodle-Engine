package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data;

import com.google.gson.JsonObject;
import com.noodlegamer76.engine.core.network.GameObjectSerializers;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import imgui.ImGui;
import imgui.type.ImString;

public class SetLocalVariable extends ExecutionNode<SetLocalVariable> {
    private final ImString variableName = new ImString(256);

    public SetLocalVariable(int id, Graph graph) {
        super(id, graph, InitNodes.SET_LOCAL_VAR, "Set Local Variable", "Data");
    }

    @Override
    public void execute(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        Object value = resolve(context, "Value", Object.class);
        if (value != null && !variableName.get().isBlank()) {
            context.setLocalVar(variableName.get(), new GenVar<>(value, GenVarSerializers.ANY_OBJECT, false, false, variableName.get()));
        }
    }

    @Override
    protected void renderContents() {
        ImGui.setNextItemWidth(150f);
        ImGui.inputText("Name##" + getId(), variableName);
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.EXECUTION, null, "Execution Point"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Object.class, "Value"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.EXECUTION, null, "Execution Point"));
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