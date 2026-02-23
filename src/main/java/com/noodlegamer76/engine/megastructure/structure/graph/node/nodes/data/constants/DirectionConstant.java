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
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.core.Direction;

import java.util.List;

public class DirectionConstant extends ValueNode<DirectionConstant> {
    private final GenVar<Direction> constant;
    private Direction direction = Direction.UP;
    private final ImString name = new ImString(256);

    public DirectionConstant(int id, Graph graph) {
        super(id, graph, InitNodes.DIRECTION_CONSTANT, "Direction Const", "Data/Constants");
        constant = new GenVar<>(Direction.UP, Direction.class, false, "Value");
    }

    @Override
    protected void renderContents() {
        ImGui.setNextItemWidth(280f);
        ImGui.inputText("Name##" + getId(), name);
        ImGui.setNextItemWidth(280f);
        if (ImGui.beginCombo("Value##" + getId(), direction.getSerializedName())) {
            for (Direction direction : Direction.values()) {
                if (ImGui.selectable(direction.getSerializedName())) {
                    this.direction = direction;
                }
            }
            ImGui.endCombo();
        }
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Direction.class, "Value"));
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        constant.setValue(direction);
        return List.of(constant);
    }

    @Override
    public JsonObject saveData() {
        JsonObject data = super.saveData();
        data.addProperty("name", name.get());
        data.addProperty("value", direction.ordinal());
        return data;
    }

    @Override
    public void loadData(JsonObject data) {
        name.set(data.get("name").getAsString());
        direction = Direction.values()[data.get("value").getAsInt()];
    }

    @Override
    public List<InspectorVariable> getInspectorVariables() {
        return List.of(
                new InspectorVariable(name, constant, this::renderContents)
        );
    }
}