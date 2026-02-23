package com.noodlegamer76.engine.megastructure.structure.graph.node;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVar;
import com.noodlegamer76.engine.megastructure.structure.variables.GenVarSerializer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionContext {
    private final Map<Integer, List<GenVar<?>>> localVariables = new HashMap<>();
    private final Map<String, GenVar<?>> namedLocalVariables = new HashMap<>();
    private final Map<String, GenVar<?>> globalVariables = new HashMap<>();

    public void addGlobalVar(GenVar<?> genVar) {
        globalVariables.put(genVar.getName(), genVar);
    }

    public void addCachedVar(int id, GenVar<?> genVar) {
        List<GenVar<?>> cachedVars = localVariables.computeIfAbsent(id, k -> new ArrayList<>());
        cachedVars.add(genVar);
    }

    public void setLocalVar(String name, GenVar<?> var) {
        namedLocalVariables.put(name, var);
    }

    @Nullable
    public GenVar<?> getLocalVar(String name) {
        return namedLocalVariables.get(name);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> GenVar<T> getGlobalVar(String name, Class<T> clazz) {
        GenVar<?> raw = globalVariables.get(name);
        if (raw == null) return null;

        if (raw.getClazz() != clazz) {
            NoodleEngine.LOGGER.error("Serializer mismatch for GenVar: " + name);
            return null;
        }

        return (GenVar<T>) raw;
    }

    public void invalidateCachedVar(int id) {
        localVariables.remove(id);
    }

    @Nullable
    public List<GenVar<?>> getLocalVars(int id) {
        return localVariables.get(id);
    }

    public void clear() {
        localVariables.clear();
        namedLocalVariables.clear();
    }
}