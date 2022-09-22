package io.github.excu101.vortex.ui.screen.list

import android.os.Build
import android.os.Environment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.unix.utils.unixDelete
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.GroupAction
import io.github.excu101.pluginsystem.ui.theme.ReplacerThemeText
import io.github.excu101.vortex.R
import io.github.excu101.vortex.base.impl.Sorter
import io.github.excu101.vortex.base.impl.StorageListContentParser
import io.github.excu101.vortex.base.impl.StorageListContentParser.Filter
import io.github.excu101.vortex.base.impl.StorageListContentParser.ResultParserImpl
import io.github.excu101.vortex.base.utils.*
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.MutablePathItemMapSet
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemMapSet
import io.github.excu101.vortex.data.trail.TrailNavigator
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.StorageProvider
import io.github.excu101.vortex.provider.storage.StorageActionProvider
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.fileListNavigatingTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningEmptyTitleKey
import io.github.excu101.vortex.ui.component.theme.key.specialSymbol
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
    private val actions: StorageActionProvider,
    private val handle: SavedStateHandle,
) : ViewModelContainerHandler<StorageScreenState, StorageScreenSideEffect>(
    loading()
) {

    companion object {
        private const val pathItemKey = "pathItem"
    }

    private val parser = StorageListContentParser()

    val content: List<Item<*>>
        get() = container.state.value.data

    private val _selected: MutableStateFlow<MutablePathItemMapSet> = MutableStateFlow(
        MutablePathItemMapSet())
    val selected: StateFlow<PathItemMapSet>
        get() = _selected.asStateFlow()

    private val _drawerGroups = MutableStateFlow(mutableListOf<GroupAction>())
    val drawerGroups: StateFlow<List<GroupAction>>
        get() = _drawerGroups.asStateFlow()

    private val _barActions = MutableStateFlow(mutableListOf<Action>())
    val barActions: StateFlow<List<Action>>
        get() = _barActions.asStateFlow()

    val navigator = TrailNavigator()

    val current: PathItem?
        get() = navigator.selectedItem

    val selectionModeEnabled: Boolean
        get() = selected.value.isNotEmpty()

    private val path =
        handle[pathItemKey] ?: PathItem(Environment.getExternalStorageDirectory().asPath())

    private var actionItem = path

    private val _filter = MutableStateFlow<Filter<PathItem>>(PathItemFilters.Empty)
    private val _resultParser = MutableStateFlow(ResultParserImpl())
    private val _sorter = MutableStateFlow(compareBy<PathItem> { it.name })

    init {
        initActions()
        checkPermission()
    }

    fun openDefaultDrawerActions() = intent {
        _drawerGroups.emit(actions.defaultDrawerGroups().toMutableList())
        delay(100L) // TODO <- REMOVE THIS!!!
        side(StorageScreenSideEffect(showDrawer = true))
    }

    fun select(
        item: PathItem,
    ) = intent {
        val data = MutablePathItemMapSet().apply {
            addAll(selected.value)
        }

        if (data.contains(item)) {
            data.remove(item)
        } else {
            data.add(item)
        }

        _selected.emit(data)
    }

    fun select(
        items: Set<PathItem>,
    ) = intent {
        val data = MutablePathItemMapSet().apply {
            addAll(selected.value)
        }

        if (data.containsAll(items)) {
            data.removeAll(items)
        } else {
            data.addAll(items)
        }

        _selected.emit(data)
    }

    fun selectAll() = intent {
        val data = MutablePathItemMapSet().apply {
            addAll(selected.value)
            addAll(state.data.filterIsInstance<PathItem>())
        }

        _selected.emit(data)
    }

    fun deselectAll() = intent {
        val data = MutablePathItemMapSet().apply {
            addAll(selected.value)
            removeAll(state.data.filterIsInstance<PathItem>())
        }

        _selected.emit(data)
    }

    fun sort(filter: Sorter<PathItem>) = intent {
        _sorter.emit(filter)
        val content = parseContent(data = state.data.filterIsInstance<PathItem>())
        state {
            StorageScreenState(data = content)
        }
    }

    fun filter(filter: Filter<PathItem>) = intent {
        _filter.emit(filter)
        val content = parseContent(data = state.data.filterIsInstance<PathItem>())
        state {
            StorageScreenState(data = content)
        }
    }

    fun delete(items: Set<PathItem> = selected.value) = intent {
        if (items == selected.value) {

        }
        FileProvider.runOperation(unixDelete(items.map { it.value }),
            observers = listOf(
                object : FileOperationObserver {
                    override fun onAction(value: Path) {
                        viewModelScope.launch {
                            side(StorageScreenSideEffect(message = "Deleting ${value.getName()}"))
                        }
                    }

                    override fun onError(value: Throwable) {
                        viewModelScope.launch {
                            side(StorageScreenSideEffect(message = "${value.message}"))
                        }
                    }

                    override fun onComplete() {
                        removeContent(items)
                    }
                }
            )
        )
    }

    private fun removeContent(items: Set<PathItem>) = intent {
        state {
            copy(data = data - items)
        }
    }

    private fun initActions() = intent {
        _drawerGroups.applyValue {
            addAll(actions.defaultDrawerGroups())
        }
        _barActions.applyValue {
            addAll(actions.defaultBarActions())
        }
    }

    fun copyPath() {
        provider.copyPath(actionItem)
    }

    fun checkPermission() = intent {
        state { loading(title = "Checking permissions") }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (provider.requiresFullStorageAccess()) {
                state { StorageScreenState(requiresAllFilePermission = true) }
            } else {
                navigateTo(path)
            }
        } else {
            if (provider.requiresPermissions()) {
                state {
                    StorageScreenState(
                        requiresPermissions = true
                    )
                }
            }
        }
    }

    fun navigateLeft() = intent {
        navigator.navigateLeft()

        navigator.selectedItem?.let {
            val data = try {
                provider.provideList(it)
            } catch (error: Throwable) {
                state {
                    StorageScreenState(
                        warningMessage = error.message
                    )
                }
                return@let
            }

            if (data.isEmpty()) {
                state {
                    StorageScreenState(
                        isWarning = true,
                        warningIcon = resources[R.drawable.ic_folder_24],
                        warningMessage = "${it.name} is empty"
                    )
                }
            } else {
                val content = parseContent(data)
                state {
                    StorageScreenState(
                        data = content
                    )
                }
            }
        }
    }

    fun navigateRight() = intent {
        navigator.navigateRight()

        navigator.selectedItem?.let {
            val data = try {
                provider.provideList(it)
            } catch (error: Throwable) {
                state {
                    StorageScreenState(
                        warningMessage = error.message
                    )
                }
                return@let
            }

            if (data.isEmpty()) {
                state {
                    StorageScreenState(
                        isWarning = true,
                        warningIcon = resources[R.drawable.ic_folder_24],
                        warningMessage = "${it.name} is empty"
                    )
                }
            } else {
                val content = parseContent(data)
                state {
                    StorageScreenState(
                        data = content
                    )
                }
            }
        }
    }

    fun openDrawerTrailsActions(item: PathItem) = intent {
        actionItem = item
        _drawerGroups.emit(actions.trailActions().toMutableList())
        delay(100L)
        side(StorageScreenSideEffect(showDrawer = true))
    }

    fun openDrawerActions() = intent {
        _drawerGroups.emit(actions.onItems(selected.value).toMutableList())
        delay(100L)
        side(StorageScreenSideEffect(showDrawer = true))
    }

    fun openDrawerSortActions() = intent {
        _drawerGroups.emit(actions.sortActions().toMutableList())
        delay(100L)
        side(StorageScreenSideEffect(showDrawer = true))
    }

    fun openDrawerMoreActions() = intent {
        _drawerGroups.emit(
            actions.moreActions(
                current = current!!,
                content = state.data.filterIsInstance<PathItem>(),
                selected = selected.value,
                isItemTrailFirst = navigator.selectedIndex.value == 0,
                isItemTrailLast = navigator.selectedIndex.value == navigator.size - 1
            ).toMutableList()
        )
        delay(100L)
        side(StorageScreenSideEffect(showDrawer = true))
    }

    fun navigateTo(
        item: PathItem,
    ) = intent {
        val current = state
        state {
            loading(title = ReplacerThemeText(
                key = fileListNavigatingTitleKey,
                old = specialSymbol,
                new = item.name
            ))
        }

        if (item.isDirectory) {
            navigator.navigateTo(item)
            handle[pathItemKey] = item
            val data = try {
                provider.provideList(item)
            } catch (error: Throwable) {
                state {
                    StorageScreenState(
                        warningMessage = error.message
                    )
                }
                return@intent
            }

            if (data.isEmpty()) {
                state {
                    StorageScreenState(
                        isWarning = true,
                        warningIcon = resources[R.drawable.ic_folder_24],
                        warningMessage = ReplacerThemeText(
                            key = fileListWarningEmptyTitleKey,
                            old = specialSymbol,
                            new = item.name
                        )
                    )
                }
            } else {
                val content = parseContent(data)
                state {
                    StorageScreenState(
                        data = content
                    )
                }
            }
        } else {
            state {
                current
            }
        }
    }

    fun parseContent(data: List<PathItem>): List<Item<*>> {
        return parser.run(
            content = data,
            sorter = _sorter.value,
            filter = _filter.value,
            parser = _resultParser.value
        )
    }

}