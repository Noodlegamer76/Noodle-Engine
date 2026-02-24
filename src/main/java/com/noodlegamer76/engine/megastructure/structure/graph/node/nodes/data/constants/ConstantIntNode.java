package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants;

import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import imgui.ImGui;
import imgui.type.ImInt;

public class ConstantIntNode extends ConstantNode<Integer, ConstantIntNode> {
    private final ImInt editorValue = new ImInt();

    public ConstantIntNode(int id, Graph graph) {
        super(
                id,
                graph,
                new GenVar<>(0, GenVarSerializers.INT, false, true, "Value"),
                InitNodes.INT_CONSTANT,
                "Integer Const",
                "Data/Constants"
        );
    }

    @Override
    protected void renderContents() {
        super.renderContents();
        editorValue.set(getValue());
        ImGui.setNextItemWidth(280f);
        if (ImGui.inputInt("Value##" + getId(), editorValue)) {
            setValue(editorValue.get());
        }
    }
}