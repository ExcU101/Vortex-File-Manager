package io.github.excu101.vortex.ui.component.list.adapter

import android.os.Parcelable

interface StatefulAdapter {

    val savedState: Parcelable

    fun restoreState(state: Parcelable)

}