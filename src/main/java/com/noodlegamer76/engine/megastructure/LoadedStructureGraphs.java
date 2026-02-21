package com.noodlegamer76.engine.megastructure;

import com.noodlegamer76.engine.gui.structure.StructureEditor;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadedStructureGraphs {
    private static final LoadedStructureGraphs INSTANCE = new LoadedStructureGraphs();

    public static LoadedStructureGraphs getInstance() {
        return INSTANCE;
    }

    private final Map<String, Graph> graphs = new HashMap<>();

    private LoadedStructureGraphs() {

    }

    public void loadGraphs() {
        List<Graph> graphs = new GraphSerializer().loadAllStructures();
        graphs.forEach(graph -> this.graphs.put(graph.getName(), graph));
    }

    public Graph getGraph(String name) {
        return graphs.get(name);
    }

    public void addGraph(Graph graph) {
        graphs.put(graph.getName(), graph);
    }

    public List<Graph> getGraphs() {
        return graphs.values().stream().toList();
    }

    public boolean containsGraph(String name) {
        return graphs.containsKey(name);
    }
}
