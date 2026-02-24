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
import java.util.function.IntPredicate;

public class ResourceLocationNode extends ConstantNode<ResourceLocation, ResourceLocationNode> {
    private final ImString namespace = new ImString(256);
    private final ImString path = new ImString(256);

    public ResourceLocationNode(int id, Graph graph) {
        super(
                id,
                graph,
                new GenVar<>(ResourceLocation.withDefaultNamespace(""), GenVarSerializers.RESOURCE_LOCATION, false, true, "Resource Location"),
                InitNodes.RESOURCE_LOCATION,
                "Resource Location Const",
                "Data/Constants"
        );
    }

    @Override
    protected void renderContents() {
        super.renderContents();

        namespace.set(getValue().getNamespace());
        path.set(getValue().getPath());

        ImGui.setNextItemWidth(280f);
        if (ImGui.inputText("Namespace##" + getId(), namespace)) {
            namespace.set(sanitize(namespace.get(), c -> (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_' || c == '-'));
            setValue(ResourceLocation.fromNamespaceAndPath(namespace.get(), path.get()));
        }

        ImGui.setNextItemWidth(280f);
        if (ImGui.inputText("Path##" + getId(), path)) {
            path.set(sanitize(path.get(), i -> validPathChar((char) i)));
            setValue(ResourceLocation.fromNamespaceAndPath(namespace.get(), path.get()));
        }
    }

    private static String sanitize(String input, IntPredicate validChar) {
        return input.chars()
                .filter(validChar)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static boolean validPathChar(char pathChar) {
        return pathChar == '_' || pathChar == '-' || pathChar >= 'a' && pathChar <= 'z' || pathChar >= '0' && pathChar <= '9' || pathChar == '/' || pathChar == '.';
    }
}