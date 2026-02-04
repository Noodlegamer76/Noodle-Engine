package com.noodlegamer76.engine.physics;

import com.noodlegamer76.engine.physics.snapshot.BodyHandle;
import com.noodlegamer76.engine.physics.snapshot.PhysicsLevelSnapshot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.ode4j.math.DQuaternionC;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.ode4j.ode.OdeHelper.*;

public class PhysicsEngine {
    private final Map<ServerLevel, PhysicsLevelSnapshot.SnapshotPair> lastServerSnapshots = new ConcurrentHashMap<>();
    private static PhysicsEngine instance;
    private int nextId;

    private PhysicsEngine() {
        OdeHelper.initODE2(0);
    }

    public static PhysicsEngine getInstance() {
        if (instance == null) {
            instance = new PhysicsEngine();
        }
        return instance;
    }

    private final Queue<Runnable> pending = new ConcurrentLinkedQueue<>();
    private final Map<ServerLevel, PhysicsLevel> levels = new HashMap<>();
    private PhysicsLevelSnapshot.SnapshotPair lastServerSnapshot;


    public void start() {
        startPhysicsLoop();
    }

    private void startPhysicsLoop() {
        Thread physicsThread = new Thread(() -> {
            double timestep = 1.0 / 60.0;
            final int STEPS_PER_MC_TICK = 3;

            long last = System.nanoTime();
            double accumulator = 0.0;
            int stepCounter = 0;

            while (!Thread.currentThread().isInterrupted()) {
                long now = System.nanoTime();
                accumulator += (now - last) / 1e9;
                last = now;

                while (accumulator >= timestep) {
                    processPending();

                    for (PhysicsLevel level : levels.values()) {
                        synchronized (level.getBodies()) {
                            level.getSpace().collide(null, level::nearCallback);
                            level.getWorld().step(timestep);
                            level.getContactGroup().empty();
                        }
                    }

                    stepCounter++;
                    if (stepCounter == STEPS_PER_MC_TICK) {
                        stepCounter = 0;
                    }

                    accumulator -= timestep;
                }
            }
        }, "Ode4j-Physics-Thread");

        physicsThread.setDaemon(true);
        physicsThread.start();
    }


    private void processPending() {
        Runnable r;
        while ((r = pending.poll()) != null) {
            r.run();
        }
    }

    public void enqueue(Runnable r) {
        pending.add(r);
    }

    @Nullable
    public BodyHandle testAdd(Vec3 pos, ServerLevel serverLevel) {
        if (!levels.containsKey(serverLevel)) return null;
        BodyHandle handle = new BodyHandle(nextId++);
        enqueue(() -> {
            PhysicsLevel physicsLevel = levels.get(serverLevel);
            DBody body = createBody(physicsLevel.getWorld());
            body.setPosition(pos.x, pos.y, pos.z);

            DMass mass = createMass();
            mass.setBoxTotal(1.0, 1.0, 1.0, 1.0);
            body.setMass(mass);

            DGeom box = createBox(physicsLevel.getSpace(), 1.0, 1.0, 1.0);
            box.setBody(body);

            physicsLevel.getBodies().put(handle, body);
        });

        return handle;
    }

    public void addLevel(ServerLevel serverLevel) {
        levels.put(serverLevel, new PhysicsLevel());
    }

    public PhysicsLevel getLevel(ServerLevel serverLevel) {
        return levels.get(serverLevel);
    }

    public void tick(MinecraftServer server) {
        for (Map.Entry<ServerLevel, PhysicsLevel> entry : levels.entrySet()) {
            PhysicsLevelSnapshot.SnapshotPair snapshot = entry.getValue().getSnapshot().requestSnapshot();
            if (snapshot != null) {
                lastServerSnapshots.put(entry.getKey(), snapshot);
            }
        }
    }

    public PhysicsLevelSnapshot.SnapshotPair getLastSnapshot(ServerLevel level) {
        return lastServerSnapshots.get(level);
    }
}
