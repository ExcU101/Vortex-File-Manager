package io.github.excu101.vortex.provider.command

import io.github.excu101.filesystem.fs.observer.PathObservableEventType
import io.github.excu101.filesystem.fs.path.Path

data class RegisterWatcherCommand(
    val source: Path,
    val types: Array<out PathObservableEventType>
) : Command {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegisterWatcherCommand

        if (source != other.source) return false
        if (!types.contentEquals(other.types)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + types.contentHashCode()
        return result
    }

}