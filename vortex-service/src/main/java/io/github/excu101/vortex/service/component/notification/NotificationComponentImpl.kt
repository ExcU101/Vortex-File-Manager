package io.github.excu101.vortex.service.component.notification

import android.app.Notification
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationChannelCompat.Builder as ChannelBuilder
import androidx.core.app.NotificationCompat.Builder as NotificationBuilder

class NotificationComponentImpl(
    private val context: Context
) : NotificationComponent {

    private val channels = mutableMapOf<String, NotificationChannelCompat>()

    private val manager = NotificationManagerCompat.from(context)

    fun notify(
        id: Int,
        notification: Notification.Builder
    ) {
        notify(id, notification.build())
    }

    fun notify(
        id: Int,
        notification: Notification
    ) {
        manager.notify(id, notification)
    }

    fun createChannel(
        id: String,
        name: String? = null,
        desc: String? = null,
        importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT
    ): NotificationComponentImpl {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = ChannelBuilder(id, importance).setName(name).setDescription(desc).build()

            channels[id] = channel


            manager.createNotificationChannel(channel)
        } else {
            return this
        }

        return this
    }

    fun createNotification(
        id: String,
        icon: Int,
        title: String = "Vortex Service",
        text: String = "Kar en tuk!",
        progress: IntRange? = null,
        indeterminate: Boolean? = null
    ): Notification {
        val builder = NotificationBuilder(context, id)

        if (progress != null && indeterminate != null) {
            builder.setProgress(progress.first, progress.last, indeterminate)
        }

        builder.setSmallIcon(icon)
        builder.setContentTitle(title)
        builder.setContentText(text)

        return builder.build()
    }

}