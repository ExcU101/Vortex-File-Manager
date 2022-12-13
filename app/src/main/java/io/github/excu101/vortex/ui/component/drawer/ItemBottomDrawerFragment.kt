package io.github.excu101.vortex.ui.component.drawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener

class ItemBottomDrawerFragment(
    vararg types: Pair<Int, ViewHolderFactory<Item<*>>>,
) : BottomSheetDialogFragment() {

    companion object {
        const val Tag = "BottomActionDrawer"
    }

    private val adapter = ItemAdapter(
        *types
    )

    private var binding: BottomSheetDrawerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = BottomSheetDrawerBinding(requireContext())
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            root.adapter = adapter
            adapter.register { _, item, _ ->

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null

    }

    fun register(listener: ItemViewListener<Item<*>>): ItemBottomDrawerFragment {
        adapter.register(listener)
        return this
    }

    fun withItems(items: List<Item<*>>): ItemBottomDrawerFragment {
        adapter.replace(items = items)
        return this
    }

}