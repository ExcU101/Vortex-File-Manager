package io.github.excu101.manifest.dsl.plugin

import io.github.excu101.manifest.dsl.source.ManifestScope
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.register

class ManifestDslPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = target.run {
        val scope = extensions.create<ManifestScope>(name = "manifest")
        val task = tasks.register<ManifestDslMergeTask>(name = "mergeManifest") {
            invoke(scope)
        }
    }

}

fun manifest(block: Action<ManifestScope>) {
    block.invoke(ManifestScope())
}

private fun test() = manifest {
    permissions {

    }
    application {
        activity {
            intentFilter {

            }
        }
        service {
            intentFilter {

            }
        }
    }
}