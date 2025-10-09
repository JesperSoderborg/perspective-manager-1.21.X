package net.laisvall.perspectivemanager.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;

import java.nio.file.Path;

public class ScreenshotUtil {
    public static NativeImage takeScreenshot(MinecraftClient client) {
        boolean prevHudHidden = client.options.hudHidden;
        client.options.hudHidden = true;
        NativeImage image = ScreenshotRecorder.takeScreenshot(client.getFramebuffer());
        client.options.hudHidden = prevHudHidden;
        return image;
    }

    public static NativeImage takeHUDLessScreenshot(MinecraftClient client) {
        Framebuffer framebuffer = client.getFramebuffer();

        framebuffer.beginWrite(true);

        client.gameRenderer.setRenderHand(false);
        client.gameRenderer.renderWorld(
                client.getRenderTickCounter()
        );
        client.gameRenderer.setRenderHand(true);

        NativeImage nativeImage = new NativeImage(framebuffer.textureWidth, framebuffer.textureHeight, false);

        RenderSystem.bindTexture(framebuffer.getColorAttachment());
        nativeImage.loadFromTextureImage(0, true);
        nativeImage.mirrorVertically();

        framebuffer.endWrite();

        return nativeImage;
    }
}
