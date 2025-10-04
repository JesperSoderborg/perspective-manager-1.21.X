package net.laisvall.perspectivemanager.client.ui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.laisvall.perspectivemanager.client.logic.PerspectiveSaver;
import net.laisvall.perspectivemanager.client.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class PerspectiveSaveScreen extends Screen {
    private final MinecraftClient client;

    private TextFieldWidget nameField;

    private TextFieldWidget xField;
    private TextFieldWidget yField;
    private TextFieldWidget zField;

    private TextFieldWidget yawField;
    private TextFieldWidget pitchField;
    private TextFieldWidget fovField;

    private TextFieldWidget hexColorField;
    private ColorSliderWidget colorSliderWidget;

    private Identifier iconTexture = Identifier.of("mymod", "textures/gui/icon.png");
    private ButtonWidget toggleWaypointButton;
    private ButtonWidget toggleActiveViewButton;
    private ButtonWidget hotkeyButton;
    private boolean waypointEnabled = true;
    private boolean activeViewEnabled = true;

    private Integer selectedHotkey = null;
    private boolean waitingForKey = false;

    private Color waypointColor;

    private NativeImage screenshot;
    private NativeImageBackedTexture screenshotTexture;
    private Identifier screenshotId;

    private final int narrowGap = 4;
    private final int wideGap = 16;
    private final int shortButtonWidth = 98;
    private final int wideButtonWidth = 2*shortButtonWidth + narrowGap;
    private final int buttonHeight = ButtonWidget.DEFAULT_HEIGHT;
    private final int iconButtonSide = ButtonWidget.DEFAULT_HEIGHT;
    private final int imgHeight = wideButtonWidth*9/16;
    private final int imgWidth = wideButtonWidth;
    private final int offsetY = 32;
    private final int textHeight = 8;

    public PerspectiveSaveScreen(MinecraftClient client, NativeImage screenshot) {
        super(Text.of("Save Perspective"));
        this.client = client;

        if (screenshot != null) {
            setScreenshot(screenshot);
        }
    }

    public void setScreenshot(NativeImage screenshot) {
        if (screenshotTexture != null) screenshotTexture.close();

        this.screenshot = screenshot;
        this.screenshotTexture = new NativeImageBackedTexture(screenshot);
        this.screenshotId = client.getTextureManager().registerDynamicTexture("temp_screenshot", screenshotTexture);
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int rowGap = (imgHeight - 4*buttonHeight - narrowGap)/2;
        int firstRow = centerY - imgHeight + offsetY;
        int secondRow = firstRow + buttonHeight + rowGap;
        int thirdRow = secondRow + buttonHeight + narrowGap;

        int secondButtonRow = centerY + narrowGap + offsetY;
        int firstButtonRow = secondButtonRow - buttonHeight - narrowGap;
        int thirdButtonRow = secondButtonRow + buttonHeight + narrowGap;

        int topRow = firstRow - buttonHeight - wideGap;
        int bottomRow = thirdButtonRow + buttonHeight + wideGap;

        int randomHue = ColorUtil.getRandomHue();
        waypointColor = ColorUtil.getColorFromHue(randomHue);

        // --- Name field ---
        nameField = new TextFieldWidget(this.textRenderer,
                centerX - wideButtonWidth/2 + narrowGap, topRow,
                wideButtonWidth - 2*iconButtonSide - 3*narrowGap, buttonHeight, Text.of("Name"));
        nameField.setPlaceholder(Text.of("Unnamed Perspective"));
        nameField.setEditableColor(ColorUtil.getHexFromColor(waypointColor));
        this.addSelectableChild(nameField);

        // --- Location Fields (X, Y, Z) ---
        int narrowFieldWidth = 40;
        int wideFieldWidth = (wideButtonWidth - narrowFieldWidth)/2 - narrowGap;

        int xFieldX = centerX + wideGap/2;
        int zFieldX = xFieldX + wideFieldWidth + narrowGap;
        int yFieldX = zFieldX + wideFieldWidth + narrowGap;

        xField = new TextFieldWidget(this.textRenderer, xFieldX, firstRow, wideFieldWidth, buttonHeight, Text.of("X"));
        zField = new TextFieldWidget(this.textRenderer, zFieldX, firstRow, wideFieldWidth, buttonHeight, Text.of("Z"));
        yField = new TextFieldWidget(this.textRenderer, yFieldX, firstRow, narrowFieldWidth, buttonHeight, Text.of("Y"));
        xField.setText(String.format("%.1f", client.player.getX()));
        zField.setText(String.format("%.1f", client.player.getZ()));
        yField.setText(String.format("%.1f", client.player.getY()));
        this.addSelectableChild(xField);
        this.addSelectableChild(yField);
        this.addSelectableChild(zField);

        // --- Direction Fields (Yaw, Pitch) ---
        int yawFieldX = centerX + wideGap/2;
        int pitchFieldX = yawFieldX + wideFieldWidth + narrowGap;
        int fovFieldX = pitchFieldX + wideFieldWidth + narrowGap;

        yawField = new TextFieldWidget(this.textRenderer, yawFieldX, secondRow, wideFieldWidth, buttonHeight, Text.of("Yaw"));
        pitchField = new TextFieldWidget(this.textRenderer, pitchFieldX, secondRow, wideFieldWidth, buttonHeight, Text.of("Pitch"));
        fovField = new TextFieldWidget(this.textRenderer, fovFieldX, secondRow, narrowFieldWidth, buttonHeight, Text.of("fov"));
        yawField.setText(String.format("%.1f", client.player.getYaw()));
        pitchField.setText(String.format("%.1f", client.player.getPitch()));
        fovField.setText(String.valueOf(client.options.getFov().getValue()));
        this.addSelectableChild(yawField);
        this.addSelectableChild(pitchField);
        this.addSelectableChild(fovField);

        // --- Color Selector ---
        int hexColorFieldWidth = 55;
        hexColorField = new TextFieldWidget(textRenderer,
                centerX + wideGap/2 + wideButtonWidth - hexColorFieldWidth, firstButtonRow,
                hexColorFieldWidth, buttonHeight, Text.of("HEX"));
        hexColorField.setPlaceholder(Text.of(ColorUtil.getHexStringFromColor(waypointColor)));
        hexColorField.setEditableColor(ColorUtil.getHexFromColor(waypointColor));
        hexColorField.setChangedListener(newText -> {
            waypointColor = ColorUtil.getColorFromHexString(hexColorField.getText());
            if (waypointColor != null) {
                hexColorField.setEditableColor(ColorUtil.getHexFromColor(waypointColor));
                nameField.setEditableColor(ColorUtil.getHexFromColor(waypointColor));
            }
        });
        this.addSelectableChild(hexColorField);

        colorSliderWidget = new ColorSliderWidget(
                centerX + wideGap/2, firstButtonRow, wideButtonWidth - hexColorFieldWidth - narrowGap,
                buttonHeight, 0, 360, randomHue, val -> {
            waypointColor = ColorUtil.getColorFromHue(val);
            hexColorField.setText(ColorUtil.getHexStringFromColor(waypointColor));
            hexColorField.setEditableColor(ColorUtil.getHexFromColor(waypointColor));
            nameField.setEditableColor(ColorUtil.getHexFromColor(waypointColor));
        });
        this.addDrawableChild(colorSliderWidget);

        // --- Icon buttons ---
        TextIconButtonWidget favoriteButton = TextIconButtonWidget.builder(Text.literal(""), b -> {}, true)
                .dimension(iconButtonSide, iconButtonSide).texture(iconTexture, 16, 16).build();
        favoriteButton.setPosition(centerX + wideButtonWidth/2 - 2*iconButtonSide - narrowGap, topRow);
        this.addDrawableChild(favoriteButton);

        TextIconButtonWidget shareButton = TextIconButtonWidget.builder(Text.literal(""), b -> {}, true)
                .dimension(iconButtonSide, iconButtonSide).texture(iconTexture, 16, 16).build();
        shareButton.setPosition(centerX + wideButtonWidth/2 - iconButtonSide, topRow);
        this.addDrawableChild(shareButton);

        TextIconButtonWidget refreshScreenshotButton = TextIconButtonWidget.builder(Text.literal(""), b -> {}, true)
                .dimension(iconButtonSide, iconButtonSide)
                .texture(iconTexture, 16, 16).build();
        refreshScreenshotButton.setPosition(centerX - wideGap/2 - wideButtonWidth - narrowGap, firstRow - narrowGap);
        this.addDrawableChild(refreshScreenshotButton);

        // --- Action Buttons ---
        ButtonWidget deleteButton = ButtonWidget.builder(Text.of("Delete"), b -> {
                    client.setScreen(null);
                    screenshot.close();
                }).dimensions(centerX - wideGap/2 - wideButtonWidth, secondButtonRow, shortButtonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.of("Delete perspective"))).build();
        this.addDrawableChild(deleteButton);

        ButtonWidget teleportButton = ButtonWidget.builder(Text.of("Teleport"), b -> {
                    // TODO teleport logic
                }).dimensions(centerX - wideGap/2 - shortButtonWidth, secondButtonRow, shortButtonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.of("Teleport to coords"))).build();
        this.addDrawableChild(teleportButton);

        ButtonWidget allPerspectivesButton = ButtonWidget.builder(Text.of("All Perspectives"), b -> {
                    // TODO open manager
                }).dimensions(centerX - wideGap/2 - wideButtonWidth, thirdButtonRow, wideButtonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.of("View all perspectives"))).build();
        this.addDrawableChild(allPerspectivesButton);

        ButtonWidget useCurrentPositionButton = ButtonWidget.builder(Text.of("Use Current Position"), b -> {
                }).dimensions(centerX + wideGap/2, thirdRow, wideButtonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.of("Use Current Position"))).build();
        this.addDrawableChild(useCurrentPositionButton);

        hotkeyButton = ButtonWidget.builder(Text.of("Hotkey"), b -> {
                    waitingForKey = true;
                    hotkeyButton.setMessage(Text.of("Press a key..."));
                }).dimensions(centerX + wideGap/2, secondButtonRow, shortButtonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.of("Assign a hotkey"))).build();
        this.addDrawableChild(hotkeyButton);

        ButtonWidget cancelButton = ButtonWidget.builder(Text.of("Cancel"), b -> {
                    client.setScreen(null);
                    screenshot.close();
                }).dimensions(centerX - shortButtonWidth - narrowGap/2, bottomRow, shortButtonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.of("Cancel"))).build();
        this.addDrawableChild(cancelButton);

        ButtonWidget saveButton = ButtonWidget.builder(Text.of("Save"), b -> {
                    String name = nameField.getText().isEmpty() ? "Unnamed" : nameField.getText();
                    int hotkey = selectedHotkey == null ? GLFW.GLFW_KEY_UNKNOWN : selectedHotkey;
                    int color = ColorUtil.getHexFromColor(waypointColor);
                    PerspectiveSaver.saveCurrentPerspective(client, screenshot, name, color, hotkey);
                    client.setScreen(null);
                }).dimensions(centerX + narrowGap/2, bottomRow, shortButtonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.of("Save perspective"))).build();
        this.addDrawableChild(saveButton);

        // --- Toggles ---
        toggleWaypointButton = ButtonWidget.builder(Text.of("Waypoint: " + (waypointEnabled ? "ON" : "OFF")), b -> {
                    waypointEnabled = !waypointEnabled;
                    toggleWaypointButton.setMessage(Text.of("Waypoint: " + (waypointEnabled ? "ON" : "OFF")));
                }).dimensions(centerX + shortButtonWidth + narrowGap + wideGap/2, secondButtonRow, shortButtonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.of("Toggle waypoint at coords"))).build();
        this.addDrawableChild(toggleWaypointButton);

        toggleActiveViewButton = ButtonWidget.builder(Text.of("Active View: " + (activeViewEnabled ? "ON" : "OFF")), b -> {
                    activeViewEnabled = !activeViewEnabled;
                    toggleActiveViewButton.setMessage(Text.of("Waypoint: " + (activeViewEnabled ? "ON" : "OFF")));
                }).dimensions(centerX + wideGap/2, thirdButtonRow, wideButtonWidth, buttonHeight)
                .tooltip(Tooltip.of(Text.of("Toggle active view"))).build();
        this.addDrawableChild(toggleActiveViewButton);

        super.init();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (waitingForKey) {
            selectedHotkey = keyCode;
            waitingForKey = false;
            hotkeyButton.setMessage(Text.of(InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText()));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
        nameField.render(context, mouseX, mouseY, delta);
        xField.render(context, mouseX, mouseY, delta);
        yField.render(context, mouseX, mouseY, delta);
        zField.render(context, mouseX, mouseY, delta);
        yawField.render(context, mouseX, mouseY, delta);
        pitchField.render(context, mouseX, mouseY, delta);
        fovField.render(context, mouseX, mouseY, delta);
        hexColorField.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(
                this.textRenderer,
                "Name",
                nameField.getX(),
                nameField.getY() - textHeight - narrowGap/2,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "X",
                xField.getX(),
                xField.getY() - textHeight - narrowGap/2,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Z",
                zField.getX(),
                zField.getY() - textHeight - narrowGap/2,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Y",
                yField.getX(),
                yField.getY() - textHeight - narrowGap/2,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Yaw",
                yawField.getX(),
                yawField.getY() - textHeight - narrowGap/2,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Pitch",
                pitchField.getX(),
                pitchField.getY() - textHeight - narrowGap/2,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "FOV",
                fovField.getX(),
                fovField.getY() - textHeight - narrowGap/2,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Hue",
                colorSliderWidget.getX(),
                colorSliderWidget.getY() - textHeight - narrowGap/2,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                "Hex",
                hexColorField.getX(),
                hexColorField.getY() - textHeight - narrowGap/2,
                0xFFFFFF
        );

        if (screenshotId != null) {
            int centerX = this.width/2;
            int centerY = this.height/2;

            int x = centerX - wideGap/2 - imgWidth;
            int y = centerY - imgHeight + offsetY;

            context.drawTexture(screenshotId, x, y, 0, 0, imgWidth, imgHeight, imgWidth, imgHeight);
        }
    }

    @Override
    public void removed() {
        if (screenshot != null) screenshot.close();
        if (screenshotTexture != null) screenshotTexture.close();
        super.removed();
    }
}