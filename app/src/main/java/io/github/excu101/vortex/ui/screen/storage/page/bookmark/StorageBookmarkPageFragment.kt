package io.github.excu101.vortex.ui.screen.storage.page.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.navigation.dsl.FragmentFactory
import io.github.excu101.vortex.ui.component.repeatedLifecycle
import io.github.excu101.vortex.ui.component.requireBar

@AndroidEntryPoint
class StorageBookmarkPageFragment : Fragment() {

    companion object : FragmentFactory<StorageBookmarkPageFragment> {
        override fun createFragment(): StorageBookmarkPageFragment {
            return StorageBookmarkPageFragment()
        }
    }

    private var binding: StorageBookmarkBinding? = null

    private val viewModel by viewModels<StorageBookmarkPageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        returnTransition = Fade(Fade.MODE_IN)
        exitTransition = Fade(Fade.MODE_OUT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = StorageBookmarkBinding(requireContext())
        binding?.onCreate()
        return binding?.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.onDestroy()
        binding = null
    }


}