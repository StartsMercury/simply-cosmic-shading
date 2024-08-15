import dev.crmodders.cosmicloom.extention.LoomGradleExtension.getCosmicQuilt
import dev.crmodders.cosmicloom.extention.LoomGradleExtension.getCosmicReach

object Constants {
    const val GROUP = "io.github.startsmercury"
    const val MODID = "simply-cosmic-shading"
    const val VERSION = "0.1.3"

    const val DISPLAY_NAME = "Simply Cosmic Shading"

    const val VERSION_KIND_COSMIC_REACH = "pre-alpha"
    const val VERSION_COSMIC_REACH = "0.1.45"

    const val VERSION_COSMIC_QUILT = "2.1.1"
    const val VERSION_JAVA = "17"
    const val VERSION_MODMENU = "1.0.5"
}

plugins {
    `java-library`
    id("cosmicloom") version "26b522b8a6"
}

base {
    group = Constants.GROUP
    archivesName = Constants.MODID
    version = createVersionString()
}

java {
    withSourcesJar()
    withJavadocJar()

    toolchain.languageVersion = JavaLanguageVersion.of(Constants.VERSION_JAVA)
}

repositories {
    ivy {
        name = "GitHub Releases"
        url = uri("https://github.com")
        patternLayout {
            artifact("[organization]/releases/download/[revision]/[module](-[classifier]).[ext]")
            artifact("[organization]/releases/download/[revision]/[module]-[revision](-[classifier]).[ext]")
            artifact("[organization]/releases/download/v[revision]/[module](-[classifier]).[ext]")
            artifact("[organization]/releases/download/v[revision]/[module]-[revision](-[classifier]).[ext]")
            setM2compatible(true)
        }
        metadataSources {
            artifact()
        }
    }
}

dependencies {
    // Cosmic Reach
    cosmicReach(getCosmicReach(Constants.VERSION_KIND_COSMIC_REACH, Constants.VERSION_COSMIC_REACH))

    // Cosmic Quilt
    modImplementation(getCosmicQuilt(Constants.VERSION_COSMIC_QUILT))

    // Mod Menu
    // modImplementation(
    //     group = "dev.crmodders",
    //     name = "modmenu",
    //     version = Constants.VERSION_MODMENU,
    // )

    // Simply Shaders
    compileOnly(
        group = "Shfloop.SimplyShaders",
        name = "SimplyShaders-1.0.2",
        version = "1.02",
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
