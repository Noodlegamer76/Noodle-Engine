package com.noodlegamer76.engine.gui.structure;

import com.noodlegamer76.engine.megastructure.structure.StructureDefinition;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.Structures;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSerializer;
import com.noodlegamer76.engine.network.PacketHandler;
import com.noodlegamer76.engine.network.StructureDefinitionUpload;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static imgui.ImGui.*;

public class Toolbar {
    private final ImString newDefinitionName = new ImString(256);
    private final ImString newExecuterName = new ImString(256);
    private final ImInt newExecuterPriority = new ImInt(0);
    private final ImInt newExecuterNodeLevel = new ImInt(0);

    private boolean createNewDefinition = false;
    private boolean createNewExecuter = false;

    public void render(StructureEditor structureEditor) {
        Collection<StructureDefinition> allDefinitions = Structures.getInstance(true).getDefinitions().values();
        StructureDefinition currentDefinition = structureEditor.getStructureDefinition();

        if (createNewDefinition) {
            if (begin("New Definition")) {
                inputText("Name", newDefinitionName);

                if (button("Create")) {
                    String name = newDefinitionName.get().trim();
                    if (!name.isEmpty()) {
                        structureEditor.createAndOpenStructureDefinition(name);
                        newDefinitionName.set("");
                        createNewDefinition = false;
                    }
                }
                sameLine();
                if (button("Cancel")) {
                    createNewDefinition = false;
                }

            }
            end();
        }

        if (createNewExecuter && currentDefinition != null) {
            if (begin("New Structure Executer")) {
                inputText("Name", newExecuterName);
                inputInt("Priority", newExecuterPriority);
                inputInt("Node Level", newExecuterNodeLevel);

                if (button("Create")) {
                    String name = newExecuterName.get().trim();
                    if (!name.isEmpty()) {
                        structureEditor.createAndOpenNewStructureExecuter(
                                name,
                                newExecuterPriority.get(),
                                newExecuterNodeLevel.get()
                        );
                        newExecuterName.set("");
                        newExecuterPriority.set(0);
                        newExecuterNodeLevel.set(0);
                        createNewExecuter = false;
                    }
                }
                sameLine();
                if (button("Cancel")) {
                    createNewExecuter = false;
                }

            }
            end();
        }

        beginMainMenuBar();

        if (beginMenu("File")) {

            if (beginMenu("Open Definition")) {
                if (allDefinitions.isEmpty()) {
                    beginDisabled();
                    menuItem("(no definitions found)");
                    endDisabled();
                } else {
                    for (StructureDefinition definition : allDefinitions) {
                        boolean isCurrent = currentDefinition != null
                                && definition.getId().equals(currentDefinition.getId());

                        if (menuItem(definition.getId(), "", isCurrent)) {
                            structureEditor.openStructureDefinition(definition);
                        }
                    }
                }
                endMenu();
            }

            if (menuItem("New Definition")) {
                createNewDefinition = !createNewDefinition;
            }

            separator();

            boolean hasDefinition = currentDefinition != null;
            if (!hasDefinition) beginDisabled();

            if (menuItem("Save Definition")) {
                structureEditor.saveCurrentDefinition();
            }

            if (menuItem("Upload Definition")) {
                try {
                    StructureDefinitionUpload upload = new StructureDefinitionUpload(currentDefinition);
                    PacketHandler.sendToServer(upload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!hasDefinition) endDisabled();

            endMenu();
        }

        if (currentDefinition != null) {
            if (beginMenu("Executer")) {

                if (menuItem("New Executer")) {
                    createNewExecuter = !createNewExecuter;
                }

                separator();

                Map<Integer, List<StructureExecuter>> allExecuters =
                        currentDefinition.getStructureExecuters();

                StructureExecuter currentExecuter =
                        structureEditor.getNodeEditor().getCurrentStructure();

                if (allExecuters.isEmpty()) {
                    beginDisabled();
                    menuItem("(no executers)");
                    endDisabled();
                } else {
                    for (Map.Entry<Integer, List<StructureExecuter>> entry : allExecuters.entrySet()) {
                        beginDisabled();
                        menuItem("Priority " + entry.getKey());
                        endDisabled();

                        for (StructureExecuter executer : entry.getValue()) {
                            boolean isCurrent = currentExecuter != null
                                    && executer.getName().equals(currentExecuter.getName());

                            if (menuItem("  " + executer.getName(), "", isCurrent)) {
                                structureEditor.getNodeEditor().setCurrentStructure(executer);
                                structureEditor.setNodeEditorOpen(true);
                            }
                        }
                    }
                }

                endMenu();
            }
        }

        if (currentDefinition != null) {
            StructureExecuter currentExecuter = structureEditor.getNodeEditor().getCurrentStructure();
            String context = "  |  " + currentDefinition.getId();
            if (currentExecuter != null) {
                context += " > " + currentExecuter.getName()
                        + " (priority: " + currentExecuter.getPriority() + ")";
            }

            beginDisabled();
            menuItem(context);
            endDisabled();
        }

        endMainMenuBar();
    }
}