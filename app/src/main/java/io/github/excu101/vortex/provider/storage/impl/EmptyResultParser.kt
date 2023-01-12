package io.github.excu101.vortex.provider.storage.impl

import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.ResultParser
import io.github.excu101.vortex.ui.component.list.adapter.Item

object EmptyResultParser : ResultParser<PathItem> {
    override fun parse(content: List<PathItem>): List<Item<*>> = content
}