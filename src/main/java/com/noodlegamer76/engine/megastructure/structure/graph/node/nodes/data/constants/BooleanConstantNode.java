package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants;

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
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import imgui.ImGui;
import imgui.type.ImBoolean;

import java.util.List;

public class BooleanConstantNode extends ValueNode<BooleanConstantNode> {
    private final GenVar<Boolean> constant;
    private ImBoolean value = new ImBoolean(false);

    public BooleanConstantNode(int id, Graph graph) {
        super(id, graph, InitNodes.BOOLEAN_CONSTANT, "Boolean Const", "Data/Constants");
        constant = new GenVar<>(false, GenVarSerializers.BOOLEAN, false, "Value");
    }

    @Override
    protected void renderContents() {
        ImGui.checkbox("Value", value);
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Boolean.class, "Value"));
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        constant.setValue(value.get());
        return List.of(constant);
    }

    @Override
    public JsonObject saveData() {
        JsonObject data = super.saveData();
        data.addProperty("value", value.get());
        return data;
    }

    @Override
    public void loadData(JsonObject data) {
        value.set(data.get("value").getAsBoolean());
    }
}