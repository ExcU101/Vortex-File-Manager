package io.github.excu101.vortex.provider.command

fun interface CommandProducer {

    fun produce(): Command

}