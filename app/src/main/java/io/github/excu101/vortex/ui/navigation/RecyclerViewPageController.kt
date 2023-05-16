package io.github.excu101.vortex.ui.navigation

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.navigation.page.PageController
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener

abstract class RecyclerViewPageController<I : Item<*>>(
    context: Context,
    vararg types: Pair<Int, ViewHolderFactory<I>>,
) : PageController(context), ItemViewListener<I>,
    ItemViewLongListener<I> {

    protected val adapter = ItemAdapter<I>(*types)

    fun onCreateRecyclerView(context: Context): RecyclerView {
        return RecyclerView(context)
    }

}