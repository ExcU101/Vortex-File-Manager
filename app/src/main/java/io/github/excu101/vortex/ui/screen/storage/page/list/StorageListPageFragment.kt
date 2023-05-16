package io.github.excu101.vortex.ui.screen.storage.page.list

// I hate imports...

import android.Manifest
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.core.graphics.luminance
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.utils.*
import io.github.excu101.manager.ui.theme.*
import io.github.excu101.vortex.R.drawable.*
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.base.utils.*
import io.github.excu101.vortex.bindVortexService
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.command.*
import io.github.excu101.vortex.provider.contract.Contracts.FullStorageAccess
import io.github.excu101.vortex.provider.contract.Contracts.Permission
import io.github.excu101.vortex.provider.contract.Contracts.RestrictedDirectoriesAccess
import io.github.excu101.vortex.provider.storage.CopyTask
import io.github.excu101.vortex.provider.storage.View.*
import io.github.excu101.vortex.service.VortexServiceBinder
import io.github.excu101.vortex.service.component.file.operation.OperationComponentState
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
import io.github.excu101.vortex.ui.screen.storage.StorageListRenameDialog
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.SideEffect.*
import io.github.excu101.vortex.utils.*
import androidx.activity.result.ActivityResultLauncher as Launcher

// TODO: REFACTOR THIS ALL

class StorageListPageFragment : Fragment(),
    MenuActionListener,
    ThemeColorChangeListener, ItemViewListener<Item<*>>, ItemViewLongListener<Item<*>>,
    CommandConsumer, ServiceConnection {

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
    private val storageAccessLauncher: Launcher<Unit> =
        registerForActivityResult(FullStorageAccess) { isGranted ->
            if (isGranted) {
                viewModel.checkPermission()
            }
        }

    private val permissionLauncher: Launcher<String> =
        registerForActivityResult(Permission) { isGranted ->
            if (isGranted) {
                viewModel.checkPermission()
            }
        }

    @RequiresApi(Q)
    private val restrictedDirectoriesLauncher: Launcher<Uri> =
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

    private var service: VortexServiceBinder? = null

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

        context?.bindVortexService(this, Context.BIND_AUTO_CREATE)

        binding = StorageListPageBinding(requireContext())
        controller = binding?.root?.let { view ->
            WindowCompat.getInsetsController(
                requireActivity().window,
                view
            )
        }

        binding?.list?.adapter = adapter

        activity?.window?.statusBarColor = ThemeColor(storageListBackgroundColorKey)
        activity?.window?.navigationBarColor = ThemeColor(storageListBackgroundColorKey)

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
                    bottom = (bar?.height ?: 0)
                            + compat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                )
                insets
            }

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

                    if (state.search != null) {
                        adapter.replace(items = state.data)
                    }

                    loading.fade(isOut = !state.isLoading, duration = 250L)
                    warning.fade(isOut = !state.isWarning, duration = 250L)
                    list.fade(isOut = state.isLoading || state.isWarning, duration = 250L)
                }
            }

