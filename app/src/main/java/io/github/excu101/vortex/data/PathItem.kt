package io.github.excu101.vortex.data

import android.net.Uri
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.attr.EmptyAttrs
import io.github.excu101.filesystem.fs.attr.containsInode
import io.github.excu101.filesystem.fs.attr.inode
import io.github.excu101.filesystem.fs.attr.mimetype.MimeType
import io.github.excu101.filesystem.fs.attr.size.Size
import io.github.excu101.filesystem.fs.attr.time.FileTime
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.unix.attr.posix.PosixAttrs
import io.github.excu101.filesystem.unix.attr.posix.PosixPermission
import io.github.excu101.pluginsystem.ui.theme.ThemeText
import io.github.excu101.vortex.R
import io.github.excu101.vortex.data.storage.PathItemContentParsers
import io.github.excu101.vortex.provider.storage.StorageBookmarkProvider
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl.Companion.EXTERNAL_STORAGE
import io.github.excu101.vortex.service.utils.PathParceler
import io.github.excu101.vortex.service.utils.toVortexServiceUri
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ViewHolderFactory
import io.github.excu101.vortex.ui.component.storage.standard.linear.StandardStorageLinearCell
import io.github.excu101.vortex.ui.component.theme.key.text.storage.item.fileListItemInfoSeparatorKey
import io.github.excu101.vortex.utils.parseThemeType
import io.github.excu101.vortex.utils.storageItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith

@Parcelize
class PathItem(
    override val value: @WriteWith<PathParceler> Path,
) : Item<Path>, Comparable<PathItem>, Parcelable, BookmarkState {

    val path: String
        get() = value.toString()

    val scheme: String
        get() = value.scheme

    val uri: Uri
        get() = value.toVortexServiceUri()

    override val id: Long
        get() = hashCode().toLong()

    override val type: Int
        get() = ItemViewTypes.storageItem

    val parent: PathItem? = value.parent?.let(::PathItem)

    @IgnoredOnParcel
    private val attrs = try {
        FileProvider.readAttrs<PosixAttrs>(value)
    } catch (exception: Exception) {
        EmptyAttrs
    }

    override fun isBookmarkExisting(): Boolean {
        return this in StorageBookmarkProvider.items
    }

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
    val icon: Int
        @DrawableRes
        get() = when {
            isDirectory -> R.drawable.ic_folder_24
            isLink -> R.drawable.ic_link_24
            else -> when (mime.type) {
                "video" -> R.drawable.ic_video_24
                "image" -> R.drawable.ic_image_24
                "audio" -> R.drawable.ic_audio_24
                "text" -> R.drawable.ic_text_24
                else -> R.drawable.ic_file_24
            }
        }

    @IgnoredOnParcel
    val info: String
        get() = resolveInfo()

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

    override fun hashCode(): Int {
        return value.hashCode()
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
            return StandardStorageLinearCell(parent.context)
        }
    }

}