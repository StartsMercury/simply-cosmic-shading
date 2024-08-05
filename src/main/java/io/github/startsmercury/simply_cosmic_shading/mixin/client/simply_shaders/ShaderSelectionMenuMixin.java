package io.github.startsmercury.simply_cosmic_shading.mixin.client.simply_shaders;

import com.shfloop.simply_shaders.ShaderSelectionMenu;
import com.shfloop.simply_shaders.Shadows;
import io.github.startsmercury.simply_cosmic_shading.impl.client.SimplyCosmicShading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin class.
 *
 * @see Mixin
 */
@Mixin(ShaderSelectionMenu.class)
public class ShaderSelectionMenuMixin {
    /**
     * {@linkplain SimplyCosmicShading#isSuppressed()} Suppresses}/disables
     * Simply Cosmic Shading if shaders are enabled.
     *
     * @param callback the mixin injector callback
     */
    @Inject(method = "applyShaderPackSelection()V", at = @At("RETURN"))
    private void updateSuppressed(final CallbackInfo callback) {
        SimplyCosmicShading.setSuppressed(Shadows.shaders_on);
    }
}
