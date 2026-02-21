package com.noodlegamer76.engine.megastructure.structure.graph;

import com.google.gson.*;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;
import com.noodlegamer76.engine.megastructure.structure.graph.node.NodeType;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class GraphSerializer {
    private static final String STRUCTURES_FOLDER = "structures/";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public void serialize(Graph graph) {
        JsonObject jsonGraph = new JsonObject();
        jsonGraph.addProperty("name", graph.getName());
        jsonGraph.addProperty("nextId", graph.getNextIdRaw());

        JsonObject jsonNodes = new JsonObject();

        for (Node<?> node : graph.getNodes().values()) {

            JsonObject nodeJson = new JsonObject();
            nodeJson.addProperty("id", node.getId());
            nodeJson.addProperty("type", node.getRegistry().getId().toString());

            nodeJson.addProperty("x", node.x);
            nodeJson.addProperty("y", node.y);

            JsonArray pinsArray = new JsonArray();

            for (NodePin pin : node.getPins()) {
                JsonObject pinJson = new JsonObject();
                pinJson.addProperty("id", pin.getId());
                pinJson.addProperty("name", pin.getDisplayName());
                pinJson.addProperty("nodeId", pin.getNodeId());
                pinJson.addProperty("kind", pin.getKind().name());
                pinJson.addProperty("category", pin.getCategory().name());
                pinJson.addProperty("dataType",
                        pin.getDataType() != null ? pin.getDataType().getName() : null);

                pinsArray.add(pinJson);
            }

            nodeJson.add("pins", pinsArray);
            jsonNodes.add(String.valueOf(node.getId()), nodeJson);
        }

        jsonGraph.add("nodes", jsonNodes);

        JsonArray linksArray = new JsonArray();

        for (Link link : graph.getLinks()) {
            JsonObject linkJson = new JsonObject();
            linkJson.addProperty("id", link.getId());
            linkJson.addProperty("startPinId", link.getStartPinId());
            linkJson.addProperty("endPinId", link.getEndPinId());
            linksArray.add(linkJson);
        }

        jsonGraph.add("links", linksArray);

        new File(STRUCTURES_FOLDER).mkdirs();

        try (FileWriter writer =
                     new FileWriter(STRUCTURES_FOLDER + graph.getName() + ".json")) {
            gson.toJson(jsonGraph, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Graph deserialize(String structureName) {
        try (FileReader reader =
                     new FileReader(STRUCTURES_FOLDER + structureName + ".json")) {

            JsonObject jsonGraph =
                    JsonParser.parseReader(reader).getAsJsonObject();

            String name = jsonGraph.get("name").getAsString();
            int nextId = jsonGraph.get("nextId").getAsInt();

            Graph graph = new Graph(name, nextId);

            Map<Integer, NodePin> pinsById = new HashMap<>();

            JsonObject jsonNodes = jsonGraph.getAsJsonObject("nodes");

            for (String nodeIdStr : jsonNodes.keySet()) {

                JsonObject nodeJson =
                        jsonNodes.getAsJsonObject(nodeIdStr);

                int nodeId = nodeJson.get("id").getAsInt();
                String typeLocation = nodeJson.get("type").getAsString();

                ResourceLocation typeLocationId = ResourceLocation.tryParse(typeLocation);
                if (typeLocationId == null) continue;
                RegistryObject<NodeType<? extends Node<?>>> registry = InitNodes.getComponentType(typeLocationId);

                Node<?> node = instantiateNode(registry, nodeId, graph);

                node.x = nodeJson.has("x") ? nodeJson.get("x").getAsInt() : 0;
                node.y = nodeJson.has("y") ? nodeJson.get("y").getAsInt() : 0;

                JsonArray pinsArray = nodeJson.getAsJsonArray("pins");

                for (JsonElement elem : pinsArray) {

                    JsonObject pinJson = elem.getAsJsonObject();

                    int pinId = pinJson.get("id").getAsInt();
                    int nodeOwner = pinJson.get("nodeId").getAsInt();

                    String pinDisplayName =
                            pinJson.get("name").getAsString();

                    PinKind kind =
                            PinKind.valueOf(pinJson.get("kind").getAsString());

                    PinCategory category =
                            PinCategory.valueOf(pinJson.get("category").getAsString());

                    Class<?> dataType = null;
                    if (!pinJson.get("dataType").isJsonNull()) {
                        dataType =
                                Class.forName(pinJson.get("dataType").getAsString());
                    }

                    NodePin pin = new NodePin(
                            pinId,
                            nodeOwner,
                            kind,
                            category,
                            dataType,
                            pinDisplayName
                    );

                    node.getPins().add(pin);
                    pinsById.put(pinId, pin);
                }

                graph.addNode(node);
            }

            JsonArray linksArray = jsonGraph.getAsJsonArray("links");

            if (linksArray != null) {
                for (JsonElement elem : linksArray) {

                    JsonObject linkJson = elem.getAsJsonObject();

                    Link link = new Link(
                            linkJson.get("id").getAsInt(),
                            linkJson.get("startPinId").getAsInt(),
                            linkJson.get("endPinId").getAsInt()
                    );

                    graph.addLink(link);
                }
            }

            return graph;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Node<? extends Node<?>> instantiateNode(RegistryObject<NodeType<? extends Node<?>>> registry, int id, Graph graph) {
        NodeType<? extends Node<?>> type = registry.get();
        return type.create(id, graph);
    }

    public List<Graph> loadAllStructures() {
        List<Graph> graphs = new ArrayList<>();
        File folder = new File(STRUCTURES_FOLDER);

        if (!folder.exists() || !folder.isDirectory()) {
            return graphs;
        }

        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json"));

        if (files == null) return graphs;

        for (File file : files) {
            Graph graph =
                    deserialize(file.getName().replaceFirst("\\.json$", ""));
            if (graph != null) {
                graphs.add(graph);
            }
        }

        return graphs;
    }
}