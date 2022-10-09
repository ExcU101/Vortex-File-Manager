package io.github.excu101.vortex.ui.screen.storage.list

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Intent
import android.net.Uri
import android.os.Build
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar.make
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.fs.utils.directoryCount
import io.github.excu101.filesystem.fs.utils.fileCount
import io.github.excu101.filesystem.fs.utils.resolve
import io.github.excu101.pluginsystem.model.Action
import io.github.excu101.pluginsystem.model.Color.Companion.Transparent
import io.github.excu101.pluginsystem.ui.theme.*
import io.github.excu101.vortex.VortexServiceApi
import io.github.excu101.vortex.base.impl.Order
import io.github.excu101.vortex.base.utils.collectEffect
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemFilters
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.provider.SafResolver
import io.github.excu101.vortex.provider.contract.Contracts
import io.github.excu101.vortex.provider.contract.Contracts.Permission
import io.github.excu101.vortex.provider.contract.Contracts.RestrictedDirectoriesAccess
import io.github.excu101.vortex.ui.component.animation.fade
import io.github.excu101.vortex.ui.component.bar
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.drawer
import io.github.excu101.vortex.ui.component.drawer.BottomActionDrawer
import io.github.excu101.vortex.ui.component.list.scroll.FastScroller
import io.github.excu101.vortex.ui.component.loading.LoadingView
import io.github.excu101.vortex.ui.component.menu.ActionListener
import io.github.excu101.vortex.ui.component.storage.StorageListView
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.trail.TrailItemView
import io.github.excu101.vortex.ui.component.trail.TrailListView
import io.github.excu101.vortex.ui.component.warning.WarningView
import io.github.excu101.vortex.ui.screen.VortexServiceFragment
import io.github.excu101.vortex.ui.screen.main.MainActivity
import io.github.excu101.vortex.ui.screen.storage.list.StorageListScreen.DialogType.RENAME
import io.github.excu101.vortex.utils.RandomString
import kotlinx.coroutines.launch


