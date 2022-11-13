package io.github.excu101.vortex.provider.storage

object StorageTaskManager {

    private val _copyTasks = mutableListOf<CopyTask>()
    val copyTasks: List<CopyTask>
        get() = _copyTasks

    fun register(task: CopyTask) {
        _copyTasks.add(task)
    }

    fun unregister(task: CopyTask) {
        _copyTasks.remove(task)
    }

}