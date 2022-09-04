package io.github.excu101.vortex.data.trail

import android.os.Parcelable
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.base.utils.logIt
import io.github.excu101.vortex.data.PathItem
import kotlinx.parcelize.Parcelize

@Parcelize
class TrailNavigator(
    val items: List<PathItem> = listOf(),
    val selectedIndex: Int = items.lastIndex,
) : Iterable<PathItem>, Parcelable {

    val selectedItem: PathItem?
        get() = items.getOrNull(selectedIndex)

    companion object {
        private fun parseSegments(path: Path): MutableList<PathItem> {
            val segments = mutableListOf<PathItem>()
            var cPath = path

            while (true) {
                segments += PathItem(cPath)
                cPath = cPath.parent ?: break
            }

            segments.reverse()
            return segments
        }
    }

    fun navigateTo(
        item: PathItem,
        withPrefixChecking: Boolean = true,
    ) = navigateTo(
        segments = parseSegments(item.value),
        withPrefixChecking = withPrefixChecking
    )

    fun navigateTo(
        segments: MutableList<PathItem>,
        selectedIndex: Int = segments.lastIndex,
        withPrefixChecking: Boolean = true,
    ): TrailNavigator {
        if (withPrefixChecking) {
            var hasPrefix = true
            for (index in segments.indices) {
                if (hasPrefix && index < items.size) {
                    if (items[index] != segments[index]) hasPrefix = false
                }
            }
            if (hasPrefix) {
                for (index in segments.size until items.size) {
                    segments.add(items[index])
                }
            }
        }
        return TrailNavigator(segments, selectedIndex)
    }

    fun navigateUp(): TrailNavigator {
        return navigateTo(segments = take(selectedIndex).toMutableList())
    }

    override fun iterator(): Iterator<PathItem> {
        return items.iterator()
    }

}