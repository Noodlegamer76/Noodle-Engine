package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSimulator;
import com.noodlegamer76.engine.megastructure.structure.graph.InspectorVariable;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializer;
import imgui.extension.imnodes.ImNodes;
import imgui.ImGui;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Node<T extends Node<T>> {
    protected final int id;
    protected final List<NodePin> pins = new ArrayList<>();
    private final Map<String, NodePin> pinsByDisplayName = new HashMap<>();
    public float x, y;
    private final RegistryObject<NodeType<T>> registry;
    protected final List<GenVar<?>> exposedVars = new ArrayList<>();

    private final String displayName;
    private final String categoryPath;
    private final Graph graph;

    protected Node(int id, Graph graph, RegistryObject<NodeType<T>> registry, String displayName, String categoryPath) {
        this.id = id;
        this.graph = graph;
        this.registry = registry;
        this.displayName = displayName;
        this.categoryPath = categoryPath;
    }

    public int getId() {
        return id;
    }

    public List<NodePin> getPins() {
        return pins;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCategoryPath() {
        return categoryPath;
    }

    public void addPin(NodePin pin) {
        pins.add(pin);
        pinsByDisplayName.put(pin.getDisplayName(), pin);
    }

    public void render() {
        ImNodes.beginNode(id);

        ImNodes.beginNodeTitleBar();
        ImGui.text(displayName);
        ImNodes.endNodeTitleBar();

        List<NodePin> inputs = pins.stream()
                .filter(p -> p.getKind() == PinKind.INPUT)
                .toList();
        List<NodePin> outputs = pins.stream()
                .filter(p -> p.getKind() == PinKind.OUTPUT)
                .toList();

        for (NodePin pin : inputs) {
            ImNodes.beginInputAttribute(pin.getId());
            ImGui.text(pin.getDisplayName());
            ImNodes.endInputAttribute();
        }

        renderContents();

        for (NodePin pin : outputs) {
            ImNodes.beginOutputAttribute(pin.getId());
            ImGui.text(pin.getDisplayName());
            ImNodes.endOutputAttribute();
        }

        ImNodes.endNode();
    }

    public RegistryObject<NodeType<T>> getRegistry() {
        return registry;
    }

    public Graph getGraph() {
        return graph;
    }

    public JsonObject saveData() {
        JsonObject data = new JsonObject();
        for (GenVar<?> var : exposedVars) {
            if (var.isPersistent()) {
                data.add(var.getName(), serializeGenVar(var));
            }
        }
        saveAdditionalData(data);
        return data;
    }

    private <V> JsonElement serializeGenVar(GenVar<V> var) {
        return var.getSerializer().serialize(var.getValue());
    }

    private <V> void deserializeGenVar(GenVar<V> var, JsonElement element) {
        var.setValue(var.getSerializer().deserialize(element));
    }

    public void saveAdditionalData(JsonObject data) {
    }

    public void loadData(JsonObject data) {
        for (GenVar<?> var: exposedVars) {
            if (var.isPersistent()) {
                deserializeGenVar(var, data.get(var.getName()));
            }
        }
        loadAdditionalData(data);
    }

    public void loadAdditionalData(JsonObject data) {

    }

    public NodePin getPin(String displayName) {
        return pinsByDisplayName.get(displayName);
    }

    protected <V> V resolve(ExecutionContext context, String pinName, Class<V> type) {
        return GraphSimulator.resolveInputByPin(getGraph(), context, getPin(pinName), type);
    }

    protected <V> V resolve(ExecutionContext context, String pinName, Class<V> type, V defaultValue) {
        V value = resolve(context, pinName, type);
        return value != null ? value : defaultValue;
    }

    public List<InspectorVariable> getInspectorVariables() {
        return List.of();
    }

    protected void registerVar(GenVar<?> var) {
        exposedVars.add(var);
    }


    protected abstract void renderContents();

    public abstract void initPins();
}