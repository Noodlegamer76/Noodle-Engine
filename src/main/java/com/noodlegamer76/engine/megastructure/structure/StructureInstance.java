package com.noodlegamer76.engine.megastructure.structure;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.megastructure.Node;
import com.noodlegamer76.engine.megastructure.StructMath;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSimulator;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.placers.Placer;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializer;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureInstance {
    private final StructureDefinition definition;
    private final GraphSimulator simulator = new GraphSimulator();
    private final FeaturePlaceContext<NoneFeatureConfiguration> ctx;
    private final List<Placer> placers = new ArrayList<>();

    public StructureInstance(StructureDefinition definition, FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        this.definition = definition;
        this.ctx = ctx;
    }

    public void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        for (List<StructureExecuter> executerList : definition.getStructureExecuters().values()) {
            for (StructureExecuter executer : executerList) {
                processExecuterForNodes(ctx, executer);
            }
        }

        for (Placer placer: getPlacers()) {
            placer.place(ctx,ctx.random(), this);
        }
    }

    private void processExecuterForNodes(FeaturePlaceContext<NoneFeatureConfiguration> ctx, StructureExecuter executer) {
        int level = executer.getNodeLevel();
        Node centerNode = new Node(ctx.origin().getX(), ctx.origin().getZ(), level);
        List<Node> nodes = StructMath.get3x3Nodes(centerNode);

        for (Node node : nodes) {
            ExecutionContext nodeContext = createExecutionContext(ctx, node, executer);
            simulator.run(executer.getFunction(), nodeContext, this);
        }
    }

    private ExecutionContext createExecutionContext(FeaturePlaceContext<NoneFeatureConfiguration> ctx, Node node, StructureExecuter executer) {
        ExecutionContext nodeContext = new ExecutionContext();
        RandomSource random = StructMath.getNodeRandom(node, ctx, executer.getId());
        nodeContext.addGlobalVar(new GenVar<>(random.nextLong(), GenVarSerializers.LONG, true, "Structure Seed"));
        return nodeContext;
    }

    public StructureDefinition getDefinition() {
        return definition;
    }

    public FeaturePlaceContext<NoneFeatureConfiguration> getContext() {
        return ctx;
    }

    public List<Placer> getPlacers() {
        return placers;
    }

    public void addPlacer(Placer placer) {
        placers.add(placer);
    }
}
