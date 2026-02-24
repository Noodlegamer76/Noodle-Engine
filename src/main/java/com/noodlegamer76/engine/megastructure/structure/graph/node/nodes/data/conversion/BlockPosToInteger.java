package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.conversion;

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

public class BlockPosToInteger extends ValueNode<BlockPosToInteger> {
    GenVar<Integer> xOut = new GenVar<>(0, GenVarSerializers.INT, false, false, "X");
    GenVar<Integer> yOut = new GenVar<>(0, GenVarSerializers.INT, false, false, "Y");
    GenVar<Integer> zOut = new GenVar<>(0, GenVarSerializers.INT, false, false, "Z");

    public BlockPosToInteger(int id, Graph graph) {
        super(id, graph, InitNodes.BLOCK_POS_TO_INT, "Block Pos to Integer", "Data/Conversion");
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        BlockPos pos = resolve(context, "Block Pos", BlockPos.class);

        if (pos == null) {
            xOut.setValue(0);
            yOut.setValue(0);
            zOut.setValue(0);
        } else {
            xOut.setValue(pos.getX());
            yOut.setValue(pos.getY());
            zOut.setValue(pos.getZ());
        }

        return List.of(
                xOut,
                yOut,
                zOut
        );
    }

    @Override
    protected void renderContents() {

    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, BlockPos.class, "Block Pos"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Integer.class, "X"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Integer.class, "Y"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Integer.class, "Z"));
    }
}