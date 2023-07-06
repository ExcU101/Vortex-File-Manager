package io.github.excu101.vortex.utils

import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_APP
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_AUDIO
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_IMAGE
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_TEXT
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_VIDEO
import io.github.excu101.vortex.theme.ThemeText

fun MimeType.parseThemeType(): String? {
    return when (type) {
        TYPE_APP -> ThemeText(io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeApplicationKey)
        TYPE_VIDEO -> ThemeText(io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeVideoKey)
        TYPE_IMAGE -> ThemeText(io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeImageKey)
        TYPE_TEXT -> ThemeText(io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeTextKey)
        TYPE_AUDIO -> ThemeText(io.github.excu101.vortex.theme.key.text.storage.item.fileListItemMimeTypeAudioKey)
        else -> null
    }
}