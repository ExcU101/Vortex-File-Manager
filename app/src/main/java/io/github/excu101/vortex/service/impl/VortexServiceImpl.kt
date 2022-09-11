package io.github.excu101.vortex.service.impl

import io.github.excu101.vortex.IVortexService
import io.github.excu101.vortex.service.VortexServiceProvider

class VortexServiceImpl : IVortexService.Stub() {

    private val providers = mutableListOf<VortexServiceProvider>()

    override fun registerProvider(provider: VortexServiceProvider) {
        providers.add(provider)
    }

}