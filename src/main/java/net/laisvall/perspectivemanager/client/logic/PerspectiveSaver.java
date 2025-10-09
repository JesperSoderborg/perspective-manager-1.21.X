package net.laisvall.perspectivemanager.client.logic;

import net.laisvall.perspectivemanager.client.data.Perspective;
import net.laisvall.perspectivemanager.client.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class PerspectiveSaver {
    private PerspectiveSaver() {}

    public static void saveCurrentPerspective(MinecraftClient client, NativeImage screenshot, String name, int color, int hotkey) {
        if (client.player == null || client.world == null) return;

        PlayerEntity player = client.player;
        GameOptions options = client.options;

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
                MathUtil.roundDouble(player.getX(), 1),
                MathUtil.roundDouble(player.getY(), 1),
                MathUtil.roundDouble(player.getZ(), 1),
                MathUtil.roundFloat(player.getYaw(), 1),
                MathUtil.roundFloat(player.getPitch(), 1),
                options.getFov().getValue(),
                hotkey,
                dimensionId.toString(),
                "thumbnails/" + id + ".png"
        );

        PerspectiveStorage.getInstance().addPerspective(newPerspective);
    }
}
