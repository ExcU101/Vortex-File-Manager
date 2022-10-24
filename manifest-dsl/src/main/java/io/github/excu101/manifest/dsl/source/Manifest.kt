package io.github.excu101.manifest.dsl.source

class ManifestScope {

    private val permissions: MutableList<PermissionScope> = mutableListOf()
    private val applications: MutableList<ApplicationScope> = mutableListOf()

    fun ManifestScope.permissions(block: PermissionScope.() -> Unit) {
        permissions.add(PermissionScope().apply(block))
    }

    fun ManifestScope.application(block: ApplicationScope.() -> Unit) {
        applications.add(ApplicationScope().apply(block))
    }

}