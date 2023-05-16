package io.github.excu101.filesystem.fs.attr.mimetype

import io.github.excu101.filesystem.fs.utils.*

interface MimeType {
    val extension: String
    val internetMediaType: String
    val isNotStandard: Boolean
    val type: Int
    val isVendor: Boolean
    override fun toString(): String

    companion object {
        const val TYPE_EMPTY = -1
        const val TYPE_APP = 0
        const val TYPE_TEXT = 1
        const val TYPE_AUDIO = 2
        const val TYPE_IMAGE = 3
        const val TYPE_VIDEO = 4

        fun fromName(name: String): MimeType {
            if (name.length <= 1) return EmptyMimeType

            return from(name.substringAfterLast(".", name))
        }

        fun from(extension: String): MimeType {
            val subtype: String = videoMimeTypes[extension] ?: imageMimeTypes[extension]
            ?: applicationMimeTypes[extension] ?: audioMimeTypes[extension]
            ?: textMimeType[extension] ?: return EmptyMimeType

            return object : MimeType {
                override val extension: String
                    get() = extension
                override val internetMediaType: String
                    get() = subtype
                override val type: Int
                    get() = when (subtype.substringBefore('/')) {
                        "application" -> TYPE_APP
                        "video" -> TYPE_VIDEO
                        "audio" -> TYPE_AUDIO
                        "text" -> TYPE_TEXT
                        else -> TYPE_EMPTY
                    }

                override val isNotStandard: Boolean
                    get() = subtype.startsWith("x-")
                override val isVendor: Boolean
                    get() = subtype.startsWith("vnd")

                override fun toString(): String = "${extension}/$subtype"
            }
        }
    }
}