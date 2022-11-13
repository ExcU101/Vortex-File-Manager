package io.github.excu101.vortex.ui.screen.storage.pager.page.list

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.observer
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.unix.utils.*
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.action
import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.action
import io.github.excu101.vortex.R.drawable.*
import io.github.excu101.vortex.base.impl.Filter
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.base.impl.Sorter
import io.github.excu101.vortex.base.utils.*
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.trail.TrailNavigator
import io.github.excu101.vortex.provider.FileOperationActionHandler
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.storage.CopyTask
import io.github.excu101.vortex.provider.storage.StorageActionProvider
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.provider.storage.StorageTaskManager
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.drawer.drawerItem
import io.github.excu101.vortex.ui.component.item.text.text
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.screen.storage.pager.page.list.StorageListPageScreen.DataResolver
import io.github.excu101.vortex.ui.screen.storage.pager.page.list.StorageListPageScreen.SideEffect
import io.github.excu101.vortex.ui.screen.storage.pager.page.list.StorageListPageScreen.State
import io.github.excu101.vortex.utils.asItems
import io.github.excu101.vortex.utils.isAndroidR
import io.github.excu101.vortex.utils.isAndroidTiramisu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

@HiltViewModel
class StorageListPageViewModel @Inject constructor(
    private val provider: StorageProvider,
    private val resources: ResourceProvider,
    private val actions: StorageActionProvider,
    private val handle: SavedStateHandle,
    private val handler: FileOperationActionHandler,
) : ViewModelContainerHandler<State, SideEffect>(
    State(
        isLoading = true,
        loadingTitle = ThemeText(fileListLoadingInitiatingTitleKey)
    )
) {
    companion object {
        private const val PathItemKey = "PathItem"
    }

    var onDispose: ((Action) -> Unit)? = null

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
        handle[PathItemKey] ?: PathItem(Environment.getExternalStorageDirectory().asPath())

    private var actionItem = path

    init {
        initActions()
        checkPermission()
    }

    fun openDefaultDrawerActions() = intent {
        side(
            SideEffect(
                showDrawer = true,
                drawerContent = actions.getGroups().asItems()
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
                drawerContent = actions.getGroups().asItems()
            )
        )
        _barActions.applyValue {
            addAll(actions.getActions())
        }
    }

    fun copyPath(item: PathItem) {
        provider.copyPath(item.value)
        message("Copied! (${item.path})")
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

    fun deselect(
        items: Set<PathItem>,
    ) = intent {
        val selected = _selected.value

        _selected.emit(selected - items)
    }

    fun selectAll() = intent {
        val newSelected = _selected.value

        val data = state.data.filterIsInstance<PathItem>()

        _selected.emit(data + newSelected)
    }

    fun deselectAll() = intent {
        _selected.emit(listOf())
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

    fun openSingleItemDrawerActions(
        item: PathItem,
        onDispose: (Action) -> Unit,
    ) = intent {
        side(
            effect = SideEffect(
                showDrawer = true,
                drawerContent = actions.onSingleItem(item).asItems(),
            )
        )
        this@StorageListPageViewModel.onDispose = onDispose
    }

    fun openSelectedItemsDrawerActions(
        items: List<PathItem> = selected.value,
        onDispose: (Action) -> Unit,
    ) = intent {
        side(
            effect = SideEffect(
                showDrawer = true,
                drawerContent = actions.onSelectedItems(items).asItems(),
            )
        )
        this@StorageListPageViewModel.onDispose = onDispose
    }

    fun openDrawerSortActions() = intent {
        side(
            effect = SideEffect(
                showDrawer = true,
                drawerContent = actions.sortActions().asItems()
            )
        )
    }

    fun openDrawerMoreActions() = intent {
        side(
            effect = SideEffect(
                showDrawer = true,
                drawerContent = actions.moreActions(
                    isItemTrailFirst = navigator.selectedIndex.value == 0,
                    isItemTrailLast = navigator.selectedIndex.value == navigator.size - 1,
                    selectedCount = selected.value.count()
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
            handle[PathItemKey] = item

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (item.value in StorageProviderImpl.restrictedDirs) {
                    state {
                        State(
                            isWarning = true,
                            warningIcon = resources[ic_info_24],
                            warningMessage = "You're trying to enter restricted directory (${item.name})",
                            warningActions = buildList {
                                action {
                                    title = "Grant"
                                    icon = resources[ic_add_24]
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

    fun updateItem(old: PathItem) = intent {
        val new = PathItem(old.value) // Create new object will update path-statistic
        val index = content.indexOf(old).logIt()
        val newData = ArrayList<Item<*>>()
        newData.addAll(content)
        newData[index] = new
        state {
            copy(
                data = newData
            )
        }

    }

    fun shuffle() = intent {
        state {
            copy(
                data = data.shuffled()
            )
        }
    }

    private fun parseState(item: PathItem) = intent {
        val data = try {
            provider.getItems(item).toList()
        } catch (error: Throwable) {
            state {
                State(
                    isWarning = true,
                    warningMessage = "Error: " + error.message
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

    fun registerCopyTask(sources: Set<Path>) {
        StorageTaskManager.register(CopyTask(sources))
        message(text = "Registered new task")
    }

    fun showTasks() = intent {
        side(
            SideEffect(
                showDrawer = true,
                drawerContent = scope {
                    text(value = "Tasks") {
                        alignment = TextView.TEXT_ALIGNMENT_CENTER
                    }
                    StorageTaskManager.copyTasks.forEachIndexed { index, task ->
                        drawerItem {
                            title =
                                "Copy task (Items: ${task.sources.map { it.getName().toString() }})"

                        }
                    }
                }
            )
        )
    }

    fun delete(
        items: Set<PathItem> = selected.value.toSet(),
    ) = intent {
        FileProvider.runOperation(
            operation = unixDelete(items.map(PathItem::value)),
            observer = observer(
                onAction = { action ->
                    handler.resolveMessage(action)?.let { title ->
                        message(text = title)
                    }
                },
                onError = { error ->
                    resolveErrorActions(error)
                },
                onComplete = {
                    current?.let { parseState(it) }
                    deselect(items)
                }
            )
        )
    }

    fun rename(
        src: PathItem,
        dest: Path,
    ) {
        FileProvider.runOperation(
            operation = unixRename(src.value, dest),
            observer = observer(
                onAction = { action ->
                    handler.resolveMessage(action)?.let { title ->
                        message(title)
                    }
                },
                onError = { error ->
                    resolveErrorActions(error)
                },
                onComplete = {
                    message("${src.name} successfully renamed to ${dest.getName()}")
                    current?.let { navigateTo(it) }
                }
            )
        )
    }

    fun createFile(
        path: Path,
        mode: Int,
        flags: Set<FileOperation.Option> = setOf(
            Options.Open.CreateNew
        ),
    ) = intent {
        FileProvider.runOperation(
            operation = unixCreateFile(
                source = path,
                mode = mode,
                flags = flags
            ),
            observer = observer(
                onAction = { action ->
                    handler.resolveMessage(action)?.let { title ->
                        message(title)
                    }
                },
                onError = { error ->
                    resolveErrorActions(error)
                },
                onComplete = {
                    val item = PathItem(path)
                    val parent = item.parent

                    if (parent == null) {
                        message(text = "File ${item.name} successfully created")
                        return@observer
                    }

                    if (parent == current) {
                        addContent(setOf(parent))
                    } else {
                        current?.let { navigateTo(it) }
                    }

                    message(
                        text = "File ${item.name} successfully created",
                        title = "Show it",
                        action = {
                            navigateTo(parent)
                        }
                    )
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
                onAction = { action ->
                    handler.resolveMessage(action)?.let { title ->
                        message(title)
                    }
                },
                onError = { error ->
                    resolveErrorActions(error)
                },
                onComplete = {
                    val item = PathItem(path)
                    val parent = item.parent

                    if (parent == null) {
                        message(text = "Directory ${item.name} successfully created")
                        return@observer
                    }

                    if (parent == current) {
                        addContent(setOf(parent))
                    } else {
                        current?.let { navigateTo(it) }
                    }

                    message(
                        text = "Directory ${item.name} successfully created",
                        title = "Show it",
                        action = {
                            navigateTo(parent)
                        }
                    )
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
                onAction = { action ->
                    handler.resolveMessage(action)?.let { title ->
                        message(title)
                    }
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

}