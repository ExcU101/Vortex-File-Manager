package io.github.excu101.filesystem.fs.attr.mimetype

import io.github.excu101.filesystem.fs.utils.*

interface MimeType {
    val extension: String
    val internetMediaType: String
    val isNotStandard: Boolean
    val type: String
    val isVendor: Boolean
    override fun toString(): String

    companion object {
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
                override val type: String
                    get() = subtype.substringBefore('/')

                override val isNotStandard: Boolean
                    get() = subtype.startsWith("x-")
                override val isVendor: Boolean
                    get() = subtype.startsWith("vnd")

                override fun toString(): String = "${extension}/$subtype"
            }
        }
    }
}