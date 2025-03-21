package io.github.startsmercury.simply_cosmic_shading.impl.client.entrypoint;

import dev.crmodders.modmenu.api.ConfigScreenFactory;
import dev.crmodders.modmenu.api.ModMenuApi;
import io.github.startsmercury.simply_cosmic_shading.impl.client.SimplyCosmicShading;
import io.github.startsmercury.simply_cosmic_shading.impl.client.config.ConfigGameState;

public class SimplyCosmicShadingModMenuInitializer implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return previousGameState -> new ConfigGameState(SimplyCosmicShading.getInstance(), previousGameState);
    }
}
