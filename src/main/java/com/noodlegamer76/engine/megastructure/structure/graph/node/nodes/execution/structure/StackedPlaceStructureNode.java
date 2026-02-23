package com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.execution.structure;

import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.StructureInstance;
import com.noodlegamer76.engine.megastructure.structure.StructureUtils;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSimulator;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;

public class StackedPlaceStructureNode extends ExecutionNode<StackedPlaceStructureNode> {

    public StackedPlaceStructureNode(int id, Graph graph) {
        super(id, graph, InitNodes.STACKED_PLACE_STRUCTURE, "Place Stacked Structure", "Execution/Structure");
    }

    @Override
    public void execute(StructureExecuter executer, ExecutionContext context, StructureInstance instance) {
        ResourceLocation location = resolve(context, "Structure Location", ResourceLocation.class);
        BlockPos position = resolve(context, "Position", BlockPos.class);

        if (location == null || position == null) return;

        FeaturePlaceContext<NoneFeatureConfiguration> ctx = instance.getContext();

        MinecraftServer server = ctx.level().getServer();
        if (server == null) return;

        StructureTemplateManager structureManager = server.getStructureManager();

        Optional<StructureTemplate> templateOptional = structureManager.get(location);

        templateOptional.ifPresent((template -> {
            BlockPos chunkMin = ctx.origin();
            BlockPos chunkMax = chunkMin.offset(15, ctx.level().getHeight() - 1, 15);

            Direction direction = resolve(context, "Direction", Direction.class);
            Integer count = resolve(context, "Count", Integer.class);
            if (direction == null || count == null) {
                direction = Direction.NORTH;
                count = 1;
            }

            RandomSource random = instance.getRandom(context);
            for (int i = 0; i < count; i ++) {
                BlockPos moved = position.relative(direction, i);

                StructureUtils.placeTemplateInChunk(ctx.level(), template, moved, chunkMin, chunkMax, new StructurePlaceSettings(), random, 2);
            }
        }));
    }

    @Override
    protected void renderContents() {

    }

    @Override
    public void initPins() {
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.EXECUTION, null, "Execution Point"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.OUTPUT , PinCategory.EXECUTION, null, "Execution Point"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, ResourceLocation.class, "Structure Location"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, BlockPos.class, "Position"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Direction.class, "Direction"));
        addPin(new NodePin(getGraph().nextId(), getId(), PinKind.INPUT, PinCategory.DATA, Integer.class, "Count"));
    }
}