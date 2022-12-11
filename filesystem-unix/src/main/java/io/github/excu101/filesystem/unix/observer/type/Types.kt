package io.github.excu101.filesystem.unix.observer.type

import io.github.excu101.filesystem.fs.observer.PathObservableEventType

interface PathObservableAccessEventType : PathObservableEventType
interface PathObservableAttributesEventType : PathObservableEventType
interface PathObservableCloseForWriteEventType : PathObservableEventType
interface PathObservableCloseNotWriteEventType : PathObservableEventType
interface PathObservableDeleteItselfEventType : PathObservableEventType
interface PathObservableMoveItselfEventType : PathObservableEventType
interface PathObservableMovedFromEventType : PathObservableEventType
interface PathObservableMovedToEventType : PathObservableEventType
interface PathObservableOpenedEventType : PathObservableEventType
interface PathObservableDoNotFollowEventType : PathObservableEventType
interface PathObservableIgnoredEventType : PathObservableEventType
interface PathObservableAllEventType : PathObservableEventType

fun AccessEventType(): PathObservableAccessEventType =
    object : PathObservableAccessEventType {}

fun AttributesEventType(): PathObservableAttributesEventType =
    object : PathObservableAttributesEventType {}

fun CloseForWriteEventType(): PathObservableCloseForWriteEventType =
    object : PathObservableCloseForWriteEventType {}

fun CloseNotWriteEventType(): PathObservableCloseNotWriteEventType =
    object : PathObservableCloseNotWriteEventType {}

fun DeleteItselfEventType(): PathObservableDeleteItselfEventType =
    object : PathObservableDeleteItselfEventType {}

fun MoveItselfEventType(): PathObservableMoveItselfEventType =
    object : PathObservableMoveItselfEventType {}

fun MovedFromEventType(): PathObservableMovedFromEventType =
    object : PathObservableMovedFromEventType {}

fun MovedToEventType(): PathObservableMovedToEventType =
    object : PathObservableMovedToEventType {}

fun OpenedEventType(): PathObservableOpenedEventType =
    object : PathObservableOpenedEventType {}

fun DoNotFollowEventType(): PathObservableDoNotFollowEventType =
    object : PathObservableDoNotFollowEventType {}

fun IgnoredEventType(): PathObservableIgnoredEventType = object : PathObservableIgnoredEventType {}

fun AllEventType(): PathObservableAllEventType = object : PathObservableAllEventType {}