package io.github.excu101.filesystem.fs.attr.time

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class FileTime(
    private val instant: Instant,
) {

    fun toNanos() = instant.nanos

    fun toSeconds() = instant.seconds

    override fun toString(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssZ",
            Locale.ENGLISH
        ).format(TimeUnit.MILLISECONDS.toDays(toSeconds()))
    }

}