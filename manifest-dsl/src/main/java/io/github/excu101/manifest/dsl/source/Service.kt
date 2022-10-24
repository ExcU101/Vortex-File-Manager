package io.github.excu101.manifest.dsl.source

class ServiceScope : ApplicationComponent {

    override var name: String = ""

    override var isExported: Boolean = false

    override fun intentFilter(block: IntentFilterScope.() -> Unit) {

    }
}