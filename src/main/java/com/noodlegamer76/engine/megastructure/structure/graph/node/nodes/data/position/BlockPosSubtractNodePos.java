package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.position;

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
import net.minecraft.core.BlockPos;

import java.util.List;

public class BlockPosSubtractNodePos extends ValueNode<BlockPosSubtractNodePos> {
    GenVar<BlockPos> output = new GenVar<>(BlockPos.ZERO, GenVarSerializers.BLOCK_POS, false, "Output");

    public BlockPosSubtractNodePos(int id, Graph graph) {
        super(id, graph, InitNodes.BLOCK_POS_SUBTRACT_NODE_POS, "Block Pos: Subtract node position", "Data/Position");
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        BlockPos worldPos = resolve(context, "World Pos", BlockPos.class);
        if (worldPos == null) return List.of(output);

        BlockPos nodeOrigin = context.getGlobalVar("Node Origin", GenVarSerializers.BLOCK_POS).getValue();

        BlockPos result = new BlockPos(
                worldPos.getX() - nodeOrigin.getX(),
                worldPos.getY(),
                worldPos.getZ() - nodeOrigin.getZ()
        );

        output.setValue(result);
        return List.of(output);
    }

    @Override
    protected void renderContents() {
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, BlockPos.class, "World Pos"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, BlockPos.class, "Output"));
    }
}