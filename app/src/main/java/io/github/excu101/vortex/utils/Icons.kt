package io.github.excu101.vortex.utils

import android.graphics.drawable.Drawable
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_AUDIO
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_IMAGE
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_TEXT
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType.Companion.TYPE_VIDEO
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.ui.icon.Icons

val PathItem.icon: Drawable?
    get() = when {
        isDirectory -> Icons.Rounded.Folder
        isLink -> Icons.Rounded.Link
        else -> when (mime.type) {
            TYPE_VIDEO -> Icons.Rounded.Video
            TYPE_IMAGE -> Icons.Rounded.Image
            TYPE_AUDIO -> Icons.Rounded.Audio
            TYPE_TEXT -> Icons.Rounded.Text
            else -> Icons.Rounded.File
        }
    }