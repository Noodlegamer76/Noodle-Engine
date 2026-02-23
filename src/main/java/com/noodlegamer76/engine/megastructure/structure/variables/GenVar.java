package com.noodlegamer76.engine.megastructure.structure.variables;

public class GenVar<T> {
    private T value;
    private final Class<T> clazz;
    private String name;
    private final boolean global;

    public GenVar(T value, Class<T> clazz, boolean global, String name) {
        if (value == null) throw new NullPointerException("GenVar default value can't be null");
        this.value = value;
        this.clazz = clazz;
        this.name = name;
        this.global = global;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() { return value; }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getName() {
        return name;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setName(String name) {
        this.name = name;
    }
}
