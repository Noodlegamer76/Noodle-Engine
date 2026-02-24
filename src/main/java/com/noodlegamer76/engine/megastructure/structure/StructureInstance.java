package com.noodlegamer76.engine.megastructure.structure;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.megastructure.Node;
import com.noodlegamer76.engine.megastructure.StructMath;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSimulator;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionNode;
import com.noodlegamer76.engine.megastructure.structure.placers.Placer;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializer;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import net.minecraft.core.BlockPos;
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
    private final Map<ExecutionContext, RandomSource> randoms = new HashMap<>();

    public StructureInstance(StructureDefinition definition, FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        this.definition = definition;
        this.ctx = ctx;
    }

    public void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        Node centerNode = new Node(ctx.origin().getX(), ctx.origin().getZ(), 0);
        List<Node> nodes = StructMath.get3x3Nodes(centerNode);

        for (Node node : nodes) {
            ExecutionContext nodeContext = createExecutionContext(ctx, node);

            for (List<StructureExecuter> executerList : definition.getStructureExecuters().values()) {
                for (StructureExecuter executer : executerList) {
                    simulator.run(executer, nodeContext, this);
                }
            }
        }

        for (Placer placer : getPlacers()) {
            placer.place(ctx, ctx.random(), this);
        }
        placers.clear();
    }

    private ExecutionContext createExecutionContext(FeaturePlaceContext<NoneFeatureConfiguration> ctx, Node node) {
        ExecutionContext nodeContext = new ExecutionContext();
        RandomSource random = StructMath.getNodeRandom(node, ctx, definition.getId().hashCode());
        nodeContext.addGlobalVar(new GenVar<>(random.nextLong(), GenVarSerializers.LONG, true, false, "Structure Seed"));
        nodeContext.addGlobalVar(new GenVar<>(new BlockPos(node.getX(), 0, node.getZ()), GenVarSerializers.BLOCK_POS, true, false, "Node Origin"));
        return nodeContext;
    }

    public RandomSource getRandom(ExecutionContext context) {
        if (randoms.containsKey(context)) return randoms.get(context);

        long seed = context.getGlobalVar("Structure Seed", Long.class).getValue();
        RandomSource random = RandomSource.create(seed);
        context.addGlobalVar(new GenVar<>(random.nextLong(), GenVarSerializers.LONG, true, false, "Structure Seed"));
        randoms.put(context, random);
        return random;
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

    public GraphSimulator getSimulator() {
        return simulator;
    }
}
