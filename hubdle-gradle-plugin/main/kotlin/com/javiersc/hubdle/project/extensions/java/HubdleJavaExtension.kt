package com.javiersc.hubdle.project.extensions.java

import com.javiersc.hubdle.project.extensions.HubdleDslMarker
import com.javiersc.hubdle.project.extensions._internal.ApplicablePlugin.Scope
import com.javiersc.hubdle.project.extensions._internal.Configurable.Priority
import com.javiersc.hubdle.project.extensions._internal.PluginId
import com.javiersc.hubdle.project.extensions._internal.getHubdleExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleConfigurableExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.project.extensions.apis.enableAndExecute
import com.javiersc.hubdle.project.extensions.java.features.HubdleJavaFeaturesExtension
import com.javiersc.hubdle.project.extensions.kotlin._internal.normalAndGeneratedDirs
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.configure

@HubdleDslMarker
public open class HubdleJavaExtension
@Inject
constructor(
    project: Project,
) : HubdleConfigurableExtension(project) {

    override val isEnabled: Property<Boolean> = property { false }

    override val priority: Priority = Priority.P3

    public val features: HubdleJavaFeaturesExtension
        get() = getHubdleExtension()

    @HubdleDslMarker
    public fun features(action: Action<HubdleJavaFeaturesExtension> = Action {}) {
        features.enableAndExecute(action)
    }

    override fun Project.defaultConfiguration() {
        applicablePlugin(
            priority = Priority.P3,
            scope = Scope.CurrentProject,
            pluginId = PluginId.GradleJava
        )
        configurableSourceSet()
    }

    private fun Project.configurableSourceSet() {
        configurable {
            configure<JavaPluginExtension> {
                sourceSets.configureEach { set ->
                    set.java.setSrcDirs(normalAndGeneratedDirs("${set.name}/java"))
                    set.resources.setSrcDirs(normalAndGeneratedDirs("${set.name}/resources"))
                }
            }
        }
    }
}

internal val HubdleEnableableExtension.hubdleJava: HubdleJavaExtension
    get() = getHubdleExtension()