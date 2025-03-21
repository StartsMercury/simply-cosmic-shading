package io.github.startsmercury.simply_cosmic_shading.impl.client.entrypoint;

import dev.crmodders.cosmicquilt.api.entrypoint.client.ClientModInitializer;
import io.github.startsmercury.simply_cosmic_shading.impl.client.SimplyCosmicShading;
import org.quiltmc.loader.api.ModContainer;

import static io.github.startsmercury.simply_cosmic_shading.impl.client.SimplyCosmicShading.MOD_NAME;

public class SimplyCosmicShadingClientInitializer implements ClientModInitializer {
    private static final String SHUTDOWN_HOOK_NAME = MOD_NAME + " Shutdown Save";

    @Override
    public void onInitializeClient(ModContainer modContainer) {
        SimplyCosmicShading.initSingleton();
        final var simplyCosmicShading = SimplyCosmicShading.getInstance();
        simplyCosmicShading.loadConfig();

        final var shutdownHook = new Thread(simplyCosmicShading::saveConfig, SHUTDOWN_HOOK_NAME);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
