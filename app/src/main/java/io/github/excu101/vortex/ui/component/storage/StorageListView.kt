package io.github.excu101.vortex.ui.component.storage

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StorageListView(context: Context) : RecyclerView(context), CoordinatorLayout.AttachedBehavior {

    private val behavior: StorageListViewBehavior = StorageListViewBehavior()

    private val adapter = StorageItemAdapter()

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(this.adapter)
    }

    override fun getAdapter() = adapter

    init {
        setHasFixedSize(true)
        setAdapter(adapter)
        isNestedScrollingEnabled = true
        clipToPadding = false
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
    }

    override fun getBehavior() = behavior

}