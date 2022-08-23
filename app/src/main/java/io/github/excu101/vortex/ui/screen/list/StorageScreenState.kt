package io.github.excu101.vortex.ui.screen.list

import io.github.excu101.filesystem.fs.FileUnit

class StorageScreenState(
    val items: List<FileUnit> = listOf(),
    val isLoading: Boolean = false,
    val loadingTitle: String? = null,
    val error: Throwable? = null,
) {

    companion object {
        fun loading(title: String? = null) = StorageScreenState(
            items = listOf(),
            isLoading = true,
            loadingTitle = title,
            error = null
        )
    }

}

sealed class StorageScreenSideEffect {

}
