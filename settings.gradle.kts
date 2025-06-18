buildscript {
    repositories {
        maven {
            name = "JitPack"
            url = uri("https://jitpack.io")
        }
        mavenCentral()
    }

    dependencies {
        classpath(
            group = "org.codeberg.CRModders",
            name = "cosmic-loom",
            version = "1a98ea8cae",
        )
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
