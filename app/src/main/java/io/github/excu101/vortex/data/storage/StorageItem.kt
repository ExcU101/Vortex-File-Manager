package io.github.excu101.vortex.data.storage

import android.os.Parcel
import android.os.Parcelable
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.FileProvider.parsePath
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.EmptyAttrs
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.attr.time.FileTime
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.attr.UnixAttributes
import io.github.excu101.vortex.provider.StorageProvider
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.adapter.Item
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class StorageItem(
    override val value: Path,
) : Item<Path>, Parcelable {

    companion object : Parceler<StorageItem> {
        override fun create(parcel: Parcel): StorageItem {
            return StorageItem(parsePath(input = parcel.readString() ?: ""))
        }

        override fun StorageItem.write(parcel: Parcel, flags: Int) {
            parcel.writeString(value.toString())
        }
    }

    val attrs: BasicAttrs
        get() = if (value == StorageProvider.ANDROID_OBB || value == StorageProvider.ANDROID_DATA)
            FileProvider.readAttrs<EmptyAttrs>(value)
        else
            FileProvider.readAttrs<UnixAttributes>(value)

    val name: String
        get() = value.getName().toString()

    val parent: StorageItem?
        get() = value.parent?.let(::StorageItem)

    val isDirectory: Boolean
        get() = attrs.isDirectory

    val isLink: Boolean
        get() = attrs.isLink

    val isFile: Boolean
        get() = attrs.isFile

    val creationTime: FileTime
        get() = attrs.creationTime

    val lastModifiedTime: FileTime
        get() = attrs.lastModifiedTime

    val lastAccessTime: FileTime
        get() = attrs.lastAccessTime

    val size: Size
        get() = attrs.size

    val mimeType: MimeType
        get() = MimeType.fromName(name)

    override val id: Long
        get() = value.hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.STORAGE

}