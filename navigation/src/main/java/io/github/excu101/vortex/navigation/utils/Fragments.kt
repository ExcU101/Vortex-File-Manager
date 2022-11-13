package io.github.excu101.vortex.navigation.utils

import androidx.fragment.app.Fragment
import io.github.excu101.vortex.navigation.HostNavigationController
import io.github.excu101.vortex.navigation.NavigationController

fun Fragment.NavigationController(): NavigationController {
    return (requireActivity() as HostNavigationController).controller
}