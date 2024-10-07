package com.javiersc.hubdle.project.extensions.kotlin.features.shared.compose

import com.javiersc.hubdle.project.extensions.HubdleDslMarker
import com.javiersc.hubdle.project.extensions._internal.DESKTOP_MAIN
import com.javiersc.hubdle.project.extensions._internal.getHubdleExtension
import com.javiersc.hubdle.project.extensions._internal.library
import com.javiersc.hubdle.project.extensions.apis.HubdleConfigurableExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.project.extensions.apis.enableAndExecute
import com.javiersc.hubdle.project.extensions.dependencies._internal.aliases.jetbrains_kotlinx_coroutines_swing
import com.javiersc.hubdle.project.extensions.kotlin._internal.forKotlinSetsDependencies
import com.javiersc.hubdle.project.extensions.kotlin.features.shared.hubdleComposeFeature
import compose
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property

public open class HubdleKotlinComposeDesktopFeatureExtension @Inject constructor(project: Project) :
    HubdleConfigurableExtension(project) {

    override val isEnabled: Property<Boolean> = property { false }

    override val requiredExtensions: Set<HubdleEnableableExtension>
        get() = setOf(hubdleComposeFeature)

    public val application: HubdleKotlinComposeDesktopApplicationFeatureExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun application(
        action: Action<HubdleKotlinComposeDesktopApplicationFeatureExtension> = Action {}
    ) {
        application.enableAndExecute(action)
    }

    override fun Project.defaultConfiguration() {
        lazyConfigurable {
            forKotlinSetsDependencies(DESKTOP_MAIN) {
                implementation(compose.desktop.currentOs)
                implementation(library(jetbrains_kotlinx_coroutines_swing))
            }
        }
    }
}

internal val HubdleEnableableExtension.hubdleComposeDesktopFeature:
    HubdleKotlinComposeDesktopFeatureExtension
    get() = getHubdleExtension()
