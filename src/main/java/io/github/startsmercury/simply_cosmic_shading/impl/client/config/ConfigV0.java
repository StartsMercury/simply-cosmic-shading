package io.github.startsmercury.simply_cosmic_shading.impl.client.config;

public record ConfigV0(
    boolean terrainBlockShadingEnabled,
    boolean blockItemShadingEnabled
) implements Config {
    public ConfigV0() {
        this(true, true);
    }

    @Override
    public int version() {
        return 0;
    }
}
