package net.laisvall.perspectivemanager.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    // --- Constants
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    private static final File CONFIG_FILE = new File(
            MinecraftClient.getInstance().runDirectory,
            "config/perspective_manager/perspective_manager_config.json"
    );

    // --- Singleton instance
    private static ModConfig instance;

    // --- Variables
    public int someHotkey = 82;
    public boolean enableFeature = true;
    public String lastPerspectiveName = "Default";

    // --- Private constructor
    private ModConfig() { }

    // --- Accessor for the singleton instance
    public static ModConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    // --- Methods
    public static ModConfig load() {
        if (!CONFIG_FILE.exists()) {
            instance = new ModConfig();
            save();
        } else {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                instance = GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
                instance = new ModConfig();
            }
        }
        return instance;
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(getInstance(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}