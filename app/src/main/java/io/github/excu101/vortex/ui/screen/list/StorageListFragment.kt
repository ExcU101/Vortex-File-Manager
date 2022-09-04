package io.github.excu101.vortex.ui.screen.list

import android.os.Bundle
import android.view.*
import android.view.Gravity.CENTER
import android.view.View.GONE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.filesystem.fs.utils.properties
import io.github.excu101.pluginsystem.model.Color.Companion.Transparent
import io.github.excu101.pluginsystem.ui.theme.ReplacerThemeText
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.provider.StorageProvider.Companion.EXTERNAL_STORAGE
import io.github.excu101.vortex.ui.component.animation.fade
import io.github.excu101.vortex.ui.component.bar
import io.github.excu101.vortex.ui.component.drawer
import io.github.excu101.vortex.ui.component.loading.LoadingView
import io.github.excu101.vortex.ui.component.storage.StorageListView
import io.github.excu101.vortex.ui.component.theme.key.fileListDirectoriesCountKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilesCountKey
import io.github.excu101.vortex.ui.component.theme.key.mainBarSurfaceColorKey
import io.github.excu101.vortex.ui.component.theme.key.specialSymbol
import io.github.excu101.vortex.ui.component.trail.TrailListView
import kotlinx.coroutines.launch
import kotlin.LazyThreadSafetyMode.NONE


@AndroidEntryPoint
class StorageListFragment : Fragment() {

    private val viewModel by viewModels<StorageListViewModel>()

    private var root: CoordinatorLayout? = null
    private var list: StorageListView? = null
    private var trail: TrailListView? = null
    private var loading: LoadingView? = null

    private val controller: WindowInsetsControllerCompat? by lazy(NONE) {
        root?.let { view ->
            WindowCompat.getInsetsController(
                requireActivity().window,
                view
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        root = container?.let {
            CoordinatorLayout(it.context).apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
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

        root?.addView(list)
        root?.addView(trail)
        root?.addView(loading)

        requireActivity().window.statusBarColor = Transparent.value
        requireActivity().window.navigationBarColor = Transparent.value

        bar?.hideOnScroll = true
        bar?.setBackgroundColor(ThemeColor(mainBarSurfaceColorKey))
        return root
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        super.onViewCreated(root, savedInstanceState)
        var isBackPressEnabled = true
        val trailAdapter = trail?.adapter!!
        val listAdapter = list?.adapter!!

        list?.setOnApplyWindowInsetsListener { v, insets ->
            val compat = WindowInsetsCompat.toWindowInsetsCompat(insets)
            v.updatePadding(
                bottom = compat.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            )
            insets
        }

        trailAdapter.register { view, item, position ->
            viewModel.navigateTo(item = item)
        }

        listAdapter.register { view, item, position ->
            when (view) {
                is ImageView -> {
                    viewModel.select(item = item as PathItem)
                }
                is FrameLayout -> {
                    viewModel.navigateTo(item = item as PathItem)
                }
            }
        }

        listAdapter.registerLong { view, item, position ->
            when (view) {
                is ImageView -> {
                    true
                }

                is FrameLayout -> {
                    true
                }

                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.collectState { state ->
                    loading?.fade(isOut = !state.isLoading)
                    list?.fade(isOut = state.isLoading)
                    loading?.title = state.loadingTitle

                    if (state.data.isNotEmpty()) {
                        listAdapter.replace(items = state.data)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trail.collect { navigator ->
                    val items = navigator.items
                    val selectedIndex = navigator.selectedIndex
                    val selectedItem = navigator.selectedItem

                    if (!viewModel.selectionModeEnabled) {
                        selectedItem?.let { wrapBarTitle(item = it) }
                    }
                    selectedItem?.let { wrapBarSubtitle(item = it) }
                    if (items.isNotEmpty()) {
                        trailAdapter.submitList(items)
                    }
                    if (selectedIndex >= 0) {
                        trail?.smoothScrollToPosition(selectedIndex)
                        trailAdapter.updateSelected(selectedIndex)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selected.collect { selected ->
                    if (viewModel.selectionModeEnabled) {
                        bar?.title = "Selected ${selected.size}"
                    } else {
                        viewModel.current?.let { wrapBarTitle(it) }
                    }

                    selected.forEach(listAdapter::choose)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.actions.collect { groups ->
                    val actions = mutableListOf<ActionHeaderItem>()
                    groups.forEach { groupAction ->
                        actions.addAll(groupAction.actions.map { ActionHeaderItem(it) })
                    }

                    drawer?.actions = actions
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            owner = viewLifecycleOwner,
            enabled = isBackPressEnabled
        ) {
            if (viewModel.trail.value.selectedItem == PathItem(EXTERNAL_STORAGE)) {
                isBackPressEnabled = false
            } else {
                viewModel.navigateBack()
            }
        }
    }

    private fun wrapBarTitle(item: PathItem) {
        val name = item.name
        val size = item.size

        bar?.title = "$name ($size)"
    }

    private fun wrapBarSubtitle(item: PathItem) {
        val props = item.value.properties()
        val folders = ReplacerThemeText(
            fileListDirectoriesCountKey,
            specialSymbol,
            props.dirsCount.toString()
        )

        val files = ReplacerThemeText(
            fileListFilesCountKey,
            specialSymbol,
            props.filesCount.toString()
        )

        bar?.subtitle = "$folders , $files"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        root = null
        list = null
        trail = null
        loading = null
    }

}