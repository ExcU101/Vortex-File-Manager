package io.github.excu101.vortex.data.storage.impl

import io.github.excu101.vortex.data.storage.PathItemPartInfoParser
import io.github.excu101.vortex.utils.parseThemeType

fun MimeTypeInfoParser() = PathItemPartInfoParser { item ->
    if (item.isDirectory) null else item.mime.parseThemeType()
}