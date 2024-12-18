package com.javiersc.hubdle.project.extensions.config.testing

import com.gradle.develocity.agent.gradle.test.TestRetryConfiguration
import com.javiersc.hubdle.project.extensions.HubdleDslMarker
import com.javiersc.hubdle.project.extensions._internal.ApplicablePlugin.Scope
import com.javiersc.hubdle.project.extensions._internal.getHubdleExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleConfigurableExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.project.extensions.config.hubdleConfig
import com.javiersc.hubdle.project.extensions.kotlin.android.hubdleAndroidAny
import com.javiersc.hubdle.project.extensions.kotlin.multiplatform.hubdleKotlinMultiplatform
import com.javiersc.hubdle.project.extensions.shared.PluginId
import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.develocity
import org.gradle.kotlin.dsl.withType

@HubdleDslMarker
public abstract class HubdleConfigTestingExtension @Inject constructor(project: Project) :
    HubdleConfigurableExtension(project) {

    override val isEnabled: Property<Boolean> = property { true }

    override val requiredExtensions: Set<HubdleEnableableExtension>
        get() = setOf(hubdleConfig)

    public val options: Property<Options> = property {
        if (hasAndroid.get()) Options.JUnit else Options.JUnitPlatform
    }

    public val maxParallelForks: Property<Int> = property {
        val isCI: Boolean = System.getenv("CI")?.toBoolean() ?: false
        val factor: Int = if (isCI) 1 else 2
        (Runtime.getRuntime().availableProcessors() / factor).takeIf { it > 0 } ?: 1
    }

    @HubdleDslMarker
    public fun maxParallelForks(forks: Int) {
        maxParallelForks.set(forks)
    }

    @HubdleDslMarker
    public fun retry(action: Action<TestRetryConfiguration>) {
        lazyConfigurable {
            tasks.withType<Test>().configureEach { test ->
                action.execute(test.develocity.testRetry)
            }
        }
    }

    public val showStandardStreams: Property<Boolean> = property { true }

    @HubdleDslMarker
    public fun showStandardStreams(enabled: Boolean = true) {
        showStandardStreams.set(enabled)
    }

    @HubdleDslMarker
    public fun test(action: Action<Test>) {
        lazyConfigurable {
            project.tasks.withType<Test>().configureEach { test -> action.execute(test) }
        }
    }

    override fun Project.defaultConfiguration() {
        pluginManager.apply(BasePlugin::class)

        applicablePlugin(scope = Scope.CurrentProject, pluginId = PluginId.AdarshrTestLogger)

        lazyConfigurable {
            val testTasks: TaskCollection<Test> = tasks.withType<Test>()
            testTasks.configureEach { task ->
                task.testLogging.showStandardStreams = showStandardStreams.get()
                task.maxParallelForks = maxParallelForks.get()

                when (options.get()) {
                    Options.JUnit -> task.useJUnit()
                    Options.JUnitPlatform -> task.useJUnitPlatform()
                    Options.TestNG -> task.useTestNG()
                    else -> task.useJUnit()
                }
            }
        }
    }

    public enum class Options {
        JUnit,
        JUnitPlatform,
        TestNG,
    }
}

private val HubdleConfigTestingExtension.hasAndroid: Property<Boolean>
    get() = property {
        hubdleAndroidAny.any { it.isFullEnabled.get() } ||
            hubdleKotlinMultiplatform.isFullEnabled.get()
    }

internal val HubdleEnableableExtension.hubdleTesting: HubdleConfigTestingExtension
    get() = getHubdleExtension()

internal val Project.hubdleTesting: HubdleConfigTestingExtension
    get() = getHubdleExtension()
