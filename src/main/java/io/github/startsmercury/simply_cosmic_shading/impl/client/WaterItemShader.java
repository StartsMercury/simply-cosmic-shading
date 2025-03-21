package io.github.startsmercury.simply_cosmic_shading.impl.client;

import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.util.Identifier;

final class WaterItemShader extends ChunkShader {
    public WaterItemShader(final Identifier vertFile, final Identifier fragFile) {
        super(vertFile, fragFile);
    }

    @Override
    public String preProcessShaderFile(
        final String shaderText,
        final ShaderType shaderType
    ) {
        final var original = super.preProcessShaderFile(shaderText, shaderType);
        return switch (shaderType) {
            case FRAG -> injectStaticShading(original);
            case VERT -> injectFaceNormal(original);
            default -> original;
        };
    }

    private static String injectFaceNormal(final String vertexShader) {
        final var storageEnd = vertexShader.indexOf(';', vertexShader.lastIndexOf("\nout ")) + 1;
        final var mainEnd = vertexShader.lastIndexOf('}');

        return vertexShader.substring(0, storageEnd)
            + "out vec3 synthetic_simplyCosmicShading_faceNormal;"
            + vertexShader.substring(storageEnd, mainEnd)
            + "synthetic_simplyCosmicShading_faceNormal = GET_FACE_NORMAL;"
            + vertexShader.substring(mainEnd);
    }

    private static String injectStaticShading(final String fragmentShader) {
        final var storageEnd = fragmentShader.indexOf(';', fragmentShader.lastIndexOf("\nin ")) + 1;
        final var afterTinting = fragmentShader.indexOf(';', fragmentShader.lastIndexOf("tintColor")) + 1;

        return fragmentShader.substring(0, storageEnd)
            + "in vec3 synthetic_simplyCosmicShading_faceNormal;"
            + fragmentShader.substring(storageEnd, afterTinting)
            + """
            vec3 synthetic_simplyCosmicShading_shading = synthetic_simplyCosmicShading_faceNormal;\
            synthetic_simplyCosmicShading_shading.x = 0.6 * abs(synthetic_simplyCosmicShading_shading.x);\
            synthetic_simplyCosmicShading_shading.z = 0.8 * abs(synthetic_simplyCosmicShading_shading.z);\
            outColor.rgb = outColor.rgb * length(synthetic_simplyCosmicShading_shading);\
            """
            + fragmentShader.substring(afterTinting);
    }
}
