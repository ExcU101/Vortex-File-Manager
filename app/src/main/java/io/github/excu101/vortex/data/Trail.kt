package io.github.excu101.vortex.data

import android.os.Parcelable
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.data.storage.StorageItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trail(
    val items: List<TrailItem>,
) : Parcelable {

    val currentSelected: Int
        get() = items.indexOfLast {
            it.isSelected
        }

    val current: TrailItem
        get() = items[currentSelected]

    companion object {

        fun parse(item: StorageItem): Trail {
            return parse(path = item.value)
        }

        fun parse(path: Path): Trail {
            val segments = parseSegments(path)
            val result = mutableListOf<TrailItem>()

            for (index in segments.indices) {
                val item = segments[index]
                val isSelected = item == path

                result.add(
                    TrailItem(
                        value = StorageItem(path),
                        isSelected = isSelected,
                        isLast = index == segments.lastIndex
                    )
                )
            }

            return Trail(
                items = result,
            )
        }

        private fun parseSegments(path: Path): List<Path> {
            val segments = arrayListOf<Path>()
            var cPath = path

            while (true) {
                segments.add(cPath)
                cPath = cPath.parent ?: break
            }

            return segments.reversed()
        }

        private fun MutableList<Path>.toItems() = map { StorageItem(it) }
    }

    fun navigateTo(
        item: StorageItem,
        selected: StorageItem? = null,
        withPrefixChecking: Boolean = true,
    ): Trail {
        val segments = parseSegments(item.value)
        val result = mutableListOf<TrailItem>()

        for (index in segments.indices) {
            val path = segments[index]

            result.add(
                TrailItem(
                    value = StorageItem(path),
                    isSelected = (selected?.value ?: item.value) == path,
                    isLast = index == segments.lastIndex
                )
            )
        }

        return navigationTo(
            newItems = result,
            withPrefixChecking = withPrefixChecking
        )
    }

    fun navigationTo(
        newItems: List<TrailItem>,
        withPrefixChecking: Boolean,
    ): Trail {
        val result = mutableListOf<TrailItem>()

        result.addAll(newItems)
        if (withPrefixChecking) {
            var hasPrefix = true
            for (index in newItems.indices) {
                if (hasPrefix && index < items.size) {
                    if (items[index] != newItems[index]) hasPrefix = false
                }
            }
            if (hasPrefix) {
                for (index in newItems.size until items.size) {
                    result.add(items[index])
                }
            }
        }

        return Trail(items = result)
    }

    fun navigateBack(): Trail {
        return navigationTo(newItems = items - items.last(), true)
    }

}