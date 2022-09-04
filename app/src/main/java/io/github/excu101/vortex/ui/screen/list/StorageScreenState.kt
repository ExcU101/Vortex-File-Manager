package io.github.excu101.vortex.ui.screen.list

import io.github.excu101.vortex.ui.component.adapter.Item

class StorageScreenState(
    val data: List<Item<*>> = listOf(),
    val isLoading: Boolean = false,
    val loadingTitle: String? = null,
    val error: Throwable? = null,
    val requiresPermissions: Boolean = false,
    val requiresAllFilePermission: Boolean = false,
) {

    companion object {
        fun content(data: List<Item<*>>) = StorageScreenState(
            data = data,
            isLoading = false,
            loadingTitle = null,
            error = null,
            requiresAllFilePermission = false,
            requiresPermissions = false
        )

        fun loading(title: String? = null) = StorageScreenState(
            data = listOf(),
            isLoading = true,
            loadingTitle = title,
            error = null,
            requiresAllFilePermission = false,
            requiresPermissions = false
        )
    }

}

sealed class StorageScreenSideEffect {

}
