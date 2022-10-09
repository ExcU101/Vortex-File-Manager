package io.github.excu101.vortex.ui.component

import androidx.fragment.app.Fragment
import io.github.excu101.vortex.ui.component.bar.Bar
import io.github.excu101.vortex.ui.component.drawer.BottomActionDrawer
import io.github.excu101.vortex.ui.screen.main.MainActivity

inline val Fragment.bar: Bar?
    get() = (requireActivity() as MainActivity).bar

inline val Fragment.drawer: BottomActionDrawer?
    get() = (requireActivity() as MainActivity).drawer