package net.laisvall.perspectivemanager.client.data;

import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

/**
 * Represents a saved perspective (camera position, orientation, and settings).
 */
public class Perspective {
    private String id;
    private String name;
    private int color;
    private double x, y, z;
    private float yaw;
    private float pitch;
    private int fov;
    private int hotkey;
    private String dimension;
    private String thumbnailDir;

    // --- Constructors ---
    public Perspective(String id,String name, int color,
                       double x, double y, double z,
                       float yaw, float pitch, int fov,
                       int hotkey, String dimension,
                       String thumbnailDir) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.fov = fov;
        this.hotkey = hotkey;
        this.dimension = dimension;
        this.thumbnailDir = thumbnailDir;
    }

    public Perspective(double x, double y, double z, float yaw, float pitch, int fov) {
        this("", "temp", 0, x, y, z, yaw, pitch, fov, GLFW.GLFW_KEY_UNKNOWN, "", "");
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getName() { return name; }
    public int getColor() { return color; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public int getFov() { return fov; }
    public int getHotkey() {
        return hotkey;
    }
    public String getThumbnailDir() { return thumbnailDir; }
    public String getDimension() { return dimension; }

    // --- Setters ---
    public void setId(String id) {this.id = id;}
    public void setName(String name) {
        this.name = name;
    }
    public void setColor(int color) { this.color = color; }
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
    public void setHotkey(int hotkey) {
        this.hotkey = hotkey;
    }
    public void setThumbnailDir(String thumbnailDir) { this.thumbnailDir = thumbnailDir; }
    public void setDimension(String dimension) { this.dimension = dimension; }

    public KeyBinding createKeyBinding() {
        if (hotkey == GLFW.GLFW_KEY_UNKNOWN) return null;
        return new KeyBinding(
                "key.perspectivemanager." + name.toLowerCase(),
                hotkey,
                "key.categories.perspectivemanager"
        );
    }
}