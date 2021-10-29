plugins {
    `kotlin-dsl`
    `javiersc-publish`
}

pluginBundle {
    tags =
        listOf(
            "versioning",
            "reckon",
            "git",
        )
}

gradlePlugin {
    plugins {
        named("com.javiersc.gradle.plugins.versioning") {
            id = "com.javiersc.gradle.plugins.versioning"
            displayName = "Versioning"
            description = "A custom plugin for Reckon Plugin and its git versioning"
        }
    }
}

dependencies {
    api(projects.shared.pluginAccessors)

    api(pluginLibs.ajoberstar.reckon.reckonGradle)
}
