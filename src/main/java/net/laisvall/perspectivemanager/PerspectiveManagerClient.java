package net.laisvall.perspectivemanager;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.laisvall.perspectivemanager.client.config.ModConfig;
import net.laisvall.perspectivemanager.client.input.KeybindHandler;

import net.fabricmc.api.ClientModInitializer;
import net.laisvall.perspectivemanager.client.logic.PerspectiveStorage;
import net.laisvall.perspectivemanager.client.ui.MenuButton;
import net.laisvall.perspectivemanager.client.ui.PerspectivePositionRenderer;

public class PerspectiveManagerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModConfig config = ModConfig.load();

        KeybindHandler.register();
        MenuButton.register();
        PerspectivePositionRenderer.init();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            PerspectiveStorage.getInstance().init(client);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            PerspectiveStorage.getInstance().save();
        });
    }
}
