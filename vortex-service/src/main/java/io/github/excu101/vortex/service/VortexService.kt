package io.github.excu101.vortex.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.IBinder
import io.github.excu101.vortex.service.component.notification.NotificationComponentImpl
import io.github.excu101.vortex.service.component.notification.NotificationIds
import io.github.excu101.vortex.service.utils.VORTEX_SERVICE_ACTION_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VortexService : Service() {

    private val notification by lazy { NotificationComponentImpl(this) }

    private val binder by lazy { VortexServiceBinder(context = this) }

    override fun onBind(intent: Intent): IBinder? {
        if (intent.action != VORTEX_SERVICE_ACTION_NAME) {
            return null
        }

        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent?.action != null) {
            if (intent.action == ServiceActions.Music.Start) {

            }
            if (intent.action == ServiceActions.Music.Stop) {

            }
        }

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        val n = notification.createChannel(
            id = "VORTEX_SERVICE",
            name = "Vortex Service messages"
        ).createNotification(
            id = "VORTEX_SERVICE",
            icon = com.google.android.material.R.drawable.ic_clock_black_24dp
        )

        startForeground(NotificationIds.Service, n)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

}