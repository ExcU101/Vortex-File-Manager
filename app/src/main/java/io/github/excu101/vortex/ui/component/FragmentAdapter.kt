package io.github.excu101.vortex.ui.component

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.excu101.vortex.provider.command.Command
import io.github.excu101.vortex.provider.command.CommandConsumer
import kotlinx.coroutines.channels.Channel

class FragmentAdapter : FragmentStateAdapter {

    constructor(fragment: FragmentActivity) : super(fragment)
    constructor(fragment: Fragment) : super(fragment)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

    private val stack = mutableListOf<Fragment>()

    operator fun get(index: Int) = stack[index]

    override fun getItemCount(): Int = stack.size

    fun addFragment(fragment: Fragment) {
        stack.add(fragment)
        notifyItemInserted(itemCount - 1)
    }

    fun pop(fragment: Fragment) {
        for (i in stack.lastIndex..0) {
            if (stack[i] != fragment) {
                stack.remove(fragment)
            } else {
                break
            }
        }
    }

    override fun createFragment(position: Int): Fragment = stack[position]

}