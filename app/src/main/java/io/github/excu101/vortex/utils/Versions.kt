package io.github.excu101.vortex.utils

import android.os.Build
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

inline val isAndroidQ
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

inline val isAndroidTiramisu
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

inline val isAndroidR
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

@ExperimentalContracts
inline fun doOnAndroid(version: Int, action: () -> Unit) {
    contract { callsInPlace(action) }

    if (Build.VERSION.SDK_INT >= version) action()
}