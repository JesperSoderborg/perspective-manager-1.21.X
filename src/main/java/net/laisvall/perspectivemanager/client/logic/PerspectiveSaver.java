package net.laisvall.perspectivemanager.client.logic;

import net.laisvall.perspectivemanager.client.data.Perspective;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class PerspectiveSaver {
    private PerspectiveSaver() {}

    public static void saveCurrentPerspective(MinecraftClient client, String name, int color, double x, double y, double z, float yaw, float pitch, int fov, int hotkey, NativeImage screenshot) {
        Identifier dimensionId = client.world.getRegistryKey().getValue();
        String id = UUID.randomUUID().toString();

        Path thumbnailDir = PerspectiveStorage.getInstance().getThumbnailDir();
        File screenshotFile = thumbnailDir.resolve(id + ".png").toFile();

        try (screenshot) {
            screenshot.writeTo(screenshotFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Perspective newPerspective = new Perspective(
                id,
                name,
                color,
                x, y, z,
                yaw, pitch,
                fov,
                hotkey,
                dimensionId.toString(),
                "thumbnails/" + id + ".png"
        );

        PerspectiveStorage.getInstance().addPerspective(newPerspective);
    }
}
