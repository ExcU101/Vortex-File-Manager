package io.github.excu101.vortex.utils

import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.ui.component.theme.key.*

fun MimeType.parseThemeType(): String {
    return when {
        subtype.startsWith(prefix = "application") -> ThemeText(fileListItemMimeTypeApplicationKey)
        subtype.startsWith(prefix = "video") -> ThemeText(fileListItemMimeTypeVideoKey)
        subtype.startsWith(prefix = "image") -> ThemeText(fileListItemMimeTypeImageKey)
        subtype.startsWith(prefix = "text") -> ThemeText(fileListItemMimeTypeTextKey)
        subtype.startsWith(prefix = "audio") -> ThemeText(fileListItemMimeTypeAudioKey)
        else -> ""
    }
}