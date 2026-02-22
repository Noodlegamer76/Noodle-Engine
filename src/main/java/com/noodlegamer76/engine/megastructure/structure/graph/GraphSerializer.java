package com.noodlegamer76.engine.megastructure.structure.graph;

import com.google.gson.*;
import com.noodlegamer76.engine.megastructure.structure.StructureDefinition;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.Structures;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;
import com.noodlegamer76.engine.megastructure.structure.graph.node.NodeType;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinKind;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GraphSerializer {
    private static final String STRUCTURES_FOLDER = "structures/";

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static JsonObject serialize(String definitionName, StructureExecuter executer) {
        JsonObject executerObject = new JsonObject();
        executerObject.addProperty("name", executer.getName());
        executerObject.addProperty("priority", executer.getPriority());
        executerObject.addProperty("nodeLevel", executer.getNodeLevel());
        executerObject.addProperty("id", executer.getId());

        Graph graph = executer.getFunction();
        JsonObject jsonGraph = new JsonObject();
        jsonGraph.addProperty("nextId", graph.getNextIdRaw());

        JsonObject jsonNodes = new JsonObject();

        for (Node<?> node : graph.getNodes().values()) {

            JsonObject nodeJson = new JsonObject();
            nodeJson.addProperty("id", node.getId());
            nodeJson.addProperty("type", node.getRegistry().getId().toString());
            nodeJson.add("data", node.saveData());

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

        executerObject.add("graph", jsonGraph);

        String dir = STRUCTURES_FOLDER + definitionName + "/";
        new File(dir).mkdirs();

        try (FileWriter writer = new FileWriter(dir + executer.getName() + ".json")) {
            gson.toJson(executerObject, writer);
            return executerObject;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static StructureExecuter deserialize(String definitionName, String structureName) {
        String path = STRUCTURES_FOLDER + definitionName + "/" + structureName + ".json";
        try (FileReader reader = new FileReader(path)) {

            JsonObject executerObject =
                    JsonParser.parseReader(reader).getAsJsonObject();

            String name = executerObject.get("name").getAsString();
            JsonObject graphObject = executerObject.getAsJsonObject("graph");
            int priority = executerObject.get("priority").getAsInt();
            int nodeLevel = executerObject.get("nodeLevel").getAsInt();
            int id = executerObject.get("id").getAsInt();

            int nextId = graphObject.get("nextId").getAsInt();

            Graph graph = new Graph(nextId);

            Map<Integer, NodePin> pinsById = new HashMap<>();

            JsonObject jsonNodes = graphObject.getAsJsonObject("nodes");

            for (String nodeIdStr : jsonNodes.keySet()) {

                JsonObject nodeJson =
                        jsonNodes.getAsJsonObject(nodeIdStr);

                int nodeId = nodeJson.get("id").getAsInt();
                String typeLocation = nodeJson.get("type").getAsString();

                ResourceLocation typeLocationId = ResourceLocation.tryParse(typeLocation);
                if (typeLocationId == null) continue;
                RegistryObject<NodeType<? extends Node<?>>> registry = InitNodes.getComponentType(typeLocationId);

                Node<?> node = instantiateNode(registry, nodeId, graph);

                if (nodeJson.has("data")) {
                    JsonObject data = nodeJson.getAsJsonObject("data");
                    if (data != null) {
                        node.loadData(data);
                    }
                }

                node.x = nodeJson.has("x") ? nodeJson.get("x").getAsFloat() : 0f;
                node.y = nodeJson.has("y") ? nodeJson.get("y").getAsFloat() : 0f;

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
                    JsonElement dataTypeElem = pinJson.get("dataType");
                    if (dataTypeElem != null && !dataTypeElem.isJsonNull()) {
                        dataType = Class.forName(dataTypeElem.getAsString());
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

            JsonArray linksArray = graphObject.getAsJsonArray("links");

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

            return new StructureExecuter(priority, name, graph, nodeLevel, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Node<? extends Node<?>> instantiateNode(RegistryObject<NodeType<? extends Node<?>>> registry, int id, Graph graph) {
        NodeType<? extends Node<?>> type = registry.get();
        return type.create(id, graph);
    }

    public static StructureDefinition loadAllExecuters(String definitionName) {
        StructureDefinition definition = new StructureDefinition(definitionName);

        File structuresDir = new File(STRUCTURES_FOLDER + definitionName + "/");
        if (!structuresDir.exists() || !structuresDir.isDirectory()) {
            return definition;
        }

        File[] files = structuresDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return definition;

        for (File file : files) {
            String structureName = file.getName().replace(".json", "");
            StructureExecuter executer = deserialize(definitionName, structureName);
            if (executer != null) {
                definition.addStructureExecuter(executer);
            }
        }

        return definition;
    }

    public static void serializeDefinition(StructureDefinition definition) {
        for (Map.Entry<Integer, List<StructureExecuter>> entry : definition.getStructureExecuters().entrySet()) {
            for (StructureExecuter executer : entry.getValue()) {
                serialize(definition.getId(), executer);
            }
        }
    }

    public static void loadAllDefinitions(boolean isClientSide) {
        File structuresRoot = new File(STRUCTURES_FOLDER);
        if (!structuresRoot.exists() || !structuresRoot.isDirectory()) return;

        File[] definitionFolders = structuresRoot.listFiles(File::isDirectory);
        if (definitionFolders == null) return;

        for (File folder : definitionFolders) {
            StructureDefinition definition = loadAllExecuters(folder.getName());
            Structures.getInstance(isClientSide).addDefinition(definition);
        }
    }

    public static StructureExecuter deserializeExecuterFromBytes(byte[] bytes) {
        try {
            String json = new String(bytes, StandardCharsets.UTF_8);
            JsonObject executerObject = JsonParser.parseString(json).getAsJsonObject();
            return GraphSerializer.deserializeFromJson(executerObject);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static StructureExecuter deserializeFromJson(JsonObject executerObject) throws Exception {
        String name = executerObject.get("name").getAsString();
        JsonObject graphObject = executerObject.getAsJsonObject("graph");
        int priority = executerObject.get("priority").getAsInt();
        int nodeLevel = executerObject.get("nodeLevel").getAsInt();
        int id = executerObject.get("id").getAsInt();

        int nextId = graphObject.get("nextId").getAsInt();
        Graph graph = new Graph(nextId);
        Map<Integer, NodePin> pinsById = new HashMap<>();

        JsonObject jsonNodes = graphObject.getAsJsonObject("nodes");
        for (String nodeIdStr : jsonNodes.keySet()) {
            JsonObject nodeJson = jsonNodes.getAsJsonObject(nodeIdStr);
            int nodeId = nodeJson.get("id").getAsInt();

            ResourceLocation typeLocationId = ResourceLocation.tryParse(nodeJson.get("type").getAsString());
            if (typeLocationId == null) continue;

            RegistryObject<NodeType<? extends Node<?>>> registry = InitNodes.getComponentType(typeLocationId);
            Node<?> node = instantiateNode(registry, nodeId, graph);

            if (nodeJson.has("data")) {
                JsonObject data = nodeJson.getAsJsonObject("data");
                if (data != null) {
                    node.loadData(data);
                }
            }

            node.x = nodeJson.has("x") ? nodeJson.get("x").getAsFloat() : 0f;
            node.y = nodeJson.has("y") ? nodeJson.get("y").getAsFloat() : 0f;

            for (JsonElement elem : nodeJson.getAsJsonArray("pins")) {
                JsonObject pinJson = elem.getAsJsonObject();

                Class<?> dataType = null;
                JsonElement dataTypeElem = pinJson.get("dataType");
                if (dataTypeElem != null && !dataTypeElem.isJsonNull()) {
                    dataType = Class.forName(dataTypeElem.getAsString());
                }

                NodePin pin = new NodePin(
                        pinJson.get("id").getAsInt(),
                        pinJson.get("nodeId").getAsInt(),
                        PinKind.valueOf(pinJson.get("kind").getAsString()),
                        PinCategory.valueOf(pinJson.get("category").getAsString()),
                        dataType,
                        pinJson.get("name").getAsString()
                );
                node.getPins().add(pin);
                pinsById.put(pin.getId(), pin);
            }
            graph.addNode(node);
        }

        JsonArray linksArray = graphObject.getAsJsonArray("links");
        if (linksArray != null) {
            for (JsonElement elem : linksArray) {
                JsonObject linkJson = elem.getAsJsonObject();
                graph.addLink(new Link(
                        linkJson.get("id").getAsInt(),
                        linkJson.get("startPinId").getAsInt(),
                        linkJson.get("endPinId").getAsInt()
                ));
            }
        }

        return new StructureExecuter(priority, name, graph, nodeLevel, id);
    }
}