@AndroidEntryPoint
class StorageListFragment : VortexServiceFragment(),
    ActionListener,
    ThemeColorChangeListener {

    private val viewModel by viewModels<StorageListViewModel>()

    private var rename: StorageListRenameDialog? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        requireBar().registerListener(listener = this)
        requireDrawer().registerListener(listener = this)

        requireBar().setNavigationClickListener { view ->
            viewModel.openDefaultDrawerActions((requireActivity() as MainActivity).isDarkTheme)
        }

        root = container?.let {
            CoordinatorLayout(it.context).apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
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

        trail = container?.let {
            TrailListView(it.context).apply {
                layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
        }

        loading = container?.let {
            LoadingView(it.context).apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                    gravity = CENTER
                }
            }
        }

        warning = container?.let {
            WarningView(it.context).apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                    gravity = CENTER
                }
                registerListener(this@StorageListFragment)
            }
        }

        scroller = container?.let {
            FastScroller(it.context).apply {
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
                    viewModel.openDrawerTrailsActions(item)
                    true
                }
                else -> false
            }
        }

        listAdapter.register { view, item, position ->
            when (view) {
                is ImageView -> {
                    viewModel.select(item = item as PathItem)
                }
                is FrameLayout -> {
                    viewModel.navigateTo(item = item as PathItem)
                    bar?.show()
                }
            }
        }

        listAdapter.registerLong { view, item, position ->
            when (view) {
                is ImageView -> {
                    viewModel.openDrawerActions()
                    true
                }

                is FrameLayout -> {
                    item as PathItem
                    if (item in viewModel.selected.value) {
                        viewModel.openDrawerActions()
                    } else {
                        viewModel.select(item = item)
                    }
                    true
                }

                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectState { state ->
                    loading?.fade(isOut = !state.isLoading)
                    warning?.fade(isOut = !state.isWarning)
                    list?.fade(isOut = state.isLoading || state.isWarning)

                    loading?.title = state.loadingTitle

                    warning?.icon = state.warningIcon
                    warning?.message = state.warningMessage
                    warning?.replaceActions(state.warningActions)


                    if (state.data.isNotEmpty()) {
                        listAdapter.replace(items = state.data)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigator.items.collect { items ->
                    if (items.isNotEmpty()) {
                        trailAdapter.replace(items)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigator.selectedIndex.collect { index ->
                    if (!viewModel.selectionModeEnabled) {
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
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selected.collect { selected ->
                    if (viewModel.selectionModeEnabled) {
                        bar?.title = ReplacerThemeText(
                            fileListSelectionTitleKey,
                            specialSymbol,
                            selected.size.toString()
                        )
                        bar?.subtitle = null
                    } else {
                        viewModel.current?.let {
                            wrapBarTitle(it)
                            wrapBarSubtitle(it)
                        }
                    }
                    listAdapter.replace(selected)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectEffect { effect ->
                    requireDrawer().controller.replace(effect.drawerActions)

                    if (effect.message != null) {
                        make(
                            requireView(),
                            effect.message,
                            effect.messageDuration
                        ).setAction(
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
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.barActions.collect { actions ->
                    bar?.replaceItems(actions)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resolver.collect { resolver ->
                    requireDrawer().controller.replaceSelected(
                        listOf(
                            resolver.parseSorterToAction(),
                            resolver.parseFilterToAction(),
                            resolver.parseOrderToAction()
                        )
                    )
                }
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
        val folders = ReplacerThemeText(
            fileListDirectoriesCountTitleKey,
            specialSymbol,
            item.value.directoryCount.toString()
        )

        val files = ReplacerThemeText(
            fileListFilesCountTitleKey,
            specialSymbol,
            item.value.fileCount.toString()
        )

        bar?.subtitle = "$folders, $files"
    }

    override fun onCall(action: Action) {
        when (action.title) {

            "" -> {
                restrictedDirectoriesLauncher.launch(Uri.parse(SafResolver.resolveUri(path = "")))
            }

            ThemeText(fileListMoreActionTitleKey) -> {
                viewModel.openDrawerMoreActions()
            }

            ThemeText(fileListSortActionTitleKey) -> {
                viewModel.openDrawerSortActions()
            }

            ThemeText(fileListTrailCopyPathActionTitleKey) -> {

            }

            ThemeText(fileListOperationDeleteActionTitleKey) -> {
                viewModel.delete()
            }

            ThemeText(fileListOperationRenameActionTitleKey) -> {
                viewModel.dialog(RENAME)
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

            ThemeText(fileListSortLastAccessTimeActionTitleKey) -> {
                viewModel.sort(PathItemSorters.LastAccessTime)
            }

            ThemeText(fileListSortCreationTimeActionTitleKey) -> {
                viewModel.sort(PathItemSorters.CreationTime)
            }

            ThemeText(fileListFilterOnlyFilesActionTitleKey) -> {
                viewModel.filter(PathItemFilters.OnlyFile)
            }

            ThemeText(fileListFilterOnlyFoldersActionTitleKey) -> {
                viewModel.filter(PathItemFilters.OnlyFolder)
            }

            ThemeText(fileListMoreInfoActionTitleKey) -> {

            }

            ThemeText(fileListMoreInfoActionTitleKey) -> {
                viewModel.dialog(StorageListScreen.DialogType.CREATE)
            }

            ThemeText(fileListWarningStorageAccessActionTitleKey) -> {

            }

            ThemeText(fileListWarningFullStorageAccessActionTitleKey) -> {
                if (Build.VERSION.SDK_INT >= R) {
                    storageAccessLauncher.launch()
                }
            }

            ThemeText(fileListWarningNotificationAccessActionTitleKey) -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(POST_NOTIFICATIONS)
                }
            }

            "Add random-name directory" -> {
                viewModel.current?.let { current ->
                    viewModel.createPath(
                        path = current.value.resolve(RandomString()),
                        isDirectory = true
                    )
                }

            }

            "Add random-name file" -> {
                viewModel.current?.let { current ->
                    viewModel.createPath(
                        path = current.value.resolve(RandomString()),
                        isDirectory = false
                    )
                }
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