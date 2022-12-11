package io.github.excu101.vortex.ui.screen.storage.page.info

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.vortex.base.utils.ViewModelContainerHandler
import io.github.excu101.vortex.base.utils.intent
import io.github.excu101.vortex.base.utils.state
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.screen.storage.page.info.StorageItemInfoPageScreen.ItemInfoParser
import io.github.excu101.vortex.ui.screen.storage.page.info.StorageItemInfoPageScreen.SideEffect
import io.github.excu101.vortex.ui.screen.storage.page.info.StorageItemInfoPageScreen.State
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StorageItemInfoPageViewModel @Inject constructor(

) : ViewModelContainerHandler<State, SideEffect>(
    initialState = State()
) {

    private val _parser = MutableStateFlow(ItemInfoParser.Default)
    val parser: StateFlow<ItemInfoParser>
        get() = _parser.asStateFlow()

    fun getInfo(item: PathItem) = intent {
        val info = viewModelScope.async(IO) {
            parser.value.parse(item)
        }

        state {
            State(
                isLoading = true,
                loadingTitle = "Getting info (${item.name})"
            )
        }

        val result = info.await()

        state {
            State(
                data = result
            )
        }
    }

}