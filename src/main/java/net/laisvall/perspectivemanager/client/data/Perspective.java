package net.laisvall.perspectivemanager.client.data;

import net.minecraft.util.math.Vec3d;

/**
 * Represents a saved perspective (camera position, orientation, and settings).
 */
public class Perspective {
    private Vec3d position;
    private float yaw;
    private float pitch;
    private int fov;
    private String name;
    private int hotkey;

    // --- Constructors ---
    public Perspective(Vec3d position, float yaw, float pitch, int fov, String name, int hotkey) {
        this.position = position;
        this.yaw = yaw;
        this.pitch = pitch;
        this.fov = fov;
        this.name = name;
        this.hotkey = hotkey;
    }

    public Perspective(Vec3d position, float yaw, float pitch, int fov) {
        this(position, yaw, pitch, fov, "Unnamed", -1);
    }

    // --- Getters ---
    public Vec3d getPosition() {
        return position;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public int getFov() {
        return fov;
    }

    public String getName() {
        return name;
    }

    public int getHotkey() {
        return hotkey;
    }

    // --- Setters ---
    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setFov(int fov) {
        this.fov = fov;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHotkey(int hotkey) {
        this.hotkey = hotkey;
    }
}