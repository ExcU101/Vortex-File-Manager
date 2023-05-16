package io.github.excu101.vortex.service.component.file.media

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import io.github.excu101.vortex.service.component.file.media.audio.AudioComponent
import io.github.excu101.vortex.service.component.file.media.audio.AudioComponentImpl

class MediaComponentImpl(
    private val context: Context
) : MediaComponent {

    private val builder = ExoPlayer.Builder(context)

    override fun getAudioComponent(): AudioComponent = AudioComponentImpl(
        context = context,
        player = builder
            .setAudioAttributes(createAudioAttrs(), true)
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .setUsePlatformDiagnostics(false)
            .build()
    )

    private fun createAudioAttrs(): AudioAttributes {
        return AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()
    }

}