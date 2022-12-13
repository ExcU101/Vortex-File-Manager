package io.github.excu101.vortex.provider.command

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class ChannelCommandController(
    capacity: Int = Channel.RENDEZVOUS
)  {

    private val _channel = Channel<Command>(capacity = capacity)
    val channel: Flow<Command>
        get() = _channel.receiveAsFlow()

}