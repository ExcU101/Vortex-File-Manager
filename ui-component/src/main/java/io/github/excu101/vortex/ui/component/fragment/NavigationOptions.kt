package io.github.excu101.vortex.ui.component.fragment

import android.view.View

class NavigationOptions(
    val name: String?,
    val elements: Map<View, String>,
) {
    class Builder {
        var name: String? = null
        private var elements = mutableMapOf<View, String>()

        fun element(view: View, name: String) {
            elements[view] = name
        }

        fun build(): NavigationOptions = NavigationOptions(
            name = name,
            elements = elements
        )
    }
}

inline fun options(block: NavigationOptions.Builder.() -> Unit): NavigationOptions {
    return NavigationOptions.Builder().apply(block).build()
}

context (NavigationOptions.Builder)
        infix fun View.element(name: String) {
    element(view = this, name = name)
}