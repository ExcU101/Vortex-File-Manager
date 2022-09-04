package io.github.excu101.vortex.data

import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.EmptyAttrs
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.attr.time.FileTime
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.toPath
import io.github.excu101.filesystem.unix.UnixFileSystem
import io.github.excu101.filesystem.unix.UnixFileSystemProvider
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.adapter.Item
import io.github.excu101.vortex.ui.component.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.storage.standard.StorageItemView
import io.github.excu101.vortex.ui.component.storage.standard.StorageItemViewHolder
import io.github.excu101.vortex.utils.PathParceler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.lang.Exception

@Parcelize
class PathItem(
    override val value: @WriteWith<PathParceler> Path,
) : Item<Path>, Parcelable {

    @IgnoredOnParcel
    val path: String = value.toString()

    @IgnoredOnParcel
    override val id: Long = hashCode().toLong()

    @IgnoredOnParcel
    override val type: Int = ItemViewTypes.STORAGE

    @IgnoredOnParcel
    private val attrs = try {
        FileProvider.readAttrs<BasicAttrs>(value)
    } catch (exception: Exception) {
        EmptyAttrs
    }

    @IgnoredOnParcel
    val name: String = value.getName().toString()

    @IgnoredOnParcel
    val mimeType: MimeType = MimeType.fromName(name)

    @IgnoredOnParcel
    val isHidden: Boolean = value.isHidden

    @IgnoredOnParcel
    val isAbsolute: Boolean = value.isAbsolute

    @IgnoredOnParcel
    val isEmpty: Boolean = value.isEmpty

    @IgnoredOnParcel
    val isDirectory: Boolean = attrs.isDirectory

    @IgnoredOnParcel
    val isFile: Boolean = attrs.isFile

    @IgnoredOnParcel
    val isLink: Boolean = attrs.isLink

    @IgnoredOnParcel
    val isOther: Boolean = attrs.isOther

    @IgnoredOnParcel
    val lastModifiedTime: FileTime = attrs.lastModifiedTime

    @IgnoredOnParcel
    val lastAccessTime: FileTime = attrs.lastAccessTime

    @IgnoredOnParcel
    val creationTime: FileTime = attrs.creationTime

    @IgnoredOnParcel
    val size: Size = attrs.size

    override fun hashCode(): Int {
        return value.hashCode()
    }

    suspend fun list(): List<PathItem> = withContext(IO) {
        FileProvider.newDirStream(path = value).use { stream ->
            stream.map(::PathItem)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PathItem
        if (value != other.value) return false

        return true
    }

    companion object : ViewHolderFactory<PathItem> {
        override fun produceView(parent: ViewGroup): View {
            return StorageItemView(parent.context)
        }

        override fun produceViewHolder(child: View): ViewHolder<PathItem> {
            return StorageItemViewHolder(child as StorageItemView)
        }
    }

}