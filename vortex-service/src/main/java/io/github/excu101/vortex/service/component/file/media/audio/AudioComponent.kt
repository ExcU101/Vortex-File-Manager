package io.github.excu101.vortex.service.component.file.media.audio

import android.media.AudioDeviceInfo

interface AudioComponent {

    var repeatMode: Int

    var skipSilence: Boolean

    var isActive: Boolean

    fun registerOnDeviceResumeConenction(block: (AudioDeviceInfo) -> Unit)

}