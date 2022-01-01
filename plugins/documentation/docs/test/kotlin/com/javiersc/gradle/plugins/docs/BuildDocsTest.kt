package com.javiersc.gradle.plugins.docs

import com.javiersc.gradle.plugins.core.test.getResource
import com.javiersc.gradle.plugins.core.test.testSandbox
import io.kotest.matchers.file.shouldBeADirectory
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldHaveSameStructureAndContentAs
import java.io.File
import kotlin.test.Test
import org.gradle.testkit.runner.BuildResult

class BuildDocsTest {

    @Test
    fun `build docs`() {
        val docsDir: File = getResource("docs")
        val sandboxDirs: List<File> =
            checkNotNull(docsDir.listFiles()).toList().filterNot {
                it.isFile || (it.isDirectory && it.name.endsWith("-actual"))
            }

        for (dir in sandboxDirs) {
            val sandboxPath = dir.toRelativeString(docsDir.parentFile).replace("\\", "/")
            val actualPath = "$sandboxPath-actual/.docs"
            testSandbox(
                sandboxPath = sandboxPath,
                test = { _: BuildResult, testProjectDir: File ->
                    val expect = File("$testProjectDir/build/.docs/")
                    val actual: File = getResource(actualPath)

                    expect shouldHaveSameStructureAndContentAs actual

                    val apiDir = File("$testProjectDir/build/docs/_site/api/")
                    apiDir.shouldExist()

                    if (sandboxPath.contains("snapshot")) {
                        File("$apiDir/snapshot/").shouldBeADirectory()
                    } else {
                        File("$apiDir/versions/").shouldBeADirectory()
                    }
                }
            )
        }
    }
}