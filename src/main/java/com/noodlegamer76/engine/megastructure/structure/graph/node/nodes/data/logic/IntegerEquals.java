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
import java.util.Objects;

public class IntegerEquals extends ValueNode<IntegerEquals> {
    GenVar<Boolean> output = new GenVar<>(false, GenVarSerializers.BOOLEAN, false, "Output");

    public IntegerEquals(int id, Graph graph) {
        super(id, graph, InitNodes.INTEGER_EQUALS, "Integer: Equals", "Data/Logic");
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        Integer a = resolve(context, "A", Integer.class);
        Integer b = resolve(context, "B", Integer.class);

        int aVal = a != null ? a : 0;
        int bVal = b != null ? b : 0;
        output.setValue(aVal == bVal);
        return List.of(output);
    }

    @Override
    protected void renderContents() {}

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "A"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "B"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Boolean.class, "Output"));
    }
}