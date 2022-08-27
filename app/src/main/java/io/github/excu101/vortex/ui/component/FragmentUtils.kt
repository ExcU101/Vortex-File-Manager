package io.github.excu101.vortex.ui.component

import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar

inline val Fragment.bar: BottomAppBar
    get() = requireActivity().findViewById(io.github.excu101.vortex.R.id.bar)