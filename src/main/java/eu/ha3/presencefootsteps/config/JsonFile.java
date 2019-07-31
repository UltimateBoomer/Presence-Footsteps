package eu.ha3.presencefootsteps.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

public abstract class JsonFile {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(getClass(), (InstanceCreator<JsonFile>)t -> this)
            .create();

    private transient Path file;

    JsonFile() { }

    public JsonFile(Path file) {
        this.file = file;

        if (Files.isReadable(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        save();
    }

    public final void load(Reader reader) {
        gson.fromJson(reader, getClass());
    }

    public final void save() {
        try {
            Files.deleteIfExists(file);

            try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                gson.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
