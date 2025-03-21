package io.github.startsmercury.simply_cosmic_shading.mixin.client.cosmicreach;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import finalforeach.cosmicreach.rendering.items.ItemModelBlock;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import io.github.startsmercury.simply_cosmic_shading.impl.client.SimplyCosmicShading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelBlock.class)
public class ItemModelBlockMixin {
    @Shadow
    GameShader shader;

    @Unique
    private GameShader simplyCosmicShading$shader;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initShaders(final CallbackInfo callback) {
        this.simplyCosmicShading$shader = this.fixAndMapShader(this.shader);
    }

    @Unique
    private GameShader fixAndMapShader(final GameShader original) {
        final var simplyCosmicShading = SimplyCosmicShading.getInstance();

        if (original == simplyCosmicShading.defaultBlockShader()) {
            this.shader = ChunkShader.DEFAULT_BLOCK_SHADER;
            return simplyCosmicShading.defaultBlockItemShader();
        } else if (original == simplyCosmicShading.waterBlockItemShader()) {
            this.shader = ChunkShader.WATER_BLOCK_SHADER;
        } else if (original == ChunkShader.DEFAULT_BLOCK_SHADER) {
            return simplyCosmicShading.defaultBlockItemShader();
        }

        return original;
    }

    @WrapMethod(method = "render")
    private void tryUseOurShader(
        final Vector3 worldPosition,
        final Camera camera,
        final Matrix4 modelMat,
        final boolean useAmbientLighting,
        final boolean applyFog,
        final Operation<Void> original
    ) {
        final var shader = this.shader;
        try {
            if (SimplyCosmicShading.getInstance().getConfig().blockItemShadingEnabled()) {
                this.shader = this.simplyCosmicShading$shader;
            }
            original.call(worldPosition, camera, modelMat, useAmbientLighting, applyFog);
        } finally {
            this.shader = shader;
        }
    }
}
