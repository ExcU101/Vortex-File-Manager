package io.github.excu101.vortex.ui.screen.list

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
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.action
import io.github.excu101.vortex.R
import io.github.excu101.vortex.R.drawable.*
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
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.screen.list.StorageScreenState.Companion.loading
import io.github.excu101.vortex.utils.isAndroidR
import io.github.excu101.vortex.utils.isAndroidTiramisu
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
    val filter: StateFlow<Filter<PathItem>>
        get() = _filter.asStateFlow()

    private val _resultParser = MutableStateFlow(ResultParserImpl())
    val resultParser: StateFlow<StorageListContentParser.ResultParser<PathItem>>
        get() = _resultParser.asStateFlow()

    private val _sorter = MutableStateFlow(compareBy<PathItem> { it.name })
    val sorter: StateFlow<Sorter<PathItem>>
        get() = _sorter.asStateFlow()

    init {
        initActions()
        checkPermission()
    }

    fun openDefaultDrawerActions(isDark: Boolean) = intent {
        _drawerGroups.emit(actions.defaultDrawerGroups(isDark).toMutableList())
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
        current?.let { parseState(it) }
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
            select(selected.value)
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

    private fun addContent(items: Set<PathItem>) = intent {
        state {
            copy(data = data + items)
        }
    }

    private fun removeContent(items: Set<PathItem>) = intent {
        state {
            copy(data = data - items)
        }
    }

    private fun initActions() = intent {
        _drawerGroups.applyValue {
            addAll(actions.defaultDrawerGroups(false))
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

        if (isAndroidTiramisu) {
            if (provider.requiresNotificationsAccess()) {
                state {
                    StorageScreenState(
                        isWarning = true,
                        warningMessage = ThemeText(fileListWarningNotificationAccessTitleKey),
                        warningIcon = resources[ic_info_24],
                        warningActions = listOf(
                            Action(
                                title = ThemeText(fileListWarningNotificationAccessActionTitleKey),
                                icon = resources[ic_add_24]
                            )
                        )
                    )
                }
                return@intent
            }
        }

        if (isAndroidR) {
            if (provider.requiresFullStorageAccess()) {
                state {
                    StorageScreenState(
                        isWarning = true,
                        warningMessage = ThemeText(fileListWarningFullStorageAccessTitleKey),
                        warningIcon = resources[ic_info_24],
                        warningActions = listOf(
                            Action(
                                title = ThemeText(fileListWarningFullStorageAccessActionTitleKey),
                                icon = resources[ic_add_24]
                            )
                        )
                    )
                }
            } else {
                navigateTo(path)
            }
        } else {
            if (provider.requiresPermissions()) {
                state {
                    StorageScreenState(
                        isWarning = true,
                        warningMessage = ThemeText(fileListWarningStorageAccessTitleKey),
                        warningIcon = resources[ic_info_24],
                        warningActions = listOf(
                            Action(
                                title = ThemeText(fileListWarningStorageAccessActionTitleKey),
                                icon = resources[ic_add_24]
                            )
                        )
                    )
                }
            }
        }
    }

    fun navigateLeft() = intent {
        navigator.navigateLeft()

        navigator.selectedItem?.let {
            parseState(it)
        }
    }

    fun navigateRight() = intent {
        navigator.navigateRight()

        navigator.selectedItem?.let {
            parseState(it)
        }
    }

    fun openDrawerTrailsActions(item: PathItem) = intent {
        actionItem = item
        _drawerGroups.emit(actions.trailActions().toMutableList())
        delay(100L)
        side(StorageScreenSideEffect(showDrawer = true))
    }

    fun openDrawerActions() = intent {
        _drawerGroups.emit(actions.onItems(
            StorageScreenContentState(
                content = state.data.filterIsInstance<PathItem>(),
                selected = selected.value.toList(),
                currentSelectedTrail = navigator.selectedIndex.value,
                trail = navigator.items.value
            )
        ).toMutableList())
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
                StorageScreenContentState(
                    content = state.data.filterIsInstance<PathItem>(),
                    selected = selected.value.toList(),
                    currentSelectedTrail = navigator.selectedIndex.value,
                    trail = navigator.items.value
                )
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

            parseState(item)
        } else {
            state {
                current
            }
        }
    }

    private fun resolveErrorActions(
        error: Throwable,
    ) {

    }

    private suspend fun parseState(item: PathItem) = intent {
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
                    ),
                    warningActions = buildList {
                        action {
                            title = "Add new"
                            icon = resources[R.drawable.ic_add_24]
                        }
                    }
                )
            }
        } else {
            state {
                StorageScreenState(
                    data = parseContent(data)
                )
            }
        }
    }

    private fun parseContent(data: List<PathItem>): List<Item<*>> {
        return parser.run(
            content = data,
            sorter = _sorter.value,
            filter = _filter.value,
            parser = _resultParser.value
        )
    }

    fun dialog(type: DialogType) {
        when (type) {
            DialogType.CREATE -> {

            }
        }
    }

}