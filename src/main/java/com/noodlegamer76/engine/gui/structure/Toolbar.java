package com.noodlegamer76.engine.gui.structure;

import com.noodlegamer76.engine.megastructure.LoadedStructureGraphs;
import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSerializer;
import imgui.type.ImString;
import net.minecraft.client.gui.GuiGraphics;

import static imgui.ImGui.*;

public class Toolbar {
    private final ImString saveAsName = new ImString(256);
    private boolean saveAs = false;

    public void render(GuiGraphics guiGraphics, NodeEditor editor) {
        if (saveAs) {
            if (begin("Save as")) {
                inputText("Name", saveAsName);

                if (button("Save")) {
                    Graph structure = editor.getCurrentStructure();
                    editor.setNodePositions();
                    structure.setName(saveAsName.get());
                    editor.getSerializer().serialize(structure);
                    LoadedStructureGraphs.getInstance().addGraph(editor.getCurrentStructure());
                    saveAs = false;
                }
                if (button("Cancel")) {
                    saveAs = false;
                }

                end();
            }
        }

        beginMainMenuBar();

        if (beginMenu("File")) {

            if (beginMenu("Open")) {
                for (Graph graph : LoadedStructureGraphs.getInstance().getGraphs()) {
                    if (menuItem(graph.getName())) {
                        editor.setCurrentStructure(graph);
                    }
                }
                endMenu();
            }

            if (menuItem("Save")) {
                editor.setNodePositions();
                editor.getSerializer().serialize(editor.getCurrentStructure());
                LoadedStructureGraphs.getInstance().addGraph(editor.getCurrentStructure());
            }

            if (menuItem("Save As")) {
                saveAs = !saveAs;
            }

            endMenu();
        }

        endMainMenuBar();
    }
}
