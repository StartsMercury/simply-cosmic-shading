import dev.crmodders.cosmicloom.extention.LoomGradleExtension.getCosmicQuilt
import dev.crmodders.cosmicloom.extention.LoomGradleExtension.getCosmicReach
import dev.crmodders.cosmicloom.task.tasks.RunClientTask
import org.apache.commons.lang3.StringUtils

buildscript {
    dependencies {
        classpath(
            group = "org.apache.commons",
            name = "commons-lang3",
            version = "3.17.0",
        )
    }
}

object Constants {
    const val GROUP = "io.github.startsmercury"
    const val MODID = "simply-cosmic-shading"
    const val VERSION = "0.3.0"

    const val DISPLAY_NAME = "Simply Cosmic Shading"

    const val VERSION_COSMIC_REACH = "0.4.11"
    const val VERSION_JAVA = "24"
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

loom {
    accessWidenerPath = file("src/main/resources/simply-cosmic-shading.accesswidener")
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
            setM2compatible(true)
        }
        metadataSources {
            artifact()
        }
    }

    ivy {
        name = "CRMM API"
        url = uri("https://api.crmm.tech/")
        patternLayout {
            artifact("[organization]/version/[revision]/[module](-[classifier]).[ext]")
            setM2compatible(true)
        }
        metadataSources {
            artifact()
        }
        content {
            includeGroupAndSubgroups("api.project")
        }
    }
}

dependencies {
    // Cosmic Reach
    cosmicReach(getCosmicReach("alpha", Constants.VERSION_COSMIC_REACH))

    // Cosmic Quilt
    modImplementation(getCosmicQuilt("60b1044fb3"))

    // Mod Menu
    modImplementation(
        group = "org.codeberg.CRModders",
        name = "modmenu",
        version = "2d0eae1916",
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

            source = "24"
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

    withType<dev.crmodders.cosmicloom.task.abstracts.AbstractRunTask> {
        javaLauncher = project.javaToolchains.launcherFor(java.toolchain)
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

tasks.withType(RunClientTask::class) {
    jvmArgs("-Dmixin.debug=true")
}

createCompatTest("fluxResourceLoader", mapOf(
    "group" to "com.github.CRModders.FluxAPI",
    "name" to "flux-resource-loader-v0",
    "version" to "d149c5d1305904199d7125d0479ca37323f49533",
))
createCompatTest("simplyShaders", mapOf(
    "group" to "api.project.simplyshaders",
    "name" to "SimplyShadersQ-1.2.1",
    "version" to "1.2.1q",
))

fun createCompatTest(name: String, objectNotation: Any, vararg dependencyNotations: Any) {
    val config = configurations.register(name)
    val configClasspath = configurations.register("${name}Classpath") {
        extendsFrom(config.get())
    }

    configurations {
        val compileOnly by getting {
            extendsFrom(config.get())
        }
    }

    dependencies {
        add(name, objectNotation)
        dependencyNotations.forEach {
            add("${name}Classpath", it)
        }
    }

    afterEvaluate {
        tasks.register<RunClientTask>("run${StringUtils.capitalize(name)}") {
            jvmArgs("-Dfabric.addMods=${configClasspath.get().files.joinToString(File.pathSeparator)}")
        }
    }
}
