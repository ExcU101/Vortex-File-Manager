package io.github.excu101.vortex.ui.screen.list

import android.os.Environment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.fs.utils.properties
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.vortex.base.utils.ViewModelContainerHandler
import io.github.excu101.vortex.base.utils.applyValue
import io.github.excu101.vortex.base.utils.intent
import io.github.excu101.vortex.base.utils.state
import io.github.excu101.vortex.data.Section
import io.github.excu101.vortex.data.Sections
import io.github.excu101.vortex.data.Trail
import io.github.excu101.vortex.data.storage.StorageItem
import io.github.excu101.vortex.data.storage.StorageItemGroup
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.StorageProvider
import io.github.excu101.vortex.ui.screen.list.StorageScreenState.Companion.loading
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

    val groups: StateFlow<List<StorageItemGroup>> =
        handle.getStateFlow(GROUPS_KEY, listOf())

    val selected: StateFlow<List<StorageItem>> =
        handle.getStateFlow(SELECTED_KEY, listOf())

    private val _actions: MutableStateFlow<MutableList<GroupAction>> =
        MutableStateFlow(mutableListOf())
    val actions: StateFlow<List<GroupAction>>
        get() = _actions.asStateFlow()

    val trail: StateFlow<Trail> =
        handle.getStateFlow(TRAIL_KEY, Trail(items = listOf()))

    val path = Environment.getExternalStorageDirectory().asPath()

    val selectionModeEnabled: Boolean
        get() = selected.value.isNotEmpty()

    init {
        viewModelScope.launch {
            navigateTo(StorageItem(path))
        }
    }

    fun select(
        item: StorageItem,
        isSelected: Boolean,
    ) {
        val data = selected.value.toMutableList()

        if (data === listOf(item)) {
            if (!isSelected && data.isNotEmpty()) {
                data.clear()
                handle[SELECTED_KEY] = data
            }
            return
        }

        if (isSelected) {
            data -= item
        } else {
            data += item
        }

        handle[SELECTED_KEY] = data
    }

    fun selectAll() = intent {
        handle[SELECTED_KEY] = state.sections.values
    }

    fun addActions(actions: List<GroupAction>) = intent {
        _actions.applyValue {
            addAll(actions)
        }
    }

    fun navigateBack() {
        val current = trail.value
        val trail = current.navigationTo(newItems = current.items - current.items.last(), true)
        handle[TRAIL_KEY] = trail

        getContent(trail.current.value)
    }

    fun navigateTo(
        item: StorageItem,
    ) = intent {
        val current = trail.value

        if (item.isDirectory) {
            handle[TRAIL_KEY] = current.navigateTo(item)
            getContent(item)
        }
    }

    private fun getContent(item: StorageItem) = intent {
        val props = item.value.properties()

        val folders = props.dirs.map { StorageItem(it) }
        val files = props.files.map { StorageItem(it) }

        val sections = Sections<String, StorageItem>()

        if (folders.isNotEmpty()) sections.addSection(Section(item = "Folders (${props.dirsCount})" to folders))
        if (files.isNotEmpty()) sections.addSection(Section(item = "Files (${props.filesCount})" to files))
        sections.addSection(Section("Useless section" to listOf()))

        state {
            StorageScreenState(
                sections = sections
            )
        }
    }

}