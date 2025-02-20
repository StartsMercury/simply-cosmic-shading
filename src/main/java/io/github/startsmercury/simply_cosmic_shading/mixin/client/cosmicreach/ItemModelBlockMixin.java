package io.github.startsmercury.simply_cosmic_shading.mixin.client.cosmicreach;

import static io.github.startsmercury.simply_cosmic_shading.impl.client.SimplyCosmicShading.STATE;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.rendering.items.ItemModelBlock;
import io.github.startsmercury.simply_cosmic_shading.impl.client.StaticShading;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemModelBlock.class)
public class ItemModelBlockMixin {
    @SuppressWarnings("deprecation")
    @WrapOperation(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lfinalforeach/cosmicreach/blocks/BlockState;addVertices(Lfinalforeach/cosmicreach/rendering/IMeshData;III)V"
        )
    )
    private void unwrapMeshDataAfterMeshing(BlockState instance, IMeshData meshData, int bx, int by, int bz, Operation<Void> original) {
        final var staticShading = STATE.getStaticShading();
        try {
            STATE.setStaticShadingUnchecked(StaticShading.SLOT);
            original.call(instance, meshData, bx, by, bz);
        } finally {
            STATE.setStaticShadingUnchecked(staticShading);
        }
    }
}
