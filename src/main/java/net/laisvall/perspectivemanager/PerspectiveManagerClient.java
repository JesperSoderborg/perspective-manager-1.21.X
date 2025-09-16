package net.laisvall.perspectivemanager;

import net.laisvall.perspectivemanager.client.config.ModConfig;
import net.laisvall.perspectivemanager.client.input.KeybindHandler;

import net.fabricmc.api.ClientModInitializer;
import net.laisvall.perspectivemanager.client.ui.MenuButton;
import net.laisvall.perspectivemanager.client.ui.PerspectivePositionRenderer;

public class PerspectiveManagerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModConfig config = ModConfig.load();

        KeybindHandler.register();
        MenuButton.register();
        PerspectivePositionRenderer.init();
    }
}
