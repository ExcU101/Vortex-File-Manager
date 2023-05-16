package io.github.excu101.vortex.ui.component.list.adapter.selection

import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.vortex.ui.component.list.adapter.listener.ClickListenerRegister

abstract class SelectionAdapter<T, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(),
    ClickListenerRegister<T>