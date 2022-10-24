package io.github.excu101.vortex.ui.component.list.adapter.selection

import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.list.adapter.listener.ClickListenerRegister
import io.github.excu101.vortex.ui.component.list.adapter.EditableAdapter

abstract class SelectionAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(),
    SelectableAdapter<T>, ClickListenerRegister<T>