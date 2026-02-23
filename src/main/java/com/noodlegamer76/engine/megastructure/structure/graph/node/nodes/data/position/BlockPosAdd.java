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

public class BlockPosAdd extends ValueNode<BlockPosAdd> {
    GenVar<BlockPos> output = new GenVar<>(BlockPos.ZERO, BlockPos.class, false, "Output");

    public BlockPosAdd(int id, Graph graph) {
        super(id, graph, InitNodes.BLOCK_POS_ADD, "Block Pos: Add", "Data/Position");
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        BlockPos a = resolve(context, "A", BlockPos.class);
        BlockPos b = resolve(context, "B", BlockPos.class);

        int ax = a == null ? 0 : a.getX();
        int ay = a == null ? 0 : a.getY();
        int az = a == null ? 0 : a.getZ();

        int bx = b == null ? 0 : b.getX();
        int by = b == null ? 0 : b.getY();
        int bz = b == null ? 0 : b.getZ();

        output.setValue(new BlockPos(ax + bx, ay + by, az + bz));
        return List.of(output);
    }

    @Override
    protected void renderContents() {
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, BlockPos.class, "A"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, BlockPos.class, "B"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, BlockPos.class, "Output"));
    }
}