package io.github.excu101.vortex.ui.component.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.reflect.KClass

class FragmentNavigatorImpl(
    private val containerId: Int,
    private val manager: FragmentManager,
) : FragmentNavigator {

    private val backStack = ArrayDeque<Int>()

    private val listener = FragmentManager.OnBackStackChangedListener {
        val newCount: Int = manager.backStackEntryCount + 1
        if (newCount < backStack.size) {
            while (backStack.size > newCount) {
                backStack.removeLast()
            }
        }
    }

    init {
        manager.addOnBackStackChangedListener(listener)
    }

    override fun popBackStack(): Boolean {
        if (manager.backStackEntryCount == 0) return false

        manager.popBackStack()

        backStack.removeLastOrNull()
        return true
    }

    override fun <T : Fragment> navigate(
        fragmentClass: KClass<T>,
        args: Bundle,
        options: NavigationOptions?,
    ) {
//        navigate(factory = FragmentFactory.Default(fragmentClass), args = args, options = options)
    }

//    override fun <T : Fragment> navigate(
//        factory: FragmentFactory<T>,
//        args: Bundle,
//        options: NavigationOptions?,
//    ) = navigate(factory.create(args), options)

    override fun <T : Fragment> navigate(
        fragment: T,
        options: NavigationOptions?,
    ) {
        val transaction = manager.beginTransaction()

        options?.elements?.forEach { (view, name) ->
            transaction.addSharedElement(view, name)
        }

        transaction.replace(containerId, fragment)
        transaction.addToBackStack(options?.name)
        transaction.setReorderingAllowed(true)
        transaction.commit()
    }
}
