package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data;

import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ValueNode;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class Vec3Node extends ValueNode<Vec3Node> {
    private final GenVar<Vec3> constant;
    private final float[] value = new float[3];

    public Vec3Node(int id, Graph graph) {
        super(id, graph, InitNodes.VEC3_NODE, "3D Vector", "data");
        constant = new GenVar<>(new Vec3(0, 0, 0), GenVarSerializers.VEC3, false, "Vec3");
    }

    @Override
    protected void renderContents() {
        ImGui.setNextItemWidth(120f);
        ImGui.inputFloat3("Vector", value, "%.2f");
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Vec3.class, "Vec3"));
    }

    @Override
    public List<GenVar<?>> evaluate(Graph graph, ExecutionContext context) {
        constant.setValue(new Vec3(value[0], value[1], value[2]));
        return List.of(
                constant
        );
    }
}
