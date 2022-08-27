package io.github.excu101.vortex.ui.screen.list

import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.vortex.data.Section
import io.github.excu101.vortex.data.StorageItem

class StorageScreenState(
    val sections: List<Section<String, StorageItem>> = listOf(),
    val isLoading: Boolean = false,
    val loadingTitle: String? = null,
    val error: Throwable? = null,
) {

    companion object {
        fun loading(title: String? = null) = StorageScreenState(
            sections = listOf(),
            isLoading = true,
            loadingTitle = title,
            error = null
        )
    }

}

sealed class StorageScreenSideEffect {

}
