package io.github.excu101.vortex.data

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.room.PrimaryKey
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.BasicAttrs
import io.github.excu101.filesystem.fs.attr.EmptyAttrs
import io.github.excu101.filesystem.fs.attr.containsInode
import io.github.excu101.filesystem.fs.attr.inode
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.attr.time.FileTime
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.parsePath
import io.github.excu101.filesystem.unix.attr.posix.PosixAttrs
import io.github.excu101.filesystem.unix.attr.posix.PosixPermission
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.data.storage.PathItemContentParsers
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl.Companion.EXTERNAL_STORAGE
import io.github.excu101.vortex.service.utils.PathParceler
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.holder.ViewHolderFactory
import io.github.excu101.vortex.ui.component.storage.standard.linear.StandardStorageLinearCell
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemInfoSeparatorKey
import io.github.excu101.vortex.ui.icon.Icons
import io.github.excu101.vortex.utils.StorageItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import java.io.File

@Parcelize
class PathItem(
    override val value: @WriteWith<PathParceler> Path,
    override val tags: List<Tag> = emptyList()
) : Item<Path>, Comparable<PathItem>, Parcelable, BookmarkState, TagOwner {

    constructor(path: String) : this(value = parsePath(path))

    constructor(path: File) : this(path = path.path)

    val path: String
        get() = value.toString()

    val scheme: String
        get() = value.scheme

    val uri: Uri
        get() = Uri.fromFile(File(path))

    @IgnoredOnParcel
    @PrimaryKey
    override val id: Long = hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.StorageItem

    val containsParent: Boolean
        get() = parent != null

    val parent: PathItem?
        get() = value.parent?.let(::PathItem)

    @IgnoredOnParcel
    private val attrs = try {
        FileProvider.readAttrs<BasicAttrs>(value)
    } catch (exception: Exception) {
        EmptyAttrs
    }

    override val bookmarkExists
        get() = this in StorageBookmarkProvider.items

    val perms: Set<PosixPermission>
        get() = (attrs as? PosixAttrs)?.perms ?: setOf()

    val owner: String?
        get() = (attrs as? PosixAttrs)?.owner

    val group: String?
        get() = (attrs as? PosixAttrs)?.group

    @IgnoredOnParcel
    val name: String = if (value == EXTERNAL_STORAGE) {
        "Internal storage"
    } else {
        value.getName().toString()
    }

    @IgnoredOnParcel
    val mime: MimeType = MimeType.fromName(name)

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

    val inode: Long?
        get() = if (!attrs.containsInode()) null else attrs.inode

    @IgnoredOnParcel
    val icon: Drawable?
        get() = when {
            isDirectory -> Icons.Rounded.Folder
            isLink -> Icons.Rounded.Link
            else -> when (mime.type) {
                "video" -> Icons.Rounded.Video
                "image" -> Icons.Rounded.Image
                "audio" -> Icons.Rounded.Audio
                "text" -> Icons.Rounded.Text
                else -> Icons.Rounded.File
            }
        }

    @IgnoredOnParcel
    val info: String = resolveInfo()

    // TODO: (if needed) do cache
    private fun resolveInfo(): String {
        var result = ""

        PathItemContentParsers.forEach { parser ->
            parser.getPartInfo(item = this)?.let { part ->
                if (result.isNotEmpty() && part.isNotEmpty()) {
                    result += ThemeText(fileListItemInfoSeparatorKey)
                }
                result += part
            }
        }

        return result
    }

    override operator fun compareTo(other: PathItem): Int {
        var result = 0

        result += isDirectory compareTo other.isDirectory
        result += name compareTo other.name

        return result
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PathItem
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        val result = value.hashCode()
        // For checking is the item same as other or updated item (experimental)
//        result = 31 * result + attrs.hashCode()
//        result = 31 * result + name.hashCode()
//        result = 31 * result + mime.hashCode()
//        result = 31 * result + isHidden.hashCode()
//        result = 31 * result + isAbsolute.hashCode()
//        result = 31 * result + isDirectory.hashCode()
//        result = 31 * result + isFile.hashCode()
//        result = 31 * result + isLink.hashCode()
//        result = 31 * result + isOther.hashCode()
        return result
    }

    companion object : ViewHolderFactory<PathItem> {
        override fun produceView(parent: ViewGroup): View {
            return StandardStorageLinearCell(parent.context)
        }
    }

}