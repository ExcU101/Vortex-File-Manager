package io.github.excu101.vortex.utils

import android.os.Build

inline val isAndroidTiramisu
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

inline val isAndroidR
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R