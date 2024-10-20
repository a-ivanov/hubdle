package com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets

import com.javiersc.hubdle.project.extensions.HubdleDslMarker
import com.javiersc.hubdle.project.extensions._internal.getHubdleExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.project.extensions.apis.enableAndExecute
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.hubdleKotlinMultiplatform
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.macos.HubdleKotlinMultiplatformMacOSArm64Extension
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets.macos.HubdleKotlinMultiplatformMacOSX64Extension
import com.javiersc.hubdle.project.extensions.kotlin.shared.HubdleKotlinMinimalSourceSetConfigurableExtension
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property

@HubdleDslMarker
public open class HubdleKotlinMultiplatformMacOSExtension @Inject constructor(project: Project) :
    HubdleKotlinMinimalSourceSetConfigurableExtension(project) {

    override val project: Project
        get() = super.project

    override val isEnabled: Property<Boolean> = property { false }

    override val targetName: String = "macos"

    public val allEnabled: Property<Boolean> = property { false }

    @HubdleDslMarker
    public fun allEnabled(value: Boolean = true) {
        allEnabled.set(value)
    }

    override val requiredExtensions: Set<HubdleEnableableExtension>
        get() = setOf(hubdleKotlinMultiplatform)

    public val macosArm64: HubdleKotlinMultiplatformMacOSArm64Extension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun macosArm64(
        action: Action<HubdleKotlinMultiplatformMacOSArm64Extension> = Action {}
    ) {
        macosArm64.enableAndExecute(action)
    }

    public val macosX64: HubdleKotlinMultiplatformMacOSX64Extension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun macosX64(action: Action<HubdleKotlinMultiplatformMacOSX64Extension> = Action {}) {
        macosX64.enableAndExecute(action)
    }

    override fun Project.defaultConfiguration() {
        lazyConfigurable {
            if (allEnabled.get()) {
                macosArm64()
                macosX64()
            }
        }
    }
}
