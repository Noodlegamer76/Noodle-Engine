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

    public StructureInstance(StructureDefinition definition) {
        this.definition = definition;
    }

    public void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        for (List<StructureExecuter> executerList : definition.getStructureExecuters().values()) {
            for (StructureExecuter executer : executerList) {
                processExecuterForNodes(ctx, executer);
            }
        }
    }

    private void processExecuterForNodes(FeaturePlaceContext<NoneFeatureConfiguration> ctx, StructureExecuter executer) {
        int level = executer.getNodeLevel();
        Node centerNode = new Node(ctx.origin().getX(), ctx.origin().getZ(), level);
        List<Node> nodes = StructMath.get3x3Nodes(centerNode);

        for (Node node : nodes) {
            ExecutionContext nodeContext = createExecutionContext(ctx, node, executer);
            simulator.run(executer.getFunction(), nodeContext);
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

}
