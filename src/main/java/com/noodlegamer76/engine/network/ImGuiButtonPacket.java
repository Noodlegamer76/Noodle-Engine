package com.noodlegamer76.engine.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ImGuiButtonPacket {
    public GuiButton id;

    public ImGuiButtonPacket(GuiButton REQUEST_GENERATE_CHUNK_STRUCTURE) {
        this.id = REQUEST_GENERATE_CHUNK_STRUCTURE;
    }

    public ImGuiButtonPacket(FriendlyByteBuf buf) {
        id = GuiButton.values()[buf.readVarInt()];
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(id.ordinal());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
    }

    public enum GuiButton {
        REQUEST_GENERATE_CHUNK_STRUCTURE
    }
}
