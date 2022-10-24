package io.github.excu101.vortex.ui.screen.storage.list.page.list

// I hate imports...

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION_CODES.Q
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.graphics.luminance
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar.make
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.utils.*
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.Color.Companion.Transparent
import io.github.excu101.pluginsystem.model.Effect
import io.github.excu101.pluginsystem.ui.theme.*
import io.github.excu101.vortex.VortexServiceApi
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.base.utils.collectEffect
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.base.utils.logIt
import io.github.excu101.vortex.base.utils.toastIt
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.provider.contract.Contracts
import io.github.excu101.vortex.provider.contract.Contracts.Permission
import io.github.excu101.vortex.provider.contract.Contracts.RestrictedDirectoriesAccess
import io.github.excu101.vortex.ui.component.animation.fade
import io.github.excu101.vortex.ui.component.bar
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.drawer
import io.github.excu101.vortex.ui.component.drawer.BottomActionDrawer
import io.github.excu101.vortex.ui.component.drawer.DrawerActionListener
import io.github.excu101.vortex.ui.component.item.icon.IconTextHeaderItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.replace
import io.github.excu101.vortex.ui.component.list.scroll.FastScroller
import io.github.excu101.vortex.ui.component.loading.LoadingView
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.ui.component.repeatedLifecycle
import io.github.excu101.vortex.ui.component.storage.StorageListView
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.trail.TrailItemView
import io.github.excu101.vortex.ui.component.trail.TrailListView
import io.github.excu101.vortex.ui.component.warning.WarningActionListener
import io.github.excu101.vortex.ui.component.warning.WarningView
import io.github.excu101.vortex.ui.screen.VortexServiceFragment
import io.github.excu101.vortex.ui.screen.main.MainActivity
import io.github.excu101.vortex.ui.screen.main.MainViewModel
import io.github.excu101.vortex.ui.screen.storage.list.*
import io.github.excu101.vortex.ui.screen.storage.list.page.list.StorageListPageScreen.DialogType.RENAME
import io.github.excu101.vortex.utils.*

