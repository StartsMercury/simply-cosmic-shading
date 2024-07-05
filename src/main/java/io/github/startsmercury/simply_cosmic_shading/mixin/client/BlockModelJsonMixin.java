package io.github.startsmercury.simply_cosmic_shading.mixin.client;

import com.badlogic.gdx.graphics.Color;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonCuboidFace;
import io.github.startsmercury.simply_cosmic_shading.impl.client.SimplyCosmicShading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class.
 *
 * @see Mixin
 */
@Mixin(BlockModelJson.class)
public abstract class BlockModelJsonMixin {
    /**
     * Calculates model face's shade.
     *
     * @param callback mixin injector callback
     * @param face the model face to calculate shade for
     * @param shadeRef shared reference storing the shade
     * @see SimplyCosmicShading#quadDirectionalShade
     */
    @Inject(
        method = "addVertices(Lfinalforeach/cosmicreach/rendering/IMeshData;IIII[S[I)V",
        at = @At(value = "INVOKE", ordinal = 0, target = """
            Lfinalforeach/cosmicreach/rendering/blockmodels/BlockModelJson;\
            addVert(\
                Lfinalforeach/cosmicreach/rendering/IMeshData;\
                F\
                F\
                F\
                F\
                F\
                I\
                S\
                I\
                I\
            )I\
        """)
    )
    private void calculateShade(
        final CallbackInfo callback,
        final @Local(ordinal = 0) BlockModelJsonCuboidFace face,
        final @Share("shade") LocalDoubleRef shadeRef
    ) {
        shadeRef.set(SimplyCosmicShading.quadDirectionalShade(
            face.x1,
            face.y1,
            face.z1,
            face.midX1,
            face.midY1,
            face.midZ1,
            face.x2,
            face.y2,
            face.z2,
            face.midX2,
            face.midY2,
            face.midZ2
        ));
    }

    /**
     * Modifies vertex lighting data to apply directional shade.
     *
     * @param callback the injector callback
     * @param meshData the mesh data to modify containing vertex data
     * @param shadeRef shared reference storing the shade
     * @see #calculateShade
     */
    @Inject(
        method = "addVertices(Lfinalforeach/cosmicreach/rendering/IMeshData;IIII[S[I)V",
        at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = """
            Lfinalforeach/cosmicreach/rendering/blockmodels/BlockModelJson;\
            addVert(\
                Lfinalforeach/cosmicreach/rendering/IMeshData;\
                F\
                F\
                F\
                F\
                F\
                I\
                S\
                I\
                I\
            )I\
        """)
    )
    private void modifyLightingTintToApplyShade(
        final CallbackInfo callback,
        final @Local(ordinal = 0, argsOnly = true) IMeshData meshData,
        final @Share("shade") LocalDoubleRef shadeRef
    ) {
        final var vertices = meshData.getVertices();
        final var items = vertices.items;
        final var idx = vertices.size - 2;

        final var color = Float.floatToRawIntBits(items[idx]);
        final var skyLight = color >> 24 & 0xFF;
        final var blue = color >> 16 & 0xFF;
        final var green = color >> 8 & 0xFF;
        final var red = color & 0xFF;

        final var shade = shadeRef.get();
        final var luma = (int) (shade * Math.max(Math.max(red, green), blue));
        items[idx] = Color.toFloatBits(
            Math.min(red, luma),
            Math.min(green, luma),
            Math.min(blue, luma),
            (int) (shade * skyLight)
        );
    }
}
