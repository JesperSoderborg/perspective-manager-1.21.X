package net.laisvall.perspectivemanager.client.ui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import org.lwjgl.glfw.GLFW;
import net.laisvall.perspectivemanager.client.data.PerspectiveStorage;

@Environment(EnvType.CLIENT)
public class PerspectiveSaveScreen extends Screen {

    private final MinecraftClient client;
    private TextFieldWidget nameField;
    private ButtonWidget hotkeyButton;

    private Integer selectedHotkey = null; // stores the pressed key
    private boolean waitingForKey = false;

    public PerspectiveSaveScreen(MinecraftClient client) {
        super(Text.of("Save Perspective"));
        this.client = client;
    }

    @Override
    protected void init() {
        // --- Name field ---
        nameField = new TextFieldWidget(this.textRenderer,
                this.width / 2 - 100, this.height / 2 - 30,
                200, 20, Text.of("Name"));
        this.addSelectableChild(nameField);

        // --- Hotkey button ---
        hotkeyButton = ButtonWidget.builder(Text.of("Set Hotkey"), button -> {
                    waitingForKey = true;
                    hotkeyButton.setMessage(Text.of("Press a key..."));
                })
                .position(this.width / 2 - 100, this.height / 2 + 10)
                .size(ButtonWidget.DEFAULT_WIDTH_SMALL, ButtonWidget.DEFAULT_HEIGHT)
                .tooltip(Tooltip.of(Text.of("Click and press a key to assign hotkey")))
                .build();
        this.addDrawableChild(hotkeyButton);

        // --- Save button ---
        ButtonWidget saveButton = ButtonWidget.builder(Text.of("Save"), button -> {
                    String name = nameField.getText().isEmpty() ? "Unnamed" : nameField.getText();
                    int hotkey = selectedHotkey == null ? GLFW.GLFW_KEY_UNKNOWN : selectedHotkey;

                    PerspectiveStorage.getInstance().savePerspective(client, name, hotkey);
                    client.setScreen(null); // close overlay
                })
                .position(this.width / 2 - 50, this.height / 2 + 50)
                .size(ButtonWidget.DEFAULT_WIDTH_SMALL, ButtonWidget.DEFAULT_HEIGHT)
                .tooltip(Tooltip.of(Text.of("Save this perspective")))
                .build();
        this.addDrawableChild(saveButton);

        // --- Cancel button ---
        ButtonWidget cancelButton = ButtonWidget.builder(Text.of("Cancel"), button -> {
                    client.setScreen(null); // close overlay
                })
                .position(this.width / 2 - 50, this.height / 2 + 80)
                .size(ButtonWidget.DEFAULT_WIDTH_SMALL, ButtonWidget.DEFAULT_HEIGHT)
                .tooltip(Tooltip.of(Text.of("Cancel saving")))
                .build();
        this.addDrawableChild(cancelButton);

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
    }
}