package io.github.excu101.vortex.ui.component

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

typealias ViewHolderFactory = (parent: ViewGroup) -> RecyclerView.ViewHolder
typealias ChildViewHolderFactory = (child: View) -> RecyclerView.ViewHolder
typealias ChildFactory = (ViewGroup) -> View

class PrefetchViewPool(
    val context: CoroutineContext = Dispatchers.Default,
) {

    private val childCounts = HashMap<View, Int>() // key: viewType, value: count
    private val parents = HashMap<Int, ChildFactory>() // key: viewType, value: View
    private val children = HashMap<View, Pair<ChildFactory, Int>>()

    var isDetached: Boolean = false

    fun prefetchViews(parent: ViewGroup) {
        if (childCounts.isEmpty()) return
        if (isDetached) return
    }

    fun replaceViewTypes(
        vararg viewType: Pair<View, Int>,
    ) {
        childCounts.clear()
        childCounts.putAll(viewType)
    }

    fun replaceChildren(
        vararg children: Pair<View, Pair<ChildFactory, Int>>,
    ) {
        this.children.clear()
        this.children.putAll(children)
    }

    private fun produce(
        parent: ViewGroup,
        count: Int,
    ) {
        MainScope().launch {
            val view = async<View?>(context) {
                if (isDetached) return@async null

                for (i in 0 until count) {
                    parents[i]?.invoke(parent)
                }

                return@async null
            }


        }
    }

}