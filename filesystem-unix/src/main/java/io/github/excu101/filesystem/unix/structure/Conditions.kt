package io.github.excu101.filesystem.unix.structure

fun ByteArray.isCurrentDirectory() = size == 1 && contentEquals(byteArrayOf('.'.code.toByte()))

fun ByteArray.isParentDirectory() = size == 2 && contentEquals("..".toByteArray())