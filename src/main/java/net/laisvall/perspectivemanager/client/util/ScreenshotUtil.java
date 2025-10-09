package net.laisvall.perspectivemanager.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.NativeImage;
import org.joml.Matrix4f;

public class ScreenshotUtil {
    private static NativeImage takeScreenshotFromCamera(MinecraftClient client, double x, double y, double z, float yaw, float pitch) {
        Framebuffer framebuffer = client.getFramebuffer();
        framebuffer.beginWrite(true);

        RenderTickCounter tickCounter = client.getRenderTickCounter();
        float tickDelta = tickCounter.getTickDelta(true);

        CustomCamera customCam = new CustomCamera(x, y, z, yaw, pitch);
        customCam.update(client.world, null, false, false, tickDelta);

        Matrix4f modelView = RenderSystem.getModelViewMatrix();
        Matrix4f texture = RenderSystem.getTextureMatrix();

        client.worldRenderer.render(
                tickCounter,
                false,
                customCam,
                client.gameRenderer,
                client.gameRenderer.getLightmapTextureManager(),
                modelView,
                texture);

        NativeImage nativeImage = new NativeImage(framebuffer.textureWidth, framebuffer.textureHeight, false);

        RenderSystem.bindTexture(framebuffer.getColorAttachment());
        nativeImage.loadFromTextureImage(0, true);
        nativeImage.mirrorVertically();

        framebuffer.endWrite();

        return nativeImage;
    }

    public static NativeImage takeScreenshot(MinecraftClient client) {
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
