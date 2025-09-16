package net.laisvall.perspectivemanager.client.data;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/**
 * Represents a saved perspective (camera position, orientation, and settings).
 */
public class Perspective {
    private double x, y, z;
    private float yaw;
    private float pitch;
    private int fov;
    private String name;
    private int hotkey;

    // --- Constructors ---
    public Perspective(double x, double y, double z, float yaw, float pitch, int fov, String name, int hotkey) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.fov = fov;
        this.name = name;
        this.hotkey = hotkey;
    }

    public Perspective(double x, double y, double z, float yaw, float pitch, int fov) {
        this(x, y, z, yaw, pitch, fov, null, GLFW.GLFW_KEY_UNKNOWN);
    }

    // --- Getters ---
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() {
        return yaw;
    }
    public float getPitch() {
        return pitch;
    }
    public int getFov() { return fov; }
    public String getName() { return name; }
    public int getHotkey() {
        return hotkey;
    }

    // --- Setters ---
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setZ(int z) { this.z = z; }
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

    public KeyBinding createKeyBinding() {
        if (hotkey == GLFW.GLFW_KEY_UNKNOWN) return null;
        return new KeyBinding(
                "key.perspectivemanager." + name.toLowerCase(),
                hotkey,
                "key.categories.perspectivemanager"
        );
    }
}