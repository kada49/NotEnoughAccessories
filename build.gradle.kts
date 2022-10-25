import gg.essential.gradle.util.noServerRunConfigs

plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults")
}

val modGroup: String by project
val modBaseName: String by project
group = modGroup
base.archivesName.set(modBaseName)
version = "0.1.1"

loom {
    noServerRunConfigs()
    launchConfigs {
        getByName("client") {
            arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
        }
    }
}

val include: Configuration by configurations.creating
configurations.implementation.get().extendsFrom(include)

dependencies {
    include("gg.essential:loader-launchwrapper:1.1.3")
    implementation("gg.essential:essential-$platform:10675+gb024b43bc")
}

tasks.jar {
    from(include.files.map { zipTree(it) })

    manifest.attributes(
        mapOf(
            "ModSide" to "CLIENT",
            "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker"
        )
    )
}