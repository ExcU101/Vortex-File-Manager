package io.github.excu101.vortex.service.notification

import android.content.Context
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationManagerCompat
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.service.VortexHandler

class VortexNotificationManager(
    private val context: Context,
) {
    companion object {
        const val PATH_OP_CHANNEL = "path_operation_channel"
    }

    private val manager = NotificationManagerCompat.from(context)
    private val handler = VortexHandler(context)
    private val notifications = mutableMapOf<Int, VortexNotification>()

    private val builder: Builder = Builder(context, PATH_OP_CHANNEL)

    fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManagerCompat.IMPORTANCE_HIGH
            val channel = NotificationChannelCompat.Builder(PATH_OP_CHANNEL, importance).apply {
                setName("Name")
                setDescription("Desc")
            }.build()

            manager.createNotificationChannel(channel)
        }
    }

    fun update(id: Int, event: Int, path: Path): VortexNotificationManager {
        val notification = notifications[id] ?: return this

        builder.setChannelId(PATH_OP_CHANNEL)
        builder.setContentText("$path (${notification.progress}/${notification.max})")
        builder.setProgress(notification.max, notification.progress, false)
        try {
            manager.notify(id, builder.build())
        } catch (se: SecurityException) {

        }
        if (notification.isFinished) handler.closeNotification(id)
        return this
    }

    fun update(id: Int, progress: Int, action: Int, path: Path): VortexNotificationManager {
        val notification = notifications[id] ?: return this
        notification.progress += progress

        builder.setChannelId(PATH_OP_CHANNEL)
        builder.setContentText("$path (${notification.progress}/${notification.max})")
        builder.setProgress(notification.max, notification.progress, false)
        try {
            manager.notify(id, builder.build())
        } catch (se: SecurityException) {

        }
        if (notification.isFinished) handler.closeNotification(id)
        return this
    }

}