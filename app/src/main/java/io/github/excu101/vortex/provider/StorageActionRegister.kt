package io.github.excu101.vortex.provider

object StorageActionRegister {

    private var action: () -> Unit = {}

    fun register(
        action: () -> Unit = {},
    ) {
        this.action = action
    }

    fun perform() {
        action.invoke()
    }

}