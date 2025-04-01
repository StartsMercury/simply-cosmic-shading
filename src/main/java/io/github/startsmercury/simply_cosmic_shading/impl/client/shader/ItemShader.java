package io.github.startsmercury.simply_cosmic_shading.impl.client.shader;

import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.util.Identifier;

public class ItemShader extends ChunkShader {
    public ItemShader(final Identifier vertFile, final Identifier fragFile) {
        super(vertFile, fragFile);
    }

    @Override
    public String preProcessShaderFile(
        final String shaderText,
        final ShaderType shaderType
    ) {
        final var original = super.preProcessShaderFile(shaderText, shaderType);
        return shaderType != ShaderType.FRAG ? original : injectStaticShading(original);
    }

    private static String injectStaticShading(final String fragmentShader) {
        final var delimiter = fragmentShader.indexOf(';', fragmentShader.lastIndexOf("tintColor")) + 1;

        return fragmentShader.substring(0, delimiter) + """
            vec3 synthetic_simplyCosmicShading_shading = faceNormal;\
            synthetic_simplyCosmicShading_shading.x = 0.6 * abs(synthetic_simplyCosmicShading_shading.x);\
            synthetic_simplyCosmicShading_shading.z = 0.8 * abs(synthetic_simplyCosmicShading_shading.z);\
            outColor.rgb = outColor.rgb * length(synthetic_simplyCosmicShading_shading);\
            """ + fragmentShader.substring(delimiter);
    }
}
