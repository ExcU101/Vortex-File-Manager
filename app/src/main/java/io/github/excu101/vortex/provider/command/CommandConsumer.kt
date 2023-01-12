package io.github.excu101.vortex.provider.command

fun interface CommandConsumer {

    suspend fun consume(command: Command)

}