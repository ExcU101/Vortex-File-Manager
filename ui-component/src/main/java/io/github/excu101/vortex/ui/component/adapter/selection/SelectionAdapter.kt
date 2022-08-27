package io.github.excu101.vortex.ui.component.adapter.selection

import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.adapter.listener.ClickListenerRegister
import io.github.excu101.vortex.ui.component.adapter.EditableAdapter

abstract class SelectionAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(),
    EditableAdapter<T>, ClickListenerRegister<T> {

    abstract fun isSelected(position: Int): Boolean

}