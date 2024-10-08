# Simply Cosmic Shading

Adds static directional shade to blocks in Cosmic Reach.

| With Shade (Mod Installed) | Without Shade (Cosmic Reach as of 0.1.43) |
|:--------------------------:|:-----------------------------------------:|
| ![coconut-scene-with-mod]  |       ![coconut-scene-without-mod]        |

[coconut-scene-with-mod]: assets/coconut-scene-with-mod.png
[coconut-scene-without-mod]: assets/coconut-scene-without-mod.png

## Downloads

Simply Cosmic Shading is only officially available on CRMM and GitHub. All
published version of this mod are both in
[CRMM](https://www.crmm.tech/mod/simply-cosmic-shading/versions) and [GitHub Releases](https://github.com/StartsMercury/simply-cosmic-shading/releases):

> * <https://www.crmm.tech/mod/simply-cosmic-shading/versions>
> * <https://github.com/StartsMercury/simply-cosmic-shading/releases>

---

This repository is generated from
https://codeberg.org/CRModders/cosmic-quilt-example.

## Wiki

For a wiki on how to use Cosmic Quilt & Quilt, please look at the [Cosmic Quilt
wiki] .

## How to test/build

For testing in the developer environment, you can use the `./gradlew run` task

For building, the usual `./gradlew build` task can be used. The mod jars will be
in the `build/libs/` folder

## Notes
- Most project properties can be changed in the <tt>[gradle.properties]</tt>
- To change author, description and stuff that is not there, edit <tt>[src/main/resources/quilt.mod.json]</tt>
- The project name is defined in <tt>[settings.gradle.kts]</tt>
- To add Quilt mods in the build, make sure to use `internal` rather than `implementation`

[src/main/resources/quilt.mod.json]: src/main/resources/quilt.mod.json
[gradle.properties]: gradle.properties
[settings.gradle.kts]: settings.gradle.kts

[Cosmic Quilt wiki]: https://codeberg.org/CRModders/cosmic-quilt/wiki
