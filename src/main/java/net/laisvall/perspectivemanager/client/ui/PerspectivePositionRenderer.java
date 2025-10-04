package net.laisvall.perspectivemanager.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.laisvall.perspectivemanager.client.data.Perspective;
import net.laisvall.perspectivemanager.client.logic.PerspectiveStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.List;
import java.util.Objects;

import static net.minecraft.client.render.VertexFormats.POSITION_COLOR;

public class PerspectivePositionRenderer {
    public static void init() {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> renderAll(Objects.requireNonNull(context.matrixStack()), context.consumers(), context.camera()));
    }

    public static void renderAll(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera) {
        matrices.push();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Vec3d camPos = camera.getPos();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z);

        List<Perspective> perspectives = PerspectiveStorage.getInstance().getPerspectives();
        perspectives.forEach(perspective ->
            drawText(
                perspective.getName(),
                perspective.getX(),
                perspective.getY(),
                perspective.getZ(),
                0.023f,
                perspective.getColor(),
                false,
                matrices,
                vertexConsumers,
                camera
            )
        );

        // drawText("From below (Helicopter)", 5, 100, 0, 0.023f, 0xff00ff, false, matrices, vertexConsumers, camera);
        // drawText("Over", 0, 100, 0, 0.023f, 0xffbb00, false, matrices, vertexConsumers, camera);
        // drawText("From the left", 0, 100, 5, 0.023f, 0x00ff55, false, matrices, vertexConsumers, camera);
        // drawText("Fortress", 5, 100, 5, 0.023f, 0x008cff, false, matrices, vertexConsumers, camera);

        RenderSystem.disableBlend();
        matrices.pop();
    }

    public static void drawText(String text, double x, double y, double z, float scale, int color, boolean shadow, MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        matrices.push();

        // Translate to world position
        //Vec3d.ofCenter(new BlockPos((int)x, (int)y, (int)z));
        matrices.translate(x, y, z);

        // Rotate to face camera
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

        // Scale text
        matrices.scale(scale, scale, scale);

        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight;
        float offsetX = -textWidth / 2f;
        float padding = 1f;

        matrices.scale(-1,1,1);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        renderBackground(matrix, offsetX - padding, -textHeight - padding, textWidth + 2*padding, textHeight + 2*padding);

        matrices.scale(1,-1,1);
        matrix = matrices.peek().getPositionMatrix();
        textRenderer.draw(
                text,
                offsetX, 0,
                color,
                shadow,
                matrix,
                vertexConsumers,
                TextRenderer.TextLayerType.NORMAL,
                0,
                15728880
        );

        matrices.pop();
    }

    private static void renderBackground(Matrix4f matrix, float x, float y, float width, float height) {
        float zIndex = 0.01f;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, POSITION_COLOR);
        int shadowColor = 0x40000000;

        buffer.vertex(matrix, x, y, zIndex).color(shadowColor);
        buffer.vertex(matrix, (x+width), y, zIndex).color(shadowColor);
        buffer.vertex(matrix, (x+width), (y+height), zIndex).color(shadowColor);
        buffer.vertex(matrix, x, (y+height), zIndex).color(shadowColor);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}