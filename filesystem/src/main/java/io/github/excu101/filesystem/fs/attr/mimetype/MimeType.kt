package io.github.excu101.filesystem.fs.attr.mimetype

import io.github.excu101.filesystem.fs.utils.*

interface MimeType {
    val type: String
    val subtype: String
    val isNotStandard: Boolean
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
                override val type: String
                    get() = extension
                override val subtype: String
                    get() = subtype
                override val isNotStandard: Boolean
                    get() = subtype.startsWith("x-")
                override val isVendor: Boolean
                    get() = subtype.startsWith("vnd")

                override fun toString(): String = "$type/$subtype"
            }
        }
    }
}