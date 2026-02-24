package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.event.NoodleEngineRegistries;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.GetLocalVariable;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.SetLocalVariable;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.constants.*;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.conversion.BlockPosToInteger;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.logic.*;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.math.*;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.position.BlockPosAdd;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.position.BlockPosAddNodePos;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.conversion.IntegerToBlockPos;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.position.BlockPosSubtract;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.position.BlockPosSubtractNodePos;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.data.random.RandomInt;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.execution.structure.PlaceStructureNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.execution.structure.StackedPlaceStructureNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.flow.IfNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.flow.StartNode;
import com.noodlegamer76.engine.megastructure.structure.graph.node.nodes.flow.WhileLoopNode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class InitNodes {
    public static final DeferredRegister<NodeType<? extends Node<?>>> NODE_TYPES = DeferredRegister.create(NoodleEngineRegistries.NODE_TYPE, NoodleEngine.MODID);

    public static final RegistryObject<NodeType<ConstantIntNode>> INT_CONSTANT =
            NODE_TYPES.register("value_node", () -> new NodeType<>(ConstantIntNode::new));

    public static final RegistryObject<NodeType<DirectionConstant>> DIRECTION_CONSTANT =
            NODE_TYPES.register("direction_constant", () -> new NodeType<>(DirectionConstant::new));

    public static final RegistryObject<NodeType<PlaceStructureNode>> PLACE_STRUCTURE =
            NODE_TYPES.register("place_structure", () -> new NodeType<>(PlaceStructureNode::new));

    public static final RegistryObject<NodeType<StackedPlaceStructureNode>> STACKED_PLACE_STRUCTURE =
            NODE_TYPES.register("stacked_place_structure", () -> new NodeType<>(StackedPlaceStructureNode::new));

    public static final RegistryObject<NodeType<ResourceLocationNode>> RESOURCE_LOCATION =
            NODE_TYPES.register("resource_location", () -> new NodeType<>(ResourceLocationNode::new));

    public static final RegistryObject<NodeType<BlockPosNode>> BLOCK_POS =
            NODE_TYPES.register("block_pos", () -> new NodeType<>(BlockPosNode::new));

    public static final RegistryObject<NodeType<StartNode>> START =
            NODE_TYPES.register("start", () -> new NodeType<>(StartNode::new));

    public static final RegistryObject<NodeType<IntegerToBlockPos>> INT_TO_BLOCK_POS =
            NODE_TYPES.register("int_to_block_pos", () -> new NodeType<>(IntegerToBlockPos::new));

    public static final RegistryObject<NodeType<BlockPosToInteger>> BLOCK_POS_TO_INT =
            NODE_TYPES.register("block_pos_to_int", () -> new NodeType<>(BlockPosToInteger::new));

    public static final RegistryObject<NodeType<RandomInt>> RANDOM_INT =
            NODE_TYPES.register("random_int", () -> new NodeType<>(RandomInt::new));

    public static final RegistryObject<NodeType<BlockPosAddNodePos>> BLOCK_POS_ADD_NODE_POS =
            NODE_TYPES.register("block_pos_add_node_pos", () -> new NodeType<>(BlockPosAddNodePos::new));

    public static final RegistryObject<NodeType<BlockPosSubtractNodePos>> BLOCK_POS_SUBTRACT_NODE_POS =
            NODE_TYPES.register("block_pos_subtract_node_pos", () -> new NodeType<>(BlockPosSubtractNodePos::new));

    public static final RegistryObject<NodeType<IntegerAdd>> INTEGER_ADD =
            NODE_TYPES.register("integer_add", () -> new NodeType<>(IntegerAdd::new));

    public static final RegistryObject<NodeType<IntegerSubtract>> INTEGER_SUBTRACT =
            NODE_TYPES.register("integer_subtract", () -> new NodeType<>(IntegerSubtract::new));

    public static final RegistryObject<NodeType<IntegerMultiply>> INTEGER_MULTIPLY =
            NODE_TYPES.register("integer_multiply", () -> new NodeType<>(IntegerMultiply::new));

    public static final RegistryObject<NodeType<IntegerDivide>> INTEGER_DIVIDE =
            NODE_TYPES.register("integer_divide", () -> new NodeType<>(IntegerDivide::new));

    public static final RegistryObject<NodeType<BlockPosAdd>> BLOCK_POS_ADD =
            NODE_TYPES.register("block_pos_add", () -> new NodeType<>(BlockPosAdd::new));

    public static final RegistryObject<NodeType<BlockPosSubtract>> BLOCK_POS_SUBTRACT =
            NODE_TYPES.register("block_pos_subtract", () -> new NodeType<>(BlockPosSubtract::new));

    public static final RegistryObject<NodeType<IntegerModulo>> INTEGER_MODULO =
            NODE_TYPES.register("integer_modulo", () -> new NodeType<>(IntegerModulo::new));

    public static final RegistryObject<NodeType<IntegerPower>> INTEGER_POWER =
            NODE_TYPES.register("integer_power", () -> new NodeType<>(IntegerPower::new));

    public static final RegistryObject<NodeType<BooleanConstantNode>> BOOLEAN_CONSTANT =
            NODE_TYPES.register("boolean_constant", () -> new NodeType<>(BooleanConstantNode::new));

    public static final RegistryObject<NodeType<BooleanAnd>> BOOLEAN_AND =
            NODE_TYPES.register("boolean_and", () -> new NodeType<>(BooleanAnd::new));

    public static final RegistryObject<NodeType<BooleanOr>> BOOLEAN_OR =
            NODE_TYPES.register("boolean_or", () -> new NodeType<>(BooleanOr::new));

    public static final RegistryObject<NodeType<BooleanNot>> BOOLEAN_NOT =
            NODE_TYPES.register("boolean_not", () -> new NodeType<>(BooleanNot::new));

    public static final RegistryObject<NodeType<IntegerGreaterThan>> INTEGER_GREATER_THAN =
            NODE_TYPES.register("integer_greater_than", () -> new NodeType<>(IntegerGreaterThan::new));

    public static final RegistryObject<NodeType<IntegerLessThan>> INTEGER_LESS_THAN =
            NODE_TYPES.register("integer_less_than", () -> new NodeType<>(IntegerLessThan::new));

    public static final RegistryObject<NodeType<IntegerEquals>> INTEGER_EQUALS =
            NODE_TYPES.register("integer_equals", () -> new NodeType<>(IntegerEquals::new));

    public static final RegistryObject<NodeType<IfNode>> IF =
            NODE_TYPES.register("if", () -> new NodeType<>(IfNode::new));

    public static final RegistryObject<NodeType<WhileLoopNode>> WHILE =
            NODE_TYPES.register("while", () -> new NodeType<>(WhileLoopNode::new));

    public static final RegistryObject<NodeType<GetLocalVariable>> GET_LOCAL_VAR =
            NODE_TYPES.register("get_local_var", () -> new NodeType<>(GetLocalVariable::new));

    public static final RegistryObject<NodeType<SetLocalVariable>> SET_LOCAL_VAR =
            NODE_TYPES.register("set_local_var", () -> new NodeType<>(SetLocalVariable::new));


    public static RegistryObject<NodeType<? extends Node<?>>> getComponentType(ResourceLocation id) {
        return NODE_TYPES.getEntries().stream().filter(entry -> entry.getId().equals(id)).findFirst().orElse(null);
    }
}
