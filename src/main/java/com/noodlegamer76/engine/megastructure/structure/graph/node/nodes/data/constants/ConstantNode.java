package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants;

import com.google.gson.JsonObject;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.InspectorVariable;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;
import com.noodlegamer76.engine.megastructure.structure.graph.node.NodeType;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ValueNode;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import imgui.ImGui;
import imgui.type.ImString;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public abstract class ConstantNode<T, U extends Node<U>> extends ValueNode<U> {
    private final ImString name = new ImString(256);
    private final GenVar<T> value;

    protected ConstantNode(int id, Graph graph, GenVar<T> var, RegistryObject<NodeType<U>> registry, String name, String category) {
        super(id, graph, registry, name, category);
        value = var;
        registerVar(value);
    }

    @Override
    public void saveAdditionalData(JsonObject data) {
        data.addProperty("name", name.get());
    }

    @Override
    protected void renderContents() {
        ImGui.setNextItemWidth(280f);
        ImGui.inputText("Name##" + getId(), name);
    }

    @Override
    public List<InspectorVariable> getInspectorVariables() {
        return List.of(
                new InspectorVariable(name, value, this::renderContents)
        );
    }

    @Override
    public void loadAdditionalData(JsonObject data) {
        name.set(data.get("name").getAsString());
    }

    public ImString getName() {
        return name;
    }

    public GenVar<T> getValueVar() {
        return value;
    }

    public T getValue() {
        return value.getValue();
    }

    public void setValue(T value) {
        this.value.setValue(value);
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        return List.of(
                value
        );
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, value.getSerializer().getHandledClass(), "Value"));
    }
}
