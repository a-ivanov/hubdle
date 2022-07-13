package com.javiersc.hubdle.extensions.kotlin.android.application._internal

import com.android.build.api.dsl.ApplicationExtension
import com.javiersc.hubdle.extensions._internal.PluginIds
import com.javiersc.hubdle.extensions._internal.state.HubdleState
import com.javiersc.hubdle.extensions._internal.state.catalogImplementation
import com.javiersc.hubdle.extensions._internal.state.hubdleState
import com.javiersc.hubdle.extensions.config.explicit.api._internal.configureExplicitApi
import com.javiersc.hubdle.extensions.dependencies._internal.constants.COM_JAVIERSC_KOTLIN_KOTLIN_STDLIB_MODULE
import com.javiersc.hubdle.extensions.dependencies._internal.constants.IO_KOTEST_KOTEST_ASSERTIONS_CORE_MODULE
import com.javiersc.hubdle.extensions.dependencies._internal.constants.ORG_JETBRAINS_KOTLINX_KOTLINX_COROUTINES_ANDROID_MODULE
import com.javiersc.hubdle.extensions.dependencies._internal.constants.ORG_JETBRAINS_KOTLINX_KOTLINX_COROUTINES_CORE_MODULE
import com.javiersc.hubdle.extensions.dependencies._internal.constants.ORG_JETBRAINS_KOTLINX_KOTLINX_COROUTINES_TEST_MODULE
import com.javiersc.hubdle.extensions.dependencies._internal.constants.ORG_JETBRAINS_KOTLINX_KOTLINX_SERIALIZATION_CORE_MODULE
import com.javiersc.hubdle.extensions.dependencies._internal.constants.ORG_JETBRAINS_KOTLINX_KOTLINX_SERIALIZATION_JSON_MODULE
import com.javiersc.hubdle.extensions.dependencies._internal.constants.ORG_JETBRAINS_KOTLIN_KOTLIN_TEST_MODULE
import com.javiersc.hubdle.extensions.kotlin._internal.configJvmTarget
import com.javiersc.hubdle.extensions.options.configDefaultAndroidSourceSets
import com.javiersc.hubdle.extensions.options.configureDefaultKotlinSourceSets
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

internal fun configureAndroidApplication(project: Project) {
    val applicationState = project.hubdleState.kotlin.android.application
    if (applicationState.isEnabled) {
        project.pluginManager.apply(PluginIds.Android.kotlin)
        project.pluginManager.apply(PluginIds.Android.application)

        project.configureExplicitApi()
        project.configJvmTarget()

        project.the<KotlinProjectExtension>().configureAndroidDependencies()
        project.the<KotlinProjectExtension>().configureDefaultKotlinSourceSets()

        project.configure<ApplicationExtension> {
            defaultConfig.applicationId = applicationState.applicationId
            defaultConfig.versionCode = applicationState.versionCode
            defaultConfig.versionName = applicationState.versionName
            compileSdk = project.hubdleState.kotlin.android.compileSdk
            defaultConfig.minSdk = project.hubdleState.kotlin.android.minSdk

            sourceSets.all { configDefaultAndroidSourceSets() }
        }
    }
}

internal val Project.androidApplicationFeatures: HubdleState.Kotlin.Android.Application.Features
    get() = hubdleState.kotlin.android.application.features

private val KotlinDependencyHandler.androidApplicationFeatures:
    HubdleState.Kotlin.Android.Application.Features
    get() = project.androidApplicationFeatures

internal fun configureKotlinAndroidApplicationRawConfig(project: Project) {
    project.hubdleState.kotlin.android.application.rawConfig.android?.execute(project.the())
}

private fun KotlinProjectExtension.configureAndroidDependencies() {
    sourceSets.named("main") { dependencies { configureMainDependencies() } }
    sourceSets.named("test") { dependencies { configureTestDependencies() } }
}

private fun KotlinDependencyHandler.configureMainDependencies() {
    if (androidApplicationFeatures.coroutines) {
        catalogImplementation(ORG_JETBRAINS_KOTLINX_KOTLINX_COROUTINES_ANDROID_MODULE)
        catalogImplementation(ORG_JETBRAINS_KOTLINX_KOTLINX_COROUTINES_CORE_MODULE)
    }
    if (androidApplicationFeatures.extendedStdlib) {
        catalogImplementation(COM_JAVIERSC_KOTLIN_KOTLIN_STDLIB_MODULE)
    }
    if (androidApplicationFeatures.serialization.isEnabled) {
        project.pluginManager.apply(PluginIds.Kotlin.serialization)
        catalogImplementation(ORG_JETBRAINS_KOTLINX_KOTLINX_SERIALIZATION_CORE_MODULE)
        if (androidApplicationFeatures.serialization.useJson) {
            catalogImplementation(ORG_JETBRAINS_KOTLINX_KOTLINX_SERIALIZATION_JSON_MODULE)
        }
    }
}

private fun KotlinDependencyHandler.configureTestDependencies() {
    catalogImplementation(ORG_JETBRAINS_KOTLIN_KOTLIN_TEST_MODULE)

    if (androidApplicationFeatures.coroutines) {
        catalogImplementation(ORG_JETBRAINS_KOTLINX_KOTLINX_COROUTINES_TEST_MODULE)
    }
    if (androidApplicationFeatures.extendedTesting) {
        catalogImplementation(IO_KOTEST_KOTEST_ASSERTIONS_CORE_MODULE)
    }
}