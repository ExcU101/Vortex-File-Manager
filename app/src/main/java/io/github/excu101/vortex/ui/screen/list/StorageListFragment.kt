package io.github.excu101.vortex.ui.screen.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.ColorUtils.calculateLuminance
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.data.Color
import io.github.excu101.vortex.databinding.FragmentStorageListBinding
import io.github.excu101.vortex.ui.theme.Theme
import io.github.excu101.vortex.ui.theme.key.trailSurfaceColorKey
import io.github.excu101.vortex.ui.view.actions
import io.github.excu101.vortex.ui.view.bar
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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            controller?.isAppearanceLightStatusBars =
                calculateLuminance(Theme<Int, Color>(trailSurfaceColorKey)) > 0.5

            trail.adapter.register { view, item, position ->
                viewModel.navigateTo(unit = item.value)
            }

            list.adapter.registerLong { view, item, position ->
                when (view) {
                    is ImageView -> {
                        Toast.makeText(requireContext(), "FUCK", Toast.LENGTH_LONG)
                        true
                    }

                    is FrameLayout -> {
                        Toast.makeText(requireContext(), "FUCK", Toast.LENGTH_LONG)
                        true
                    }

                    else -> false
                }
            }

            list.adapter.register { view, item, position ->
                when (view) {
                    is ImageView -> {
                        viewModel.choose(setOf(item), true)
                    }

                    is FrameLayout -> {
                        viewModel.navigateTo(unit = item)
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.collectState { state ->
                        list.adapter.submitList(state.items)
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
                        list.adapter.replaceSelectedFiles(data.toList())
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.actions.collect { groups ->
                        actions.updateActions(groups)
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