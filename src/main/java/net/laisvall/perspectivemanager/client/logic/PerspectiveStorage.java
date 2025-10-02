package net.laisvall.perspectivemanager.client.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.laisvall.perspectivemanager.client.data.Perspective;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.WorldSavePath;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PerspectiveStorage {
    private static PerspectiveStorage instance;

    public static PerspectiveStorage getInstance() {
        if (instance == null) {
            instance = new PerspectiveStorage();
        }
        return instance;
    }

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Type LIST_TYPE = new TypeToken<List<Perspective>>() {}.getType();
    private List<Perspective> perspectives = new ArrayList<>();

    private Path saveDir;
    private Path jsonFile;
    private Path thumbnailDir;

    private Perspective activePerspective = null;

    private PerspectiveStorage() {}

    public void init(MinecraftClient client) {
        saveDir = client.getServer().getSavePath(WorldSavePath.ROOT).resolve("perspective_manager");
        jsonFile = saveDir.resolve("perspectives.json");
        thumbnailDir = saveDir.resolve("thumbnails");

        try {
            Files.createDirectories(saveDir);
            Files.createDirectories(thumbnailDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        load();
    }

    public void addPerspective(Perspective perspective) {
        perspectives.add(perspective);
        setActivePerspective(perspective);
        save();
    }

    public void removePerspective(Perspective perspective) {
        perspectives.remove(perspective);
        if (activePerspective == perspective) {
            activePerspective = null;
        }
        save();
    }

    public void removePerspective(int index) {
        if (index >= 0 && index < perspectives.size()) {
            Perspective removed = perspectives.remove(index);
            if (removed == activePerspective) {
                activePerspective = null;
            }
            save();
        }
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(jsonFile)) {
            GSON.toJson(perspectives, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        try {
            if (!Files.exists(saveDir)) {
                Files.createDirectories(saveDir);
            }
            if (!Files.exists(thumbnailDir)) {
                Files.createDirectories(thumbnailDir);
            }

            if (Files.exists(jsonFile)) {
                try (Reader reader = Files.newBufferedReader(jsonFile)) {
                    List<Perspective> loaded = GSON.fromJson(reader, LIST_TYPE);
                    perspectives = loaded != null ? loaded : new ArrayList<>();
                }
            } else {
                perspectives = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            perspectives = new ArrayList<>();
        }
    }

    public void setActivePerspective(Perspective perspective) {
        if (perspectives.contains(perspective)) {
            activePerspective = perspective;
        }
    }

    public void setActivePerspective(int index) {
        if (index >= 0 && index < perspectives.size()) {
            activePerspective = perspectives.get(index);
        }
    }

    public Perspective getActivePerspective() { return activePerspective; }
    public List<Perspective> getPerspectives() { return perspectives; }
    public Path getThumbnailDir() { return thumbnailDir; }
}
