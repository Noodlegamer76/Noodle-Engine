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

public class DirectionConstant extends ConstantNode<Direction, DirectionConstant> {
    public DirectionConstant(int id, Graph graph) {
        super(
                id,
                graph,
                new GenVar<>(Direction.UP, GenVarSerializers.DIRECTION, false, true, "Value"),
                InitNodes.DIRECTION_CONSTANT,
                "Direction Const",
                "Data/Constants"
        );
    }

    @Override
    protected void renderContents() {
        super.renderContents();
        ImGui.setNextItemWidth(280f);
        if (ImGui.beginCombo("Value##" + getId(), getValue().getSerializedName())) {
            for (Direction direction : Direction.values()) {
                if (ImGui.selectable(direction.getSerializedName())) {
                    setValue(direction);
                }
            }
            ImGui.endCombo();
        }
    }
}