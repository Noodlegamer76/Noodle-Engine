package com.noodlegamer76.engine.megastructure.structure;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Structures {
    private static final Structures CLIENT_INSTANCE = new Structures();
    private static final Structures SERVER_INSTANCE = new Structures();

    public static Structures getInstance(boolean isClientSide) {
        return isClientSide ? CLIENT_INSTANCE : SERVER_INSTANCE;
    }

    private final Map<String, StructureDefinition> definitions = new HashMap<>();
    private int nextId;

    public void addDefinition(StructureDefinition definition) {
        definitions.put(definition.getId(), definition);
    }

    public void clearDefinitions() {
        definitions.clear();
    }

    public Map<String, StructureDefinition> getDefinitions() {
        return definitions;
    }

    public int nextId() {
        return nextId++;
    }
}