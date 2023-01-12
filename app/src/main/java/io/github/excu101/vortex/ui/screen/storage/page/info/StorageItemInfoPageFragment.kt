package io.github.excu101.vortex.ui.screen.storage.page.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.Fade
import io.github.excu101.vortex.base.utils.collectEffect
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.navigation.dsl.FragmentFactory
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.ItemViewTypes.DividerItem
import io.github.excu101.vortex.ui.component.animation.fade
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView
import io.github.excu101.vortex.ui.component.item.divider.DividerHeaderItem
import io.github.excu101.vortex.ui.component.item.info.InfoItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.loading.LoadingView
import io.github.excu101.vortex.ui.component.parcelable
import io.github.excu101.vortex.ui.component.repeatedLifecycle
import io.github.excu101.vortex.ui.component.requireBar
import io.github.excu101.vortex.ui.navigation.AppNavigation.Args

class StorageItemInfoPageFragment : Fragment() {

    companion object : FragmentFactory<StorageItemInfoPageFragment> {
        override fun createFragment(): StorageItemInfoPageFragment {
            return StorageItemInfoPageFragment()
        }
    }

    private val viewModel by viewModels<StorageItemInfoPageViewModel>()

    private var root: CoordinatorLayout? = null
    private var loading: LoadingView? = null
    private var list: ItemRecyclerView? = null

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
        root = CoordinatorLayout(requireContext()).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        list = ItemRecyclerView(requireContext()).apply {
            adapter.add(ItemViewTypes.TextItem, TextItem as ViewHolderFactory<Item<*>>)
            adapter.add(DividerItem, DividerHeaderItem as ViewHolderFactory<Item<*>>)
            adapter.add(ItemViewTypes.InfoItem, InfoItem as ViewHolderFactory<Item<*>>)
        }

        loading = LoadingView(requireContext()).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        root?.addView(list, LayoutParams(MATCH_PARENT, MATCH_PARENT))
        root?.addView(loading, LayoutParams(MATCH_PARENT, MATCH_PARENT))

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemAdapter = list!!.adapter
        requireBar().navigationIcon.progress = 1F

        requireBar().subtitle = null
        requireBar().replaceItems(listOf())

        repeatedLifecycle {
            viewModel.collectState { state ->
                list?.fade(isOut = state.isLoading)
                loading?.fade(isOut = !state.isLoading)

                loading?.title = state.loadingTitle

                itemAdapter.replace(state.data)
            }
        }

        repeatedLifecycle {
            viewModel.collectEffect { effect ->

            }
        }

        requireArguments().parcelable<PathItem>(Args.Storage.ItemInfoPage.ItemInfoPageKey)
            ?.let { item ->
                requireBar().title = item.name
                viewModel.getInfo(item)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        requireBar().setNavigationClickListener(null)
    }

}