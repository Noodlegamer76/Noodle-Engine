package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants;

import com.google.gson.JsonObject;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.InspectorVariable;
import com.noodlegamer76.engine.megastructure.structure.graph.node.*;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import imgui.ImGui;
import imgui.type.ImString;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class ResourceLocationNode extends ValueNode<ResourceLocationNode> {
    private final GenVar<ResourceLocation> locationGenVar;
    private final ImString namespace = new ImString(256);
    private final ImString path = new ImString(256);
    private final ImString name = new ImString(256);

    public ResourceLocationNode(int id, Graph graph) {
        super(id, graph, InitNodes.RESOURCE_LOCATION, "Resource Location Const", "Data/Constants");
        locationGenVar = new GenVar<>(ResourceLocation.withDefaultNamespace("dummy"), ResourceLocation.class, false, "Resource Location");
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        locationGenVar.setValue(ResourceLocation.fromNamespaceAndPath(namespace.get(), path.get()));
        return List.of(
                locationGenVar
        );
    }

    @Override
    protected void renderContents() {
        ImGui.setNextItemWidth(280f);
        ImGui.inputText("Name##" + getId(), name);
        ImGui.setNextItemWidth(280f);
        ImGui.inputText("Namespace##" + getId(), namespace);
        ImGui.setNextItemWidth(280f);
        ImGui.inputText("Path##" + getId(), path);
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, ResourceLocation.class, "Resource Location"));
    }

    @Override
    public JsonObject saveData() {
        JsonObject data = new JsonObject();
        data.addProperty("name", name.get());
        data.addProperty("namespace", namespace.get());
        data.addProperty("path", path.get());
        return data;
    }

    @Override
    public void loadData(JsonObject data) {
        name.set(data.get("name").getAsString());
        if (data.has("namespace")) namespace.set(data.get("namespace").getAsString());
        if (data.has("path")) path.set(data.get("path").getAsString());
    }

    @Override
    public List<InspectorVariable> getInspectorVariables() {
        return List.of(
                new InspectorVariable(name, locationGenVar, this::renderContents)
        );
    }
}