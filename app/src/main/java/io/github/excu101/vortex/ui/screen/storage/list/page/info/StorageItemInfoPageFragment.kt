package io.github.excu101.vortex.ui.screen.storage.list.page.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.excu101.vortex.R
import io.github.excu101.vortex.base.utils.collectEffect
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.ItemViewTypes.dividerItem
import io.github.excu101.vortex.ui.component.ItemViewTypes.iconTextItem
import io.github.excu101.vortex.ui.component.ItemViewTypes.textItem
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView
import io.github.excu101.vortex.ui.component.item.divider.DividerHeaderItem
import io.github.excu101.vortex.ui.component.item.icon.IconTextHeaderItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.parcelable
import io.github.excu101.vortex.ui.component.repeatedLifecycle
import io.github.excu101.vortex.ui.component.requireBar

class StorageItemInfoPageFragment : Fragment() {

    companion object {
        private const val BundleKey = "pathItem"

        fun newInstance(item: PathItem): StorageItemInfoPageFragment {
            val args = Bundle()
            args.putParcelable(BundleKey, item)
            val fragment = StorageItemInfoPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by viewModels<StorageItemInfoPageViewModel>()

    private var root: CoordinatorLayout? = null
    private var list: ItemRecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        requireBar().navigationIcon = requireContext().getDrawable(R.drawable.ic_back_24)

        root = CoordinatorLayout(requireContext()).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        list = ItemRecyclerView(requireContext()).apply {
            adapter.add(textItem, TextItem as ViewHolderFactory<Item<*>>)
            adapter.add(iconTextItem, IconTextHeaderItem as ViewHolderFactory<Item<*>>)
            adapter.add(dividerItem, DividerHeaderItem as ViewHolderFactory<Item<*>>)
        }

        root?.addView(list, LayoutParams(MATCH_PARENT, MATCH_PARENT))

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemAdapter = list!!.adapter

        requireArguments().parcelable<PathItem>(key = BundleKey)?.let { item ->
            viewModel.getInfo(item)
        }

        repeatedLifecycle {
            viewModel.collectState { state ->
                itemAdapter.replace(state.data)
            }
        }

        repeatedLifecycle {
            viewModel.collectEffect { effect ->

            }
        }
    }

}