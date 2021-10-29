plugins {
    `kotlin-dsl`
    `javiersc-publish`
}

pluginBundle {
    tags =
        listOf(
            "gradle",
            "versions catalogs",
        )
}

gradlePlugin {
    plugins {
        named("com.javiersc.gradle.plugins.build.version.catalogs.updater") {
            id = "com.javiersc.gradle.plugins.build.version.catalogs.updater"
            displayName = "Build Version Catalogs"
            description = "A plugin for updating Build Version Catalogs"
        }
    }
}

dependencies {
    api(projects.shared.pluginAccessors)

    implementation(libs.jsoup.jsoup)
    implementation(libs.javiersc.semanticVersioning.semanticVersioningCore)

    compileOnly(pluginLibs.jetbrains.kotlin.kotlinGradlePlugin)
}
