package io.github.excu101.vortex.ui.component.item

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter

class ItemRecyclerView(context: Context) : RecyclerView(context) {

    private val _adapter = ItemAdapter<Item<*>>()

    override fun getAdapter(): ItemAdapter<Item<*>> {
        return _adapter
    }

    init {
        adapter = _adapter
        layoutManager = LinearLayoutManager(context)
    }

}