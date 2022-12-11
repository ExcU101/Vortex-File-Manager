package io.github.excu101.vortex.ui.screen.storage.page.list

// I hate imports...

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
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
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.graphics.luminance
import androidx.core.os.bundleOf
import androidx.core.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar.make
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.utils.*
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.Color.Companion.Transparent
import io.github.excu101.pluginsystem.model.action
import io.github.excu101.pluginsystem.ui.theme.*
import io.github.excu101.vortex.R.drawable.*
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.base.impl.Order
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
import io.github.excu101.vortex.provider.contract.Contracts
import io.github.excu101.vortex.provider.contract.Contracts.Permission
import io.github.excu101.vortex.provider.contract.Contracts.RestrictedDirectoriesAccess
import io.github.excu101.vortex.provider.storage.CopyTask
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.ui.component.*
import io.github.excu101.vortex.ui.component.animation.fade
import io.github.excu101.vortex.ui.component.drawer.DrawerActionListener
import io.github.excu101.vortex.ui.component.drawer.ItemBottomDrawerFragment
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.info.InfoItem
import io.github.excu101.vortex.ui.component.list.adapter.*
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.icon.Icons
import io.github.excu101.vortex.ui.navigation.AppNavigation.Args.Storage
import io.github.excu101.vortex.ui.navigation.AppNavigation.Args.Storage.CreatePage
import io.github.excu101.vortex.ui.navigation.AppNavigation.Routes
import io.github.excu101.vortex.ui.screen.storage.StorageListRenameDialog
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.Dialog.StorageAction
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.Dialog.StorageItemCreate
import io.github.excu101.vortex.utils.*

