package io.github.excu101.vortex.ui.screen.storage.pager.page.list

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
import androidx.core.graphics.luminance
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar.make
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.MaterialSharedAxis.Z
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.utils.*
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.Color.Companion.Transparent
import io.github.excu101.pluginsystem.ui.theme.*
import io.github.excu101.vortex.R.drawable.*
import io.github.excu101.vortex.ViewIds.StorageListItem.iconId
import io.github.excu101.vortex.ViewIds.StorageListItem.rootId
import io.github.excu101.vortex.VortexFileManagerService
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.base.utils.collectEffect
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.base.utils.logIt
import io.github.excu101.vortex.base.utils.toastIt
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.navigation.dsl.Arguments
import io.github.excu101.vortex.navigation.navigator.FragmentNavigator
import io.github.excu101.vortex.navigation.utils.NavigationController
import io.github.excu101.vortex.provider.contract.Contracts
import io.github.excu101.vortex.provider.contract.Contracts.Permission
import io.github.excu101.vortex.provider.contract.Contracts.RestrictedDirectoriesAccess
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.ui.component.*
import io.github.excu101.vortex.ui.component.animation.fade
import io.github.excu101.vortex.ui.component.drawer.DrawerActionListener
import io.github.excu101.vortex.ui.component.fragment.FragmentFactory
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.trail.TrailItemView
import io.github.excu101.vortex.ui.component.warning.WarningActionListener
import io.github.excu101.vortex.ui.navigation.AppNavigation.Args
import io.github.excu101.vortex.ui.navigation.AppNavigation.Routes
import io.github.excu101.vortex.ui.screen.main.MainActivity
import io.github.excu101.vortex.ui.screen.storage.pager.*
import io.github.excu101.vortex.utils.*

