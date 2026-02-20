package com.noodlegamer76.engine.worldgen.megastructure.structure;

import com.noodlegamer76.engine.NoodleEngine;
import com.noodlegamer76.engine.worldgen.megastructure.structure.placers.Placer;
import com.noodlegamer76.engine.worldgen.megastructure.structure.context.GenVar;
import com.noodlegamer76.engine.worldgen.megastructure.structure.context.GenVarSerializer;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureInstance {
    private final StructureDefinition definition;
    private final List<Placer> placers = new ArrayList<>();
    private final Map<String, GenVar<?>> genVars = new HashMap<>();

    public StructureInstance(StructureDefinition definition) {
        this.definition = definition;
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

    public void addGenVar(GenVar<?> genVar) {
        genVars.put(genVar.getName(), genVar);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public <T> GenVar<T> getGenVar(String name, GenVarSerializer<T> serializer) {
        GenVar<?> raw = genVars.get(name);
        if (raw == null) return null;

        if (raw.getSerializer() != serializer) {
            NoodleEngine.LOGGER.error("Serializer mismatch for GenVar: " + name);
            return null;
        }

        return (GenVar<T>) raw;
    }

}
