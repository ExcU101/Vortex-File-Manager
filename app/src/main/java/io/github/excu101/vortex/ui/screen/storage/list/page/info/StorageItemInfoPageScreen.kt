package io.github.excu101.vortex.ui.screen.storage.list.page.info

import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.text.text
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.screen.storage.list.page.info.StorageItemInfoPageScreen.ItemInfoParser
import io.github.excu101.vortex.utils.convertToThemeText

object StorageItemInfoPageScreen {

    data class State(
        val data: List<Item<*>> = listOf(),
    )

    class SideEffect(

    )

    fun interface ItemInfoParser {
        companion object {
            val Default = ItemInfoParser { item ->
                scope {
                    text("Name: ${item.name}")
                    text("Path: ${item.path}")
                    text("Type: " + if (item.isDirectory) "Directory" else if (item.isLink) "Link" else "File")
                    text("Size: ${item.size.convertToThemeText()} (${item.size.memory})")
                    text("Is Absolute: ${item.isAbsolute}")
                    text("Is Hidden: ${item.isHidden}")
                    text("Last modified time: ${item.lastModifiedTime}")
                    text("Last access time: ${item.lastAccessTime}")
                    text("Creation time: ${item.creationTime}")
                }
            }
        }

        fun parse(item: PathItem): List<Item<*>>
    }

}