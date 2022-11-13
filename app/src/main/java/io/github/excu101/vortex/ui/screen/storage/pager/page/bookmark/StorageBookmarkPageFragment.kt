package io.github.excu101.vortex.ui.screen.storage.pager.page.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.ui.component.ViewBindingFragment
import io.github.excu101.vortex.ui.component.fragment.FragmentFactory
import io.github.excu101.vortex.ui.component.repeatedLifecycle
import io.github.excu101.vortex.ui.component.requireBar

class StorageBookmarkPageFragment : ViewBindingFragment<StorageBookmarkBinding>() {

    companion object : FragmentFactory<StorageBookmarkPageFragment> {
        override fun createFragment(): StorageBookmarkPageFragment {
            return StorageBookmarkPageFragment()
        }
    }

    private val viewModel by viewModels<StorageBookmarkPageViewModel>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): StorageBookmarkBinding {
        return StorageBookmarkBinding(requireContext())
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            requireBar().title = "Bookmarks"
            val adapter = list.adapter

            repeatedLifecycle {
                viewModel.collectState { state ->
                    adapter.replace(state.data)
                }
            }
        }
    }

}