package io.github.excu101.vortex.ui.screen.storage.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.ui.component.storage.StorageListPageAdapter
import kotlinx.coroutines.launch

class StorageListPagerFragment : Fragment() {

    private var root: CoordinatorLayout? = null
    private var pager: ViewPager2? = null
    private val viewModel by viewModels<StorageListPagerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        if (container != null) {
            root = CoordinatorLayout(container.context).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
            pager = ViewPager2(container.context).apply {
                adapter = StorageListPageAdapter(pager = this@StorageListPagerFragment)
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        }

        root?.addView(pager)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager?.apply {
            val adapter = adapter as StorageListPageAdapter

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.collectState { state ->
                        adapter.replace(state.fragments, null)
                    }
                }
            }
        }


    }

}