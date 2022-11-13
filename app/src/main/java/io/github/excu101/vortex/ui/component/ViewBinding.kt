package io.github.excu101.vortex.ui.component

import android.view.View

interface ViewBinding {

    val root: View

    fun onCreate()

    fun onDestroy()

}