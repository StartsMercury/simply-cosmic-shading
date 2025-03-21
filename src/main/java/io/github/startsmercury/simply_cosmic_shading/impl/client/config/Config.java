package io.github.startsmercury.simply_cosmic_shading.impl.client.config;

public sealed interface Config permits ConfigV0 {
    int version();
}
