package io.github.excu101.vortex.ui.screen.storage.page.list

// I hate imports...

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.core.graphics.luminance
import androidx.core.os.bundleOf
import androidx.core.view.*
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar.make
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.utils.*
import io.github.excu101.pluginsystem.model.Color.Companion.Transparent
import io.github.excu101.pluginsystem.ui.theme.*
import io.github.excu101.vortex.R.drawable.*
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.base.utils.collectEffect
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.base.utils.logIt
import io.github.excu101.vortex.base.utils.toastIt
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.navigation.dsl.Arguments
import io.github.excu101.vortex.navigation.dsl.FragmentFactory
import io.github.excu101.vortex.navigation.navigator.FragmentNavigator
import io.github.excu101.vortex.navigation.utils.NavigationController
import io.github.excu101.vortex.provider.command.Command
import io.github.excu101.vortex.provider.command.CommandConsumer
import io.github.excu101.vortex.provider.command.CopyFilesCommand
import io.github.excu101.vortex.provider.command.CreateDirectoryCommand
import io.github.excu101.vortex.provider.command.CreateFileCommand
import io.github.excu101.vortex.provider.command.CutFilesCommand
import io.github.excu101.vortex.provider.command.DeleteFilesCommand
import io.github.excu101.vortex.provider.command.RegisterWatcherCommand
import io.github.excu101.vortex.provider.command.RenameFileCommand
import io.github.excu101.vortex.provider.contract.Contracts
import io.github.excu101.vortex.provider.contract.Contracts.Permission
import io.github.excu101.vortex.provider.contract.Contracts.RestrictedDirectoriesAccess
import io.github.excu101.vortex.provider.storage.CopyTask
import io.github.excu101.vortex.provider.storage.Order
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.provider.storage.View.*
import io.github.excu101.vortex.ui.component.*
import io.github.excu101.vortex.ui.component.animation.fade
import io.github.excu101.vortex.ui.component.drawer.ItemBottomDrawerFragment
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.info.InfoItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.*
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.navigation.AppNavigation.Args.Storage
import io.github.excu101.vortex.ui.navigation.AppNavigation.Args.Storage.CreatePage
import io.github.excu101.vortex.ui.navigation.AppNavigation.Routes
import io.github.excu101.vortex.ui.navigation.PageFragment
import io.github.excu101.vortex.ui.screen.storage.StorageListRenameDialog
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.SideEffect.*
import io.github.excu101.vortex.utils.*

