package io.github.excu101.vortex.utils

import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.*

fun MimeType.parseThemeType(): String {
    return when (type) {
        "application" -> ThemeText(fileListItemMimeTypeApplicationKey)
        "video" -> ThemeText(fileListItemMimeTypeVideoKey)
        "image" -> ThemeText(fileListItemMimeTypeImageKey)
        "text" -> ThemeText(fileListItemMimeTypeTextKey)
        "audio" -> ThemeText(fileListItemMimeTypeAudioKey)
        else -> ""
    }
}