package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.core.component.ComponentType;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.event.NoodleEngineRegistries;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.Vec3Node;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constant.BlockPosNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constant.ConstantIntNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constant.ResourceLocationNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.executor.PlaceStructureNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.executor.StartNode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.json.JsonValue;

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


    public static RegistryObject<NodeType<? extends Node<?>>> getComponentType(ResourceLocation id) {
        return NODE_TYPES.getEntries().stream().filter(entry -> entry.getId().equals(id)).findFirst().orElse(null);
    }
}
