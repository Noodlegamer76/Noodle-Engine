package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.flow;

import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;

public class StartNode extends ExecutionNode<StartNode> {

    public StartNode(int id, Graph graph) {
        super(id, graph, InitNodes.START, "Start", "Flow");
    }

    @Override
    public void execute(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        //Let the simulator just move to the next execution node
    }

    @Override
    protected void renderContents() {
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.EXECUTION, null, ""));
    }
}