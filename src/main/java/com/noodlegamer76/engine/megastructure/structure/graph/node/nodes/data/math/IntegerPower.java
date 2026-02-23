package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.math;

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

public class IntegerPower extends ValueNode<IntegerPower> {
    GenVar<Integer> output = new GenVar<>(0, Integer.class, false, "Output");

    public IntegerPower(int id, Graph graph) {
        super(id, graph, InitNodes.INTEGER_POWER, "Integer: Power", "Data/Math");
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        Integer base = resolve(context, "Base", Integer.class);
        Integer exponent = resolve(context, "Exponent", Integer.class);

        int b = base == null ? 0 : base;
        int e = exponent == null ? 0 : exponent;

        int result = (int) Math.pow(b, e);

        output.setValue(result);
        return List.of(output);
    }

    @Override
    protected void renderContents() {
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "Base"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "Exponent"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Integer.class, "Output"));
    }
}