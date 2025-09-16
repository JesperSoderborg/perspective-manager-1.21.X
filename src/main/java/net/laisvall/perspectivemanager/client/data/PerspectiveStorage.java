package net.laisvall.perspectivemanager.client.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PerspectiveStorage {
    private final List<Perspective> perspectives = new ArrayList<>();
    private final File storageFile;
    private static Perspective activePerspective = null;
    private final Gson gson = new Gson();

    private static PerspectiveStorage instance;

    private PerspectiveStorage() {
        File configDir = new File(MinecraftClient.getInstance().runDirectory, "config");
        if (!configDir.exists()) configDir.mkdirs();
        storageFile = new File(configDir, "perspectives.json");

        load();
    }

    public static PerspectiveStorage getInstance() {
        if (instance == null) {
            instance = new PerspectiveStorage();
        }
        return instance;
    }

    public void savePerspective(MinecraftClient client, String name, int hotkey) {
        Perspective newPerspective = new Perspective(
                client.player.getX(),
                client.player.getY(),
                client.player.getZ(),
                client.player.getYaw(),
                client.player.getPitch(),
                client.options.getFov().getValue(),
                name,
                hotkey
        );

        perspectives.add(newPerspective);
        setActivePerspective(newPerspective);

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
        try (FileWriter writer = new FileWriter(storageFile)) {
            gson.toJson(perspectives, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        if (!storageFile.exists()) return;

        try (FileReader reader = new FileReader(storageFile)) {
            Type listType = new TypeToken<ArrayList<Perspective>>() {}.getType();
            List<Perspective> loaded = gson.fromJson(reader, listType);
            if (loaded != null) {
                perspectives.clear();
                perspectives.addAll(loaded);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public Perspective getActivePerspective() {
        return activePerspective;
    }

    public List<Perspective> getPerspectives() {
        return perspectives;
    }
}
