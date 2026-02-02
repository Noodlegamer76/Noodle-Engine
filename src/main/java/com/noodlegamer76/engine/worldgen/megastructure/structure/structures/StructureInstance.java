package com.noodlegamer76.engine.worldgen.megastructure.structure.structures;

import com.noodlegamer76.engine.worldgen.megastructure.structure.StructureDefinition;
import com.noodlegamer76.engine.worldgen.megastructure.structure.placers.Placer;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureInstance {
    private final StructureDefinition definition;
    private final Map<String, Object> variables = new HashMap<>();
    private final List<Placer> placers = new ArrayList<>();

    public StructureInstance(StructureDefinition definition) {
        this.definition = definition;
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    public <T> T getVariable(String name, Class<T> type) {
        Object value = variables.get(name);
        if (type.isInstance(value)) return type.cast(value);
        else return null;
    }

    public void addPlacer(Placer placer) {
        placers.add(placer);
    }

    public List<Placer> getPlacers() {
        return placers;
    }

    public void generate(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        definition.generate(ctx, this);
    }

    public StructureDefinition getDefinition() {
        return definition;
    }
}
