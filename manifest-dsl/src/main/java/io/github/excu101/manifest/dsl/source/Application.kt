package io.github.excu101.manifest.dsl.source

interface ApplicationComponent : IntentFilterOwner {
    var name: String
    var isExported: Boolean
}

class ApplicationScope {

    var name: String = ""

    private val activities = mutableListOf<ActivityScope>()
    private val services = mutableListOf<ServiceScope>()

    fun ApplicationScope.service(block: ServiceScope.() -> Unit) {
        services.add(ServiceScope().apply(block))
    }

    fun ApplicationScope.activity(block: ActivityScope.() -> Unit) {
        activities.add(ActivityScope().apply(block))
    }

}