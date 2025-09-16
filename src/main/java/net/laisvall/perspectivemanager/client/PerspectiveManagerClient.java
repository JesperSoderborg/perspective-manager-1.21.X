package net.laisvall.perspectivemanager.client;

import net.laisvall.perspectivemanager.client.config.ModConfig;
import net.laisvall.perspectivemanager.client.data.PerspectiveStorage;
import net.laisvall.perspectivemanager.client.input.KeybindHandler;

import net.fabricmc.api.ClientModInitializer;
import net.laisvall.perspectivemanager.client.ui.MenuButton;

public class PerspectiveManagerClient implements ClientModInitializer {

    private static PerspectiveStorage storage;
    private static ModConfig config;

    @Override
    public void onInitializeClient() {
        storage = new PerspectiveStorage();
        config = ModConfig.load();

        KeybindHandler.register();
        MenuButton.register();

    }

    public static PerspectiveStorage getStorage() {
        return storage;
    }

    public static ModConfig getConfig() {
        return config;
    }
}
