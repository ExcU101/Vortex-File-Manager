package io.github.excu101.vortex.ui.screen.storage.page.list

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.observer.type.CreateEventType
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.observer
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.unix.observer.type.OpenedEventType
import io.github.excu101.filesystem.unix.utils.*
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.action
import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.pluginsystem.utils.action
import io.github.excu101.vortex.base.impl.Filter
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.base.impl.Sorter
import io.github.excu101.vortex.base.utils.*
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.trail.TrailNavigator
import io.github.excu101.vortex.provider.FileOperationActionHandler
import io.github.excu101.vortex.provider.StorageActionContentProvider
import io.github.excu101.vortex.provider.storage.CopyTask
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.provider.storage.Task
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.info.info
import io.github.excu101.vortex.ui.component.item.text.text

import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.icon.Icons
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.DataResolver
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.Dialog.StorageAction
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.SideEffect
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.State
import io.github.excu101.vortex.utils.isAndroidR
import io.github.excu101.vortex.utils.isAndroidTiramisu
import io.github.excu101.vortex.utils.onTrue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StorageListPageViewModel @Inject constructor(
    private val provider: StorageProvider,
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

    private val _dialog = Channel<StorageListPageScreen.Dialog>()
    val dialog: Flow<StorageListPageScreen.Dialog>
        get() = _dialog.receiveAsFlow()

    private val _tasks = MutableStateFlow(listOf<Task>())
    val tasks: StateFlow<List<Task>>
        get() = _tasks.asStateFlow()

    private var onDispose: ((Action) -> Unit)? = null

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

    val navigator = TrailNavigator()

    val current: PathItem?
        get() = navigator.selection.value.item

    private val _restrictedDirectories = MutableStateFlow<Uri?>(null)
    val restrictedDirectories: StateFlow<Uri?>
        get() = _restrictedDirectories.asStateFlow()

    private val path =
        handle[PathItemKey] ?: PathItem(Environment.getExternalStorageDirectory().asPath())

    init {
        checkPermission()
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
                        warningIcon = Icons.Rounded.Info,
                        warningActions = listOf(
                            action(
                                title = ThemeText(fileListWarningNotificationAccessActionTitleKey),
                                icon = Icons.Rounded.Add
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
                        warningIcon = Icons.Rounded.Info,
                        warningActions = listOf(
                            action(
                                title = ThemeText(fileListWarningFullStorageAccessActionTitleKey),
                                icon = Icons.Rounded.Add
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
                        warningIcon = Icons.Rounded.Info,
                        warningActions = listOf(
                            action(
                                title = ThemeText(fileListWarningStorageAccessActionTitleKey),
                                icon = Icons.Rounded.Add
                            )
                        )
                    )
                }
            }
        }
    }

    fun navigateLeft() = intent {
        navigator.navigateLeft()

        current?.let {
            parseState(it)
        }
    }

    fun navigateRight() = intent {
        navigator.navigateRight()

        current?.let {
            parseState(it)
        }
    }

    fun disposeAction(action: Action): Boolean {
        if (onDispose == null) return false
        onDispose?.invoke(action)
        onDispose = null
        return true
    }

    fun openSingleItemDrawerActions(
        item: PathItem,
        dispose: (Action) -> Unit,
    ) = intent {
        dialog(StorageAction(StorageActionContentProvider().onSingleItem(item)))
        this@StorageListPageViewModel.onDispose = dispose
    }

    fun openSelectedItemsDrawerActions(
        items: List<PathItem> = selected.value,
        onDispose: (Action) -> Unit,
    ) = intent {
        dialog(StorageAction(StorageActionContentProvider().onSelectedItems(items)))
        this@StorageListPageViewModel.onDispose = onDispose
    }

    fun openDrawerSortActions() = intent {
        dialog(StorageAction(StorageActionContentProvider().onSortActions()))
    }

    fun openDrawerMoreActions() = intent {
        dialog(StorageAction(StorageActionContentProvider().onMoreActions()))
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
                            warningIcon = Icons.Rounded.Info,
                            warningMessage = "You're trying to enter restricted directory (${item.name})",
                            warningActions = buildList {
                                action {
                                    title = "Grant"
                                    icon = Icons.Rounded.Add
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
        message("Error ")
        error.printStackTrace()
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
                    warningMessage = "Error: " + error.message,
                    warningActions = buildList {
                        action {
                            title = "Reload"
                            icon = Icons.Rounded.Refresh
                        }
                        if (item.containsParent) {
                            action {
                                title = "Back to parent"
                                icon = Icons.Rounded.Back
                            }
                        }
                    }
                )
            }
            return@intent
        }

        if (data.isEmpty()) {
            state {
                State(
                    isWarning = true,
                    warningIcon = Icons.Rounded.Folder,
                    warningMessage = FormatterThemeText(
                        key = fileListWarningEmptyTitleKey,
                        item.name
                    ),
                    warningActions = buildList {
                        action {
                            title = "Add new"
                            icon = Icons.Rounded.Add
                        }
                        action {
                            title = "Back to parent"
                            icon = Icons.Rounded.Back
                        }
                        action {
                            title = "Reload"
                            icon = Icons.Rounded.Refresh
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

    fun showTasks() = intent {
        dialog(
            StorageAction(
                content = scope {
                    text(value = "Tasks (${provider.tasks.size})")
                    provider.tasks.forEachIndexed { index, task ->
                        when (task) {
                            is CopyTask -> {
                                var names = ""
                                task.sources.forEach { path ->
                                    names += path.getName()
                                        .toString() + if (task.sources.last() != path) " | " else ""
                                }
                                info(value = names, description = "Copy task $index")
                            }
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
        operation(
            operation = unixCreateDirectory(
                source = path,
                mode = mode
            ),
            onComplete = {
                val item = PathItem(path)
                val parent = item.parent

                if (parent == null) {
                    message(text = "Directory ${item.name} successfully created")
                    return@operation
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
    }

    fun createLink(
        target: Path,
        link: Path,
    ) = intent {
        operation(
            operation = unixCreateSymbolicLink(
                target = target,
                link = link
            ),
            onComplete = {
                message(text = "Link ${link.getName()} successfully created")
            }
        )
    }

    private inline fun operation(
        operation: FileOperation,
        crossinline onComplete: () -> Unit,
    ) {
        FileProvider.runOperation(
            operation = operation,
            observer = observer(
                onAction = { action ->
                    handler.resolveMessage(action)?.let(::message)
                },
                onError = { error ->
                    resolveErrorActions(error)
                },
                onComplete = onComplete
            )
        )
    }

    fun dialog(dialog: StorageListPageScreen.Dialog) = intent {
        _dialog.send(dialog)
    }

    fun task(task: Task) = intent {
        provider.registerTask(task).onTrue {
            _tasks.emit(provider.tasks)
            message("Choose destination and select option in `tasks`")
        }
    }

    fun watcher(item: PathItem) = intent {
        provider.registerObserver(
            item.value,
            CreateEventType(),
            OpenedEventType()
        ).events.collect { event ->
            event.logIt()
        }
    }

    fun performTask(position: Int) {
        when (val task = provider.tasks.getOrNull(position)) {
            is CopyTask -> {
                val dest = current!!
                operation(
                    operation = unixCopy(task.sources, dest.value),
                    onComplete = {
                        message("Elements copied to ${dest.name}")
                    }
                )
            }
        }
    }

}