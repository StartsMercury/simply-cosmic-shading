package io.github.startsmercury.simply_cosmic_shading.impl.client.shader;

import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import finalforeach.cosmicreach.util.Identifier;

public class BlockShader extends ChunkShader {
    public BlockShader(final Identifier vertFile, final Identifier fragFile) {
        super(vertFile, fragFile);
    }

    @Override
    public String preProcessShaderFile(
        final String shaderText,
        final GameShader.ShaderType shaderType
    ) {
        final var original = super.preProcessShaderFile(shaderText, shaderType);
        return switch (shaderType) {
            case FRAG -> disableSunBasedShading(original);
            case VERT -> injectStaticShading(original);
            default -> original;
        };
    }

    private static String disableSunBasedShading(final String fragmentShader) {
        return fragmentShader.replaceFirst(
            "(?<![a-zA-Z0-9_])vec3\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s*=\\s*skyAmbientColor(?![a-zA-Z0-9_])",
            "$0; vec3 synthetic_simplyCosmicShading_$1 = skyAmbientColor"
        );
    }

    private static String injectStaticShading(final String vertexShader) {
        final var delimiter = vertexShader.lastIndexOf('}');
        return vertexShader.substring(0, delimiter) + """
            vec3 synthetic_simplyCosmicShading_shading = faceNormal;\
            synthetic_simplyCosmicShading_shading.x = 0.6 * abs(synthetic_simplyCosmicShading_shading.x);\
            synthetic_simplyCosmicShading_shading.z = 0.8 * abs(synthetic_simplyCosmicShading_shading.z);\
            if (synthetic_simplyCosmicShading_shading.y < 0.0) {\
                synthetic_simplyCosmicShading_shading.y = 0.5 * -synthetic_simplyCosmicShading_shading.y;\
            }\
            blocklight = blocklight * length(synthetic_simplyCosmicShading_shading);\
            """ + vertexShader.substring(delimiter);
    }
}
