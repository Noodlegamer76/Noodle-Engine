package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constant;

import com.google.gson.JsonObject;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.node.*;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import imgui.ImGui;
import imgui.type.ImString;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ResourceLocationNode extends ValueNode<ResourceLocationNode> {
    private final GenVar<ResourceLocation> locationGenVar;
    private final ImString namespace = new ImString(256);
    private final ImString path = new ImString(256);

    public ResourceLocationNode(int id, Graph graph) {
        super(id, graph, InitNodes.RESOURCE_LOCATION, "Resource Location", "Data/Constants");
        locationGenVar = new GenVar<>(ResourceLocation.withDefaultNamespace("dummy"), GenVarSerializers.RESOURCE_LOCATION, false, "Resource Location");
    }

    @Override
    public List<GenVar<?>> evaluate(Graph graph, ExecutionContext context) {
        locationGenVar.setValue(ResourceLocation.fromNamespaceAndPath(namespace.get(), path.get()));
        return List.of(
                locationGenVar
        );
    }

    @Override
    protected void renderContents() {
        ImGui.setNextItemWidth(120f);
        ImGui.inputText("Namespace##" + getId(), namespace);
        ImGui.setNextItemWidth(240);
        ImGui.inputText("Path##" + getId(), path);
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, ResourceLocation.class, "Resource Location"));
    }

    @Override
    public JsonObject saveData() {
        JsonObject data = new JsonObject();
        data.addProperty("namespace", namespace.get());
        data.addProperty("path", path.get());
        return data;
    }

    @Override
    public void loadData(JsonObject data) {
        if (data.has("namespace")) namespace.set(data.get("namespace").getAsString());
        if (data.has("path")) path.set(data.get("path").getAsString());
    }
}