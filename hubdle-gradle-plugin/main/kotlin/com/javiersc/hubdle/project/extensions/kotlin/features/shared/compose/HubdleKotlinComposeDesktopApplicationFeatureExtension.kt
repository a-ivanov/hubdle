package com.javiersc.hubdle.project.extensions.kotlin.features.shared.compose

import com.javiersc.hubdle.project.extensions.HubdleDslMarker
import com.javiersc.hubdle.project.extensions.apis.HubdleConfigurableExtension
import com.javiersc.hubdle.project.extensions.apis.HubdleEnableableExtension
import com.javiersc.hubdle.project.extensions.compose._internal.configureComposeExtension
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

public open class HubdleKotlinComposeDesktopApplicationFeatureExtension
@Inject
constructor(project: Project) : HubdleConfigurableExtension(project) {

    override val isEnabled: Property<Boolean> = property { true }

    override val requiredExtensions: Set<HubdleEnableableExtension>
        get() = setOf(hubdleComposeDesktopFeature)

    public val packageName: Property<String> = property { deducePackageNameFromProject() }

    @HubdleDslMarker
    public fun packageName(packageName: String) {
        this.packageName.set(packageName)
    }

    public val mainClassName: Property<String> = property { Application.defaultMainClassName }

    @HubdleDslMarker
    public fun mainClassName(mainClassName: String) {
        this.mainClassName.set(mainClassName)
    }

    public val mainClass: Property<String> = property {
        "${packageName.get()}.${mainClassName.get()}"
    }

    @HubdleDslMarker
    public fun mainClass(mainClass: String) {
        this.mainClass.set(mainClass)
    }

    public val proguardVersion: Property<String> = property { BuildTypes.proguardVersion }

    @HubdleDslMarker
    public fun proguardVersion(proguardVersion: String) {
        this.proguardVersion.set(proguardVersion)
    }

    public val dmg: Property<Boolean> = property { true }

    @HubdleDslMarker
    public fun dmg(enabled: Boolean) {
        this.dmg.set(enabled)
    }

    public val msi: Property<Boolean> = property { true }

    @HubdleDslMarker
    public fun msi(enabled: Boolean) {
        this.msi.set(enabled)
    }

    public val deb: Property<Boolean> = property { true }

    @HubdleDslMarker
    public fun deb(enabled: Boolean) {
        this.deb.set(enabled)
    }

    public val packageVersion: Property<String> = property { computeNextReleaseVersion() }

    @HubdleDslMarker
    public fun packageVersion(packageVersion: String) {
        this.packageVersion.set(packageVersion)
    }

    public object Application {
        public const val defaultMainClassName: String = "MainKt"
    }

    public object BuildTypes {
        public const val proguardVersion: String = "7.4.0"
    }

    internal companion object {
        private const val MINIMUM_ALLOWED_DMG_VERSION: String = "1.0.0"
    }

    override fun Project.defaultConfiguration() {
        lazyConfigurable {
            configureComposeExtension {
                extensions.configure<DesktopExtension> {
                    application.mainClass = mainClass.get()

                    application.buildTypes.release.proguard.version.set(proguardVersion)

                    val targetFormats = buildSet {
                        if (dmg.get()) add(TargetFormat.Dmg)
                        if (msi.get()) add(TargetFormat.Msi)
                        if (deb.get()) add(TargetFormat.Deb)
                    }
                    application.nativeDistributions.targetFormats = targetFormats
                    application.nativeDistributions.packageName = packageName.get()
                    application.nativeDistributions.packageVersion = packageVersion.get()
                }
            }
        }
    }

    private fun Project.deducePackageNameFromProject(): String {
        val projectName = rootProject.name.replace('-', '.')
        return "${rootProject.group}.${projectName}"
    }

    private fun computeNextReleaseVersion(): String {
        val projectVersion = project.version.toString()
        val version = projectVersion.substringBeforeLast('.')
        if (dmg.get()) return adaptReleaseVersionForDmg(version)
        return version
    }

    private fun adaptReleaseVersionForDmg(version: String): String {
        if (version.first() == '0') return MINIMUM_ALLOWED_DMG_VERSION
        return version
    }
}
