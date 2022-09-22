package io.github.excu101.vortex.base.impl

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.github.excu101.vortex.base.FragmentNavigator

internal class ActivityFragmentNavigator(
    private val activity: FragmentActivity,
    private val containerId: Int,
) : FragmentNavigator {

    private val backStack = mutableListOf<Fragment>()
    private var currentFragment = 0

    override fun navigateTo(fragment: Fragment, tag: String?) {
        if (fragment !in backStack)
            backStack.add(fragment)

        activity.supportFragmentManager.beginTransaction().replace(containerId, fragment, tag)
            .commit()
    }

    override fun navigateUp() {
        backStack.getOrNull(currentFragment - 1)?.let {
            activity.supportFragmentManager.beginTransaction().replace(containerId, it).commit()
            currentFragment -= 1
        }
    }

}