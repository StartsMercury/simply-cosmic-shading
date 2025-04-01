package io.github.startsmercury.simply_cosmic_shading.impl.client.util;

import finalforeach.cosmicreach.lang.Lang;
import java.util.Objects;

public final class LangUtil {
    public static String get(final String key, final String fallback) {
        final var value = Lang.get(key);
        return Objects.equals(value, key) ? fallback : value;
    }

    private LangUtil() {}
}
