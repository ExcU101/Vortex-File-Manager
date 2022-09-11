package io.github.excu101.vortex.service.impl

import android.os.Binder
import io.github.excu101.vortex.IVortexService

class VortexServiceBinder : Binder() {

    fun getService(): IVortexService {
        return VortexServiceImpl()
    }

}