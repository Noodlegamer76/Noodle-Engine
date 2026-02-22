package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.event.NoodleEngineRegistries;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants.Vec3Node;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants.BlockPosNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants.ConstantIntNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants.ResourceLocationNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.position.BlockPosWorldToNodeSpace;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.conversion.IntegerToBlockPos;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.random.RandomInt;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.execution.structure.PlaceStructureNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.flow.StartNode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitNodes {
    public static final DeferredRegister<NodeType<? extends Node<?>>> NODE_TYPES = DeferredRegister.create(NoodleEngineRegistries.NODE_TYPE, NoodleEngine.MODID);

    public static final RegistryObject<NodeType<ConstantIntNode>> INT_CONSTANT =
            NODE_TYPES.register("value_node", () -> new NodeType<>(ConstantIntNode::new));

    public static final RegistryObject<NodeType<Vec3Node>> VEC3_NODE =
            NODE_TYPES.register("vec3_node", () -> new NodeType<>(Vec3Node::new));

    public static final RegistryObject<NodeType<PlaceStructureNode>> PLACE_STRUCTURE =
            NODE_TYPES.register("place_structure", () -> new NodeType<>(PlaceStructureNode::new));

    public static final RegistryObject<NodeType<ResourceLocationNode>> RESOURCE_LOCATION =
            NODE_TYPES.register("resource_location", () -> new NodeType<>(ResourceLocationNode::new));

    public static final RegistryObject<NodeType<BlockPosNode>> BLOCK_POS =
            NODE_TYPES.register("block_pos", () -> new NodeType<>(BlockPosNode::new));

    public static final RegistryObject<NodeType<StartNode>> START =
            NODE_TYPES.register("start", () -> new NodeType<>(StartNode::new));

    public static final RegistryObject<NodeType<IntegerToBlockPos>> INT_TO_BLOCK_POS =
            NODE_TYPES.register("int_to_block_pos", () -> new NodeType<>(IntegerToBlockPos::new));

    public static final RegistryObject<NodeType<RandomInt>> RANDOM_INT =
            NODE_TYPES.register("random_int", () -> new NodeType<>(RandomInt::new));

    public static final RegistryObject<NodeType<BlockPosWorldToNodeSpace>> BLOCK_POS_WORLD_TO_NODE_SPACE =
            NODE_TYPES.register("block_pos_world_to_node_space", () -> new NodeType<>(BlockPosWorldToNodeSpace::new));


    public static RegistryObject<NodeType<? extends Node<?>>> getComponentType(ResourceLocation id) {
        return NODE_TYPES.getEntries().stream().filter(entry -> entry.getId().equals(id)).findFirst().orElse(null);
    }
}
