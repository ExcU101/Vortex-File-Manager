package io.github.excu101.vortex.base

import androidx.fragment.app.Fragment

interface FragmentNavigator {

    fun navigateTo(fragment: Fragment)

    fun navigateUp()

}