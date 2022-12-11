package io.github.excu101.vortex.ui.component

import android.view.View

interface ViewBinding<V : View> {

    val root: V

    fun onCreate()

    fun onDestroy()

}

//inline fun <V : View, VB : ViewBinding<V>> VB.apply(block: VB.(root: V) -> Unit) {
//    block(this, root)
//}