package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.flow;

import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSimulator;
import com.noodlegamer76.engine.megastructure.structure.graph.node.*;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class IfNode extends ExecutionNode<IfNode> {
    private boolean lastResult = false;

    public IfNode(int id, Graph graph) {
        super(id, graph, InitNodes.IF, "If", "Flow");
    }

    @Override
    public void execute(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        NodePin conditionPin = getPins().stream()
                .filter(p -> p.getDisplayName().equals("Condition"))
                .findFirst().orElseThrow();

        Boolean condition = GraphSimulator.resolveInputByPin(
                executer.getFunction(), context, conditionPin, Boolean.class);

        lastResult = condition != null && condition;
    }

    @Override
    public String getNextExecutionPin(ExecutionContext context) {
        return lastResult ? "True" : "False";
    }

    @Override
    protected void renderContents() {

    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.EXECUTION, null, ""));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Boolean.class, "Condition"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.EXECUTION, null, "True"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.EXECUTION, null, "False"));
    }
}