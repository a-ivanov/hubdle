package com.javiersc.hubdle.project.extensions.kotlin.features.shared

import com.javiersc.hubdle.project.extensions.HubdleDslMarker
import com.javiersc.hubdle.project.extensions._internal.ApplicablePlugin.Scope
import com.javiersc.hubdle.project.extensions._internal.COMMON_MAIN
import com.javiersc.hubdle.project.extensions._internal.Configurable.Priority
import com.javiersc.hubdle.project.extensions._internal.MAIN
import com.javiersc.hubdle.project.extensions._internal.PluginId
import com.javiersc.hubdle.project.extensions._internal.getHubdleExtension
import com.javiersc.hubdle.project.extensions._internal.library
import com.javiersc.hubdle.project.extensions.apis.BaseHubdleDelegateExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleConfigurableExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.project.extensions.apis.enableAndExecute
import com.javiersc.hubdle.project.extensions.dependencies._internal.aliases.cash_molecule_moleculeRuntime
import com.javiersc.hubdle.project.extensions.kotlin._internal.forKotlinSetsDependencies
import com.javiersc.hubdle.project.extensions.kotlin.hubdleKotlinAny
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

public open class HubdleKotlinMoleculeFeatureExtension
@Inject
constructor(
    project: Project,
) : HubdleConfigurableExtension(project) {

    override val isEnabled: Property<Boolean> = property { false }

    override val oneOfExtensions: Set<HubdleEnableableExtension>
        get() = hubdleKotlinAny

    override val priority: Priority = Priority.P4

    override fun Project.defaultConfiguration() {
        val mustApplyMoleculeGradlePlugin: Provider<Boolean> = provider {
            val isFullEnabled = isFullEnabled.get()
            val isComposeFullEnabled = hubdleComposeFeature.isFullEnabled.get()
            isFullEnabled && !isComposeFullEnabled
        }
        applicablePlugin(
            isEnabled = mustApplyMoleculeGradlePlugin,
            priority = Priority.P4,
            scope = Scope.CurrentProject,
            pluginId = PluginId.Molecule
        )

        configurable {
            forKotlinSetsDependencies(MAIN, COMMON_MAIN) {
                implementation(library(cash_molecule_moleculeRuntime))
            }
        }
    }
}

public interface HubdleKotlinMoleculeDelegateFeatureExtension : BaseHubdleDelegateExtension {

    public val molecule: HubdleKotlinMoleculeFeatureExtension
        get() = project.getHubdleExtension()

    @HubdleDslMarker
    public fun molecule(action: Action<HubdleKotlinMoleculeFeatureExtension> = Action {}) {
        molecule.enableAndExecute(action)
    }
}

internal val HubdleEnableableExtension.hubdleMoleculeFeature: HubdleKotlinMoleculeFeatureExtension
    get() = getHubdleExtension()

internal val Project.hubdleMoleculeFeature: HubdleKotlinMoleculeFeatureExtension
    get() = getHubdleExtension()
