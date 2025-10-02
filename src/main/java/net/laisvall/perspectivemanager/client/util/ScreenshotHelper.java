package net.laisvall.perspectivemanager.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;

public class ScreenshotHelper {
    public static NativeImage takeScreenshot(MinecraftClient client) {
        boolean prevHudHidden = client.options.hudHidden;
        client.options.hudHidden = true;
        NativeImage image = ScreenshotRecorder.takeScreenshot(client.getFramebuffer());
        client.options.hudHidden = prevHudHidden;
        return image;
    }
}
