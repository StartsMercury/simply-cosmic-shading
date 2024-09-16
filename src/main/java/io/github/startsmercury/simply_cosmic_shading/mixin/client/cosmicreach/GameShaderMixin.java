package io.github.startsmercury.simply_cosmic_shading.mixin.client.cosmicreach;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import java.util.regex.Pattern;

@Mixin(GameShader.class)
public abstract class GameShaderMixin {
    @Unique
    private static final Pattern TARGET_ASSIGNMENT = Pattern.compile(
        "(?<![a-zA-Z0-9_])vec3\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*skyAmbientColor(?![a-zA-Z0-9_])"
    );

    @Unique
    private static final String REPLACEMENT_ASSIGNMENT =
        "$0; vec3 synthetic_simplyCosmicShading_$1 = skyAmbientColor";

    @ModifyReturnValue(at = @At("RETURN"), method = """
        loadShaderFile(\
            Lfinalforeach/cosmicreach/util/Identifier;\
            Lfinalforeach/cosmicreach/rendering/shaders/GameShader$ShaderType;\
        )Ljava/lang/String;\
    """)
    private String removeDynamicShade(String original) {
        return TARGET_ASSIGNMENT.matcher(original).replaceAll(REPLACEMENT_ASSIGNMENT);
    }
}
