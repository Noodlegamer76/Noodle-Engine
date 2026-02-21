package com.noodlegamer76.engine.megastructure.structure;

import com.noodlegamer76.engine.megastructure.Node;
import com.noodlegamer76.engine.megastructure.StructMath;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSimulator;
import com.noodlegamer76.engine.megastructure.structure.graph.node.ExecutionContext;
import com.noodlegamer76.engine.megastructure.structure.placers.Placer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.*;

public class StructureDefinition {
    //Integer is node level
    private final TreeMap<Integer, List<StructureExecuter>> structureExecuters =
            new TreeMap<>(Comparator.reverseOrder());
    private int highestNodeLevel = 0;
    private String id;

    public StructureDefinition(String id) {
        this.id = id;
    }

    public Map<Integer, List<StructureExecuter>> getStructureExecuters() {
        return structureExecuters;
    }

    public void addStructureExecuter(StructureExecuter structure) {
        int level = structure.getNodeLevel();
        List<StructureExecuter> list = structureExecuters.computeIfAbsent(level, k -> new ArrayList<>());

        list.add(structure);
        list.sort(Comparator.comparingInt(StructureExecuter::getPriority).reversed());

        if (level > highestNodeLevel) highestNodeLevel = level;
    }

    public int getHighestNodeLevel() {
        return highestNodeLevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
