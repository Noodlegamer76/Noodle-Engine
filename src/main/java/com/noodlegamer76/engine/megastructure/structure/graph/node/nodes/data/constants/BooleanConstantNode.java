package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants;

import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;

public class BooleanConstantNode extends ConstantNode<Boolean, BooleanConstantNode> {
    private final ImBoolean editorValue = new ImBoolean();

    public BooleanConstantNode(int id, Graph graph) {
        super(
                id,
                graph,
                new GenVar<>(false, GenVarSerializers.BOOLEAN, false, true, "Value"),
                InitNodes.BOOLEAN_CONSTANT,
                "Boolean Const",
                "Data/Constants"
        );
    }

    @Override
    protected void renderContents() {
        super.renderContents();
        editorValue.set(getValue());
        ImGui.setNextItemWidth(280f);
        if (ImGui.checkbox("Value##" + getId(), editorValue)) {
            setValue(editorValue.get());
        }
    }
}