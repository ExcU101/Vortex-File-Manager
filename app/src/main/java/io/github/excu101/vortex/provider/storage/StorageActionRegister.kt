package io.github.excu101.vortex.provider.storage

object StorageActionRegister {

    private var action: () -> Unit = {}

    fun register(
        action: () -> Unit = {},
    ) {
        StorageActionRegister.action = action
    }

    fun perform() {
        action.invoke()
    }

}