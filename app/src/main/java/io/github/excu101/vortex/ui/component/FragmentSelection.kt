package io.github.excu101.vortex.ui.component

import androidx.fragment.app.Fragment

interface FragmentSelection {

    fun onSelected()

}

fun Fragment?.callSelection() {
    (this as? FragmentSelection)?.onSelected()
}