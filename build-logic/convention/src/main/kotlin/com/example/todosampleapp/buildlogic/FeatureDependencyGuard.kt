package com.example.todosampleapp.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

/**
 * feature モジュールが infra モジュールに依存していないことをビルド時に検証する。
 *
 * feature は core:domain の interface だけを見て、実体は app で Hilt が束ねる。
 * feature → infra の依存を許すと Room/KSP などの重いビルドが feature に波及するため禁止する。
 */
internal fun Project.guardFeatureDependencies() {
    afterEvaluate {
        configurations.forEach { config ->
            config.dependencies
                .filterIsInstance<ProjectDependency>()
                .firstOrNull { it.path.startsWith(":infra") }
                ?.let { error("${project.path} は ${it.path} に依存できません（feature → infra は禁止）") }
        }
    }
}
