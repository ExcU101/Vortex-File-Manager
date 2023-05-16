package io.github.excu101.vortex.ui.screen.storage.page.bookmark

import androidx.lifecycle.SavedStateHandle
import io.github.excu101.vortex.base.utils.ViewModelContainerHandler
import io.github.excu101.vortex.base.utils.intent
import io.github.excu101.vortex.base.utils.state
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.text.text
import io.github.excu101.vortex.ui.screen.storage.page.bookmark.StorageBookmarkScreen.SideEffect
import io.github.excu101.vortex.ui.screen.storage.page.bookmark.StorageBookmarkScreen.State
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class StorageBookmarkPageViewModel @Inject constructor(
    private val provider: StorageBookmarkProvider,
    private val handle: SavedStateHandle,
) : ViewModelContainerHandler<State, SideEffect>(
    State()
) {

    init {
        getItems()
    }

    fun getItems() = intent {
        provider.bookmarks.collectLatest { bookmarks ->
            val dirs = bookmarks.count { it.isDirectory }
            val files = bookmarks.count { it.isFile }

            val data = scope {
                text(value = "Dirs ($files)") {

                }
                text(value = "Files ($files)") {

                }
            }

            state {
                State(data)
            }
        }


    }

}