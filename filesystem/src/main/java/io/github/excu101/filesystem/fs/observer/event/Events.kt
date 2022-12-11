package io.github.excu101.filesystem.fs.observer.event

import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.path.Path

interface CreateEvent : PathObservableKey.Event {
    val path: Path
}

fun CreateEvent(path: Path): CreateEvent {
    return object : CreateEvent {
        override val path: Path = path
    }
}
