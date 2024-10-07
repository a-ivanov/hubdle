package com.javiersc.hubdle.project.extensions.compose._internal

import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.compose.ComposeExtension

internal fun Project.composeExtension(): ComposeExtension =
    extensions.findByType<ComposeExtension>() ?: error("Compose plugin not being applied")

internal fun Project.configureComposeExtension(block: ComposeExtension.() -> Unit) {
    block(composeExtension())
}
