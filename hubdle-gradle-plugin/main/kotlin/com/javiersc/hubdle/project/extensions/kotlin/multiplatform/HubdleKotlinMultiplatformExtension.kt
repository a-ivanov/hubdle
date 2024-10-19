package com.javiersc.hubdle.project.extensions.kotlin.multiplatform

import com.javiersc.hubdle.project.extensions.HubdleDslMarker
import com.javiersc.hubdle.project.extensions._internal.ApplicablePlugin.Scope
import com.javiersc.hubdle.project.extensions._internal.configurableDependencies
import com.javiersc.hubdle.project.extensions._internal.getHubdleExtension
import com.javiersc.hubdle.project.extensions._internal.withKotlinMultiplatform
import com.javiersc.hubdle.project.extensions.apis.HubdleConfigurableExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.project.extensions.apis.enableAndExecute
import com.javiersc.hubdle.project.extensions.config.publishing.maven.configurableMavenPublishing
import com.javiersc.hubdle.project.extensions.kotlin.hubdleKotlin
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.features.HubdleKotlinMultiplatformFeaturesExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformAndroidExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformAndroidNativeExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformAppleExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformCommonExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformDesktopExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformJsExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformJvmAndAndroidExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformJvmExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformLinuxExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformMinGWExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformNativeExtension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.HubdleKotlinMultiplatformWAsmJsExtension
import com.javiersc.hubdle.project.extensions.kotlin.shared.enableAndExecuteOnMain
import com.javiersc.hubdle.project.extensions.shared.PluginId
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

@HubdleDslMarker
public open class HubdleKotlinMultiplatformExtension @Inject constructor(project: Project) :
    HubdleConfigurableExtension(project) {

    override val isEnabled: Property<Boolean> = property { false }

    override val requiredExtensions: Set<HubdleEnableableExtension>
        get() = setOf(hubdleKotlin)

    public val features: HubdleKotlinMultiplatformFeaturesExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun features(action: Action<HubdleKotlinMultiplatformFeaturesExtension> = Action {}) {
        features.enableAndExecute(action)
    }

    private val common: HubdleKotlinMultiplatformCommonExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun commonMain(action: Action<KotlinSourceSet> = Action {}) {
        common.enableAndExecuteOnMain(action)
    }

    public val android: HubdleKotlinMultiplatformAndroidExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun android(action: Action<HubdleKotlinMultiplatformAndroidExtension> = Action {}) {
        android.enableAndExecute(action)
    }

    public val androidNative: HubdleKotlinMultiplatformAndroidNativeExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun androidNative(
        action: Action<HubdleKotlinMultiplatformAndroidNativeExtension> = Action {}
    ) {
        androidNative.enableAndExecute(action)
    }

    public val apple: HubdleKotlinMultiplatformAppleExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun apple(action: Action<HubdleKotlinMultiplatformAppleExtension> = Action {}) {
        apple.enableAndExecute(action)
    }

    public val jvm: HubdleKotlinMultiplatformJvmExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun jvm(action: Action<HubdleKotlinMultiplatformJvmExtension> = Action {}) {
        jvm.enableAndExecute(action)
    }

    private val desktop: HubdleKotlinMultiplatformDesktopExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun desktopMain(action: Action<KotlinSourceSet> = Action {}) {
        desktop.enableAndExecuteOnMain(action)
    }

    public val jvmAndAndroid: HubdleKotlinMultiplatformJvmAndAndroidExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun jvmAndAndroid(
        action: Action<HubdleKotlinMultiplatformJvmAndAndroidExtension> = Action {}
    ) {
        jvmAndAndroid.enableAndExecute(action)
    }

    public val js: HubdleKotlinMultiplatformJsExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun js(action: Action<HubdleKotlinMultiplatformJsExtension> = Action {}) {
        js.enableAndExecute(action)
    }

    public val linux: HubdleKotlinMultiplatformLinuxExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun linux(action: Action<HubdleKotlinMultiplatformLinuxExtension> = Action {}) {
        linux.enableAndExecute(action)
    }

    public val mingw: HubdleKotlinMultiplatformMinGWExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun mingw(action: Action<HubdleKotlinMultiplatformMinGWExtension> = Action {}) {
        mingw.enableAndExecute(action)
    }

    public val native: HubdleKotlinMultiplatformNativeExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun native(action: Action<HubdleKotlinMultiplatformNativeExtension> = Action {}) {
        native.enableAndExecute(action)
    }

    public val wasmJs: HubdleKotlinMultiplatformWAsmJsExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun wasmJs(action: Action<HubdleKotlinMultiplatformWAsmJsExtension> = Action {}) {
        wasmJs.enableAndExecute(action)
    }

    @HubdleDslMarker
    public fun kotlin(action: Action<KotlinMultiplatformExtension>) {
        lazyConfigurable { action.execute(the()) }
    }

    override fun Project.defaultConfiguration() {
        applicablePlugin(
            scope = Scope.CurrentProject,
            pluginId = PluginId.JetbrainsKotlinMultiplatform,
        )

        withKotlinMultiplatform {
            configure<KotlinMultiplatformExtension> { applyDefaultHierarchyTemplate() }
        }

        configurableDependencies()
        configurableMavenPublishing(configEmptyJavaDocs = true)
    }
}

internal val HubdleEnableableExtension.hubdleKotlinMultiplatform: HubdleKotlinMultiplatformExtension
    get() = getHubdleExtension()

internal val Project.hubdleKotlinMultiplatform: HubdleKotlinMultiplatformExtension
    get() = getHubdleExtension()
