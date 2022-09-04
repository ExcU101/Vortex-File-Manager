package io.github.excu101.vortex.ui.screen.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.vortex.base.utils.*
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.header.TextHeaderItem
import io.github.excu101.vortex.data.storage.MutablePathItemMapSet
import io.github.excu101.vortex.data.storage.PathItemMapSet
import io.github.excu101.vortex.data.storage.StorageItem
import io.github.excu101.vortex.data.storage.PathItemGroup
import io.github.excu101.vortex.data.trail.TrailNavigator
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.StorageProvider
import io.github.excu101.vortex.ui.component.adapter.Item
import io.github.excu101.vortex.ui.screen.list.StorageScreenState.Companion.content
import io.github.excu101.vortex.ui.screen.list.StorageScreenState.Companion.loading
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageListViewModel @Inject constructor(
    private val provider: StorageProvider,
    private val resources: ResourceProvider,
    private val handle: SavedStateHandle,
) : ViewModelContainerHandler<StorageScreenState, StorageScreenSideEffect>(
    loading()
) {

    companion object {
        private const val TRAIL_KEY = "trail"
        private const val SELECTED_KEY = "selected"
        private const val GROUPS_KEY = "groups"
    }

    val content: List<Item<*>>
        get() = container.state.value.data

    val groups: StateFlow<List<PathItemGroup>> =
        handle.getStateFlow(GROUPS_KEY, listOf())

    private val _selected: MutableStateFlow<MutablePathItemMapSet> = MutableStateFlow(
        MutablePathItemMapSet())
    val selected: StateFlow<PathItemMapSet>
        get() = _selected.asStateFlow()

    private val _actions: MutableStateFlow<MutableList<GroupAction>> =
        MutableStateFlow(mutableListOf())
    val actions: StateFlow<List<GroupAction>>
        get() = _actions.asStateFlow()

    val trail: StateFlow<TrailNavigator> =
        handle.getStateFlow(TRAIL_KEY, TrailNavigator())

    val current: PathItem?
        get() = trail.value.selectedItem

    val selectionModeEnabled: Boolean
        get() = selected.value.isNotEmpty()

    private val path = PathItem(StorageProvider.EXTERNAL_STORAGE)

    init {
        viewModelScope.launch {
            navigateTo(path)
        }
    }

    fun select(
        item: PathItem,
    ) = intent {
        val data = _selected.value

        if (item in data) {
            data -= item
            logIt("Removed ${item.name}")
        } else {
            data += item
            logIt("Added ${item.name}")
        }

        _selected.emit(data)
    }

    fun selectAll() = intent {
        handle[SELECTED_KEY] =
            state.data.filterIsInstance<StorageItem>()
    }

    fun addActions(actions: List<GroupAction>) = intent {
        _actions.applyValue {
            addAll(actions)
        }
    }

    fun navigateBack() = intent {
        val current = trail.value
        val trail = current.navigateUp()
        handle[TRAIL_KEY] = trail

        trail.selectedItem?.let {
            val content = parseContent(it)
            state {
                StorageScreenState(
                    data = content
                )
            }
        }
    }

    fun navigateTo(
        item: PathItem,
    ) = intent {
//        state {
//            loading("Getting content...")
//        }
        val current = trail.value

        if (item.isDirectory) {
            handle[TRAIL_KEY] = current.navigateTo(item)
            val content = parseContent(item)

            state {
                StorageScreenState(
                    data = content
                )
            }
        }
    }

    suspend fun parseContent(path: PathItem): MutableList<Item<*>> {
        val content = mutableListOf<Item<*>>()
        val data = path.list()

        val folders = data.filter { it.isDirectory }
        val files = data.filter { it.isFile }

        content.add(TextHeaderItem("Folders (${folders.size})"))
        content.addAll(folders)
        content.add(TextHeaderItem("Files (${files.size})"))
        content.addAll(files)

        return content
    }

}