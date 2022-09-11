package io.github.excu101.vortex.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.github.excu101.vortex.base.utils.logIt
import io.github.excu101.vortex.service.VortexLifecycleOwner.VortexLifecycle
import io.github.excu101.vortex.service.VortexLifecycleOwner.VortexLifecycle.*
import io.github.excu101.vortex.service.impl.VortexServiceBinder

class VortexService : Service() {

    private val binder = VortexServiceBinder()

    private val owners = mutableListOf<VortexLifecycleOwner>()
    private val observers = mutableListOf<VortexLifecycleObserver>()
    private val providers = mutableListOf<VortexServiceProvider>()

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        notify(event = CREATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notify(event = START)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notify(event = DESTROY)
    }

    private fun notify(event: VortexLifecycle) {
        for (observer in observers) {
            observer.onChange(event)
            event.logIt()
        }
    }

}