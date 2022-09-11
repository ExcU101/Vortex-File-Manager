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
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolder
import io.github.excu101.vortex.ui.component.storage.standard.StorageItemView
import io.github.excu101.vortex.ui.component.storage.standard.StorageItemViewHolder
import io.github.excu101.vortex.utils.PathParceler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
class PathItem(
    override val value: @WriteWith<PathParceler> Path,
) : Item<Path>, Parcelable {

    val path: String
        get() = value.toString()

    override val id: Long
        get() = hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.STORAGE

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

    val isHidden: Boolean
        get() = value.isHidden

    val isAbsolute: Boolean
        get() = value.isAbsolute

    val isEmpty: Boolean
        get() = value.isEmpty

    val isDirectory: Boolean
        get() = attrs.isDirectory

    val isFile: Boolean
        get() = attrs.isFile

    val isLink: Boolean
        get() = attrs.isLink

    val isOther: Boolean
        get() = attrs.isOther

    val lastModifiedTime: FileTime
        get() = attrs.lastModifiedTime

    val lastAccessTime: FileTime
        get() = attrs.lastAccessTime

    val creationTime: FileTime
        get() = attrs.creationTime

    val size: Size
        get() = attrs.size

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