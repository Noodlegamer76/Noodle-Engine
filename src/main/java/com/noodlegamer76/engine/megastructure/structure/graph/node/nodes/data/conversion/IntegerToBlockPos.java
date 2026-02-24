package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.conversion;

import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSimulator;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.NodeType;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ValueNode;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class IntegerToBlockPos extends ValueNode<IntegerToBlockPos> {
    GenVar<BlockPos> output = new GenVar<>(new BlockPos(0, 0, 0), GenVarSerializers.BLOCK_POS, false, false, "Block Pos");

    public IntegerToBlockPos(int id, Graph graph) {
        super(id, graph, InitNodes.INT_TO_BLOCK_POS, "Integer to Block Pos", "Data/Conversion");
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        Integer xVar = resolve(context, "X", Integer.class);
        Integer yVar = resolve(context, "Y", Integer.class);
        Integer zVar = resolve(context, "Z", Integer.class);

        int x = xVar == null ? 0 : xVar;
        int y = yVar == null ? 0 : yVar;
        int z = zVar == null ? 0 : zVar;
        output.setValue(new BlockPos(x, y, z));

        return List.of(
                output
        );
    }

    @Override
    protected void renderContents() {

    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "X"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "Y"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "Z"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, BlockPos.class, "Block Pos"));
    }
}
