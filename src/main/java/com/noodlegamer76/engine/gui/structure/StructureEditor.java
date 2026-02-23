package com.noodlegamer76.engine.gui.structure;

import com.noodlegamer76.engine.megastructure.structure.StructureDefinition;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.Structures;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSerializer;
import com.noodlegamer76.engine.megastructure.structure.graph.InspectorVariable;
import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;
import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class StructureEditor {
    private static final StructureEditor INSTANCE = new StructureEditor();

    public static StructureEditor getInstance() {
        return INSTANCE;
    }

    private boolean nodeEditorOpen = false;
    private final NodeEditor nodeEditor;
    private final StructureVisualEditor visualEditor;
    private final Toolbar toolbar;
    private StructureDefinition structureDefinition;

    private StructureEditor() {
        ImNodes.createContext();
        nodeEditor = new NodeEditor();
        visualEditor = new StructureVisualEditor();
        toolbar = new Toolbar();
        structureDefinition = new StructureDefinition("Untitled");
    }

    public void render(GuiGraphics guiGraphics) {
        toolbar.render(this);

        if (nodeEditorOpen) {
            nodeEditor.render(guiGraphics);
        } else {
            visualEditor.render();
        }

        renderInspector();
    }

    public void renderInspector() {
        float inspectorWidth = 500;
        float workSizeX = ImGui.getMainViewport().getWorkSizeX();
        float workSizeY = ImGui.getMainViewport().getWorkSizeY();
        float workPosX = ImGui.getMainViewport().getWorkPosX();
        float workPosY = ImGui.getMainViewport().getWorkPosY();

        ImGui.setNextWindowPos(workPosX + workSizeX - inspectorWidth, workPosY);
        ImGui.setNextWindowSize(inspectorWidth, workSizeY);

        ImGui.begin("Inspector",
                ImGuiWindowFlags.NoResize |
                        ImGuiWindowFlags.NoMove |
                        ImGuiWindowFlags.NoCollapse |
                        ImGuiWindowFlags.NoBringToFrontOnFocus);

        if (structureDefinition != null) {
            ImGui.text(structureDefinition.getId());
            ImGui.separator();

            for (List<StructureExecuter> executerList : structureDefinition.getStructureExecuters().values()) {
                for (StructureExecuter executer : executerList) {
                    String header = executer.getName() + " (Level: " + executer.getNodeLevel() + ", Priority: " + executer.getPriority() + ")";
                    if (ImGui.collapsingHeader(header + "##exec" + executer.getId())) {
                        ImGui.indent();
                        for (Node<?> node : executer.getFunction().getNodes().values()) {
                            List<InspectorVariable> vars = node.getInspectorVariables();
                            if (vars.isEmpty()) continue;

                            boolean hasNamedVars = vars.stream().anyMatch(v -> !v.getName().get().isBlank());
                            if (!hasNamedVars) continue;

                            for (InspectorVariable var : vars) {
                                if (var.getName().get().isBlank()) continue;
                                ImGui.spacing();
                                ImGui.text(var.getName().get());
                                ImGui.sameLine();
                                ImGui.textDisabled("(" + node.getDisplayName() + ")");
                                ImGui.separator();
                                ImGui.spacing();
                                var.render();
                                ImGui.spacing();
                            }
                        }
                        ImGui.unindent();
                    }
                }
            }
        }

        ImGui.end();
    }

    public void saveStructureExecuter(int priority, String name, Graph function, int nodeLevel) {
        NodeEditor nodeEditor = getNodeEditor();
        StructureExecuter executer = new StructureExecuter(priority, name, function, nodeLevel);
        GraphSerializer.serialize(structureDefinition.getId(), executer);
        structureDefinition.addStructureExecuter(executer);
    }

    public NodeEditor getNodeEditor() {
        return nodeEditor;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public boolean isNodeEditorOpen() {
        return nodeEditorOpen;
    }

    public void setNodeEditorOpen(boolean nodeEditorOpen) {
        this.nodeEditorOpen = nodeEditorOpen;
    }

    public StructureDefinition getStructureDefinition() {
        return structureDefinition;
    }

    public StructureVisualEditor getVisualEditor() {
        return visualEditor;
    }

    public void openStructureDefinition(StructureDefinition structureDefinition) {
        nodeEditor.setNodePositions();
        this.structureDefinition = structureDefinition;
        nodeEditorOpen = false;
    }

    public void createAndOpenStructureDefinition(String name) {
        nodeEditor.setNodePositions();
        this.structureDefinition = new StructureDefinition(name);
        nodeEditorOpen = false;
    }

    public void createAndOpenNewStructureExecuter(String name, int priority, int nodeLevel) {
        Graph function = new Graph();
        nodeEditor.setNodePositions();
        StructureExecuter executer = new StructureExecuter(priority, name, function, nodeLevel);
        structureDefinition.addStructureExecuter(executer);
        GraphSerializer.serialize(structureDefinition.getId(), executer);
        nodeEditor.setCurrentStructure(executer);
        nodeEditorOpen = true;
    }

    public void openStructureExecuter(StructureExecuter executer) {
        nodeEditor.setCurrentStructure(executer);
        nodeEditorOpen = true;
    }

    public void saveCurrentDefinition() {
        GraphSerializer.serializeDefinition(structureDefinition);
        Structures.getInstance(true).addDefinition(structureDefinition);
    }
}