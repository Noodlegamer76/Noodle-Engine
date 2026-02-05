package com.noodlegamer76.engine.physics;

import com.noodlegamer76.engine.physics.snapshot.BodyHandle;
import com.noodlegamer76.engine.physics.snapshot.PhysicsLevelSnapshot;
import com.noodlegamer76.engine.physics.snapshot.RenderBody;
import net.minecraft.world.level.ChunkPos;
import org.ode4j.ode.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.ode4j.ode.OdeHelper.*;
import static org.ode4j.ode.OdeHelper.createPlane;

public class PhysicsLevel {
    private final DWorld world;
    private final DSpace space;
    private final DJointGroup contactGroup;

    private final Map<BodyHandle, DBody> bodies = new HashMap<>();

    private final PhysicsLevelSnapshot snapshot;

    public PhysicsLevel() {
        snapshot = new PhysicsLevelSnapshot(this);
        world = createWorld();
        world.setGravity(0, -9.81, 0);
        world.setCFM(1e-5);

        space = createSimpleSpace();
        contactGroup = createJointGroup();

        createPlane(space, 0, 1, 0, 0);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            contactGroup.destroy();
            space.destroy();
            world.destroy();
            OdeHelper.closeODE();
        }));
    }

    public void nearCallback(Object data, DGeom g1, DGeom g2) {
        DBody b1 = g1.getBody();
        DBody b2 = g2.getBody();

        if (b1 == null && b2 == null) return;

        if (b1 != null && b1 == b2) return;

        DContactBuffer contacts = new DContactBuffer(8);
        int count = collide(g1, g2, 8, contacts.getGeomBuffer());

        for (int i = 0; i < count; i++) {
            DContact c = contacts.get(i);

            c.surface.mode =
                    OdeConstants.dContactBounce |
                            OdeConstants.dContactApprox1;

            c.surface.mu = 0.9;
            c.surface.bounce = 0.2;
            c.surface.bounce_vel = 0.1;

            DJoint j = createContactJoint(world, contactGroup, c);
            j.attach(b1, b2);
        }
    }

    public DWorld getWorld() {
        return world;
    }

    public DSpace getSpace() {
        return space;
    }

    public DJointGroup getContactGroup() {
        return contactGroup;
    }

    public Map<BodyHandle, DBody> getBodies() {
        return bodies;
    }

    public PhysicsLevelSnapshot getSnapshot() {
        return snapshot;
    }
}
