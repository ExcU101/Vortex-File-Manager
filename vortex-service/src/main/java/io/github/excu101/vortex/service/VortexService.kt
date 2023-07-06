package io.github.excu101.vortex.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.github.excu101.vortex.service.utils.VORTEX_SERVICE_ACTION_NAME
import io.github.excu101.vortex.di.DaggerServiceComponent.builder as ServiceComponentBuilder

class VortexService : Service() {

    private val binder by lazy(LazyThreadSafetyMode.NONE) {
        VortexServiceBinder(
            ServiceComponentBuilder().bindContext(this).build()
        )
    }

    override fun onBind(intent: Intent): IBinder? {
        if (intent.action != VORTEX_SERVICE_ACTION_NAME) {
            return null
        }

        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent?.action != null) {
            when (intent.action) {
                ServiceActions.Music.Start -> {

                }

                ServiceActions.Music.Stop -> {

                }
            }
        }

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}