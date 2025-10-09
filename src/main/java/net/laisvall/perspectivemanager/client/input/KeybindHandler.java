package net.laisvall.perspectivemanager.client.input;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.laisvall.perspectivemanager.client.logic.PerspectiveStorage;
import net.laisvall.perspectivemanager.client.logic.PerspectiveSwitcher;
import net.laisvall.perspectivemanager.client.ui.PerspectiveSaveScreen;
import net.laisvall.perspectivemanager.client.util.ScreenshotUtil;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.InputUtil;

import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {
    public static KeyBinding SAVE_PERSPECTIVE;
    public static KeyBinding CAROUSEL;
    public static KeyBinding TOGGLE_LAST;

    public static void register() {
        SAVE_PERSPECTIVE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.perspectivemanager.save_perspective",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.perspectivemanager"
        ));

        CAROUSEL = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.perspectivemanager.carousel",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.perspectivemanager"
        ));

        TOGGLE_LAST = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.perspectivemanager.toggle_last",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "key.categories.perspectivemanager"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (SAVE_PERSPECTIVE.wasPressed()) {
                NativeImage image = ScreenshotUtil.takeHUDLessScreenshot(client);
                client.setScreen(new PerspectiveSaveScreen(client, image));
            }

            if (CAROUSEL.isPressed()) {
            } else {
            }

            if (TOGGLE_LAST.wasPressed()) {
                var activePerspective = PerspectiveStorage.getInstance().getActivePerspective();
                if (activePerspective != null) {
                    PerspectiveSwitcher.getInstance().switchPerspective(client, activePerspective);
                } else {
                    client.player.sendMessage(Text.of("§cPlease select a perspective (Alt+F4)."), true);
                }
            }
        });
    }
}
