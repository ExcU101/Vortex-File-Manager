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
import io.github.excu101.vortex.R
import io.github.excu101.vortex.base.utils.*
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.data.header.TextHeaderItem
import io.github.excu101.vortex.data.storage.MutablePathItemMapSet
import io.github.excu101.vortex.data.storage.PathItemGroup
import io.github.excu101.vortex.data.storage.PathItemMapSet
import io.github.excu101.vortex.data.trail.TrailNavigator
import io.github.excu101.vortex.provider.ResourceProvider
import io.github.excu101.vortex.provider.StorageProvider
import io.github.excu101.vortex.provider.storage.StorageActionProvider
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.screen.list.StorageScreenState.Companion.loading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class StorageListViewModel  @Inject  constructor(
    private val provider: StorageProvider,
    private val resources: ResourceProvider,
    private val actions: StorageActionProvider,
    private val handle: SavedStateHandle,
) : ViewModelContainerHandler<StorageScreenState, StorageScreenSideEffect>(
    loading()
) {

    private val wrapper: StorageServiceWrapper = StorageServiceWrapper()

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

    private val path = PathItem(Environment.getExternalStorageDirectory().asPath())

    init {
        initActions()
        checkPermission()
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

    fun selectAll() = intent {
        handle[SELECTED_KEY] =
            state.data.filterIsInstance<PathItem>()
    }

    fun addActions(actions: List<GroupAction>) = intent {

    }

    fun delete(items: Set<PathItem> = selected.value) = intent {

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

    fun navigateBack() = intent {
        navigator.navigateUp()

        navigator.selectedItem?.let {
            val content = try {
                parseContent(provider.provideList(it))
            } catch (error: Throwable) {
                state {
                    StorageScreenState(
                        warningMessage = error.message
                    )
                }
                return@let
            }

            state {
                StorageScreenState(
                    data = content
                )
            }
        }
    }

    fun showDrawer() = intent {
        side(
            StorageScreenSideEffect(
                showDrawer = true,
                drawerActions = listOf(
                    TextHeaderItem("Default"),
                    ActionHeaderItem(
                        Action(
                            title = "Delete",
                            icon = resources.getDrawable(R.drawable.ic_delete_24)
                        )
                    )
                )
            )
        )
    }

    fun navigateTo(
        item: PathItem,
    ) = intent {
        state {
            loading(title = "Navigating to ${item.name}...")
        }

        if (item.isDirectory) {
            navigator.navigateTo(item)
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
                        warningMessage = "${item.name} is empty"
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

    fun parseContent(data: List<PathItem>): MutableList<Item<*>> {
        val content = mutableListOf<Item<*>>()

        val folders = data.filter { it.isDirectory }
        val files = data.filter { it.isFile }

        if (folders.isNotEmpty()) {
            content.add(TextHeaderItem("Folders (${folders.size})"))
            content.addAll(folders)
        }

        if (files.isNotEmpty()) {
            content.add(TextHeaderItem("Files (${files.size})"))
            content.addAll(files)
        }

        return content
    }

}