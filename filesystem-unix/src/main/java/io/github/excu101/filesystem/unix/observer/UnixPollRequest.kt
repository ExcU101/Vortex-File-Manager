package io.github.excu101.filesystem.unix.observer

import io.github.excu101.filesystem.fs.observer.PathObservableEventType
import io.github.excu101.filesystem.unix.path.UnixPath
import kotlinx.coroutines.CompletableDeferred

internal sealed class UnixPollRequest {

    object Run : UnixPollRequest()

    class Register(
        val path: UnixPath,
        val types: Array<out PathObservableEventType>,
        val response: CompletableDeferred<UnixPathObservableKey>,
    ) : UnixPollRequest()

    class Enqueue(
        val key: UnixPathObservableKey,
    ) : UnixPollRequest()

    class Cancel(
        val key: UnixPathObservableKey,
    ) : UnixPollRequest()

    object Close : UnixPollRequest()

}