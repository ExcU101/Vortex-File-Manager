package io.github.excu101.vortex.ui.component.storage

import android.os.FileObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.excu101.vortex.ui.component.list.adapter.EditableAdapter
import io.github.excu101.vortex.ui.screen.storage.list.StorageListPagerFragment
import io.github.excu101.vortex.ui.screen.storage.list.page.list.StorageListPageFragment

class StorageListPageAdapter(
    pager: StorageListPagerFragment,
) : FragmentStateAdapter(pager), EditableAdapter<Fragment> {

    override fun replace(items: List<Fragment>, differ: DiffUtil.Callback?) {
        instances.clear()
        instances.addAll(items)

        if (differ != null) {

        } else {
            notifyDataSetChanged()
        }
    }

    override fun add(item: Fragment) = add(itemCount, item)

    override fun add(position: Int, item: Fragment) {
        instances.add(position, item)
        notifyItemInserted(position)
    }

    override fun add(items: Iterable<Fragment>) {
        val from = itemCount
        instances.addAll(items)
        val to = itemCount
        notifyItemRangeInserted(from, to)
    }

    override fun remove(item: Fragment) = remove(position(item))

    override fun remove(position: Int) {
        instances.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun position(item: Fragment): Int = instances.indexOf(item)

    override fun item(position: Int): Fragment = instances[position]

    private val instances = mutableListOf<Fragment>()

    override fun getItemCount(): Int = instances.size

    override fun createFragment(position: Int): Fragment {
        val fragment = item(position)

        when (fragment) {
            is StorageListPageFragment -> {
//                fragment.arguments?.putString(pageKey, "$position")
            }
        }

        return fragment
    }

    override fun changed(position: Int) {

    }

}