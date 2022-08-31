package io.github.excu101.vortex.ui.screen.list

import io.github.excu101.vortex.data.Sections
import io.github.excu101.vortex.data.storage.StorageItem

class StorageScreenState(
    val sections: Sections<String, StorageItem> = Sections(),
    val isLoading: Boolean = false,
    val loadingTitle: String? = null,
    val error: Throwable? = null,
) {

    companion object {
        fun loading(title: String? = null) = StorageScreenState(
            sections = Sections(),
            isLoading = true,
            loadingTitle = title,
            error = null
        )
    }

}

sealed class StorageScreenSideEffect {

}
