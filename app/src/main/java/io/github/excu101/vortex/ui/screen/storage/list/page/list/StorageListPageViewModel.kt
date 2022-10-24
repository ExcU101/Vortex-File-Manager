package io.github.excu101.vortex.ui.screen.storage.list.page.list

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.operation.observer
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.unix.utils.unixCreateDirectory
import io.github.excu101.filesystem.unix.utils.unixCreateFile
import io.github.excu101.filesystem.unix.utils.unixCreateSymbolicLink
import io.github.excu101.filesystem.unix.utils.unixDelete
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.action
import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.action
import io.github.excu101.pluginsystem.utils.effect
import io.github.excu101.vortex.R.drawable.*
import io.github.excu101.vortex.base.impl.Filter
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.base.impl.Sorter
import io.github.excu101.vortex.base.utils.*
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.trail.TrailNavigator
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.storage.StorageActionProvider
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.provider.storage.task.CopyPathTask
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.screen.storage.list.page.list.StorageListPageScreen.Content
import io.github.excu101.vortex.ui.screen.storage.list.page.list.StorageListPageScreen.DataResolver
import io.github.excu101.vortex.ui.screen.storage.list.page.list.StorageListPageScreen.SideEffect
import io.github.excu101.vortex.ui.screen.storage.list.page.list.StorageListPageScreen.State
import io.github.excu101.vortex.ui.screen.storage.list.page.list.operation.observer.ViewModelDeleteObserver
import io.github.excu101.vortex.utils.asItems
import io.github.excu101.vortex.utils.isAndroidR
import io.github.excu101.vortex.utils.isAndroidTiramisu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StorageListPageViewModel @Inject constructor(
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

    private val _selected = MutableStateFlow(listOf<PathItem>())
    val selected: StateFlow<List<PathItem>>
        get() = _selected.asStateFlow()

    val isSelectionEnabled: Boolean
        get() = selected.value.isNotEmpty()

    private val _resolver = MutableStateFlow(DataResolver())
    val resolver: StateFlow<DataResolver>
        get() = _resolver.asStateFlow()

    val content: List<Item<*>>
        get() = container.state.value.data

    private val _barActions = MutableStateFlow(mutableListOf<Action>())
    val barActions: StateFlow<List<Action>>
        get() = _barActions.asStateFlow()

    val navigator = TrailNavigator()

    val current: PathItem?
        get() = navigator.selectedItem

    private val _restrictedDirectories = MutableStateFlow<Uri?>(null)
    val restrictedDirectories: StateFlow<Uri?>
        get() = _restrictedDirectories.asStateFlow()

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

    fun delete(items: Set<PathItem>) = intent {
        FileProvider.runOperation(unixDelete(items.map { it.value }),
            observer = ViewModelDeleteObserver(viewModel = this@StorageListPageViewModel) {
                removeContent(items)
            }
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

    fun copyPath(item: PathItem) {
        provider.registerTask(CopyPathTask(context = provider.context, path = item.value))
    }

    fun choose(element: PathItem) = intent {
        val selected = _selected.value.toMutableList()

        if (selected.contains(element)) {
            selected -= element
        } else {
            selected += element
        }

        _selected.emit(
            value = selected
        )
    }

    fun selectAll() = intent {
        val newSelected = _selected.value

        val data = state.data.filterIsInstance<PathItem>()

        _selected.emit(data + newSelected)
    }

    fun deselectAll() = intent {
        _selected.emit(listOf())
    }

    fun createFile(
        path: Path,
        mode: Int,
    ) = intent {
        FileProvider.runOperation(
            operation = unixCreateFile(
                source = path,
                mode = mode,
            ),
            observer = observer(
                onNext = { source ->
                    message(text = "Creating ${source.getName()} with mode $mode")
                },
                onError = { error ->
                    resolveErrorActions(error)
                },
                onComplete = {
                    message(text = "File ${path.getName()} successfully created")
                }
            )
        )
    }

    fun createDirectory(
        path: Path,
        mode: Int,
    ) = intent {
        FileProvider.runOperation(
            operation = unixCreateDirectory(
                source = path,
                mode = mode
            ),
            observer = observer(
                onNext = { source ->
                    message(text = "Creating ${path.getName()} with mode $mode")
                },
                onError = { error ->
                    resolveErrorActions(error)
                },
                onComplete = {
                    message(text = "Directory ${path.getName()} successfully created")
                }
            )
        )
    }

    fun createLink(
        target: Path,
        link: Path,
    ) = intent {
        FileProvider.runOperation(
            operation = unixCreateSymbolicLink(
                target = target,
                link = link
            ),
            observer = observer(
                onNext = { source ->
                    message(text = "Creating ${link.getName()} link to ${target.getName()}")
                },
                onError = { error ->
                    resolveErrorActions(error)
                },
                onComplete = {
                    message(text = "Link ${link.getName()} successfully created")
                }
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
                            action(
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
                            action(
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
                            action(
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

    fun openDrawerActions(
        items: Set<PathItem>,
    ) = intent {
        side(
            effect = SideEffect(
                showDrawer = true,
                drawerActions = actions.onItems(
                    Content(
                        content = state.data.filterIsInstance<PathItem>(),
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
//        state {
//            State(
//                isLoading = true,
//                loadingTitle = FormatterThemeText(
//                    key = fileListLoadingNavigatingTitleKey,
//                    item.name
//                )
//            )
//        }

        if (item.isDirectory) {
            navigator.navigateTo(item)
            handle[pathItemKey] = item


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (item.value in StorageProvider.restrictedDirs) {
                    state {
                        State(
                            isWarning = true,
                            warningIcon = resources[ic_info_24],
                            warningMessage = "You're trying to enter restricted directory (${item.name})",
                            warningActions = buildList {
                                effect {
                                    title = "Grant"
                                    icon = resources[ic_add_24]
                                    onInvoke = {

                                    }
                                }
                            }
                        )
                    }
                    return@intent
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

    fun shuffle() = intent {
        state {
            copy(
                data = data.filterIsInstance<PathItem>().shuffled()
            )
        }
    }

    private fun parseState(item: PathItem) = intent {
        val data = try {
            provider.provideList(item)
        } catch (error: Throwable) {
            state {
                State(
                    isWarning = true,
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
                    warningMessage = FormatterThemeText(
                        key = fileListWarningEmptyTitleKey,
                        item.name
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

    fun message(
        text: String,
        title: String? = null,
        action: View.OnClickListener? = null,
    ) = intent {
        side(
            SideEffect(
                message = text,
                messageActionTitle = title,
                messageAction = action
            )
        )
    }

    fun dialog(type: StorageListPageScreen.DialogType) = intent {
        when (type) {
            StorageListPageScreen.DialogType.CREATE -> {
                side(
                    effect = SideEffect(
                        showCreateDialog = true
                    )
                )
            }
            StorageListPageScreen.DialogType.RENAME -> {
                side(
                    effect = SideEffect(
                        showRenameDialog = true
                    )
                )
            }
            StorageListPageScreen.DialogType.FILE_SYSTEM_STATUS -> {

            }
        }
    }

}