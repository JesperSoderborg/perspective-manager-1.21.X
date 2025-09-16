package net.laisvall.perspectivemanager.client.logic;

import net.laisvall.perspectivemanager.client.data.Perspective;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class PerspectiveSwitcher {
    private static Perspective activePerspective = null;

    public static void switchPerspective(MinecraftClient client, Perspective perspective) {

        Vec3d pos = perspective.getPosition();
        client.player.setPos(pos.x, pos.y, pos.z);

        client.player.setYaw(perspective.getYaw());
        client.player.setPitch(perspective.getPitch());
        client.options.getFov().setValue(perspective.getFov());

    }

    public static Perspective getActivePerspective() {
        return activePerspective;
    }

    public static void setActivatedPerspective(Perspective perspective) {
        activePerspective = perspective;
    }
}
