package io.github.excu101.vortex.ui.navigation

import androidx.fragment.app.Fragment

abstract class PageFragment : Fragment {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    abstract fun onPageSelected()

    abstract fun onPageUnselected()
}