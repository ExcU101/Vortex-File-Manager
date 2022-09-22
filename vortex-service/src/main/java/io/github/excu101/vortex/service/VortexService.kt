package io.github.excu101.vortex.service

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import io.github.excu101.vortex.VortexServiceApi
import io.github.excu101.vortex.service.impl.VortexServiceImpl
import io.github.excu101.vortex.service.utils.VORTEX_SERVICE_ACTION_NAME

class VortexService : LifecycleService() {

    private val binder = VortexServiceImpl(context = this)

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)

        if (intent.action == null) return null
        if (intent.action != VORTEX_SERVICE_ACTION_NAME) return null

        return binder
    }

    fun getInterface(): VortexServiceApi {
        return binder
    }

}