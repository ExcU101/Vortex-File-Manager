package io.github.excu101.vortex.service.component.file.media.audio

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioFormat
import android.media.MediaSession2
import android.media.session.MediaSession
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioProcessor
import com.google.android.exoplayer2.metadata.MetadataDecoder
import io.github.excu101.filesystem.fs.path.Path

class AudioComponentImpl(
    context: Context,
    private val player: ExoPlayer
) : AudioComponent, Player.Listener {

    private val session = MediaSession(context, "")

    override var isActive: Boolean = session.isActive

    override var repeatMode: Int = player.repeatMode

    override var skipSilence: Boolean = player.skipSilenceEnabled

    override fun registerOnDeviceResumeConenction(block: (AudioDeviceInfo) -> Unit) {

    }

    fun clear() {
        player.removeListener(this)
        player.stop()
        player.release()
    }

    override fun onMediaItemTransition(item: MediaItem?, reason: Int) {
        if (item != null) {

        }
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
    }

}