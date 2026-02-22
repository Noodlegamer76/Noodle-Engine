package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.logic;

import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSimulator;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ValueNode;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;

import java.util.List;

public class BooleanOr extends ValueNode<BooleanOr> {
    GenVar<Boolean> output = new GenVar<>(false, GenVarSerializers.BOOLEAN, false, "Output");

    public BooleanOr(int id, Graph graph) {
        super(id, graph, InitNodes.BOOLEAN_OR, "Boolean: OR", "Data/Logic");
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        Boolean a = resolve(context, "A", Boolean.class);
        Boolean b = resolve(context, "B", Boolean.class);

        output.setValue((a != null && a) || (b != null && b));
        return List.of(output);
    }

    @Override
    protected void renderContents() {}

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Boolean.class, "A"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Boolean.class, "B"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Boolean.class, "Output"));
    }
}