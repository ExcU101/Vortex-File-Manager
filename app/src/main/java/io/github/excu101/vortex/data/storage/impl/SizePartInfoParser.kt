package io.github.excu101.vortex.data.storage.impl

import io.github.excu101.vortex.data.storage.PathItemPartInfoParser

fun SizePartInfoParser() = PathItemPartInfoParser { item ->
    if (item.isDirectory) null else item.size.toString()
}