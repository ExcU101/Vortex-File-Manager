package io.github.excu101.vortex.ui.screen.storage.page.info

import io.github.excu101.filesystem.fs.utils.count
import io.github.excu101.filesystem.fs.utils.directoryCount
import io.github.excu101.filesystem.fs.utils.directorySize
import io.github.excu101.filesystem.fs.utils.fileCount
import io.github.excu101.pluginsystem.ui.theme.ThemeColor
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.component.dsl.scope
import io.github.excu101.vortex.ui.component.item.info.info
import io.github.excu101.vortex.ui.component.item.text.text
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.theme.key.accentColorKey
import io.github.excu101.vortex.ui.screen.storage.page.info.StorageItemInfoPageScreen.ItemInfoParser
import io.github.excu101.vortex.utils.convertToThemeString
import io.github.excu101.vortex.utils.convertToThemeText
import io.github.excu101.vortex.utils.parseThemeType

object StorageItemInfoPageScreen {

    data class State(
        val data: List<Item<*>> = listOf(),
        val isLoading: Boolean = false,
        val loadingTitle: String? = null,
    )

    class SideEffect

    fun interface ItemInfoParser {
        companion object {
            val Default = ItemInfoParser { item ->
                scope {
                    text(value = "Main") {
                        color = ThemeColor(accentColorKey)
                        size = 16F
                    }
                    info(
                        value = item.name,
                        description = "Name"
                    )
                    info(
                        value = item.path,
                        description = "Path"
                    )
                    info(
                        value = if (item.isDirectory) "Directory"
                        else if (item.isLink) "Link"
                        else item.mime.parseThemeType(),
                        description = "Type"
                    )
                    if (item.isDirectory) {
                        info(
                            value = "${item.value.count} (${item.value.directoryCount}, ${item.value.fileCount})",
                            description = "Count (Directory count, File count)"
                        )
                    }
                    item.owner?.let { owner ->
                        info(
                            value = owner,
                            description = "Owner"
                        )
                    }
                    item.group?.let { group ->
                        info(
                            value = group,
                            description = "Group"
                        )
                    }
                    info(
                        value = item.perms.convertToThemeString(),
                        description = "Mode (Owner, Group, Other)"
                    )
                    with(if (item.isDirectory) item.value.directorySize else item.size) {
                        info(
                            value = convertToThemeText() + " (${memory})",
                            description = "Size (B)"
                        )
                    }
                    text(value = "Time") {
                        color = ThemeColor(accentColorKey)
                        size = 16F
                    }
                    info(
                        value = item.lastModifiedTime.toString(),
                        description = "Last modified time"
                    )
                    info(
                        value = item.lastAccessTime.toString(),
                        description = "Last access time"
                    )
                    info(
                        value = item.creationTime.toString(),
                        description = "Creation time"
                    )
                    text(value = "Additional") {
                        color = ThemeColor(accentColorKey)
                        size = 16F
                    }
                    info(
                        value = item.uri.toString(),
                        description = "Uri"
                    )
                    info(
                        value = if (item.isAbsolute) "Yes" else "No",
                        description = "Absolute"
                    )
                    info(
                        value = if (item.isHidden) "Yes" else "No",
                        description = "Hidden"
                    )
                    item.inode?.let { inode ->
                        info(
                            value = inode.toString(),
                            description = "Inode"
                        )
                    }
                }
            }
        }

        suspend fun parse(item: PathItem): List<Item<*>>
    }

}