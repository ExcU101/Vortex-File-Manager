package io.github.excu101.vortex.ui.component.storage

import io.github.excu101.pluginsystem.ui.theme.ThemeColorInfo
import io.github.excu101.pluginsystem.ui.theme.ThemeDimenInfo
import io.github.excu101.pluginsystem.ui.theme.ThemeTextInfo
import io.github.excu101.vortex.ui.component.theme.key.*
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.*

object StorageInfo {

    object Item {
        val texts = ThemeTextInfo(
            name = "Storage item texts",
            keys = listOf(
                fileListItemNameKey,
                fileListItemSizeKey,
                fileListItemInfoSeparatorKey,
                fileListItemsCountKey,
                fileListItemCountKey,
                fileListItemEmptyKey
            )
        )

        val dimens = ThemeDimenInfo(
            name = "Storage item dimens",
            keys = listOf(
                storageListItemHorizontalTitlePaddingKey,
                storageListItemHorizontalInfoPaddingKey,
                storageListItemLinearWidthDimenKey,
                storageListItemLinearHeightDimenKey
            )
        )

        val colors = ThemeColorInfo(
            name = "Storage item colors",
            keys = listOf(
                storageListItemSurfaceColorKey,
                storageListItemSurfaceSelectedColorKey,
                storageListItemSurfaceRippleColorKey,
                storageListItemSurfaceRippleSelectedColorKey,
                storageListItemIconTintColorKey,
                storageListItemIconSelectedTintColorKey,
                storageListItemIconBackgroundColorKey,
                storageListItemIconBackgroundSelectedColorKey,
                storageListItemTitleTextColorKey,
                storageListItemTitleSelectedTextColorKey,
                storageListItemSecondaryTextColorKey,
                storageListItemSecondarySelectedTextColorKey,
            )
        )
    }

}