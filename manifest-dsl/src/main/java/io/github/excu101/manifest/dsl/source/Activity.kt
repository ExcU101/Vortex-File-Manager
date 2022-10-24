package io.github.excu101.manifest.dsl.source

class ActivityScope : ApplicationComponent {

    override var name = ""
    override var isExported = false

    override fun intentFilter(block: IntentFilterScope.() -> Unit) {

    }

}