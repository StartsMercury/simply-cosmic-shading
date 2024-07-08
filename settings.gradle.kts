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
            version = "1.0.0"
        )
    }
}