@AndroidEntryPoint
class StorageListPageFragment : PageFragment(),
    MenuActionListener,
    ThemeColorChangeListener, ItemViewListener<Item<*>>, ItemViewLongListener<Item<*>>,
    CommandConsumer {

    companion object : FragmentFactory<StorageListPageFragment> {
        override fun createFragment(): StorageListPageFragment = StorageListPageFragment()
    }

    private val viewModel by viewModels<StorageListPageViewModel>()

    private val adapter: ItemAdapter<SuperItem> = ItemAdapter(
        ItemViewTypes.TextItem with TextItem,
        ItemViewTypes.StorageItem with PathItem
    )

    private var binding: StorageListPageBinding? = null

    private var lastDialog: Dialog? = null

    private val backPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.navigateLeft()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private val storageAccessLauncher: ActivityResultLauncher<Unit> =
        registerForActivityResult(Contracts.FullStorageAccess) { isGranted ->
            if (isGranted) {
                viewModel.checkPermission()
            }
        }

    private val permissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(Permission) { isGranted ->
            if (isGranted) {
                viewModel.checkPermission()
            }
        }

    @RequiresApi(Q)
    private val restrictedDirectoriesLauncher: ActivityResultLauncher<Uri> =
        registerForActivityResult(
            RestrictedDirectoriesAccess
        ) { uri ->
            if (uri != null) {
                requireContext().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
        }

    private var controller: WindowInsetsControllerCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("operation") { key, bundle ->
            val path = bundle.getString("path")!!
            val mode = bundle.getInt("mode")
            val isDir = bundle.getBoolean("isDir")

            if (isDir) {
                viewModel.createDirectory(path = parsePath(path), mode)
            } else {
                viewModel.createFile(path = parsePath(path), mode)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = StorageListPageBinding(requireContext())
        controller = binding?.root?.let { view ->
            WindowCompat.getInsetsController(
                requireActivity().window,
                view
            )
        }

        binding?.list?.adapter = adapter

        activity?.window?.statusBarColor = Transparent.value
        activity?.window?.navigationBarColor = Transparent.value

        Theme.registerColorChangeListener(listener = this)

        return binding?.root
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        super.onViewCreated(root, savedInstanceState)
        val trailAdapter = binding?.trail?.adapter!!

        binding?.apply {
            list.setOnApplyWindowInsetsListener { view, insets ->
                val compat = WindowInsetsCompat.toWindowInsetsCompat(insets)
                view.updatePadding(
                    bottom = compat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                )
                insets
            }
            list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.layoutManager?.findViewByPosition(0) == null) {
                        bar?.hide()
                    } else {
                        bar?.show()
                    }
                }
            })

            scroller.bindRecycler(binding?.list)

            trailAdapter.register(listener = this@StorageListPageFragment)
            trailAdapter.registerLong(listener = this@StorageListPageFragment)

            adapter.register(listener = this@StorageListPageFragment)
            adapter.registerLong(listener = this@StorageListPageFragment)

            repeatedLifecycle {
                viewModel.collectState { state ->
                    loading.title = state.loadingTitle

                    warning.icon = state.warningIcon
                    warning.message = state.warningMessage

                    bar?.replaceItems(state.actions)

                    if (state.data.isNotEmpty()) {
                        adapter.replace(items = state.data)
                    }

                    loading.fade(isOut = !state.isLoading, duration = 250L)
                    warning.fade(isOut = !state.isWarning, duration = 250L)
                    list.fade(isOut = state.isLoading || state.isWarning, duration = 250L)
                }
            }

            repeatedLifecycle {
                viewModel.trail.collect { (items, selectedIndex, selected) ->
                    if (items.isNotEmpty()) {
                        trailAdapter.replace(items)
                    }

                    if (!viewModel.isSelectionEnabled) {
                        onPageSelected()
                    }
                    if (selectedIndex >= 0) {
                        binding?.trail?.smoothScrollToPosition(selectedIndex)
                        trailAdapter.updateSelected(selectedIndex)
                    }

                    if (selected == PathItem(getExternalStorageDirectory().asPath())) {
                        backPressed.isEnabled = false
                    }
                }
            }

            repeatedLifecycle {
                viewModel.selected.collect { selected ->
                    onPageSelected()

                    adapter.replaceSelected(selected = selected)
                }
            }

            repeatedLifecycle {
                viewModel.view.collect {
                    when (it) {
                        COLUMN -> {
                            list.layoutManager = LinearLayoutManager(requireContext())
                        }

                        GRID -> {
                            list.layoutManager =
                                GridLayoutManager(requireContext(), 3).apply {
                                    spanSizeLookup = object : SpanSizeLookup() {
                                        override fun getSpanSize(position: Int): Int =
                                            if (adapter[position] is TextItem) {
                                                3
                                            } else {
                                                1
                                            }
                                    }
                                }
                        }
                    }
                }
            }

            repeatedLifecycle {
                viewModel.collectEffect { effect ->
                    when (effect) {
                        is Snackbar -> {
                            make(
                                requireView(),
                                effect.message,
                                effect.messageDuration
                            ).setAnchorView(bar).setAction(
                                effect.messageActionTitle,
                                effect.messageAction
                            ).show()
                        }

                        is StorageItemCreate -> {
                            NavigationController().navigate(
                                route = Routes.Storage.Create.Page,
                                Arguments(CreatePage.ParentDirectoryKey to effect.parent)
                            )
                        }

                        is StorageFilter -> {
                            if (lastDialog != null) lastDialog?.hide()
                            lastDialog = StorageListFilterDialog(
                                requireContext(),
                                onViewChange = viewModel::view,
                                onFilterChange = viewModel::filter,
                                onOrderChange = viewModel::order,
                                onSortChange = viewModel::sort
                            )

                            lastDialog?.show()
                        }

                        is StorageAction -> {
                            if (lastDialog != null) lastDialog?.hide()

                            lastDialog = ItemBottomDrawerFragment(
                                requireContext(),
                                *DrawerViewHolderFactories + (ItemViewTypes.InfoItem to InfoItem as ViewHolderFactory<Item<*>>)
                            ).withItems(
                                items = effect.content
                            ).register(::onClick)
                            lastDialog?.show()
                        }
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(backPressed)
    }

    override fun onClick(view: View, item: Item<*>, position: Int) {
        when (item) {
            is DrawerItem -> {
                onDrawerActionCall(item.value)
            }

            is InfoItem -> {
                // Removes 1 because list has a text element which is not out task
                viewModel.performTask(position - 1)
            }

            is PathItem -> {
                when (view.id) {
                    ViewIds.Storage.Trail.RootId -> {
                        viewModel.navigateTo(item = item)
                        bar?.show()
                    }

                    ViewIds.Storage.Item.IconId -> {
                        viewModel.choose(element = item)
                    }

                    ViewIds.Storage.Item.RootId -> {
                        viewModel.navigateTo(item = item)
                        bar?.show()
                    }
                }
            }
        }
    }

    override fun onLongClick(view: View, item: Item<*>, position: Int): Boolean {
        item as PathItem
        return when (view.id) {
            ViewIds.Storage.Trail.RootId -> {
                viewModel.openSingleItemDrawerActions(item) { action ->
                    onSingleItemActionCall(action, item, view)
                }
                true
            }

            ViewIds.Storage.Item.IconId -> {
                if (viewModel.selected.value.size <= 1) {
                    viewModel.openSingleItemDrawerActions(item) { action ->
                        onSingleItemActionCall(
                            action = action,
                            item = item,
                            view = view
                        )
                    }
                } else {
                    viewModel.openSelectedItemsDrawerActions { action ->
                        onSelectedActionCall(
                            action = action,
                            view = view
                        )
                    }
                }
                true
            }

            ViewIds.Storage.Item.RootId -> {
                if (item in viewModel.selected.value) {
                    if (viewModel.selected.value.size <= 1) {
                        viewModel.openSingleItemDrawerActions(item) { action ->
                            onSingleItemActionCall(
                                action = action,
                                item = item,
                                view = view
                            )
                        }
                    } else {
                        viewModel.openSelectedItemsDrawerActions { action ->
                            onSelectedActionCall(
                                action = action,
                                view = view
                            )
                        }
                    }
                } else {
                    viewModel.choose(element = item)
                }
                true
            }

            else -> false
        }
    }

    fun onSingleItemActionCall(
        action: MenuAction,
        item: PathItem,
        view: View,
    ) {
        when (action.id) {
            ViewIds.Storage.Menu.OpenId -> viewModel.navigateTo(item)

            ViewIds.Storage.Menu.AddNewId -> {
                if (lastDialog != null) {
                    lastDialog?.dismiss()
                }
                viewModel.createNew()
            }

            ViewIds.Storage.Menu.CopyPathId -> viewModel.copyPath(item)

            ViewIds.Storage.Menu.RenameId -> {
                StorageListRenameDialog(
                    context = requireContext(),
                    source = item.value
                ) { dest ->
                    viewModel.rename(
                        item,
                        dest
                    )
                }.show()
            }

            ViewIds.Storage.Menu.AddBookmarkId -> {
                if (StorageBookmarkProvider.unregister(item)) {
                    adapter.changed(item)
                }
            }

            ViewIds.Storage.Menu.RemoveBookmarkId -> {
                if (StorageBookmarkProvider.unregister(item)) {
                    adapter.changed(item)
                }
            }

            ViewIds.Storage.Menu.CopyId -> viewModel.task(CopyTask(setOf(item.value)))

            ViewIds.Storage.Menu.DeleteId -> viewModel.deleteItems(setOf(item))

            ViewIds.Storage.Menu.AddWatcherId -> {
//                viewModel.watcher(item)
            }

            ViewIds.Storage.Menu.InfoId -> {
                NavigationController().navigate(
                    Routes.Storage.List.ItemPageInfo,
                    Arguments(Storage.ItemInfoPage.ItemInfoPageKey to item)
                )
            }
        }
    }

    fun onSelectedActionCall(
        action: MenuAction,
        view: View,
    ) {
        when (action.title) {
            ThemeText(storageListOperationDeleteActionTitleKey) -> viewModel.deleteItems()

            ThemeText(storageListOperationCopyActionTitleKey) -> {
                viewModel.task(CopyTask(viewModel.selected.value.map { it.value }.toSet()))
                viewModel.deselectAll()
            }

            "Deselect" -> viewModel.deselect(viewModel.selected.value.toSet())
        }
    }

    fun onDrawerActionCall(action: MenuAction) {
        if (viewModel.disposeAction(action)) return

        // TODO: Replace by action.id
        when (action.title) {
            "Get file system info" -> {
                viewModel.current?.value?.let { path ->
                    var result = ""
                    val store = FileProvider.getFileStore(path)
                    result += "Name:  ${store.name}\n"
                    result += "Type: ${store.type}\n"
                    result.toastIt(requireContext())
                }
            }

            ThemeText(fileListMoreSelectAllActionTitleKey) -> viewModel.selectAll()

            ThemeText(fileListMoreDeselectAllActionTitleKey) -> viewModel.deselectAll()

            ThemeText(fileListMoreNavigateLeftActionTitleKey) -> viewModel.navigateLeft()

            ThemeText(fileListMoreNavigateRightActionTitleKey) -> viewModel.navigateRight()

            ThemeText(fileListOrderAscendingActionTitleKey) -> viewModel.order(Order.ASCENDING)

            ThemeText(fileListOrderDescendingActionTitleKey) -> viewModel.order(Order.DESCENDING)

            ThemeText(fileListSortNameActionTitleKey) -> viewModel.sort(PathItemSorters.Name)

            ThemeText(fileListSortPathActionTitleKey) -> viewModel.sort(PathItemSorters.Path)

            ThemeText(fileListSortSizeActionTitleKey) -> viewModel.sort(PathItemSorters.Size)

            ThemeText(fileListSortLastModifiedTimeActionTitleKey) -> viewModel.sort(PathItemSorters.LastModifiedTime)

            ThemeText(fileListSortLastAccessTimeActionTitleKey) -> viewModel.sort(PathItemSorters.LastAccessTime)

            ThemeText(fileListSortCreationTimeActionTitleKey) -> viewModel.sort(PathItemSorters.CreationTime)

            // Filter
            ThemeText(fileListFilterOnlyFilesActionTitleKey) -> viewModel.filter(PathItemFilters.OnlyFile)

            ThemeText(fileListFilterOnlyFoldersActionTitleKey) -> viewModel.filter(PathItemFilters.OnlyFolder)

            ThemeText(fileListFilterOnlyVideoFileActionTitleKey) -> viewModel.filter(PathItemFilters.OnlyVideoFile)

            ThemeText(fileListFilterOnlyVideoFileActionTitleKey) -> viewModel.filter(PathItemFilters.OnlyApplicationFile)

            ThemeText(fileListFilterOnlyImageFileActionTitleKey) -> viewModel.filter(PathItemFilters.OnlyImageFile)

            ThemeText(fileListFilterOnlyImageFileActionTitleKey) -> viewModel.filter(PathItemFilters.OnlyImageFile)

            ThemeText(fileListMoreInfoActionTitleKey) -> {
                viewModel.current?.let { current ->
                    NavigationController().navigate(
                        route = Routes.Storage.List.ItemPageInfo,
                        FragmentNavigator.Arguments(
                            bundleOf(Storage.ItemInfoPage.ItemInfoPageKey to current)
                        )
                    )
                }
            }

            "Open tasks" -> viewModel.showTasks()

            "Create symbolic link" -> {

            }

            "Shuffle list" -> viewModel.shuffle()

            "Add random-name link" -> {
                var item: Item<*>? = null
                while (item !is PathItem) {
                    item = viewModel.container.state.value.data.random()
                }

                viewModel.current?.let { current ->
                    viewModel.createLink(
                        target = item.value,
                        link = (item.value.parent!! resolve RandomString()).logIt(),
                    )
                }
            }

            "Add random-name directory" -> {
                viewModel.current?.let { current ->
                    viewModel.createDirectory(
                        path = current.value.resolve(RandomString()),
                        mode = defaultMode
                    )
                }
            }

            "Add random-name file" -> {
                viewModel.current?.let { current ->
                    viewModel.createFile(
                        path = current.value.resolve(RandomString()),
                        mode = defaultMode
                    )
                }
            }
        }
        lastDialog?.dismiss()
    }

    fun onWarningActionCall(action: MenuAction) {
        when (action.title) {
            "Grant" -> {
                if (isAndroidQ) {

                }
            }

            "Add new" -> viewModel.createNew()

            "Reload" -> viewModel.current?.let(viewModel::navigateTo)

            "Back to parent" -> viewModel.navigateLeft()

            ThemeText(fileListWarningStorageAccessActionTitleKey) -> {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            ThemeText(fileListWarningFullStorageAccessActionTitleKey) -> {
                if (isAndroidR) {
                    storageAccessLauncher.launch()
                }
            }

            ThemeText(fileListWarningNotificationAccessActionTitleKey) -> {
                if (isAndroidTiramisu) {
                    permissionLauncher.launch(POST_NOTIFICATIONS)
                }
            }
        }
    }

    // Using as EventBus pattern
    override suspend fun consume(command: Command) {
        when (command) {
            is CreateFileCommand -> viewModel.createFile(
                path = command.dest,
                mode = command.mode
            )

            is CreateDirectoryCommand -> viewModel.createDirectory(
                path = command.dest,
                mode = command.mode
            )

            is DeleteFilesCommand -> viewModel.delete(
                items = command.sources
            )

            is RenameFileCommand -> viewModel.rename(
                src = command.source,
                dest = command.dest
            )

            is CopyFilesCommand -> viewModel.copy(
                sources = command.sources,
                dest = command.dest,
                options = command.options
            )

            is CutFilesCommand -> viewModel.cut(
                sources = command.sources,
                dest = command.dest,
                options = command.options
            )

            is RegisterWatcherCommand -> viewModel.watcher(
                path = command.source,
                types = command.types
            )
        }
    }

    fun onSelected() {
        if (viewModel.selected.value.isNotEmpty()) {
            bar?.title = viewModel.selected.value.size.toString()
            bar?.subtitle = Size(viewModel.selected.value.fold(initial = 0L) { init, current ->
                init + current.size.memory
            }).convertToThemeText()
        } else {
            bar?.subtitle = null
            bar?.title = null
        }
    }

    override fun onPageSelected() {
        bar?.registerListener(listener = this)

        bar?.show()
        bar?.replaceItems(viewModel.container.state.value.actions)

        onSelected()
    }

    override fun onPageUnselected() {
        bar?.unregisterListener(listener = this)
    }

    override fun onMenuActionCall(action: MenuAction) {
        // TODO: Replace by action.id
        when (action.id) {
            ViewIds.Storage.Menu.ProvideFullStorageAccessId -> {
                storageAccessLauncher.launch()
            }

            ViewIds.Storage.Menu.ProvideStorageAccessId -> {
                // Android will accept both types (read and write)
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            ViewIds.Storage.Menu.SearchId -> {

            }

            ViewIds.Storage.Menu.TasksId -> {
                viewModel.showTasks()
            }

            ViewIds.Storage.Menu.MoreId -> {
                viewModel.openDrawerMoreActions()
            }

            ViewIds.Storage.Menu.SortId -> {
                viewModel.openDrawerSortActions()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Theme.unregisterColorChangeListener(this)
        binding?.onDestroy()
        binding = null
        backPressed.isEnabled = false
        lastDialog = null
        onPageUnselected()
    }

    override fun onColorChanged() {
        val isLight = ThemeColor(trailSurfaceColorKey).luminance > 0.5F
        controller?.isAppearanceLightStatusBars = isLight
        controller?.isAppearanceLightNavigationBars = isLight
    }
}