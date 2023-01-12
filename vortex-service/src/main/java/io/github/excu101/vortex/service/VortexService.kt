package io.github.excu101.vortex.service

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import io.github.excu101.vortex.service.impl.VortexFileManagerService
import io.github.excu101.vortex.service.utils.VORTEX_SERVICE_ACTION_NAME

class VortexService : LifecycleService() {

    private val binder = VortexFileManagerService(
//        NotificationCenterImpl(this)
    )

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)

        if (intent.action != VORTEX_SERVICE_ACTION_NAME) return null

        binder.subscribe()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        binder.unsubscribe()
        return super.onUnbind(intent)
    }

}