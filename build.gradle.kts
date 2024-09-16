import dev.crmodders.cosmicloom.extention.LoomGradleExtension.getCosmicQuilt
import dev.crmodders.cosmicloom.extention.LoomGradleExtension.getCosmicReach

object Constants {
    const val GROUP = "io.github.startsmercury"
    const val MODID = "simply-cosmic-shading"
    const val VERSION = "0.1.5"

    const val DISPLAY_NAME = "Simply Cosmic Shading"

    const val VERSION_COSMIC_REACH = "0.2.5"
    const val VERSION_JAVA = "17"
}

plugins {
    `java-library`
    `maven-publish`
    id("cosmicloom")
}

if (group == path.substring(1).replace(':', '.')) {
    group = Constants.GROUP
}

base {
    archivesName = Constants.MODID
}

if (Project.DEFAULT_VERSION == version) {
    version = createVersionString()
}

java {
    withSourcesJar()
    withJavadocJar()

    toolchain.languageVersion = JavaLanguageVersion.of(Constants.VERSION_JAVA)
}

dependencies {
    // Cosmic Reach
    cosmicReach(getCosmicReach("pre-alpha", Constants.VERSION_COSMIC_REACH))

    // Cosmic Quilt
    modImplementation(getCosmicQuilt("2.2.0"))

    // Mod Menu
    modImplementation(
        group = "org.codeberg.CRModders",
        name = "modmenu",
        version = "1.0.7",
    )

    // Simply Shaders (v1.1.2)
    compileOnly(
        group = "com.github.Shfloop",
        name = "SimplyShaders",
        version = "PR3-SNAPSHOT",
    )
}

tasks {
    withType<ProcessResources> {
        // Locations of where to inject the properties
        val resourceTargets = listOf(
            "quilt.mod.json",
        )

        // Left item is the name in the target, right is the variable name
        val replaceProperties = mutableMapOf<String, Any>(
            "group" to Constants.GROUP,
            "modid" to Constants.MODID,
            "display_name" to Constants.DISPLAY_NAME,
            "version" to Constants.VERSION,

            "cosmicreach_version" to Constants.VERSION_COSMIC_REACH,
        )

        inputs.properties(replaceProperties)
        replaceProperties["project"] = project

        filesMatching(resourceTargets) {
            expand(replaceProperties)
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    javadoc {
        options {
            this as StandardJavadocDocletOptions

            source = "17"
            encoding = "UTF-8"
            charSet = "UTF-8"
            memberLevel = JavadocMemberLevel.PACKAGE
            addStringOption("Xdoclint:none", "-quiet")
            tags(
                "apiNote:a:API Note:",
                "implSpec:a:Implementation Requirements:",
                "implNote:a:Implementation Note:",
            )
        }

        source(sourceSets.main.get().allJava)
        classpath = files(sourceSets.main.get().compileClasspath)
        include("**/api/**")
        isFailOnError = true
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

fun createVersionString(): String {
    val builder = StringBuilder()

    val isReleaseBuild = project.hasProperty("build.release")
    val buildId = System.getenv("GITHUB_RUN_NUMBER")

    if (isReleaseBuild) {
        builder.append(Constants.VERSION)
    } else {
        builder.append(Constants.VERSION.substringBefore('-'))
        builder.append("-snapshot")
    }

    builder.append("+cr").append(Constants.VERSION_COSMIC_REACH)

    if (!isReleaseBuild) {
        if (buildId != null) {
            builder.append("-build.${buildId}")
        } else {
            builder.append("-local")
        }
    }

    return builder.toString()
}

