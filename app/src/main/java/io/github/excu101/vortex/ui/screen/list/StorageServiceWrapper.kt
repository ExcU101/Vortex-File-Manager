package io.github.excu101.vortex.ui.screen.list

import io.github.excu101.vortex.base.utils.logIt
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.service.VortexServiceProviderWrapper

class StorageServiceWrapper : VortexServiceProviderWrapper() {
    override fun navigateTo(item: PathItem) {
        "Navigating to ${item.name}".logIt()
    }

    override fun runCommand(name: String) {

    }
}