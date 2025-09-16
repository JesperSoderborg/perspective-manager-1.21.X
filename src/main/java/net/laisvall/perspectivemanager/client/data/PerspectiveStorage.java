package net.laisvall.perspectivemanager.client.data;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PerspectiveStorage {

    private final List<Perspective> perspectives = new ArrayList<>();
    private final File storageFile;



    public PerspectiveStorage() {

        File configDir = new File(MinecraftClient.getInstance().runDirectory, "config");
        if (!configDir.exists()) configDir.mkdirs();
        storageFile = new File(configDir, "perspectives.json");

        load();
    }

    public static void saveCurrentView(ClientPlayerEntity player) {

    }

    // --- Persistence ---
    public void save() {

    }

    public void load() {

    }
}
