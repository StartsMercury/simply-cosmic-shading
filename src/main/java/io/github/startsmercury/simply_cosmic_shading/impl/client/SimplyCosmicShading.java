package io.github.startsmercury.simply_cosmic_shading.impl.client;

import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.util.Identifier;
import io.github.startsmercury.simply_cosmic_shading.impl.client.config.ConfigV0;
import io.github.startsmercury.simply_cosmic_shading.impl.client.config.LatestConfigParser;
import io.github.startsmercury.simply_cosmic_shading.impl.client.shader.BlockShader;
import io.github.startsmercury.simply_cosmic_shading.impl.client.shader.ItemShader;
import io.github.startsmercury.simply_cosmic_shading.impl.client.shader.WaterItemShader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Objects;
import org.quiltmc.loader.api.QuiltLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for Simply Cosmic Shading.
 */
public final class SimplyCosmicShading {
    public static final String MOD_NAME = "Simply Cosmic Shading";
    public static final String MOD_ID = "simply-cosmic-shading";
    public static final String CONFIG = MOD_ID + ".json";

    private static SimplyCosmicShading instance;

    public static SimplyCosmicShading getInstance() {
        final var instance = SimplyCosmicShading.instance;
        if (instance == null) {
            final var message = SimplyCosmicShading.class.getName() + " is not yet initialized";
            throw new IllegalStateException(message);
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public static void initSingleton() {
        if (instance != null) {
            final var message = SimplyCosmicShading.class.getName() + "is already initialized";
            throw new IllegalStateException(message);
        }
        instance = new SimplyCosmicShading();
    }

    private final ChunkShader defaultBlockShader;

    private final ChunkShader defaultBlockItemShader;

    private final ChunkShader waterBlockItemShader;

    private final LatestConfigParser configParser = new LatestConfigParser();

    private final Logger logger = LoggerFactory.getLogger(MOD_NAME);

    private ConfigV0 config = new ConfigV0();

    private boolean suppressed;

    /**
     * Creates a new instance of SimplyCosmicShading.
     */
    private SimplyCosmicShading() {
        defaultBlockShader = new BlockShader(
            Identifier.of("shaders/chunk.vert.glsl"),
            Identifier.of("shaders/chunk.frag.glsl")
        );
        defaultBlockItemShader = new ItemShader(
            Identifier.of("shaders/chunk.vert.glsl"),
            Identifier.of("shaders/chunk.frag.glsl")
        );
        waterBlockItemShader = new WaterItemShader(
            Identifier.of("shaders/chunk-water.vert.glsl"),
            Identifier.of("shaders/chunk-water.frag.glsl")
        );
    }

    public ChunkShader defaultBlockShader() {
        return this.defaultBlockShader;
    }

    public ChunkShader defaultBlockItemShader() {
        return this.defaultBlockItemShader;
    }

    public ChunkShader waterBlockItemShader() {
        return this.waterBlockItemShader;
    }

    public ConfigV0 getConfig() {
        return this.config;
    }

    public void setConfig(final ConfigV0 config) {
        Objects.requireNonNull(config, "Parameter config is null");
        this.config = config;
    }

    public void loadConfig() {
        try (final var reader = Files.newBufferedReader(QuiltLoader.getConfigDir().resolve(CONFIG))) {
            this.setConfig(this.configParser.read(reader));
            this.logger.info("[{}] Successfully loaded config", MOD_NAME);
        } catch (final NoSuchFileException cause) {
            this.saveConfig("[{}] Successfully created config", "[{}] Unable to create config");
        } catch (final IOException | RuntimeException cause) {
            this.logger.warn("[{}] Unable to load config", MOD_NAME, cause);
            this.saveConfig("[{}] Successfully recreated config", "[{}] Unable to recreate config");
        }
    }

    public void saveConfig() {
        this.saveConfig("[{}] Successfully saved config", "[{}] Unable to save config");
    }

    private void saveConfig(final String success, final String error) {
        try (final var writer = Files.newBufferedWriter(QuiltLoader.getConfigDir().resolve(CONFIG))) {
            this.configParser.write(writer, this.config);
            this.logger.info(success, MOD_NAME);
        } catch (final IOException | RuntimeException cause) {
            this.logger.warn(error, MOD_NAME, cause);
        }
    }

    public boolean isSuppressed() {
        return this.suppressed;
    }

    public void setSuppressed(final boolean suppressed) {
        this.suppressed = suppressed;
    }
}
