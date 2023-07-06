package io.github.excu101.vortex.ui.navigation

import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewSelectionListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.SelectionListener

interface ListPageController<I> : ItemViewListener<I>, ItemViewLongListener<I>,
    ItemViewSelectionListener<I>, SelectionListener<I>