@AndroidEntryPoint
class StorageListPageFragment : VortexServiceFragment(),
    MenuActionListener,
    DrawerActionListener,
    WarningActionListener,
    ThemeColorChangeListener {

    companion object {
        const val pageKey = "storagePageTitle"

        fun newInstance(title: String): StorageListPageFragment {
            val args = Bundle()
            args.putString(pageKey, title)
            val fragment = StorageListPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by viewModels<StorageListPageViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private var rename: StorageListRenameDialog? = null
    private var create: StorageListCreateDialog? = null

    private var service: VortexServiceApi? = null

    private var root: CoordinatorLayout? = null
    private var list: StorageListView? = null
    private var trail: TrailListView? = null
    private var loading: LoadingView? = null
    private var warning: WarningView? = null
    private var scroller: FastScroller? = null

    @RequiresApi(R)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireBar().setNavigationClickListener { view ->
            viewModel.openDefaultDrawerActions((requireActivity() as MainActivity).isDarkTheme)
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireBar().setNavigationClickListener(null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        requireBar().registerListener(listener = this)
        requireDrawer().registerListener(listener = this)

        with(requireContext()) {
            root = CoordinatorLayout(this).apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }

            controller = root?.let { view ->
                WindowCompat.getInsetsController(
                    requireActivity().window,
                    view
                )
            }

            list = root?.let {
                StorageListView(it.context).apply {
                    visibility = GONE
                    layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }
            }

            trail = root?.let {
                TrailListView(it.context).apply {
                    layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                }
            }

            loading = root?.let {
                LoadingView(it.context).apply {
                    layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                        gravity = CENTER
                    }
                }
            }

            warning = root?.let {
                WarningView(it.context).apply {
                    layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                        gravity = CENTER
                    }
                    registerListener(this@StorageListPageFragment)
                }
            }

            scroller = root?.let {
                FastScroller(it.context).apply {

                }
            }
        }

        root?.addView(list)
        root?.addView(scroller)
        root?.addView(trail)
        root?.addView(loading)
        root?.addView(warning)


        requireActivity().window.statusBarColor = Transparent.value
        requireActivity().window.navigationBarColor = Transparent.value

        requireBar().hideOnScroll = true

        Theme.registerColorChangeListener(listener = this)

        return root
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        super.onViewCreated(root, savedInstanceState)
        var isBackPressEnabled = true
        val trailAdapter = trail?.adapter!!
        val listAdapter = list?.adapter!!
        list?.setOnApplyWindowInsetsListener { view, insets ->
            val compat = WindowInsetsCompat.toWindowInsetsCompat(insets)
            view.updatePadding(
                bottom = compat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            )
            insets
        }

        scroller?.bindRecycler(list)

        trailAdapter.register { view, item, position ->
            viewModel.navigateTo(item = item)
            bar?.show()
        }

        trailAdapter.registerLong { view, item, position ->
            when (view) {
                is TrailItemView -> {
                    drawer?.registerListener { action ->
                        onActionCall(item, action)
                    }
                    viewModel.openDrawerActions(setOf(item))
                    true
                }
                else -> false
            }
        }

        listAdapter.register { view, item, position ->
            item as PathItem
            when (view) {
                is ImageView -> {
                    viewModel.choose(element = item)
                }
                is FrameLayout -> {
                    viewModel.navigateTo(item = item)
                    bar?.show()
                }
            }
        }

        listAdapter.registerLong { view, item, position ->
            item as PathItem
            when (view) {
                is ImageView -> {
                    viewModel.openDrawerActions(setOf(item))
                    true
                }

                is FrameLayout -> {
                    if (item in viewModel.selected.value) {
                        viewModel.openDrawerActions(setOf(item))
                    } else {
                        viewModel.choose(element = item)
                    }
                    true
                }

                else -> false
            }
        }

        repeatedLifecycle {
            viewModel.collectState { state ->
                loading?.title = state.loadingTitle

                warning?.icon = state.warningIcon
                warning?.message = state.warningMessage
                warning?.replaceActions(state.warningActions)

                if (state.data.isNotEmpty()) {
                    val data = state.data.toMutableList()
                    listAdapter.replace(items = data)
                }

                loading?.fade(isOut = !state.isLoading, duration = 250L)
                warning?.fade(isOut = !state.isWarning, duration = 250L)
                list?.fade(isOut = state.isLoading || state.isWarning, duration = 250L)
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
                    trail?.smoothScrollToPosition(index)
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

                listAdapter.replaceSelected(selected = selected)
            }
        }

        repeatedLifecycle {
            viewModel.collectEffect { effect ->
                requireDrawer().controller.replace(
                    effect.drawerActions
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
                    requireDrawer().show()
                }
                if (effect.showRenameDialog) {
                    requireRenameDialog().show()
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
                requireDrawer().controller.replaceSelected(
                    listOf(
                        resolver.parseSorterToAction(),
                        resolver.parseFilterToAction(requireContext()),
                        resolver.parseOrderToAction()
                    ).map {
                        IconTextHeaderItem(it)
                    }
                )
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            owner = viewLifecycleOwner,
            enabled = isBackPressEnabled
        ) {
            if (viewModel.navigator.selectedItem == PathItem(getExternalStorageDirectory().asPath())) {
                requireActivity().finish()
            } else {
                viewModel.navigateLeft()
            }
        }
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

    fun onActionCall(
        item: PathItem,
        action: Action,
    ) = onActionCall(listOf(item), action)

    fun onActionCall(
        items: List<PathItem>,
        action: Action,
    ) {
        when (action.title) {
            ThemeText(fileListTrailCopyPathActionTitleKey) -> {

            }

            ThemeText(fileListOperationDeleteActionTitleKey) -> {

            }

            ThemeText(fileListOperationRenameActionTitleKey) -> {
                viewModel.dialog(RENAME)
            }
        }
        drawer?.dismiss()
    }

    override fun onDrawerActionCall(action: Action) {
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
        drawer?.dismiss()
    }

    override fun onWarningActionCall(action: Action) {
        when (action.title) {
            "Grant" -> {
                if (isAndroidQ) {

                }
            }

            "Add new" -> {
                requireCreateDialog()
                    .onPreShow(
                        path = viewModel.current?.path
                    )
                    .show()
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
        if (action is Effect) {
            action()
        }
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

    private fun requireDrawer(): BottomActionDrawer {
        return drawer ?: throw IllegalArgumentException()
    }

    private fun requireBar(): Bar {
        return bar ?: throw IllegalArgumentException()
    }

    private fun requireRenameDialog(): StorageListRenameDialog {
        if (rename == null) {
            rename = StorageListRenameDialog(requireContext())
        }

        return rename!!
    }

    private fun requireCreateDialog(): StorageListCreateDialog {
        if (create == null) {
            create = StorageListCreateDialog(requireContext())
        }

        return create!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Theme.unregisterColorChangeListener(this)
        root = null
        list = null
        trail = null
        loading = null
        rename = null
    }

    override fun onChanged() {
        val isLight = ThemeColor(trailSurfaceColorKey).luminance > 0.5F
        controller?.isAppearanceLightStatusBars = isLight
        controller?.isAppearanceLightNavigationBars = isLight
    }

}