package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.google.gson.JsonObject;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
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

    protected void addPin(NodePin pin) {
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
        return new JsonObject();
    }

    public void loadData(JsonObject data) {
    }

    public NodePin getPin(String displayName) {
        return pinsByDisplayName.get(displayName);
    }

    protected abstract void renderContents();

    public abstract void initPins();
}