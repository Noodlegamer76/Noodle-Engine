
package com.noodlegamer76.engine.physics.snapshot;

import com.noodlegamer76.engine.physics.PhysicsLevel;
import org.ode4j.ode.DBody;

import java.util.HashMap;
import java.util.Map;

public class PhysicsLevelSnapshot {
    private Map<BodyHandle, RenderBody> currentSnapshot = new HashMap<>();
    private Map<BodyHandle, RenderBody> previousSnapshot = new HashMap<>();
    private final PhysicsLevel level;

    public PhysicsLevelSnapshot(PhysicsLevel level) {
        this.level = level;
    }

    public PhysicsLevel getLevel() {
        return level;
    }

    public SnapshotPair requestSnapshot() {
        Map<BodyHandle, RenderBody> newSnapshot = new HashMap<>();
        synchronized (level.getBodies()) {
            for (Map.Entry<BodyHandle, DBody> body: level.getBodies().entrySet()) {
                newSnapshot.put(body.getKey(), new RenderBody(body.getValue()));
            }
        }

        synchronized (this) {
            previousSnapshot = currentSnapshot;
            currentSnapshot = newSnapshot;
        }

        return new SnapshotPair(previousSnapshot, currentSnapshot);
    }

    public static class SnapshotPair {
        public final Map<BodyHandle, RenderBody> previous;
        public final Map<BodyHandle, RenderBody> current;

        public SnapshotPair(Map<BodyHandle, RenderBody> previous, Map<BodyHandle, RenderBody> current) {
            this.previous = previous;
            this.current = current;
        }
    }
}