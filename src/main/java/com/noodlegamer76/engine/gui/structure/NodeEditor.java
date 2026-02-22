package com.noodlegamer76.engine.gui.structure;

import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSerializer;
import com.noodlegamer76.engine.megastructure.structure.graph.Link;
import com.noodlegamer76.engine.megastructure.structure.graph.node.InitNodes;
import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;
import com.noodlegamer76.engine.megastructure.structure.graph.node.NodeType;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.NodePin;
import com.noodlegamer76.engine.megastructure.structure.graph.pin.PinCategory;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.ImNodesContext;
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImInt;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NodeEditor {
    private final GraphSerializer serializer = new GraphSerializer();
    private StructureExecuter currentStructure;
    private final ImNodesContext context;
    private boolean pendingPositionReset = false;

    private Node<?> draggingNodePreview;

    private NodeType<? extends Node<?>> draggingNodeType = null;

    public NodeEditor() {
        context = ImNodes.editorContextCreate();
    }

    public void render(GuiGraphics guiGraphics) {

        int windowFlags =
                ImGuiWindowFlags.MenuBar |
                        ImGuiWindowFlags.NoDecoration |
                        ImGuiWindowFlags.NoSavedSettings |
                        ImGuiWindowFlags.NoResize |
                        ImGuiWindowFlags.NoCollapse |
                        ImGuiWindowFlags.NoMove |
                        ImGuiWindowFlags.NoBringToFrontOnFocus |
                        ImGuiWindowFlags.NoNavFocus;

        ImGui.setNextWindowPos(ImGui.getMainViewport().getWorkPosX(),
                ImGui.getMainViewport().getWorkPosY());

        ImGui.setNextWindowSize(ImGui.getMainViewport().getWorkSizeX(),
                ImGui.getMainViewport().getWorkSizeY());

        ImGui.begin("Structure Editor", windowFlags);

        float totalWidth = ImGui.getContentRegionAvailX();
        float totalHeight = ImGui.getContentRegionAvailY();

        float leftWidth = 250f;
        float rightWidth = 300f;

        ImGui.beginChild("LeftPanel", leftWidth, totalHeight, true);
        renderLeftPanel();
        ImGui.endChild();

        ImGui.sameLine();

        ImGui.beginChild("NodeEditorPanel",
                totalWidth - leftWidth - rightWidth,
                totalHeight,
                false);

        renderNodeEditor();
        handleNodeDrop();
        ImGui.endChild();

        ImGui.sameLine();

        ImGui.beginChild("RightPanel", rightWidth, totalHeight, true);
        ImGui.text("Inspector");
        ImGui.endChild();

        ImGui.end();
    }

    private void renderLeftPanel() {
        ImGui.text("Node Palette");
        ImGui.separator();

        Map<String, Object> tree = new TreeMap<>();

        InitNodes.NODE_TYPES.getEntries().forEach(regObj -> {
            NodeType<? extends Node<?>> type = regObj.get();
            Node<?> preview = type.create(-1, currentStructure.getFunction());
            preview.initPins();

            String[] parts = preview.getCategoryPath().split("/");
            insertIntoTree(tree, parts, 0, type);
        });

        renderTree(tree);
    }

    @SuppressWarnings("unchecked")
    private void insertIntoTree(Map<String, Object> node, String[] parts, int depth, NodeType<? extends Node<?>> type) {
        String key = parts[depth];
        if (depth == parts.length - 1) {
            node.computeIfAbsent(key, k -> new ArrayList<NodeType<? extends Node<?>>>());
            Object existing = node.get(key);
            if (existing instanceof List) {
                ((List<NodeType<? extends Node<?>>>) existing).add(type);
            } else if (existing instanceof Map) {
                ((Map<String, Object>) existing)
                        .computeIfAbsent("", k -> new ArrayList<NodeType<? extends Node<?>>>());
                ((List<NodeType<? extends Node<?>>>) ((Map<String, Object>) existing).get("")).add(type);
            }
        } else {
            node.computeIfAbsent(key, k -> new TreeMap<String, Object>());
            Object existing = node.get(key);
            if (existing instanceof Map) {
                insertIntoTree((Map<String, Object>) existing, parts, depth + 1, type);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void renderTree(Map<String, Object> tree) {
        for (Map.Entry<String, Object> entry : tree.entrySet()) {
            String label = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof List) {
                if (ImGui.collapsingHeader(label)) {
                    renderNodeButtons((List<NodeType<? extends Node<?>>>) value);
                }
            } else if (value instanceof Map) {
                if (ImGui.collapsingHeader(label)) {
                    ImGui.indent();
                    renderTree((Map<String, Object>) value);
                    ImGui.unindent();
                }
            }
        }
    }

    private void renderNodeButtons(List<NodeType<? extends Node<?>>> types) {
        for (NodeType<? extends Node<?>> type : types) {
            Node<?> preview = type.create(-1, currentStructure.getFunction());
            preview.initPins();

            ImGui.button(preview.getDisplayName());
            if (ImGui.isItemActive() && ImGui.isMouseDragging(0)) {
                draggingNodeType = type;
                draggingNodePreview = preview;
            }
        }
    }

    private void handleNodeDrop() {
        if (draggingNodeType != null) {
            float mouseX = ImGui.getMousePosX();
            float mouseY = ImGui.getMousePosY();

            ImGui.setNextWindowPos(mouseX, mouseY);
            ImGui.begin("NodePreview", ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoInputs);
            ImGui.text(draggingNodeType.create(-1, currentStructure.getFunction()).getDisplayName());
            draggingNodePreview.initPins();
            ImGui.end();

            if (!ImGui.isMouseDown(0)) {
                int id = currentStructure.getFunction().nextId();
                Node<?> node = draggingNodeType.create(id, currentStructure.getFunction());
                node.initPins();
                node.x = mouseX;
                node.y = mouseY;
                currentStructure.getFunction().addNode(node);
                ImNodes.setNodeScreenSpacePos(node.getId(), node.x, node.y);

                draggingNodeType = null;
            }
        }
    }

    private void renderNodeEditor() {
        ImNodes.editorContextSet(context);
        ImNodes.beginNodeEditor();

        for (Node<?> node : currentStructure.getFunction().getNodes().values()) {
            node.render();
        }

        if (pendingPositionReset) {
            for (Node<?> node : currentStructure.getFunction().getNodes().values()) {
                ImNodes.setNodeEditorSpacePos(node.getId(), node.x, node.y);
            }
            pendingPositionReset = false;
        }

        for (Link link : currentStructure.getFunction().getLinks()) {
            ImNodes.link(link.getId(), link.getStartPinId(), link.getEndPinId());
        }

        //Context Menu
        if (ImGui.isMouseClicked(ImGuiMouseButton.Right) && ImGui.isWindowHovered()) {
            ImGui.openPopup("context_menu");
        }

        if (ImGui.beginPopup("context_menu")) {
            ImGui.text("Context Menu");

            if (ImGui.button("Delete Selected")) {
                clearSelection();
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }

        ImNodes.miniMap(0.2f, ImNodesMiniMapLocation.TopRight);
        ImNodes.endNodeEditor();

        handleLinkCreation();
    }

    private void handleLinkCreation() {
        ImInt startAttr = new ImInt();
        ImInt endAttr = new ImInt();

        if (ImNodes.isLinkCreated(startAttr, endAttr)) {
            NodePin startPin = currentStructure.getFunction().getPin(startAttr.get());
            NodePin endPin = currentStructure.getFunction().getPin(endAttr.get());

            if (!isLinkValid(startPin, endPin)) {
                return;
            }

            int linkId = currentStructure.getFunction().nextId();
            currentStructure.getFunction().addLink(new Link(linkId, startAttr.get(), endAttr.get()));
        }

        ImInt linkId = new ImInt();
        if (ImNodes.isLinkDestroyed(linkId)) {
            currentStructure.getFunction().removeLink(linkId.get());
        }
    }

    private boolean isLinkValid(NodePin start, NodePin end) {
        if (start == null || end == null) return false;

        if (start.getKind() == end.getKind()) return false;

        if (start.getCategory() != end.getCategory()) return false;

        if (start.getCategory() == PinCategory.DATA) {
            Class<?> startType = start.getDataType();
            Class<?> endType = end.getDataType();

            if (startType == null || endType == null) return false;
            return endType.isAssignableFrom(startType);
        }

        return true;
    }

    public StructureExecuter getCurrentStructure() {
        return currentStructure;
    }

    public void setCurrentStructure(StructureExecuter currentStructure) {
        this.currentStructure = currentStructure;
        pendingPositionReset = true;
    }

    public GraphSerializer getSerializer() {
        return serializer;
    }


    public void clearSelection() {
        List<Link> selectedLink = getSelectedLink();
        List<Node<?>> selectedNodes = getSelectedNodes();

        for (Node<?> node: selectedNodes) {
            selectedLink.addAll(currentStructure.getFunction().getLinksFrom(node));
        }

        for (Map.Entry<Integer, List<Link>> edges: currentStructure.getFunction().getAdjacencyList().entrySet()) {
            edges.getValue().removeAll(selectedLink);
        }

        for (Link link: selectedLink) {
            currentStructure.getFunction().removeLink(link.getId());
        }

        for (Node node: selectedNodes) {
            currentStructure.getFunction().removeNode(node.getId());
        }

        ImNodes.clearLinkSelection();
        ImNodes.clearNodeSelection();
    }

    public List<Node<?>> getSelectedNodes() {
        List<Node<?>> nodes = new ArrayList<>();
        for (Node<?> node : currentStructure.getFunction().getNodes().values()) {
            if (ImNodes.isNodeSelected(node.getId())) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    public List<Link> getSelectedLink() {
        List<Link> links = new ArrayList<>();
        for (Link link : currentStructure.getFunction().getLinks()) {
            if (ImNodes.isLinkSelected(link.getId())) {
                links.add(link);
            }
        }
        return links;
    }

    public void setNodePositions() {
        if (currentStructure == null) return;
        for (Node<?> node : currentStructure.getFunction().getNodes().values()) {
            ImVec2 pos = new ImVec2();
            ImNodes.getNodeEditorSpacePos(node.getId(), pos);
            node.x = pos.x;
            node.y = pos.y;
        }
    }
}
