package io.github.excu101.vortex.service

import android.content.Context
import android.os.Binder
import com.google.common.collect.ImmutableList
import io.github.excu101.vortex.service.component.file.FileComponent
import io.github.excu101.vortex.service.component.file.FileComponentImpl
import io.github.excu101.vortex.service.component.file.media.MediaComponent
import io.github.excu101.vortex.service.component.file.media.audio.AudioComponent
import io.github.excu101.vortex.service.component.file.operation.OperationComponent
import io.github.excu101.vortex.service.component.notification.NotificationComponent
import io.github.excu101.vortex.service.component.notification.NotificationComponentImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class VortexServiceBinder @JvmOverloads internal constructor(
    private val _subscribeCount: MutableStateFlow<Int> = MutableStateFlow(0),
    context: Context
) : Binder() {
    private val _file: FileComponentImpl = FileComponentImpl(context)
    private val _notification: NotificationComponentImpl = NotificationComponentImpl(context)

    internal suspend fun updateCount(count: Int) {
        _subscribeCount.emit(count)
    }

    val subscribeCount: StateFlow<Int>
        get() = _subscribeCount.asStateFlow()

    val file: FileComponent
        get() = _file

    val operation: OperationComponent
        get() = file.getOperationComponent()

    val media: MediaComponent
        get() = file.getMediaComponent()

    val audio: AudioComponent
        get() = media.getAudioComponent()

    val notification: NotificationComponent
        get() = _notification

}