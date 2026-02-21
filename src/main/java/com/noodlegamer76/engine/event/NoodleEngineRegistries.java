package com.noodlegamer76.engine.event;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.core.component.ComponentType;
import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;
import com.noodlegamer76.engine.megastructure.structure.graph.node.NodeType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = NoodleEngine.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NoodleEngineRegistries {
    public static final ResourceLocation COMPONENT_TYPE = ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "component_type");
    public static final ResourceLocation NODE_TYPE = ResourceLocation.fromNamespaceAndPath(NoodleEngine.MODID, "node_type");

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.create(new RegistryBuilder<ComponentType<?>>()
                .setName(COMPONENT_TYPE));
        event.create(new RegistryBuilder<NodeType<? extends Node>>()
                .setName(NODE_TYPE));
    }
}
