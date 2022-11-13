package io.github.excu101.vortex.ui.component.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass


interface FragmentNavigator {

    fun popBackStack(): Boolean

    fun <T : Fragment> navigate(
        fragmentClass: KClass<T>,
        args: Bundle,
        options: NavigationOptions? = null,
    )

    fun <T : Fragment> navigate(
        factory: FragmentFactory<T>,
        args: Bundle,
        options: NavigationOptions? = null,
    )

    fun <T : Fragment> navigate(
        fragment: T,
        options: NavigationOptions? = null,
    )

}

inline fun <reified T : Fragment> FragmentNavigator.navigate(
    args: Bundle,
    options: NavigationOptions? = null,
) = navigate(
    fragmentClass = T::class,
    args = args,
    options = options
)