//            repeatedLifecycle {
//                viewModel.bookmarked.collectLatest { bookmarks ->
//                    bookmarks.forEach { bookmark ->
//                        adapter.changed(bookmark, true)
//                    }
//                }
//            }

            repeatedLifecycle {
                viewModel.trail.collect { (items, selectedIndex, selected) ->
                    if (items.isNotEmpty()) {
                        trailAdapter.replace(items)
                    }

                    if (!viewModel.isSelectionEnabled) {

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
                service?.subscribeCount?.collect { count ->
                    count.toastIt(
                        context = requireContext(),
                    )
                }
            }

            repeatedLifecycle {
                service?.operation?.state?.collect { state ->
                    when (state) {
                        OperationComponentState.Idle -> {

                        }

                        is OperationComponentState.OperationAction -> {

                        }

                        is OperationComponentState.OperationCompleted -> {

                        }

                        is OperationComponentState.OperationError -> {

                        }
                    }
                }
            }

            repeatedLifecycle {
                viewModel.selected.collect { selected ->
                    onSelected()

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

                        }
                    }
                }
            }

            repeatedLifecycle {
                viewModel.collectEffect { effect ->
                    when (effect) {
                        is Message -> {
                            effect.title.snackIt(
                                view = requireView(),
                                duration = effect.duration,
                                anchorView = bar,
                                actionTitle = effect.actionTitle,
                                listener = effect.action
                            )
                        }

                        is StorageItemCreate -> {

//                            NavigationController().navigate(
//                                route = Routes.Storage.Create.Page,
//                                Arguments(CreatePage.ParentDirectoryKey to effect.parent)
//                            )
                        }

                        is StorageFilter -> {
                            if (lastDialog != null) lastDialog?.hide()
                            lastDialog = StorageListFilterDialog(
                                context = requireContext(),
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
                // Removes 1 because list has a text element which is not our task
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

    override fun onServiceConnected(name: ComponentName?, serviceBinder: IBinder?) {
        service = serviceBinder as? VortexServiceBinder
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        service = null
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

    private fun onSingleItemActionCall(
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

//            ViewIds.Storage.Menu.AddBookmarkId -> viewModel.addBookmark(item)
//
//            ViewIds.Storage.Menu.RemoveBookmarkId -> viewModel.removeBookmark(item)

            ViewIds.Storage.Menu.CopyId -> viewModel.task(CopyTask(setOf(item.value)))

            ViewIds.Storage.Menu.DeleteId -> viewModel.deleteItems(setOf(item))

            ViewIds.Storage.Menu.AddWatcherId -> viewModel.addWatcher(item)

            ViewIds.Storage.Menu.RemoveWatcherId -> viewModel.removeWatcher(item)

            ViewIds.Storage.Menu.InfoId -> {}

        }
    }

    private fun onSelectedActionCall(
        action: MenuAction,
        view: View,
    ) {
        when (action.id) {
            ViewIds.Storage.Menu.DeleteId -> viewModel.deleteItems()

            ViewIds.Storage.Menu.CopyId -> {
                viewModel.task(CopyTask(viewModel.selected.value.map { it.value }.toSet()))
                viewModel.deselectAll()
            }

            ViewIds.Storage.Menu.DeselectAll -> viewModel.deselect(viewModel.selected.value.toSet())
        }
    }

    private fun onDrawerActionCall(action: MenuAction) {
        if (viewModel.disposeAction(action)) return

        when (action.id) {
            ViewIds.Storage.Menu.ShowTasks -> viewModel.showTasks()
        }

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

            ThemeText(fileListMoreInfoActionTitleKey) -> {

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

            is RegisterWatcherCommand -> viewModel.addWatcher(
                path = command.source,
                types = command.types
            )
        }
    }

    private fun onSelected() {
        if (isAdded)
            if (viewModel.selected.value.isNotEmpty()) {
                bar?.setTitleWithAnimation(
                    title = viewModel.selected.value.size.toString(),
                    isReverse = adapter.getSelectedCount() < viewModel.selected.value.size
                )
                bar?.subtitle = Size(viewModel.selected.value.fold(initial = 0L) { init, current ->
                    init + current.size.memory
                }).convertToThemeText()
            } else {
                bar?.setTitleWithAnimation(
                    title = viewModel.current?.name,
                    isVertical = false,
                    isReverse = false
                )
                bar?.subtitle = null
            }
    }

//    override fun onPageSelected() {
//        super.onPageSelected()
//        bar?.registerListener(listener = this)
//
//        bar?.show()
//        bar?.replaceItems(viewModel.container.state.value.actions)
//
//        onSelected()
//    }
//
//    override fun onPageUnselected() {
//        bar?.unregisterListener(listener = this)
//        super.onPageSelected()
//    }

    override fun onMenuActionCall(action: MenuAction): Boolean {
        when (action.id) {
            ViewIds.Storage.Menu.ProvideFullStorageAccessId -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    storageAccessLauncher.launch()
                }
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

        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Theme.unregisterColorChangeListener(this)
        binding?.onDestroy()
        binding = null
        backPressed.isEnabled = false
        lastDialog = null
        context?.unbindService(this)
    }

    override fun onColorChanged() {
        val isLight = ThemeColor(trailSurfaceColorKey).luminance > 0.5F
        controller?.isAppearanceLightStatusBars = isLight
        controller?.isAppearanceLightNavigationBars = isLight
    }
}