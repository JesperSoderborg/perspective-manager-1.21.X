package net.laisvall.perspectivemanager.client.logic;

import net.laisvall.perspectivemanager.client.data.Perspective;
import net.minecraft.client.MinecraftClient;

public class PerspectiveSwitcher {
    private static final PerspectiveSwitcher INSTANCE = new PerspectiveSwitcher();

    private PerspectiveSwitcher() { }

    public static PerspectiveSwitcher getInstance() {
        return INSTANCE;
    }

    private boolean inPerspective = false;
    private Perspective originalPerspective;

    public void switchPerspective(MinecraftClient client, Perspective perspective) {
        if (!inPerspective) {
            originalPerspective = new Perspective(
                    client.player.getX(),
                    client.player.getY(),
                    client.player.getZ(),
                    client.player.getYaw(),
                    client.player.getPitch(),
                    client.options.getFov().getValue()
            );
            setPerspective(client, perspective);
            client.options.hudHidden = true;
            inPerspective = true;
        } else {
            setPerspective(client, originalPerspective);
            client.options.hudHidden = false;
            inPerspective = false;
            // TODO: showIndicator();
        }
    }

    private void setPerspective(MinecraftClient client, Perspective perspective) {
        client.player.setPos(perspective.getX(), perspective.getY(), perspective.getZ());
        client.player.setYaw(perspective.getYaw());
        client.player.setPitch(perspective.getPitch());
        client.options.getFov().setValue(perspective.getFov());

        client.player.setVelocityClient(0, 0, 0);
    }
}