package com.javiersc.gradle.plugins.all.projects.install.pre.commit

import com.javiersc.gradle.plugins.all.projects.install.InstallTask
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class InstallPreCommitTask : InstallTask, DefaultTask() {

    @get:Input internal abstract val preCommitName: String

    @get:Internal
    internal val preCommitOutputDir: File
        get() = project.file("${project.buildDir}/install/pre-commits/")

    @get:OutputFile
    internal val preCommitOutputFile: File
        get() = project.file("$preCommitOutputDir/$preCommitName.pre-commit")

    companion object {
        internal const val taskGroup: String = "install"

        const val name: String = "installPreCommit"

        internal fun getInstallPreCommitTask(project: Project): TaskProvider<Task> =
            project.tasks.named(name)

        internal fun register(project: Project) =
            project.tasks.register(name) { task -> task.group = taskGroup }
    }
}

internal fun InstallPreCommitTask.createInstallPreCommitGradleTask() {
    val headerText = "[$preCommitName autogenerated by `javiersc-all-projects` plugin]"
    val text =
        """
            |
            |# $headerText
            |./gradlew $preCommitName
            |
        """.trimMargin()

    preCommitOutputFile.writeText(text)
}

internal val Project.preCommitFile: File
    get() {
        check(this == rootProject) { "This task should be only configured in the root project" }
        return file("${layout.projectDirectory.asFile}/.git/hooks/pre-commit").apply {
            parentFile.mkdirs()
            createNewFile()
        }
    }
