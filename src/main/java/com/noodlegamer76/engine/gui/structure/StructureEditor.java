package com.noodlegamer76.engine.gui.structure;

import com.noodlegamer76.engine.megastructure.structure.StructureDefinition;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.Structures;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSerializer;
import imgui.extension.imnodes.ImNodes;
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
    }

    public void saveStructureExecuter(int priority, String name, Graph function, int nodeLevel) {
        NodeEditor nodeEditor = getNodeEditor();
        nodeEditor.setNodePositions();
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
        this.structureDefinition = structureDefinition;
    }

    public void createAndOpenStructureDefinition(String name) {
        this.structureDefinition = new StructureDefinition(name);
    }

    public void createAndOpenNewStructureExecuter(String name, int priority, int nodeLevel) {
        Graph function = new Graph();
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