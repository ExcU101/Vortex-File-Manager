package io.github.excu101.manifest.dsl.source

interface IntentFilterScope {
    fun action()
}

interface IntentFilterOwner {

    fun intentFilter(block: IntentFilterScope.() -> Unit)

}