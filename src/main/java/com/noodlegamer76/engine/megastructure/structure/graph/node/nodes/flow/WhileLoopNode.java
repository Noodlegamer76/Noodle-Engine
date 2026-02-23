package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.flow;

import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.NodeType;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import net.minecraftforge.registries.RegistryObject;

public class WhileLoopNode extends ExecutionNode<WhileLoopNode> {
    public WhileLoopNode(int id, Graph graph) {
        super(id, graph, InitNodes.WHILE, "While", "Flow");
    }

    @Override
    public void execute(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        Boolean condition = resolve(context, "Condition", Boolean.class);

        if (condition == null) {
            condition = false;
        }

        if (condition) {
            instance.getSimulator().scheduleAfter(this);
        }
    }

    @Override
    protected void renderContents() {

    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.EXECUTION, null, "Execution Point"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Boolean.class, "Condition"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.EXECUTION, null, "Do"));
    }
}
