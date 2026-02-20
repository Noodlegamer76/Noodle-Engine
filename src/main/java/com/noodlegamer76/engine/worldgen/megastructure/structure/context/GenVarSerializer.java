package com.noodlegamer76.engine.worldgen.megastructure.structure.context;

import net.minecraft.nbt.CompoundTag;

public interface GenVarSerializer<T> {
    void serialize(CompoundTag tag, String name, T value);
    T deserialize(CompoundTag tag, String name);

    @FunctionalInterface
    interface Writer<T> {
        void write(CompoundTag tag, String name, T value);
    }

    @FunctionalInterface
    interface Reader<T> {
        T read(CompoundTag tag, String name);
    }

    static <T> GenVarSerializer<T> create(GenVarSerializer.Writer<T> writer, GenVarSerializer.Reader<T> reader) {
        return new GenVarSerializer<>() {
            @Override
            public void serialize(CompoundTag tag, String name, T value) {
                writer.write(tag, name, value);
            }

            @Override
            public T deserialize(CompoundTag tag, String name) {
                return reader.read(tag, name);
            }
        };
    }
}
