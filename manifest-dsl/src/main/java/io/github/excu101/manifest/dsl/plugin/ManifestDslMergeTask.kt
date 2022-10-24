package io.github.excu101.manifest.dsl.plugin

import io.github.excu101.manifest.dsl.source.ManifestScope
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class ManifestDslMergeTask : DefaultTask() {

    @TaskAction
    fun invoke(scope: ManifestScope) {

    }

}