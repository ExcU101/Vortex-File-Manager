package io.github.excu101.vortex.provider.command

fun interface CommandSender {

    suspend fun send(command: Command)

}