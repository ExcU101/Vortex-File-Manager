package io.github.excu101.vortex.service.component.file.media

import io.github.excu101.vortex.service.component.file.media.audio.AudioComponent

interface MediaComponent {

    fun getAudioComponent(): AudioComponent

}