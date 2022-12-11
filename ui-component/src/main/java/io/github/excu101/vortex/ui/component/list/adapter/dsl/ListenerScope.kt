package io.github.excu101.vortex.ui.component.list.adapter.dsl

import android.view.View

interface ViewListenerScope : ViewHolderScope.ListenerScope {

    var onClick: (View.OnClickListener) -> Unit
    var onLongClick: (View.OnLongClickListener) -> Boolean

}