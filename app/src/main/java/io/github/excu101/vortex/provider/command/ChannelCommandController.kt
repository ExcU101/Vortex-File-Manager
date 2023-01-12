package io.github.excu101.vortex.provider.command

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.receiveAsFlow

class ChannelCommandController(
    capacity: Int = Channel.RENDEZVOUS
) {

    private val _channel = Channel<Command>(capacity = capacity)

    suspend fun receive(): Command {
        return _channel.receive()
    }

    suspend fun collect(collector: FlowCollector<Command>) {
        _channel.receiveAsFlow().collect(collector)
    }

}