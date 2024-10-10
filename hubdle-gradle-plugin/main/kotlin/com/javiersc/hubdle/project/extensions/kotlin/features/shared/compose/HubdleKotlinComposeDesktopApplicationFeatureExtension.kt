package com.javiersc.hubdle.project.extensions.kotlin.features.shared.compose

import com.javiersc.hubdle.project.extensions.apis.HubdleConfigurableExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.project.extensions.compose._internal.configureComposeExtension
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.desktop.DesktopExtension

public open class HubdleKotlinComposeDesktopApplicationFeatureExtension
@Inject
constructor(project: Project) : HubdleConfigurableExtension(project) {

    override val isEnabled: Property<Boolean> = property { true }

    override val requiredExtensions: Set<HubdleEnableableExtension>
        get() = setOf(hubdleComposeDesktopFeature)

    public val mainClassName: Property<String> = property { "MainKt" }

    public val mainClass: Property<String> = property {
        "${mainClassPackageNameDeductedFromProject}.${mainClassName.get()}"
    }

    override fun Project.defaultConfiguration() {
        lazyConfigurable {
            configureComposeExtension {
                extensions.configure<DesktopExtension> { application.mainClass = mainClass.get() }
            }
        }
    }

    private val Project.mainClassPackageNameDeductedFromProject: String
        get() {
            val projectName = rootProject.name.replace('-', '.')
            return "${rootProject.group}.${projectName}"
        }
}
