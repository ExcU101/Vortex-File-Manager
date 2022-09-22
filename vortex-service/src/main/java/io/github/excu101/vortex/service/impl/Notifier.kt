package io.github.excu101.vortex.service.impl

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat.*

class Notifier(context: Context) {

    companion object {
        private const val channelId = "Vortex Service Channel"
    }

    enum class Importance(val original: Int) {
        OPERATION(IMPORTANCE_MAX),
        MESSAGE(IMPORTANCE_DEFAULT)
    }

    private val manager = from(context)
    private val builder = NotificationCompat.Builder(context, channelId)

    fun setTitle(
        title: String,
    ): Notifier {
        builder.setContentTitle(title)

        return this
    }

    fun setMessage(
        message: String,
    ): Notifier {
        builder.setContentText(message)
        return this
    }

    fun notify(
        id: Int,
        title: String? = null,
        message: String? = null,
    ): Notifier {
        title?.let { setTitle(it) }
        message?.let { setMessage(message) }

        manager.notify(id, builder.build())

        return this
    }

    private fun checkChannel() {
        if (manager.getNotificationChannelCompat(channelId) == null) {
            createChannel()
        }
    }

    private fun createChannel() {
        val channel = NotificationChannelCompat.Builder(channelId, 3).build()

        manager.createNotificationChannel(channel)
    }
}