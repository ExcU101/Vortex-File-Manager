package io.github.excu101.vortex.ui.screen.settings

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import dagger.hilt.android.AndroidEntryPoint
import io.github.excu101.vortex.base.utils.collectState
import io.github.excu101.vortex.data.settings.SwitchItem
import io.github.excu101.vortex.navigation.dsl.FragmentFactory
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.bar
import io.github.excu101.vortex.ui.component.dp
import io.github.excu101.vortex.ui.component.item.ItemRecyclerView
import io.github.excu101.vortex.ui.component.list.adapter.DrawerViewHolderFactories
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.with
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.ui.component.repeatedLifecycle
import io.github.excu101.vortex.ui.navigation.PageFragment
import io.github.excu101.vortex.utils.SwitchItem

@AndroidEntryPoint
class SettingsPageFragment : PageFragment(), MenuActionListener {

    companion object : FragmentFactory<SettingsPageFragment> {
        override fun createFragment(): SettingsPageFragment {
            return SettingsPageFragment()
        }
    }

    private var root: FrameLayout? = null
    private var list: ItemRecyclerView? = null
    private val adapter = ItemAdapter(
        *DrawerViewHolderFactories + (ItemViewTypes.SwitchItem with SwitchItem)
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
            updatePadding(
                bottom = 56.dp
            )
        }

        list?.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (recyclerView.layoutManager?.findViewByPosition(0) == null) {
                    bar?.hide()
                } else {
                    bar?.show()
                }
            }
        })

        root?.addView(list, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            gravity = Gravity.BOTTOM
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatedLifecycle {
            viewModel.collectState { state ->
                adapter.replace(state.content)
                bar?.replaceItems(state.actions)
            }
        }
    }

    override fun onPageSelected() {
        bar?.registerListener(listener = this)
        bar?.replaceItems(viewModel.container.state.value.actions)
    }

    override fun onPageUnselected() {
        bar?.unregisterListener(listener = this)
    }

    override fun onMenuActionCall(action: MenuAction) {

    }


}