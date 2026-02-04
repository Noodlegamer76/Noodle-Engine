package com.noodlegamer76.engine.physics.snapshot;

import org.ode4j.math.DQuaternionC;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DBody;

public final class RenderBody {
    //Position
    public final double x, y, z;
    //Rotation
    public final double qx, qy, qz, qw;
    //Linear velocity
    public final double lx, ly, lz;
    //Angular velocity
    public final double ax, ay, az;

    public RenderBody(DBody body) {
        DVector3C p = body.getPosition();
        DQuaternionC q = body.getQuaternion();
        DVector3C l = body.getLinearVel();
        DVector3C a = body.getAngularVel();

        this.x = p.get0();
        this.y = p.get1();
        this.z = p.get2();

        this.qw = q.get0();
        this.qx = q.get1();
        this.qy = q.get2();
        this.qz = q.get3();

        this.lx = l.get0();
        this.ly = l.get1();
        this.lz = l.get2();

        this.ax = a.get0();
        this.ay = a.get1();
        this.az = a.get2();
    }
}