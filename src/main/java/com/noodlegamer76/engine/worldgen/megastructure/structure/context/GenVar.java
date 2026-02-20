package com.noodlegamer76.engine.worldgen.megastructure.structure.context;

public class GenVar<T> {
    private T value;
    private final GenVarSerializer<T> serializer;
    private final String name;

    public GenVar(T value, GenVarSerializer<T> serializer, String name) {
        if (value == null) throw new NullPointerException("GenVar default value can't be null");
        this.value = value;
        this.serializer = serializer;
        this.name = name;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() { return value; }

    public GenVarSerializer<T> getSerializer() { return serializer; }

    public String getName() {
        return name;
    }
}
