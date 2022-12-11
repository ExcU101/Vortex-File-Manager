package io.github.excu101.filesystem.fs.observer.type

import io.github.excu101.filesystem.fs.observer.PathObservableEventType

interface PathObservableModifyEventType : PathObservableEventType
interface PathObservableDeleteEventType : PathObservableEventType
interface PathObservableCreateEventType : PathObservableEventType

fun ModifyEventType(): PathObservableModifyEventType = object : PathObservableModifyEventType {}
fun DeleteEventType(): PathObservableDeleteEventType = object : PathObservableDeleteEventType {}
fun CreateEventType(): PathObservableCreateEventType = object : PathObservableCreateEventType {}

object EmptyEventType : PathObservableEventType