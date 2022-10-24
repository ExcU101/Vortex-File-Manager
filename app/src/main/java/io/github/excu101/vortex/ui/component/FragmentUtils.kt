package io.github.excu101.vortex.ui.component

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.excu101.vortex.base.FragmentNavigator
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.drawer.BottomActionDrawer
import io.github.excu101.vortex.ui.screen.main.MainActivity
import io.github.excu101.vortex.utils.isAndroidTiramisu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun Fragment.repeatedLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    noinline block: suspend CoroutineScope.() -> Unit,
) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(state, block)
    }
}

inline val Fragment.bar: Bar?
    get() = (requireActivity() as MainActivity).bar

fun Fragment.requireBar(): Bar {
    return bar ?: throw IllegalArgumentException()
}

inline val Fragment.drawer: BottomActionDrawer?
    get() = (requireActivity() as MainActivity).drawer

fun Fragment.requireDrawer(): BottomActionDrawer {
    return drawer ?: throw IllegalArgumentException()
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    isAndroidTiramisu -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}
