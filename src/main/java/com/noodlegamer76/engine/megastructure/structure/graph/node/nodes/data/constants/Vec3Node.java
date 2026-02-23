package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants;

import com.google.gson.JsonObject;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.InspectorVariable;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ValueNode;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import imgui.ImGui;
import imgui.type.ImString;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Vec3Node extends ValueNode<Vec3Node> {
    private final GenVar<Vec3> constant;
    private final float[] value = new float[3];
    private final ImString name = new ImString(256);

    public Vec3Node(int id, Graph graph) {
        super(id, graph, InitNodes.VEC3_NODE, "3D Vector Const", "Data/Constants");
        constant = new GenVar<>(new Vec3(0, 0, 0),Vec3.class, false, "Vec3");
    }

    @Override
    protected void renderContents() {
        ImGui.setNextItemWidth(280f);
        ImGui.inputText("Name##" + getId(), name);
        ImGui.setNextItemWidth(280f);
        ImGui.inputFloat3("Vector##" + getId(), value);
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Vec3.class, "Vec3"));
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        constant.setValue(new Vec3(value[0], value[1], value[2]));
        return List.of(
                constant
        );
    }

    @Override
    public JsonObject saveData() {
        JsonObject data = super.saveData();
        data.addProperty("name", name.get());
        data.addProperty("x", value[0]);
        data.addProperty("y", value[1]);
        data.addProperty("z", value[2]);
        return data;
    }

    @Override
    public void loadData(JsonObject data) {
        name.set(data.get("name").getAsString());
        value[0] = data.get("x").getAsFloat();
        value[1] = data.get("y").getAsFloat();
        value[2] = data.get("z").getAsFloat();
        constant.setValue(new Vec3(value[0], value[1], value[2]));
    }

    @Override
    public List<InspectorVariable> getInspectorVariables() {
        return List.of(
                new InspectorVariable(name, constant, this::renderContents)
        );
    }
}
