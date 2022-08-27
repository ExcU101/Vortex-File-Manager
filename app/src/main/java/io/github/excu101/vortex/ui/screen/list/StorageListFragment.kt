package io.github.excu101.vortex.ui.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.pluginsystem.model.Color.Companion.Transparent
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.data.TextHeaderItem
import io.github.excu101.vortex.data.StorageItem
import io.github.excu101.vortex.databinding.FragmentStorageListBinding
import io.github.excu101.vortex.ui.component.adapter.Item
import io.github.excu101.vortex.ui.component.bar
import io.github.excu101.vortex.ui.component.theme.key.mainBarSurfaceColorKey
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

        bar.hideOnScroll = true
        bar.setBackgroundColor(ThemeColor(mainBarSurfaceColorKey))
        requireActivity().window.statusBarColor = Transparent.value
        requireActivity().window.navigationBarColor = ThemeColor(mainBarSurfaceColorKey)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            trail.adapter.register { view, item, position ->
                viewModel.navigateTo(item = StorageItem(item.value))
            }

            bar.setOnMenuItemClickListener {
                when (it) {

                    else -> false
                }
            }

            list.adapter.registerLong { view, item, position ->
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

            list.adapter.register { view, item, position ->
                when (view) {
                    is ImageView -> {
                        viewModel.choose(setOf(item) as Collection<StorageItem>, true)
                    }

                    is FrameLayout -> {
                        viewModel.navigateTo(item = item as StorageItem)
                    }
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
                        trail.smoothScrollToPosition(data.currentSelected)
                        trail.adapter.submitList(data.items)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.selected.collect { data ->

                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.actions.collect { groups ->

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}