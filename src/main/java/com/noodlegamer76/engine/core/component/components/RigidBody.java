package com.noodlegamer76.engine.core.component.components;

import com.noodlegamer76.engine.core.component.Component;
import com.noodlegamer76.engine.core.component.InitComponents;
import com.noodlegamer76.engine.core.network.GameObjectSerializers;
import com.noodlegamer76.engine.core.network.SyncedVar;
import com.noodlegamer76.engine.entity.GameObject;
import com.noodlegamer76.engine.physics.PhysicsEngine;
import com.noodlegamer76.engine.physics.PhysicsLevel;
import com.noodlegamer76.engine.physics.snapshot.BodyHandle;
import com.noodlegamer76.engine.physics.snapshot.PhysicsLevelSnapshot;
import com.noodlegamer76.engine.physics.snapshot.RenderBody;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RigidBody extends Component {
    private BodyHandle handle;

    public RigidBody(GameObject gameObject) {
        super(InitComponents.RIGID_BODY, gameObject);
    }

    public void setHandle(BodyHandle handle) {
        this.handle = handle;
    }

    public BodyHandle getHandle() {
        return handle;
    }

    @Override
    public void tick() {
        if (gameObject.level() instanceof ServerLevel serverLevel) {
            if (handle == null) return;

            PhysicsLevelSnapshot.SnapshotPair pair = PhysicsEngine.getInstance().getLastSnapshot(serverLevel);

            if (pair == null) return;


            RenderBody currentBody = pair.current.get(getHandle());

            if (currentBody == null) return;

            Quaternionf currentRotation = new Quaternionf((float) currentBody.qx, (float) currentBody.qy, (float) currentBody.qz, (float) currentBody.qw);

            Vector3f currentPos = new Vector3f((float) currentBody.x, (float) currentBody.y, (float) currentBody.z);

            Vector3f lv = new Vector3f((float) currentBody.lx, (float) currentBody.ly, (float) currentBody.lz);
            Vector3f av = new Vector3f((float) currentBody.ax, (float) currentBody.ay, (float) currentBody.az);

            gameObject.moveTo(currentPos.x, currentPos.y, currentPos.z);
            gameObject.setPhysicsPosition(currentPos);
            gameObject.setRotation(currentRotation);
            gameObject.setLinearVelocity(lv);
            gameObject.setAngularVelocity(av);
        }
    }
}
