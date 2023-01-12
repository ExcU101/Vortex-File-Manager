package io.github.excu101.vortex.ui.screen.storage.page.list

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.observer.PathObservableEventType
import io.github.excu101.filesystem.fs.operation.FileOperation
import io.github.excu101.filesystem.fs.operation.observer
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.operation.option.Options.Copy.NoFollowLinks
import io.github.excu101.filesystem.fs.operation.option.Options.Copy.ReplaceExists
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.unix.utils.unixCopy
import io.github.excu101.filesystem.unix.utils.unixCreateDirectory
import io.github.excu101.filesystem.unix.utils.unixCreateFile
import io.github.excu101.filesystem.unix.utils.unixCreateSymbolicLink
import io.github.excu101.filesystem.unix.utils.unixCut
import io.github.excu101.filesystem.unix.utils.unixDelete
import io.github.excu101.filesystem.unix.utils.unixRename
import io.github.excu101.pluginsystem.ui.theme.FormatterThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.base.utils.ViewModelContainerHandler
import io.github.excu101.vortex.base.utils.intent
import io.github.excu101.vortex.base.utils.logIt
import io.github.excu101.vortex.base.utils.new
import io.github.excu101.vortex.base.utils.side
import io.github.excu101.vortex.base.utils.state
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.trail.TrailNavigator
import io.github.excu101.vortex.provider.FileOperationActionHandler
import io.github.excu101.vortex.provider.StorageActionContentProvider
import io.github.excu101.vortex.provider.storage.CopyTask
import io.github.excu101.vortex.provider.storage.Filter
import io.github.excu101.vortex.provider.storage.MoveTask
import io.github.excu101.vortex.provider.storage.Order
import io.github.excu101.vortex.provider.storage.Sorter
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.provider.storage.Task
import io.github.excu101.vortex.provider.storage.View.COLUMN
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.info.info
import io.github.excu101.vortex.ui.component.item.text.text
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.theme.key.fileListLoadingInitiatingTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningEmptyTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningFullStorageAccessTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningNotificationAccessTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningStorageAccessTitleKey
import io.github.excu101.vortex.ui.icon.Icons
import io.github.excu101.vortex.ui.screen.storage.Actions
import io.github.excu101.vortex.ui.screen.storage.Actions.BarActions
import io.github.excu101.vortex.ui.screen.storage.Actions.Tasks
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.DataResolver
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.SideEffect
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.SideEffect.Snackbar
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.SideEffect.StorageAction
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.SideEffect.StorageFilter
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.State
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
    private val handle: SavedStateHandle,
    private val handler: FileOperationActionHandler<String?>,
) : ViewModelContainerHandler<State, SideEffect>(
    State(
        isLoading = true,
        loadingTitle = ThemeText(fileListLoadingInitiatingTitleKey)
    )
) {
    companion object {
        private const val PathItemKey = "PathItem"
    }

    private var onDispose: ((MenuAction) -> Unit)? = null

    private val _selected = MutableStateFlow(listOf<PathItem>())
    val selected: StateFlow<List<PathItem>>
        get() = _selected.asStateFlow()

    val isSelectionEnabled: Boolean
        get() = selected.value.isNotEmpty()

    private val _resolver = MutableStateFlow(DataResolver())
    val resolver: StateFlow<DataResolver>
        get() = _resolver.asStateFlow()

    private val _view = MutableStateFlow(COLUMN)
    val view: StateFlow<io.github.excu101.vortex.provider.storage.View>
        get() = _view.asStateFlow()

    val content: List<Item<*>>
        get() = container.state.value.data

    private val navigator = TrailNavigator()
    val current: PathItem?
        get() = navigator.selected

    val trail: StateFlow<TrailNavigator.Trail>
        get() = navigator.trail

    private val _restrictedDirectories = MutableStateFlow<Uri?>(null)
    val restrictedDirectories: StateFlow<Uri?>
        get() = _restrictedDirectories.asStateFlow()

    private val path =
        handle[PathItemKey] ?: PathItem(Environment.getExternalStorageDirectory().asPath())


    init {
        checkPermission()
        navigateTo(path)
    }

    fun view(
        view: io.github.excu101.vortex.provider.storage.View
    ) = intent {
        _view.emit(view)
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
                        actions = listOf(
                            Actions.ProvideFullStorageAccess
                        )
                    )
                }
                return@intent
            }
        } else {
            if (provider.requiresPermissions()) {
                state {
                    State(
                        isWarning = true,
                        warningMessage = ThemeText(fileListWarningStorageAccessTitleKey),
                        warningIcon = Icons.Rounded.Info,
                        actions = listOf(
                            Actions.ProvideStorageAccess
                        )
                    )
                }
                return@intent
            }
        }

        startCheckingPathTrail()
        startCheckingTasks()
    }

    private fun startCheckingPathTrail() = intent {
        navigator.trail.collect { (items, selectedIndex, selected) ->
            selected?.let { parseState(item = it) }
        }
    }

    private fun startCheckingTasks() = intent {
        provider.tasks.collect { tasks ->
            if (tasks.isNotEmpty()) {
                state {
                    copy(
                        actions = actions + Tasks
                    )
                }
                message("Choose destination and select option in `tasks`")
            }
        }
    }

    fun navigateLeft() = intent {
        navigator.navigateLeft()
    }

    fun navigateRight() = intent {
        navigator.navigateRight()
    }

    fun disposeAction(action: MenuAction): Boolean {
        if (onDispose == null) return false
        onDispose?.invoke(action)
        onDispose = null
        return true
    }

    fun openSingleItemDrawerActions(
        item: PathItem,
        dispose: (MenuAction) -> Unit,
    ) = intent {
        side(StorageAction(StorageActionContentProvider().onSingleItem(item)))
        this@StorageListPageViewModel.onDispose = dispose
    }

    fun openSelectedItemsDrawerActions(
        items: List<PathItem> = selected.value,
        onDispose: (MenuAction) -> Unit,
    ) = intent {
        side(StorageAction(StorageActionContentProvider().onSelectedItems(items)))
        this@StorageListPageViewModel.onDispose = onDispose
    }

    fun openDrawerSortActions() = intent {
        side(
            StorageFilter(
                currentSorter = resolver.value.sorter,
                currentFilter = resolver.value.filter
            )
        )
    }

    fun openDrawerMoreActions() = intent {
        side(StorageAction(StorageActionContentProvider().onMoreActions()))
    }

    fun createNew() = intent {
        side(SideEffect.StorageItemCreate(parent = current))
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
                    actions = if (provider.tasks.value.isNotEmpty()) BarActions + Tasks else BarActions
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
                    actions = if (provider.tasks.value.isNotEmpty()) BarActions + Tasks else BarActions
                )
            }
        } else {
            state {
                State(
                    data = parseContent(data),
                    actions = if (provider.tasks.value.isNotEmpty()) BarActions + Tasks else BarActions
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
            Snackbar(
                message = text,
                messageActionTitle = title,
                messageAction = action
            )
        )
    }

    fun showTasks() = intent {
        side(
            StorageAction(
                content = scope {
                    text(value = "Tasks (${provider.tasks.value.size})")
                    provider.tasks.value.forEachIndexed { index, task ->
                        when (task) {
                            is CopyTask -> {
                                var names = ""
                                task.sources.forEach { path ->
                                    names += path.getName()
                                        .toString() + if (task.sources.lastOrNull() != path) " | " else ""
                                }
                                info(value = names, description = "Copy task $index")
                            }

                            is MoveTask -> {
                                var names = ""
                                task.sources.forEach { path ->
                                    names += path.getName()
                                        .toString() + if (task.sources.lastOrNull() != path) " | " else ""
                                }
                                info(value = names, description = "Cut task $index")
                            }
                        }
                    }
                }
            )
        )
    }

    fun deleteItems(
        items: Set<PathItem> = selected.value.toSet(),
    ) = delete(items.map(PathItem::value).toSet())

    fun delete(
        items: Set<Path>,
    ) = intent {
        operation(
            operation = unixDelete(items),
            onComplete = {
                current?.let { parseState(it) }
                deselect(items.map(::PathItem).toSet())
            }
        )
    }

    fun rename(
        src: PathItem,
        dest: Path,
    ) = rename(src = src.value, dest = dest)

    fun rename(
        src: Path,
        dest: Path,
    ) {
        operation(
            operation = unixRename(src, dest),
            onComplete = {
                message("${src.getName()} successfully renamed to ${dest.getName()}")
                current?.let { navigateTo(it) }
            }
        )
    }

    fun createFile(
        path: Path,
        mode: Int,
        flags: Int = Options.Open.CreateNew or Options.Open.Read or
                Options.Open.Write or
                Options.Open.Append,
    ) {
        operation(
            operation = unixCreateFile(
                source = path,
                mode = mode,
                flags = flags
            ),
            onComplete = {
                val item = PathItem(path)
                val parent = item.parent

                if (parent == null) {
                    message(text = "File ${item.name} successfully created")
                    return@operation
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

    fun task(task: Task) = intent {
        provider.registerTask(task)
    }

    fun watcher(
        item: PathItem,
        vararg types: PathObservableEventType,
    ) = watcher(item.value, *types)

    fun watcher(
        path: Path,
        vararg types: PathObservableEventType,
    ) = intent {
        provider.registerObserver(
            path,
            *types
        ).events.collect { event ->
            event.logIt()
        }
    }

    fun copy(
        sources: Set<Path>,
        dest: Path,
        options: Int = NoFollowLinks and ReplaceExists
    ) = intent {
        operation(
            operation = unixCopy(sources, dest, options),
            onComplete = {
                message("Elements copied to ${dest.getName()}")
            }
        )
    }

    fun cut(
        sources: Set<Path>,
        dest: Path,
        options: Int = NoFollowLinks and ReplaceExists
    ) = intent {
        operation(
            operation = unixCut(sources, dest, options),
            onComplete = {
                message("Elements cut to ${dest.getName()}")
            }
        )
    }

    fun performTask(position: Int) = intent {
        when (val task = provider.tasks.value.getOrNull(position)) {
            is CopyTask -> {
                val dest = current!!.value
                val sources = task.sources

                copy(
                    sources = sources,
                    dest = dest
                )

                provider.unregisterTask(task)
            }
        }
    }

}