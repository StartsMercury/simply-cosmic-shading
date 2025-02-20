package io.github.startsmercury.simply_cosmic_shading.impl.client;

/**
 * Utility class for Simply Cosmic Shading.
 */
public final class SimplyCosmicShading {
    public final static class GlobalState {
        private GlobalState() {}

        /**
         * Hint if should this mod be disabled.
         */
        private boolean suppressed;

        /**
         * Though this mod does not yet have a config file, some mods have
         * incompatibility or this mod make no sense to stay active in their
         * presence.
         *
         * @return boolean hint if this mod should be disabled
         */
        public boolean isSuppressed() {
            return this.suppressed;
        }

        /**
         * Changes {@code suppressed} hint for this mod.
         *
         * @param suppressed sets the hint
         * @see #isSuppressed()
         */
        public void setSuppressed(final boolean suppressed) {
            this.suppressed = suppressed;
        }

        private StaticShading staticShading = StaticShading.WORLD;

        public StaticShading getStaticShading() {
            return this.staticShading;
        }

        @Deprecated
        @SuppressWarnings("DeprecatedIsStillUsed")
        public void setStaticShadingUnchecked(final StaticShading staticShading) {
            this.staticShading = staticShading;
        }
    }

    public static final GlobalState STATE = new GlobalState();

    /**
     * @deprecated Utility class need not be instantiated.
     *
     * Creates a new instance of SimplyCosmicShading.
     */
    @Deprecated
    private SimplyCosmicShading() {}
}
