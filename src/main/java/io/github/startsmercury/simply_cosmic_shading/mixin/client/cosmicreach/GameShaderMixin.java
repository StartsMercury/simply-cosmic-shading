package io.github.startsmercury.simply_cosmic_shading.mixin.client.cosmicreach;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import io.github.startsmercury.simply_cosmic_shading.impl.client.SimplyCosmicShading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameShader.class)
public abstract class GameShaderMixin {
    @ModifyReturnValue(method = "getShaderForBlockState", at = @At("RETURN"))
    private static GameShader replaceBlockStateShader(final GameShader original) {
        final var simplyCosmicShading = SimplyCosmicShading.getInstance();

        if (simplyCosmicShading.isSuppressed()) return original;

        final var config = simplyCosmicShading.getConfig();

        if (original == ChunkShader.DEFAULT_BLOCK_SHADER && config.terrainBlockShadingEnabled()) {
            return simplyCosmicShading.defaultBlockShader();
        }

        if (original == ChunkShader.WATER_BLOCK_SHADER && config.blockItemShadingEnabled()) {
            return simplyCosmicShading.waterBlockItemShader();
        }

        return original;
    }
}
