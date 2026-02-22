package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constant;

import com.google.gson.JsonObject;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
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
import net.minecraft.core.BlockPos;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class BlockPosNode extends ValueNode<BlockPosNode> {
    private final GenVar<BlockPos> constant;
    private final int[] value = new int[3];

    public BlockPosNode(int id, Graph graph) {
        super(id, graph, InitNodes.BLOCK_POS, "Block Pos", "Data/Constants");
        constant = new GenVar<>(new BlockPos(0, 0, 0), GenVarSerializers.BLOCK_POS, false, "Position");
    }

    @Override
    public List<GenVar<?>> evaluate(Graph graph, ExecutionContext context) {
        constant.setValue(new BlockPos(value[0], value[1], value[2]));
        return List.of(
                constant
        );
    }

    @Override
    protected void renderContents() {
        ImGui.setNextItemWidth(180);
        ImGui.inputInt3("Position##" + getId(), value);
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, BlockPos.class, "Position"));
    }

    @Override
    public JsonObject saveData() {
        JsonObject data = super.saveData();
        data.addProperty("x", value[0]);
        data.addProperty("y", value[1]);
        data.addProperty("z", value[2]);
        return data;
    }

    @Override
    public void loadData(JsonObject data) {
        value[0] = data.get("x").getAsInt();
        value[1] = data.get("y").getAsInt();
        value[2] = data.get("z").getAsInt();
        constant.setValue(new BlockPos(value[0], value[1], value[2]));
    }
}
