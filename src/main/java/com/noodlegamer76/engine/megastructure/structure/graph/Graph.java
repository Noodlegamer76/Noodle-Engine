package com.noodlegamer76.engine.megastructure.structure.graph;

import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;

import java.util.*;

public class Graph {
    private String name;

    private int nextId;

    private final Map<Integer, Node<?>> nodes = new HashMap<>();
    private final Map<Integer, NodePin> pins = new HashMap<>();
    private final List<Link> links = new ArrayList<>();
    private final Map<Integer, List<Link>> adjacencyList = new HashMap<>();

    public Graph(String name) {
        this(name, 1);
    }

    public Graph(String name, int nextId) {
        this.name = name;
        this.nextId = nextId;
    }

    public int nextId() {
        return nextId++;
    }

    public int getNextIdRaw() {
        return nextId;
    }

    public void addNode(Node<?> node) {
        nodes.put(node.getId(), node);
        adjacencyList.put(node.getId(), new ArrayList<>());

        for (NodePin pin : node.getPins()) {
            pins.put(pin.getId(), pin);
        }
    }

    public void removeNode(int nodeId) {
        Node<?> node = nodes.remove(nodeId);
        if (node == null) return;

        adjacencyList.remove(nodeId);

        for (NodePin pin : node.getPins()) {
            pins.remove(pin.getId());
            links.removeIf(link ->
                    link.getStartPinId() == pin.getId() ||
                            link.getEndPinId() == pin.getId());
        }

        adjacencyList.values().forEach(list ->
                list.removeIf(link -> node.getPins().stream()
                        .anyMatch(pin -> link.getEndPinId() == pin.getId())));
    }

    public void addLink(Link link) {
        NodePin start = pins.get(link.getStartPinId());
        NodePin end = pins.get(link.getEndPinId());

        if (start == null || end == null) {
            throw new IllegalStateException("Invalid link pins.");
        }

        links.add(link);
        adjacencyList.get(start.getNodeId()).add(link);
    }

    public void removeLink(int linkId) {
        links.stream()
                .filter(l -> l.getId() == linkId)
                .findFirst()
                .ifPresent(link -> {
                    NodePin start = pins.get(link.getStartPinId());
                    if (start != null) {
                        adjacencyList.get(start.getNodeId()).remove(link);
                    }
                });

        links.removeIf(l -> l.getId() == linkId);
    }

    public Node<?> getNode(int nodeId) {
        return nodes.get(nodeId);
    }

    public Map<Integer, Node<?>> getNodes() {
        return Collections.unmodifiableMap(nodes);
    }

    public NodePin getPin(int pinId) {
        return pins.get(pinId);
    }

    public List<Link> getLinks() {
        return Collections.unmodifiableList(links);
    }

    public Optional<NodePin> getConnectedOutput(NodePin inputPin) {

        return links.stream()
                .filter(link -> link.getEndPinId() == inputPin.getId())
                .map(link -> pins.get(link.getStartPinId()))
                .findFirst();
    }

    public List<NodePin> getConnectedInputs(NodePin outputPin) {

        List<NodePin> connected = new ArrayList<>();

        for (Link link : links) {
            if (link.getStartPinId() == outputPin.getId()) {
                connected.add(pins.get(link.getEndPinId()));
            }
        }

        return connected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Link> getLinksFrom(Node<?> node) {
        return Collections.unmodifiableList(
                adjacencyList.getOrDefault(node.getId(), Collections.emptyList())
        );
    }

    public Map<Integer, List<Link>> getAdjacencyList() {
        return adjacencyList;
    }
}