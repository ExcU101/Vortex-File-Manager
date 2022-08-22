package io.github.excu101.vortex.ui.screen.list

import android.os.Environment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.vortex.base.utils.ViewModelContainerHandler
import io.github.excu101.vortex.base.utils.intent
import io.github.excu101.vortex.base.utils.new
import io.github.excu101.vortex.base.utils.state
import io.github.excu101.vortex.data.TrailData
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.StorageProvider
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
) : ViewModelContainerHandler<StorageScreenState, StorageScreenSideEffect>(StorageScreenState(
    isLoading = true)
) {

    companion object {
        private const val TRAIL_KEY = "trail"
    }

    private val _selected: MutableStateFlow<MutableSet<FileUnit>> =
        MutableStateFlow(mutableSetOf())
    val selected: StateFlow<Set<FileUnit>>
        get() = _selected.asStateFlow()

    private val _actions: MutableStateFlow<MutableList<GroupAction>> =
        MutableStateFlow(mutableListOf())
    val actions: StateFlow<List<GroupAction>>
        get() = _actions.asStateFlow()

    val trail: StateFlow<TrailData> =
        handle.getStateFlow(TRAIL_KEY, TrailData(items = listOf()))

    val path = Environment.getExternalStorageDirectory().asPath()

    init {
        viewModelScope.launch {
            navigateTo(FileUnit(path))
        }
    }

    fun choose(
        items: Collection<FileUnit>,
        isSelected: Boolean = selected.value.containsAll(items),
    ) = intent {
        val data = mutableSetOf<FileUnit>().apply {
            addAll(selected.value)
            addAll(items)
        }
        _selected.emit(data)
    }

    fun selectAll() = intent {
        _selected.new {
            state.items.toMutableSet()
        }
    }

    fun addActions(actions: List<GroupAction>) = intent {
        _actions.new {
            val list = mutableListOf<GroupAction>()
            list.addAll(this)
            list.addAll(actions)
            list
        }
    }

    fun navigateTo(unit: FileUnit) = intent {
        val current = trail.value

        if (unit.isDirectory) {
            handle[TRAIL_KEY] = current.navigateTo(unit)
            val list = provider.provideList(unit.path).sortedBy { it.name }
            state {
                StorageScreenState(
                    items = list
                )
            }
        }
    }

}