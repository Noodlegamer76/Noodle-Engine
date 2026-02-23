package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.random;

import com.google.gson.JsonObject;
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
import imgui.ImGui;
import imgui.type.ImInt;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class RandomInt extends ValueNode<RandomInt> {
    GenVar<Integer> output = new GenVar<>(0, Integer.class, false, "Random Int");
    ImInt from = new ImInt(0);
    ImInt to = new ImInt(100);

    public RandomInt(int id, Graph graph) {
        super(id, graph, InitNodes.RANDOM_INT, "Random Integer", "Data/Random");
    }

    @Override
    public List<GenVar<?>> evaluate(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        Graph graph = executer.getFunction();
        NodePin fromPin = getPins().stream()
                .filter(p -> p.getDisplayName().equals("From"))
                .findFirst().orElseThrow();

        NodePin toPin = getPins().stream()
                .filter(p -> p.getDisplayName().equals("To"))
                .findFirst().orElseThrow();

        Integer fromVal = GraphSimulator.resolveInputByPin(graph, context, fromPin, Integer.class);
        if (fromVal == null) fromVal = from.get();

        Integer toVal = GraphSimulator.resolveInputByPin(graph, context, toPin, Integer.class);
        if (toVal == null) toVal = to.get();

        RandomSource random = instance.getRandom(context);
        output.setValue(fromVal + random.nextInt(toVal - fromVal + 1));

        return List.of(output);
    }

    @Override
    protected void renderContents() {
        boolean fromConnected = getGraph().getConnectedOutput(
                getPins().stream().filter(p -> p.getDisplayName().equals("From")).findFirst().orElse(null)
        ).isPresent();

        boolean toConnected = getGraph().getConnectedOutput(
                getPins().stream().filter(p -> p.getDisplayName().equals("To")).findFirst().orElse(null)
        ).isPresent();

        if (!fromConnected) {
            ImGui.setNextItemWidth(80f);
            ImGui.inputInt("From##" + getId(), from);
        }
        if (!toConnected) {
            ImGui.setNextItemWidth(80f);
            ImGui.inputInt("To##" + getId(), to);
        }
    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "From"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "To"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT, PinCategory.DATA, Integer.class, "Random Int"));
    }

    @Override
    public JsonObject saveData() {
        JsonObject data = new JsonObject();
        data.addProperty("from", from.get());
        data.addProperty("to", to.get());
        return data;
    }

    @Override
    public void loadData(JsonObject data) {
        if (data.has("from")) from.set(data.get("from").getAsInt());
        if (data.has("to")) to.set(data.get("to").getAsInt());
    }
}