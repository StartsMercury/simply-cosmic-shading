package io.github.startsmercury.simply_cosmic_shading.impl.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.util.List;

public final class LatestConfigParser {
    private final Gson gson;
    private final JsonParser parser;

    public LatestConfigParser() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.parser = new JsonParser();
    }

    public ConfigV0 read(final Reader reader) {
        final var jsonObject = (JsonObject) this.parser.parse(reader);

        final var version = jsonObject.get("version").getAsInt();
        final var clazz = switch (version) {
            case 0 -> ConfigV0.class;
            default -> ConfigV0.class;
        };
        final var config = this.gson.fromJson(jsonObject, clazz);

        return upgrade(version, config);
    }

    private static ConfigV0 upgrade(final int version, final Config config) {
        // Replace with switch-pattern on `config` when available.
        return switch (version) {
            case 0 -> (ConfigV0) config;
            default -> (ConfigV0) config;
        };
    }

    public void write(final Appendable appendable, final Config config) {
        final var jsonObject = (JsonObject) this.gson.toJsonTree(config);
        final var entries = List.copyOf(jsonObject.entrySet());
        jsonObject.entrySet().clear();
        jsonObject.addProperty("version", config.version());
        for (final var entry : entries) {
            jsonObject.add(entry.getKey(), entry.getValue());
        }
        this.gson.toJson(jsonObject, appendable);
    }
}
