package com.javiersc.hubdle.project.extensions.kotlin.multiplatform.targets

import com.javiersc.hubdle.project.extensions.HubdleDslMarker
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import javax.inject.Inject

@HubdleDslMarker
public open class HubdleKotlinMultiplatformDesktopExtension @Inject constructor(project: Project) :
    HubdleKotlinMultiplatformJvmExtension(project) {

    override val targetName: String = "desktop"

    override fun Project.defaultConfiguration() {
        lazyConfigurable { configure<KotlinMultiplatformExtension> { jvm("desktop") } }
    }
}
