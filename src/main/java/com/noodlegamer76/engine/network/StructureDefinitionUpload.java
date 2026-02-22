package com.noodlegamer76.engine.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.megastructure.structure.StructureDefinition;
import com.noodlegamer76.engine.megastructure.structure.StructureExecuter;
import com.noodlegamer76.engine.megastructure.structure.Structures;
import com.noodlegamer76.engine.megastructure.structure.graph.GraphSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class StructureDefinitionUpload {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public StructureDefinition definition;

    public StructureDefinitionUpload(StructureDefinition definition) {
        this.definition = definition;
    }

    public StructureDefinitionUpload(FriendlyByteBuf buf) {
        String definitionName = buf.readUtf();
        this.definition = new StructureDefinition(definitionName);

        int count = buf.readVarInt();
        for (int i = 0; i < count; i++) {
            byte[] bytes = buf.readByteArray();
            StructureExecuter executer = GraphSerializer.deserializeExecuterFromBytes(bytes);
            if (executer != null) {
                definition.addStructureExecuter(executer);
            }
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(definition.getId());

        int count = 0;
        for (Map.Entry<Integer, List<StructureExecuter>> executers : definition.getStructureExecuters().entrySet()) {
            count += executers.getValue().size();
        }
        buf.writeVarInt(count);

        for (Map.Entry<Integer, List<StructureExecuter>> executers : definition.getStructureExecuters().entrySet()) {
            for (StructureExecuter executer : executers.getValue()) {
                JsonObject executerObject = GraphSerializer.serialize(definition.getId(), executer);
                byte[] bytes = gson.toJson(executerObject).getBytes(StandardCharsets.UTF_8);
                buf.writeByteArray(bytes);
            }
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Structures.getInstance(false).addDefinition(definition);
        });
        context.setPacketHandled(true);
    }
}