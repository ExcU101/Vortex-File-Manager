package io.github.excu101.vortex.service.impl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import io.github.excu101.vortex.service.notification.NotificationCenter

internal class NotificationCenterImpl(
    private val context: Context
) : NotificationCenter {

    companion object {
        const val OperationChannelId = "OPERATIONS"
    }

    init {
        checkChannels()
    }

    private val manager = context.getSystemService<NotificationManager>()
    private fun checkChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Operations"
            val desc = "Notify about performing operations"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(OperationChannelId, name, importance).apply {
                description = desc
            }

            manager?.createNotificationChannel(channel)
        }
    }

    override fun message(
        id: Int,
        title: String,
        message: String,
        @DrawableRes
        iconRes: Int,
        priority: Int
    ) {
        manager?.notify(
            id, NotificationCompat.Builder(context, OperationChannelId).setSmallIcon(
                iconRes
            ).setContentTitle(title)
                .setContentText(message)
                .setPriority(priority)
                .build()
        )
    }

}