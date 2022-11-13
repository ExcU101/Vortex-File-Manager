package io.github.excu101.vortex.ui.component

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.excu101.vortex.navigation.NavigationGraph
import io.github.excu101.vortex.navigation.destination.FragmentDestination
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.drawer.NavigationDrawer
import io.github.excu101.vortex.ui.component.fragment.FragmentFactory
import io.github.excu101.vortex.ui.component.fragment.FragmentNavigator
import io.github.excu101.vortex.ui.screen.main.MainActivity
import io.github.excu101.vortex.ui.screen.main.MainLayoutBinding
import io.github.excu101.vortex.utils.isAndroidTiramisu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.repeatedLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(state, block)
    }
}

inline val Fragment.bar: Bar?
    get() = (requireActivity() as? BarOwner)?.bar

fun Fragment.requireBar(): Bar {
    return bar ?: throw IllegalArgumentException()
}

inline val Fragment.drawer: NavigationDrawer?
    get() = (requireActivity() as MainActivity).binding?.root

fun Fragment.requireDrawer(): NavigationDrawer {
    return drawer ?: throw IllegalArgumentException()
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    isAndroidTiramisu -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