@AndroidEntryPoint
class StorageListPageFragment : Fragment(),
    MenuActionListener,
    DrawerActionListener,
    WarningActionListener,
    ThemeColorChangeListener, ItemViewListener<Item<*>>, ItemViewLongListener<Item<*>> {

    companion object : FragmentFactory<StorageListPageFragment> {
        override fun createFragment(): StorageListPageFragment {
            return StorageListPageFragment()
        }
    }

    private val service: VortexFileManagerService?
        get() = (requireActivity() as? MainActivity)?.service

    private val viewModel by viewModels<StorageListPageViewModel>()

    private var binding: StorageListPageBinding? = null

    private val backPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.navigator.selectedItem == PathItem(getExternalStorageDirectory().asPath())) {
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
        returnTransition = MaterialSharedAxis(Z, false)
        exitTransition = MaterialSharedAxis(Z, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        requireBar().registerListener(listener = this)
        requireBar().setNavigationClickListener { view ->
            if (requireDrawer().isOpen) {
                requireDrawer().hide()
            } else if (viewModel.isSelectionEnabled) {
                viewModel.deselectAll()
            } else {
                viewModel.openDefaultDrawerActions()
            }
        }
        requireDrawer().registerListener(listener = this)

        binding = StorageListPageBinding(requireContext())
        binding?.warning?.registerListener(listener = this)

        controller = binding?.root?.let { view ->
            WindowCompat.getInsetsController(
                requireActivity().window,
                view
            )
        }

        requireActivity().window.statusBarColor = Transparent.value
        requireActivity().window.navigationBarColor = Transparent.value

        requireBar().hideOnScroll = true

        Theme.registerColorChangeListener(listener = this)

        return binding?.root
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        super.onViewCreated(root, savedInstanceState)

        val trailAdapter = binding?.trail?.adapter!!
        val listAdapter = binding?.list?.adapter!!
        binding?.list?.setOnApplyWindowInsetsListener { view, insets ->
            val compat = WindowInsetsCompat.toWindowInsetsCompat(insets)
            view.updatePadding(
                bottom = compat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            )
            insets
        }

        binding?.scroller?.bindRecycler(binding?.list)

        trailAdapter.register { view, item, position ->
            viewModel.navigateTo(item = item)
            bar?.show()
        }

        trailAdapter.registerLong { view, item, position ->
            when (view) {
                is TrailItemView -> {
                    viewModel.openSingleItemDrawerActions(item) { action ->
                        onSingleItemActionCall(action, item, view)
                    }
                    true
                }
                else -> false
            }
        }

        listAdapter.register(listener = this)
        listAdapter.registerLong(listener = this)

        repeatedLifecycle {
            viewModel.collectState { state ->
                binding?.loading?.title = state.loadingTitle

                binding?.warning?.icon = state.warningIcon
                binding?.warning?.message = state.warningMessage
                binding?.warning?.replaceActions(state.warningActions)

                if (state.data.isNotEmpty()) {
                    val data = state.data.toMutableList()
                    listAdapter.replace(items = data)
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
            viewModel.navigator.selectedIndex.collect { index ->
                if (!viewModel.isSelectionEnabled) {
                    viewModel.current?.let {
                        wrapBarTitle(item = it)
                        wrapBarSubtitle(item = it)
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
                    requireBar().title = FormatterThemeText(
                        fileListSelectionTitleKey,
                        selected.size
                    )
                    requireBar().subtitle = Size(selected.fold(initial = 0L) { init, current ->
                        init + current.size.memory
                    }).convertToThemeText()
                } else {
                    viewModel.current?.let { item ->
                        wrapBarTitle(item)
                        wrapBarSubtitle(item)
                    }
                }

                listAdapter.replaceSelected(selected = selected)
            }
        }

        repeatedLifecycle {
            viewModel.collectEffect { effect ->
                requireDrawer().replaceNavigation(
                    effect.drawerContent
                )

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

                if (effect.showDrawer) {
                    requireBar().show()
                    requireDrawer().show()
                }
            }
        }

        repeatedLifecycle {
            viewModel.barActions.collect { actions ->
                bar?.replaceItems(actions)
            }
        }

        repeatedLifecycle {
            viewModel.resolver.collect { resolver ->
                requireDrawer().replaceSelected(
                    listOf(
                        DrawerItem(resolver.parseSorterToAction()),
                        DrawerItem(resolver.parseFilterToAction(requireContext())),
                        DrawerItem(resolver.parseOrderToAction())
                    )
                )
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
        item as PathItem
        when (view.id) {
            iconId -> {
                viewModel.choose(element = item)
            }
            rootId -> {
                viewModel.navigateTo(item = item)
                bar?.show()
            }
        }
    }

    override fun onLongClick(view: View, item: Item<*>, position: Int): Boolean {
        item as PathItem
        return when (view.id) {
            iconId -> {
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

            rootId -> {
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
            "Open" -> {
                viewModel.navigateTo(item)
            }

            "Add new" -> {
                StorageListItemCreateDialog(
                    context = requireContext(),
                    directory = item.value
                ) { path, mode, isDirectory ->
                    if (isDirectory) {
                        viewModel.createDirectory(path, mode)
                    } else {
                        viewModel.createFile(path, mode)
                    }
                }.show()
            }

            "Copy path" -> {
                viewModel.copyPath(item)
            }

            ThemeText(fileListOperationRenameActionTitleKey) -> {
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

            "Add to Bookmarks" -> {
                StorageBookmarkProvider.register(item)
            }

            "Remove from Bookmarks" -> {
                StorageBookmarkProvider.unregister(item)
            }

            ThemeText(fileListOperationCopyActionTitleKey) -> {
                viewModel.registerCopyTask(setOf(item.value))
            }

            ThemeText(fileListOperationDeleteActionTitleKey) -> {
                viewModel.delete(setOf(item))
            }

            ThemeText(fileListMoreInfoActionTitleKey) -> {
                NavigationController().navigate(
                    Routes.Storage.List.ItemPageInfo,
                    Arguments(Args.Storage.ItemInfoPage.ItemInfoPageKey to item)
                )
            }
        }
    }

    fun onSelectedActionCall(
        action: Action,
        view: View,
    ) {
        when (action.title) {
            ThemeText(fileListOperationDeleteActionTitleKey) -> {
                viewModel.delete()
            }

            "Deselect" -> {
                viewModel.deselect(viewModel.selected.value.toSet())
            }
        }
    }

    override fun onDrawerActionCall(action: Action) {
        if (viewModel.onDispose != null) {
            viewModel.onDispose?.invoke(action)
            viewModel.onDispose = null
            return
        }

        when (action.title) {
            "Get file system info" -> {
                viewModel.current?.value?.let { path ->
                    var result = ""
                    for (store in FileProvider.stores) {

                        result += "Name:  ${store.name}\n"
                        result += "Type: ${store.type}\n"
                        result += '\n'
                    }

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

            ThemeText(fileListMoreInfoActionTitleKey) -> {
                viewModel.current?.let { current ->
                    NavigationController().navigate(
                        route = Routes.Storage.List.ItemPageInfo,
                        FragmentNavigator.Arguments(
                            bundleOf(Args.Storage.ItemInfoPage.ItemInfoPageKey to current)
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
//        drawer?.hide()
    }

    override fun onWarningActionCall(action: Action) {
        when (action.title) {
            "Grant" -> {
                if (isAndroidQ) {

                }
            }

            "Add new" -> {
                StorageListItemCreateDialog(
                    requireContext(),
                    viewModel.current!!.value,
                    onResult = { path, mode, isDirectory ->
                        path.toString().logIt(message = "Path: ")
                        if (isDirectory) {
                            viewModel.createDirectory(
                                path = path,
                                mode = mode
                            )
                        } else {
                            viewModel.createFile(
                                path = path,
                                mode = mode
                            )
                        }
                    }
                ).show()
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
        binding?.onDestroy()
        binding = null
        backPressed.isEnabled = false
        requireBar().setNavigationClickListener(null)
    }

    override fun onChanged() {
        val isLight = ThemeColor(trailSurfaceColorKey).luminance > 0.5F
        controller?.isAppearanceLightStatusBars = isLight
        controller?.isAppearanceLightNavigationBars = isLight
    }

}