package com.noodlegamer76.engine.megastructure.structure.graph;

public class Link {
    private final int id;
    private final int startPinId;
    private final int endPinId;

    public Link(int id, int startPinId, int endPinId) {
        this.id = id;
        this.startPinId = startPinId;
        this.endPinId = endPinId;
    }

    public int getId() {
        return id;
    }

    public int getStartPinId() {
        return startPinId;
    }

    public int getEndPinId() {
        return endPinId;
    }
}