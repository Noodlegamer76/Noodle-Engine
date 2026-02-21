package com.noodlegamer76.engine.gui.structure;

import com.noodlegamer76.engine.megastructure.structure.graph.Graph;
import imgui.type.ImString;
import net.minecraft.client.gui.GuiGraphics;

import static imgui.ImGui.*;

public class Toolbar {
    private final ImString saveAsName = new ImString(256);
    private boolean saveAs = false;

   //public void render(GuiGraphics guiGraphics, StructureEditor structureEditor) {
   //    NodeEditor nodeEditor = structureEditor.getNodeEditor();
   //    if (saveAs) {
   //        if (begin("Save as")) {
   //            inputText("Name", saveAsName);

   //            if (button("Save")) {
   //                Graph structure = nodeEditor.getCurrentStructure();
   //                structure.setName(saveAsName.get());
   //                saveAs = false;
   //            }
   //            if (button("Cancel")) {
   //                saveAs = false;
   //            }

   //            end();
   //        }
   //    }

   //    beginMainMenuBar();

   //    if (beginMenu("File")) {

   //        if (beginMenu("Open")) {
   //            for (Graph graph : structureEditor.getStructureDefinition().getStructureExecuters()) {
   //                if (menuItem(graph.getName())) {
   //                    nodeEditor.setCurrentStructure(graph);
   //                }
   //            }
   //            endMenu();
   //        }

   //        if (menuItem("Save")) {
   //            structureEditor.saveStructureExecuter();
   //        }

   //        if (menuItem("Save As")) {
   //            saveAs = !saveAs;
   //        }

   //        endMenu();
   //    }

   //    endMainMenuBar();
   //}
}
