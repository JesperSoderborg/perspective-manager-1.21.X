package net.laisvall.perspectivemanager.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FramedImageWidget extends ClickableWidget {
    private static final Identifier FRAME_TEXTURE = Identifier.ofVanilla("widget/button");
    private final Identifier imageTextureId;
    private final int imageWidth;
    private final int imageHeight;

    public FramedImageWidget(int x, int y, int width, int height, Identifier imageTextureId) {
        super(x, y, width, height, Text.empty());
        this.active = false;
        this.visible = true;

        this.imageTextureId = imageTextureId;
        this.imageWidth = width - 6;
        this.imageHeight = height - 6;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawGuiTexture(FRAME_TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight());

        RenderSystem.enableBlend();
        context.drawTexture(
                this.imageTextureId,
                this.getX() + 3, this.getY() + 3,
                0, 0,
                this.imageWidth, this.imageHeight,
                this.imageWidth, this.imageHeight
        );
        RenderSystem.disableBlend();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {}
}