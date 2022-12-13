package io.github.excu101.vortex.provider.command

fun interface CommandConsumer {

    fun consume(command: Command)

}