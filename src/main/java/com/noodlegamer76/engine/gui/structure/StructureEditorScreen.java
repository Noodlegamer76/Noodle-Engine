package com.noodlegamer76.engine.gui.structure;

import com.noodlegamer76.engine.gui.imgui.ImGuiRenderer;
import com.noodlegamer76.engine.gui.imgui.ImGuiScreen;
import net.minecraft.client.gui.GuiGraphics;

public class StructureEditorScreen extends ImGuiScreen {
    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        ImGuiRenderer.getInstance().draw(() -> StructureEditor.getInstance().render(pGuiGraphics));
    }
}
