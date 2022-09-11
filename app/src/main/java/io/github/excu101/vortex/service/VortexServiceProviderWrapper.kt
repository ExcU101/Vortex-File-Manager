package io.github.excu101.vortex.service

import io.github.excu101.vortex.data.PathItem

abstract class VortexServiceProviderWrapper : VortexServiceProvider.Stub() {

    abstract override fun navigateTo(item: PathItem)

    abstract override fun runCommand(name: String)

}