@AndroidEntryPoint
class StorageListPageFragment : Fragment(),
    MenuActionListener,
    DrawerActionListener,
    ThemeColorChangeListener, ItemViewListener<Item<*>>, ItemViewLongListener<Item<*>> {

    companion object : FragmentFactory<StorageListPageFragment> {
        override fun createFragment(): StorageListPageFragment = StorageListPageFragment()
    }

    private val viewModel by viewModels<StorageListPageViewModel>()

    private var binding: StorageListPageBinding? = null
    private val adapter: ItemAdapter<Item<*>>?
        get() = binding?.list?.adapter

    private var lastDialog: DialogFragment? = null

    private val backPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.navigator.selection.value.item == PathItem(getExternalStorageDirectory().asPath())) {
                isEnabled = false
            } else {
                viewModel.navigateLeft()
            }
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
        bar?.registerListener(listener = this)

        binding = StorageListPageBinding(requireContext())
        controller = binding?.root?.let { view ->
            WindowCompat.getInsetsController(
                requireActivity().window,
                view
            )
        }

        requireActivity().window.statusBarColor = Transparent.value
        requireActivity().window.navigationBarColor = Transparent.value

        bar?.hideOnScroll = true

        Theme.registerColorChangeListener(listener = this)

        return binding?.root
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        super.onViewCreated(root, savedInstanceState)
        val trailAdapter = binding?.trail?.adapter!!
        binding?.fab?.let {
            val actParent = bar?.parent as? CoordinatorLayout
            if (actParent?.contains(it) == true) {
                actParent.addView(it)
            }
        }

        bar?.replaceItems(
            listOf(
                action(
                    title = ThemeText(fileListMoreActionTitleKey),
                    icon = Icons.Rounded.More
                ),
                action(
                    title = ThemeText(fileListSortActionTitleKey),
                    icon = Icons.Rounded.Filter
                ),
                action(
                    title = ThemeText(fileListMoreInfoActionTitleKey),
                    icon = Icons.Rounded.Search
                )
            )
        )
        binding?.fab?.setOnClickListener { _ ->
            viewModel.showTasks()
        }
        binding?.list?.setOnApplyWindowInsetsListener { view, insets ->
            val compat = WindowInsetsCompat.toWindowInsetsCompat(insets)
            view.updatePadding(
                bottom = compat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            )
            insets
        }
        binding?.list?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.layoutManager?.findViewByPosition(0) == null) {
                    binding?.fab?.shrink()
                } else {
                    binding?.fab?.extend()
                }
            }
        })

        binding?.scroller?.bindRecycler(binding?.list)

        trailAdapter.register(listener = this)
        trailAdapter.registerLong(listener = this)

        adapter?.register(listener = this)
        adapter?.registerLong(listener = this)

        repeatedLifecycle {
            viewModel.collectState { state ->
                binding?.loading?.title = state.loadingTitle

                binding?.warning?.icon = state.warningIcon
                binding?.warning?.message = state.warningMessage

                if (state.data.isNotEmpty()) {
                    val data = state.data.toMutableList()
                    adapter?.replace(items = data)
                }

                binding?.loading?.fade(isOut = !state.isLoading, duration = 250L)
                binding?.warning?.fade(isOut = !state.isWarning, duration = 250L)
                binding?.list?.fade(isOut = state.isLoading || state.isWarning, duration = 250L)
            }
        }

        repeatedLifecycle {
            viewModel.navigator.items.collect { items ->
                if (items.isNotEmpty()) {
                    trailAdapter.replace(items)
                }
            }
        }

        repeatedLifecycle {
            viewModel.tasks.collect { tasks ->
                if (tasks.isEmpty()) {
                    binding?.fab?.hide()
                } else {
                    binding?.fab?.show()
                }
            }
        }

        repeatedLifecycle {
            viewModel.navigator.selection.collect { (index, item) ->
                if (!viewModel.isSelectionEnabled) {
                    viewModel.current?.let { item ->
                        wrapBarTitle(item = item)
                        wrapBarSubtitle(item = item)
                    }
                }
                if (index >= 0) {
                    binding?.trail?.smoothScrollToPosition(index)
                    trailAdapter.updateSelected(index)
                }
            }
        }

        repeatedLifecycle {
            viewModel.selected.collect { selected ->
                if (selected.isNotEmpty()) {
                    bar?.title = FormatterThemeText(
                        fileListSelectionTitleKey,
                        selected.size
                    )
                    bar?.subtitle = Size(selected.fold(initial = 0L) { init, current ->
                        init + current.size.memory
                    }).convertToThemeText()
                } else {
                    viewModel.current?.let { item ->
                        wrapBarTitle(item)
                        wrapBarSubtitle(item)
                    }
                }

                adapter?.replaceSelected(selected = selected)
            }
        }

        repeatedLifecycle {
            viewModel.dialog.collect { dialog ->
                when (dialog) {
                    is StorageItemCreate -> {
                        NavigationController().navigate(
                            route = Routes.Storage.Create.Page,
                            Arguments(CreatePage.ParentDirectoryKey to dialog.parent)
                        )
                    }
                    is StorageAction -> {
                        if (lastDialog != null) {
                            lastDialog?.dismiss()
                        }

                        lastDialog = ItemBottomDrawerFragment(
                            *DrawerViewHolderFactories + (ItemViewTypes.InfoItem to InfoItem as ViewHolderFactory<Item<*>>)
                        ).withItems(items = dialog.content).register(::onClick)
                        lastDialog?.show(childFragmentManager, ItemBottomDrawerFragment.Tag)
                    }
                }
            }
        }

        repeatedLifecycle {
            viewModel.collectEffect { effect ->
                if (effect.message != null) {
                    make(
                        requireView(),
                        effect.message,
                        effect.messageDuration
                    ).setAnchorView(bar).setAction(
                        effect.messageActionTitle,
                        effect.messageAction
                    ).show()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(backPressed)
    }

    private fun wrapBarTitle(item: PathItem) {
        val name = item.name

        bar?.title = name
    }

    private fun wrapBarSubtitle(item: PathItem) {
        val folders = FormatterThemeText(
            fileListDirectoriesCountTitleKey,
            item.value.directoryCount
        )

        val files = FormatterThemeText(
            fileListFilesCountTitleKey,
            item.value.fileCount
        )

        bar?.subtitle = "$folders, $files"
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
                    ViewIds.Storage.Trail.rootId -> {
                        viewModel.navigateTo(item = item)
                        bar?.show()
                    }

                    ViewIds.Storage.Item.iconId -> {
                        viewModel.choose(element = item)
                    }

                    ViewIds.Storage.Item.rootId -> {
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
            ViewIds.Storage.Trail.rootId -> {
                viewModel.openSingleItemDrawerActions(item) { action ->
                    onSingleItemActionCall(action, item, view)
                }
                true
            }

            ViewIds.Storage.Item.iconId -> {
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

            ViewIds.Storage.Item.rootId -> {
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
        action: Action,
        item: PathItem,
        view: View,
    ) {
        when (action.title) {
            ThemeText(storageListOperationOpenTitleKey) -> {
                viewModel.navigateTo(item)
            }

            ThemeText(storageListOperationAddActionNewTitleKey) -> {
                if (lastDialog != null) {
                    lastDialog?.dismiss()
                }
                viewModel.dialog(StorageItemCreate(item))
            }

            ThemeText(storageListOperationCopyPathTitleKey) -> {
                viewModel.copyPath(item)
            }

            ThemeText(storageListOperationRenameActionTitleKey) -> {
                StorageListRenameDialog(
                    requireContext(),
                    source = item.value
                ) { dest ->
                    viewModel.rename(
                        item,
                        dest
                    )
                }.show()
            }

            ThemeText(storageListOperationAddBookmarkTitleKey) -> {
                if (StorageBookmarkProvider.unregister(item)) {
                    adapter?.changed(item)
                }
            }

            ThemeText(storageListOperationRemoveBookmarkTitleKey) -> {
                if (StorageBookmarkProvider.unregister(item)) {
                    adapter?.changed(item)
                }
            }

            ThemeText(storageListOperationCopyActionTitleKey) -> {
                viewModel.task(CopyTask(setOf(item.value)))
            }

            ThemeText(storageListOperationDeleteActionTitleKey) -> {
                viewModel.delete(setOf(item))
            }

            "Add watcher" -> {
                viewModel.watcher(item)
            }

            ThemeText(fileListMoreInfoActionTitleKey) -> {
                NavigationController().navigate(
                    Routes.Storage.List.ItemPageInfo,
                    Arguments(Storage.ItemInfoPage.ItemInfoPageKey to item)
                )
            }
        }
    }

    fun onSelectedActionCall(
        action: Action,
        view: View,
    ) {
        when (action.title) {
            ThemeText(storageListOperationDeleteActionTitleKey) -> {
                viewModel.delete()
            }

            ThemeText(storageListOperationCopyActionTitleKey) -> {
                viewModel.task(CopyTask(viewModel.selected.value.map { it.value }.toSet()))
                viewModel.deselectAll()
            }

            "Deselect" -> {
                viewModel.deselect(viewModel.selected.value.toSet())
            }
        }
    }

    override fun onDrawerActionCall(action: Action) {
        if (viewModel.disposeAction(action)) return

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

            ThemeText(fileListMoreSelectAllActionTitleKey) -> {
                viewModel.selectAll()
            }

            ThemeText(fileListMoreDeselectAllActionTitleKey) -> {
                viewModel.deselectAll()
            }

            ThemeText(fileListMoreNavigateLeftActionTitleKey) -> {
                viewModel.navigateLeft()
            }

            ThemeText(fileListMoreNavigateRightActionTitleKey) -> {
                viewModel.navigateRight()
            }

            ThemeText(fileListOrderAscendingActionTitleKey) -> {
                viewModel.order(Order.ASCENDING)
            }

            ThemeText(fileListOrderDescendingActionTitleKey) -> {
                viewModel.order(Order.DESCENDING)
            }

            ThemeText(fileListSortNameActionTitleKey) -> {
                viewModel.sort(PathItemSorters.Name)
            }

            ThemeText(fileListSortPathActionTitleKey) -> {
                viewModel.sort(PathItemSorters.Path)
            }

            ThemeText(fileListSortSizeActionTitleKey) -> {
                viewModel.sort(PathItemSorters.Size)
            }

            ThemeText(fileListSortLastModifiedTimeActionTitleKey) -> {
                viewModel.sort(PathItemSorters.LastModifiedTime)
            }

            ThemeText(fileListSortLastAccessTimeActionTitleKey) -> {
                viewModel.sort(PathItemSorters.LastAccessTime)
            }

            ThemeText(fileListSortCreationTimeActionTitleKey) -> {
                viewModel.sort(PathItemSorters.CreationTime)
            }

            // Filter
            ThemeText(fileListFilterOnlyFilesActionTitleKey) -> {
                viewModel.filter(PathItemFilters.OnlyFile)
            }

            ThemeText(fileListFilterOnlyFoldersActionTitleKey) -> {
                viewModel.filter(PathItemFilters.OnlyFolder)
            }

            ThemeText(fileListFilterOnlyVideoFileActionTitleKey) -> {
                viewModel.filter(PathItemFilters.OnlyVideoFile)
            }

            ThemeText(fileListFilterOnlyVideoFileActionTitleKey) -> {
                viewModel.filter(PathItemFilters.OnlyApplicationFile)
            }

            ThemeText(fileListFilterOnlyImageFileActionTitleKey) -> {
                viewModel.filter(PathItemFilters.OnlyImageFile)
            }

            ThemeText(fileListFilterOnlyImageFileActionTitleKey) -> {
                viewModel.filter(PathItemFilters.OnlyImageFile)
            }

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

            "Open tasks" -> {
                viewModel.showTasks()
            }

            "Create symbolic link" -> {

            }

            "Shuffle list" -> {
                viewModel.shuffle()
            }

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
                        mode = 777
                    )
                }
            }

            "Add random-name file" -> {
                viewModel.current?.let { current ->
                    viewModel.createFile(
                        path = current.value.resolve(RandomString()),
                        mode = 777
                    )
                }
            }
        }
        lastDialog?.dismiss()
    }

    fun onWarningActionCall(action: Action) {
        when (action.title) {
            "Grant" -> {
                if (isAndroidQ) {

                }
            }

            "Add new" -> {
                viewModel.dialog(StorageItemCreate(viewModel.current))
            }

            "Reload" -> {
                viewModel.current?.let(viewModel::navigateTo)
            }

            "Back to parent" -> {
                viewModel.navigateLeft()
            }

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

    override fun onMenuActionCall(action: Action) {
        when (action.title) {
            ThemeText(fileListSearchActionTitleKey) -> {

            }

            ThemeText(fileListMoreActionTitleKey) -> {
                viewModel.openDrawerMoreActions()
            }

            ThemeText(fileListSortActionTitleKey) -> {
                viewModel.openDrawerSortActions()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Theme.unregisterColorChangeListener(this)
        (bar?.parent as? CoordinatorLayout)?.removeView(binding?.fab)
        binding?.onDestroy()
        binding = null
        backPressed.isEnabled = false
        lastDialog = null
        requireBar().setNavigationClickListener(null)
    }

    override fun onChanged() {
        val isLight = ThemeColor(trailSurfaceColorKey).luminance > 0.5F
        controller?.isAppearanceLightStatusBars = isLight
        controller?.isAppearanceLightNavigationBars = isLight
    }

}