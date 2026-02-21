package com.noodlegamer76.engine.gui.structure;

import imgui.extension.imnodes.ImNodes;
import net.minecraft.client.gui.GuiGraphics;

public class StructureEditor {
    private static final StructureEditor INSTANCE = new StructureEditor();

    public static StructureEditor getInstance() {
        return INSTANCE;
    }

    private final NodeEditor nodeEditor;

    private StructureEditor() {
        ImNodes.createContext();
        nodeEditor = new NodeEditor();
    }

    public void render(GuiGraphics guiGraphics) {
        nodeEditor.render(guiGraphics);
    }

    public NodeEditor getNodeEditor() {
        return nodeEditor;
    }
}