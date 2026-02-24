package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants;

import com.google.gson.JsonObject;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.InspectorVariable;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.NodeType;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ValueNode;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import imgui.ImGui;
import imgui.type.ImString;
import net.minecraft.core.BlockPos;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class BlockPosNode extends ConstantNode<BlockPos, BlockPosNode> {
    private final GenVar<Integer> x;
    private final GenVar<Integer> y;
    private final GenVar<Integer> z;
    private final int[] editorValue = new int[3];

    public BlockPosNode(int id, Graph graph) {
        super(
                id,
                graph,
                new GenVar<>(BlockPos.ZERO, GenVarSerializers.BLOCK_POS, false, true, "Value"),
                InitNodes.BLOCK_POS,
                "Block Pos",
                "Data/Constants"
        );
        x = new GenVar<>(0, GenVarSerializers.INT, false, false, "X");
        y = new GenVar<>(0, GenVarSerializers.INT, false, false, "Y");
        z = new GenVar<>(0, GenVarSerializers.INT, false, false, "Z");
        registerVar(x);
        registerVar(y);
        registerVar(z);
    }

    @Override
    protected void renderContents() {
        super.renderContents();

        editorValue[0] = getValue().getX();
        editorValue[1] = getValue().getY();
        editorValue[2] = getValue().getZ();

        ImGui.setNextItemWidth(280f);
        if (ImGui.inputInt3("Position##" + getId(), editorValue)) {
            setValue(new BlockPos(editorValue[0], editorValue[1], editorValue[2]));
        }
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        BlockPos pos = getValue();
        x.setValue(pos.getX());
        y.setValue(pos.getY());
        z.setValue(pos.getZ());
        List<GenVar<?>> vars = new ArrayList<>();
        vars.add(x);
        vars.add(y);
        vars.add(z);
        vars.addAll(super.evaluate(executer, context, instance));
        return vars;
    }

    @Override
    public void initPins() {
        super.initPins();
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, x.getSerializer().getHandledClass(), "X"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, y.getSerializer().getHandledClass(), "Y"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, z.getSerializer().getHandledClass(), "Z"));
    }
}
