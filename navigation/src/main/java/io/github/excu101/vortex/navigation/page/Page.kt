package io.github.excu101.vortex.navigation.page

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import io.github.excu101.vortex.navigation.ArgumentOwner

abstract class Page : LifecycleOwner, ArgumentOwner<Bundle?> {

    private val lifecycle: PageLifecycle = PageLifecycle(owner = this)

    protected var context: Context? = null
        private set

    override var arguments: Bundle? = null

    abstract fun onCreateView(
        container: ViewPager,
        state: Bundle
    ): View?

    abstract fun onViewCreated(
        root: View,
        state: Bundle
    )

    fun onAttach() {

    }

    fun onDetach() {

    }

    open fun onLowMemory() {

    }

    open fun onConfigurationChanged(configuration: Configuration) {

    }

    override fun getLifecycle(): Lifecycle = lifecycle

}