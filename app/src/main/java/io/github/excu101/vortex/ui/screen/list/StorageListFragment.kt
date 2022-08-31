package io.github.excu101.vortex.ui.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
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
import io.github.excu101.vortex.data.header.ActionHeaderItem
import io.github.excu101.vortex.data.header.TextHeaderItem
import io.github.excu101.vortex.data.storage.StorageItem
import io.github.excu101.vortex.databinding.FragmentStorageListBinding
import io.github.excu101.vortex.provider.StorageProvider.Companion.EXTERNAL_STORAGE
import io.github.excu101.vortex.ui.component.adapter.Item
import io.github.excu101.vortex.ui.component.bar
import io.github.excu101.vortex.ui.component.drawer
import io.github.excu101.vortex.ui.component.theme.key.fileListDirectoriesCountKey
import io.github.excu101.vortex.ui.component.theme.key.fileListFilesCountKey
import io.github.excu101.vortex.ui.component.theme.key.mainBarSurfaceColorKey
import io.github.excu101.vortex.ui.component.theme.key.specialSymbol
import kotlinx.coroutines.launch
import kotlin.LazyThreadSafetyMode.NONE

@AndroidEntryPoint
class StorageListFragment : Fragment() {

    private val viewModel by viewModels<StorageListViewModel>()

    private var binding: FragmentStorageListBinding? = null

    private val controller: WindowInsetsControllerCompat? by lazy(NONE) {
        binding?.root?.let { view ->
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
        binding = FragmentStorageListBinding.inflate(layoutInflater, container, false)

//        bar?.hideOnScroll = true
        bar?.setBackgroundColor(ThemeColor(mainBarSurfaceColorKey))
        requireActivity().window.statusBarColor = Transparent.value
        requireActivity().window.navigationBarColor = Transparent.value

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isBackPressEnabled = true
        binding?.apply {
            trail.adapter.register { view, item, position ->
                viewModel.navigateTo(item = item.value)
            }

//            bar?.setOnMenuItemClickListener {
//                when (it) {
//
//                    else -> false
//                }
//            }

            list.adapter.register { view, item, position ->
                when (view) {
                    is FrameLayout -> {
                        viewModel.navigateTo(item = item as StorageItem)
                    }
                }
            }

            list.adapter.registerLong { view, item, position ->
                when (view) {
                    is FrameLayout -> {
                        true
                    }

                    else -> false
                }
            }

            list.adapter.registerSelection { view, item, _, isSelected ->
                viewModel.select(item = item as StorageItem, isSelected = isSelected)

                when (view) {
                    is ImageView -> {
                        true
                    }

                    else -> false
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.collectState { state ->
                        val data = mutableListOf<Item<*>>()

                        state.sections.forEach { (id, values) ->
                            data.add(TextHeaderItem(id))
                            data.addAll(values)
                        }

                        list.adapter.submitList(data)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.trail.collect { data ->
                        val currentItem = data.current.value
                        if (data.current.value.value == EXTERNAL_STORAGE) {
                            isBackPressEnabled = false
                        }

                        if (!viewModel.selectionModeEnabled) {
                            wrapBarTitle(item = currentItem)
                        }
                        wrapBarSubtitle(item = currentItem)
                        trail.smoothScrollToPosition(data.currentSelected)
                        trail.adapter.submitList(data.items)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.selected.collect { data ->
                        if (viewModel.selectionModeEnabled) {
                            bar?.title = "Selected ${data.count()}"
                        } else {
                            wrapBarTitle()
                        }

                        list.adapter.select(data)
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

                        this@StorageListFragment.drawer?.actions = actions
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            owner = viewLifecycleOwner,
            enabled = isBackPressEnabled
        ) {
            viewModel.navigateBack()
        }
    }

    private fun wrapBarTitle(item: StorageItem = viewModel.trail.value.current.value) {
        val name = item.name

        bar?.title = name
    }

    private fun wrapBarSubtitle(item: StorageItem = viewModel.trail.value.current.value) {
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
        binding = null
    }

}