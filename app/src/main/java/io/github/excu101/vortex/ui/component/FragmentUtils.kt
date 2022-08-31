package io.github.excu101.vortex.ui.component

import androidx.fragment.app.Fragment
import io.github.excu101.vortex.ui.MainActivity
import io.github.excu101.vortex.ui.component.action.ActionDialog
import io.github.excu101.vortex.ui.component.bar.Bar

inline val Fragment.bar: Bar?
    get() = (requireActivity() as MainActivity).bar

inline val Fragment.drawer: ActionDialog?
    get() = (requireActivity() as MainActivity).drawer