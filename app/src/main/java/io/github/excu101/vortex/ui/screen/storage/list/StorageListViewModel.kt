package io.github.excu101.vortex.ui.screen.storage.list

import android.os.Build
import android.os.Environment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.operation.FileOperationObserver
import io.github.excu101.filesystem.fs.operation.observer
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.unix.utils.unixCreateDirectory
import io.github.excu101.filesystem.unix.utils.unixCreateFile
import io.github.excu101.filesystem.unix.utils.unixDelete
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.ui.theme.ReplacerThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.action
import io.github.excu101.vortex.R.drawable.*
import io.github.excu101.vortex.base.impl.Filter
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.base.impl.Sorter
import io.github.excu101.vortex.base.utils.*
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.MutablePathItemMapSet
import io.github.excu101.vortex.data.storage.PathItemMapSet
import io.github.excu101.vortex.data.trail.TrailNavigator
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.SafResolver
import io.github.excu101.vortex.provider.storage.StorageActionProvider
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.screen.storage.list.StorageListScreen.Content
import io.github.excu101.vortex.ui.screen.storage.list.StorageListScreen.SideEffect
import io.github.excu101.vortex.ui.screen.storage.list.StorageListScreen.State
import io.github.excu101.vortex.ui.screen.storage.list.StorageListScreen.DataResolver
import io.github.excu101.vortex.utils.asItems
import io.github.excu101.vortex.utils.isAndroidR
import io.github.excu101.vortex.utils.isAndroidTiramisu
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
) : ViewModelContainerHandler<State, SideEffect>(
    State(
        isLoading = true,
        loadingTitle = ThemeText(fileListLoadingInitiatingTitleKey)
    )
) {

    companion object {
        private const val pathItemKey = "pathItem"
    }

    private val _resolver = MutableStateFlow(DataResolver())
    val resolver: StateFlow<DataResolver>
        get() = _resolver.asStateFlow()

    val content: List<Item<*>>
        get() = container.state.value.data

    private val _selected: MutableStateFlow<MutablePathItemMapSet> = MutableStateFlow(
        MutablePathItemMapSet())
    val selected: StateFlow<PathItemMapSet>
        get() = _selected.asStateFlow()

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

    init {
        initActions()
        checkPermission()
    }

    fun openDefaultDrawerActions(isDark: Boolean) = intent {
        side(
            SideEffect(
                showDrawer = true,
                drawerActions = actions.drawerActions().asItems()
            )
        )
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

    fun order(order: Order) = intent {
        _resolver.new {
            copy(order = order)
        }
        current?.let { parseState(it) }
    }

    fun sort(sorter: Sorter<PathItem>) = intent {
        _resolver.new {
            copy(sorter = sorter)
        }
        current?.let { parseState(it) }
    }

    fun filter(filter: Filter<PathItem>) = intent {
        _resolver.new {
            copy(filter = filter)
        }
        current?.let { parseState(it) }
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
                            side(SideEffect(message = "Deleting ${value.getName()}"))
                        }
                    }

                    override fun onError(value: Throwable) {
                        viewModelScope.launch {
                            side(SideEffect(message = "${value.message}"))
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
            copy(data = parseContent(data = data.filterIsInstance<PathItem>() + items))
        }
    }

    private fun removeContent(items: Set<PathItem>) = intent {
        state {
            copy(data = data - items)
        }
    }

    private fun initActions() = intent {
        side(
            effect = SideEffect(
                showDrawer = false,
                drawerActions = actions.drawerActions().asItems()
            )
        )
        _barActions.applyValue {
            addAll(actions.barActions())
        }
    }

    fun copyPath() {
        provider.copyPath(actionItem)
    }

    fun createPath(
        path: Path,
        isDirectory: Boolean,
    ) {
        FileProvider.runOperation(
            if (isDirectory) unixCreateDirectory(source = path)
            else unixCreateFile(source = path),
            observers = listOf(
                observer(
                    onComplete = {
                        current?.let { navigateTo(it) }
//                        addContent(setOf(PathItem(path)))
                    }
                )
            )
        )
    }

    fun checkPermission() = intent {
        state { State(isLoading = true, loadingTitle = "Checking permissions") }

        if (isAndroidTiramisu) {
            if (provider.requiresNotificationsAccess()) {
                state {
                    State(
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
                    State(
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
                    State(
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
        side(
            SideEffect(
                showDrawer = true,
                drawerActions = actions.trailActions().asItems()
            )
        )
    }

    fun openDrawerActions() = intent {
        side(
            effect = SideEffect(
                showDrawer = true,
                drawerActions = actions.onItems(
                    Content(
                        content = state.data.filterIsInstance<PathItem>(),
                        selected = selected.value.toList(),
                        currentSelectedTrail = navigator.selectedIndex.value,
                        trail = navigator.items.value
                    )
                ).asItems()
            )
        )
    }

    fun openDrawerSortActions() = intent {
        side(
            effect = SideEffect(
                showDrawer = true,
                drawerActions = actions.sortActions().asItems()
            )
        )
    }

    fun openDrawerMoreActions() = intent {
        side(
            effect = SideEffect(
                showDrawer = true,
                drawerActions = actions.moreActions(
                    Content(
                        content = state.data.filterIsInstance<PathItem>(),
                        selected = selected.value.toList(),
                        currentSelectedTrail = navigator.selectedIndex.value,
                        trail = navigator.items.value
                    )
                ).asItems()
            )
        )
    }

    fun navigateTo(
        item: PathItem,
    ) = intent {
        val current = state
        state {
            State(
                isLoading = true,
                loadingTitle = ReplacerThemeText(
                    key = fileListLoadingNavigatingTitleKey,
                    old = specialSymbol,
                    new = item.name
                )
            )
        }

        if (item.isDirectory) {
            navigator.navigateTo(item)
            handle[pathItemKey] = item


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (item.path in SafResolver.restrictedDirs) {

                }
            }


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
                State(
                    warningMessage = error.message
                )
            }
            return@intent
        }

        if (data.isEmpty()) {
            state {
                State(
                    isWarning = true,
                    warningIcon = resources[ic_folder_24],
                    warningMessage = ReplacerThemeText(
                        key = fileListWarningEmptyTitleKey,
                        old = specialSymbol,
                        new = item.name
                    ),
                    warningActions = buildList {
                        action {
                            title = "Add new"
                            icon = resources[ic_add_24]
                        }
                    }
                )
            }
        } else {
            state {
                State(
                    data = parseContent(data)
                )
            }
        }
    }

    private fun parseContent(data: List<PathItem>): List<Item<*>> {
        return resolver.value.run(
            data = data
        )
    }

    fun dialog(type: StorageListScreen.DialogType) = intent {
        when (type) {
            StorageListScreen.DialogType.CREATE -> {
                side(
                    effect = SideEffect(
                        showCreateDialog = true
                    )
                )
            }
            StorageListScreen.DialogType.RENAME -> {
                side(
                    effect = SideEffect(
                        showRenameDialog = true
                    )
                )
            }
        }
    }

}