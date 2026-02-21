// NodePin.java
package com.noodlegamer76.engine.megastructure.structure.graph.pin;

import com.noodlegamer76.engine.megastructure.structure.graph.node.Node;

import java.util.ArrayList;
import java.util.List;

public class NodePin {
    private final int id;
    private final int nodeId;
    private final PinKind kind;
    private final PinCategory category;
    private final Class<?> dataType;
    private final String displayName;

    public NodePin(int id,
                   int nodeId,
                   PinKind kind,
                   PinCategory category,
                   Class<?> dataType, String displayName) {
        this.id = id;
        this.nodeId = nodeId;
        this.kind = kind;
        this.category = category;
        this.dataType = dataType;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public int getNodeId() {
        return nodeId;
    }

    public PinKind getKind() {
        return kind;
    }

    public PinCategory getCategory() {
        return category;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    public String getDisplayName() {
        return displayName;
    }
}