package io.github.excu101.vortex.utils

import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.ui.component.theme.key.*

fun Size.convertToThemeText(): String {
    return convertSi()
}

private fun Size.convertSi(): String {
    var result = ""
    toSiType()
    when (name) {
        "YB" -> ThemeText(fileListItemSizeYiBKey)
        "ZB" -> ThemeText(fileListItemSizeZiBKey)
        "EB" -> ThemeText(fileListItemSizeEiBKey)
        "PB" -> ThemeText(fileListItemSizePiBKey)
        "TB" -> ThemeText(fileListItemSizeTiBKey)
        "GB" -> ThemeText(fileListItemSizeGiBKey)
        "MB" -> ThemeText(fileListItemSizeMiBKey)
        "KB" -> ThemeText(fileListItemSizeKiBKey)
        else -> ThemeText(fileListItemSizeBKey)
    }
    result += memory
    result += name
    return result
}