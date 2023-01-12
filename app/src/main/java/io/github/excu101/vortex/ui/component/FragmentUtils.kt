package io.github.excu101.vortex.ui.component

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.utils.isAndroidTiramisu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.repeatedLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit,
) {
    viewLifecycleOwner.repeatedLifecycle(state, block)
}


fun LifecycleOwner.repeatedLifecycle(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit,
) {
    lifecycleScope.launch {
        repeatOnLifecycle(state, block)
    }
}

inline val Fragment.bar: Bar?
    get() = (activity as? BarOwner)?.bar

fun Fragment.requireBar(): Bar {
    return bar ?: throw IllegalArgumentException()
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    isAndroidTiramisu -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}