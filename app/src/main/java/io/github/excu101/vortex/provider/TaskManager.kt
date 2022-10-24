package io.github.excu101.vortex.provider

import io.github.excu101.vortex.provider.TaskManager.Task
import kotlin.reflect.KClass

interface TaskManager<T : Task<*, *>> {

    abstract class AsyncTask : Task<Unit, Unit> {
        final override fun perform(input: Unit) {
            perform()
        }

        abstract fun perform()
    }

    interface Task<I, O> {
        fun perform(input: I): O
    }

    fun registerTask(task: T): TaskManager<T>

    fun performTask(clazz: KClass<T>): TaskManager<T>

}