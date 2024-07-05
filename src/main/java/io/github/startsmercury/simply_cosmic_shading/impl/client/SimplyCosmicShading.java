package io.github.startsmercury.simply_cosmic_shading.impl.client;

/**
 * Utility class for Simply Cosmic Shading.
 */
public final class SimplyCosmicShading {
    /**
     * Calculates quad directional shade. Quad vertices are labeled A to D, are in
     * counter-clockwise order, and which vertex comes first doesn't matter.
     *
     * @param ax vertex A's x-component
     * @param ay vertex A's y-component
     * @param az vertex A's z-component
     * @param bx vertex B's x-component
     * @param by vertex B's y-component
     * @param bz vertex B's z-component
     * @param cx vertex C's x-component
     * @param cy vertex C's y-component
     * @param cz vertex C's z-component
     * @param dx vertex D's x-component
     * @param dy vertex D's y-component
     * @param dz vertex D's z-component
     * @return quad directional shade
     * @see #normalVectorShade
     */
    public static double quadDirectionalShade(
        final double ax,
        final double ay,
        final double az,
        final double bx,
        final double by,
        final double bz,
        final double cx,
        final double cy,
        final double cz,
        final double dx,
        final double dy,
        final double dz
    ) {
        // U := D - A + C - B
        final double ux = dx - ax + cx - bx;
        final double uy = dy - ay + cy - by;
        final double uz = dz - az + cz - bz;
        // V := D - C + A - B
        final double vx = dx - cx + ax - bx;
        final double vy = dy - cy + ay - by;
        final double vz = dz - cz + az - bz;
        // N := dot(U, V)
        final double nx = uy * vz - uz * vy;
        final double ny = uz * vx - ux * vz;
        final double nz = ux * vy - uy * vx;

        return normalVectorShade(nx, ny, nz);
    }

    /**
     * Calculates directional shade. Approximates directional lighting from the sun
     * as shade or amount of brightness
     * <p>
     * If any component is infinite, before normalization, any infinite value is
     * replaced by {@code +1.0} or {@code -1.0} depending on their sign and any
     * finite value is replaced zero (as if division by infinity occurred);
     * normalization proceeds as normal.
     *
     * @param x the normal vector x-component
     * @param y the normal vector y-component
     * @param z the normal vector z-component
     * @return directional shade
     * @throws IllegalArgumentException if any of the components is {@code NaN}.
     * @see #unscaledNormalVectorShade
     */
    public static double normalVectorShade(final double x, final double y, final double z) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            throw new IllegalArgumentException("NaN is not supported");
        }

        // Detect and handle infinity
        {
            final var xInfinite = Double.isInfinite(x);
            final var yInfinite = Double.isInfinite(y);
            final var zInfinite = Double.isInfinite(z);

            if (xInfinite || yInfinite || zInfinite) {
                // OPTIMIZATION: Calculate 1 / sqrt(n) defined and optimized for n = 1, 2, or 3
                final var magnitude = switch (
                    (xInfinite ? 1 : 0) + (yInfinite ? 1 : 0) + (zInfinite ? 1 : 0)
                ) {
                    case 1 -> 1.0;
                    case 2 -> 0.7071067811865475;
                    case 3 -> 0.5773502691896258;
                    default -> throw new AssertionError("only defined for 1, 2, and 3");
                };

                // Normalizes the normal containing infinities by:
                //  - rescaling singed infinity to a sign with the correct magnitude
                //  - rescaling finite numbers to zero
                return unscaledNormalVectorShade(
                    xInfinite ? Math.copySign(magnitude, x) : 0.0,
                    yInfinite ? Math.copySign(magnitude, y) : 0.0,
                    zInfinite ? Math.copySign(magnitude, z) : 0.0
                );
            }
        }

        // Regular normalization for finite components
        final var magnitude = Math.hypot(Math.hypot(x, y), z);
        return unscaledNormalVectorShade(x / magnitude, y / magnitude, z / magnitude);
    }

    /**
     * Calculates directional shade without normalization. Approximates directional
     * lighting from the sun as shade or amount of brightness. A normalized vector
     * is a vector with a magnitude/absolute value/length of exactly one
     * ({@code 1.0}).
     *
     * @param x the normal vector x-component
     * @param y the normal vector y-component
     * @param z the normal vector z-component
     * @return directional shade represented as a decimal between zero and one
     * @implSpec <pre>{@code
     * ( x,  y,  z) -> brightness
     * ( 0, +1,  0) -> 1.0
     * ( 0,  0, ±1) -> 0.8
     * (±1,  0,  0) -> 0.6
     * ( 0, -1,  0) -> 0.5
     * }</pre>
     * @deprecated Behavior is undefined for non-normalized vectors.
     */
    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    public static double unscaledNormalVectorShade(
        final double x,
        final double y,
        final double z
    ) {
        // Calculate shade per component.
        final double shadeX = 0.6 * Math.abs(x);
        final double shadeY = y >= 0.0 ? y : -0.5 * y;
        final double shadeZ = 0.8 * Math.abs(z);

        // Somehow join component shade into one value.
        // return Math.max(Math.max(shadeX, shadeY), shadeZ);
        return Math.hypot(Math.hypot(shadeX, shadeY), shadeZ);
    }

    /**
     * @deprecated Utility class need not be instantiated.
     *
     * Creates a new instance of SimplyCosmicShading.
     */
    @Deprecated
    private SimplyCosmicShading() {}
}
