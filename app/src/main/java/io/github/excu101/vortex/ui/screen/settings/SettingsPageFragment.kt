package io.github.excu101.vortex.ui.screen.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.data.settings.SwitchItem
import io.github.excu101.vortex.navigation.dsl.FragmentFactory
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.with
import io.github.excu101.vortex.ui.component.repeatedLifecycle
import io.github.excu101.vortex.utils.SwitchItem

class SettingsPageFragment : Fragment() {

    companion object : FragmentFactory<SettingsPageFragment> {
        override fun createFragment(): SettingsPageFragment {
            return SettingsPageFragment()
        }
    }

    private var root: FrameLayout? = null
    private var list: ItemRecyclerView? = null
    private val adapter = ItemAdapter(
        ItemViewTypes.TextItem with TextItem,
        ItemViewTypes.SwitchItem with SwitchItem
    )
    private val viewModel by viewModels<SettingsPageViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = FrameLayout(requireContext())
        list = ItemRecyclerView(requireContext()).apply {
            adapter = this@SettingsPageFragment.adapter
        }
        root?.addView(list, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatedLifecycle {
            viewModel.collectState { state ->
                adapter.replace(state.content)
            }
        }

    }


}