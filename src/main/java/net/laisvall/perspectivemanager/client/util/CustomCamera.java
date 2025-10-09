package net.laisvall.perspectivemanager.client.util;

import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

public class CustomCamera extends Camera {

    public CustomCamera(double x, double y, double z, float yaw, float pitch) {
        super();
        this.setPosPublic(new Vec3d(x,y,z));
        this.setRotationPublic(yaw, pitch);
    }

    public void setPosPublic(Vec3d pos) {
        super.setPos(pos);
    }

    public void setRotationPublic(float yaw, float pitch) {
        super.setRotation(yaw, pitch);
    }
}
