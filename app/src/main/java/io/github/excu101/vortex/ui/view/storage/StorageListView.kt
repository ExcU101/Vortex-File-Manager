package io.github.excu101.vortex.ui.view.storage

import android.content.Context
import android.util.AttributeSet
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StorageListView : RecyclerView, CoordinatorLayout.AttachedBehavior {

    constructor(context: Context) : super(context) {
        behavior = StorageListViewBehavior()
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
    ) : super(
        context,
        attrs
    ) {
        behavior = StorageListViewBehavior(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        behavior = StorageListViewBehavior(context, attrs)
    }

    private val behavior: StorageListViewBehavior

    private val listAdapter = StorageListAdapter().apply {
        setHasStableIds(true)
    }

    override fun getAdapter(): StorageListAdapter = listAdapter

    init {
        setHasFixedSize(true)
        adapter = listAdapter
        isNestedScrollingEnabled = true
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
    }

    override fun getBehavior() = behavior

}