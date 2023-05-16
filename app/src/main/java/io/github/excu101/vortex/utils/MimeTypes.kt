package io.github.excu101.vortex.utils

import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_APP
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_AUDIO
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_IMAGE
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_TEXT
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_VIDEO
import io.github.excu101.manager.ui.theme.ThemeText
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.*

fun MimeType.parseThemeType(): String? {
    return when (type) {
        TYPE_APP -> ThemeText(fileListItemMimeTypeApplicationKey)
        TYPE_VIDEO -> ThemeText(fileListItemMimeTypeVideoKey)
        TYPE_IMAGE -> ThemeText(fileListItemMimeTypeImageKey)
        TYPE_TEXT -> ThemeText(fileListItemMimeTypeTextKey)
        TYPE_AUDIO -> ThemeText(fileListItemMimeTypeAudioKey)
        else -> null
    }
}