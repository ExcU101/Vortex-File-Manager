package io.github.excu101.vortex.data

import android.os.Parcelable
import io.github.excu101.filesystem.fs.FileUnit
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.base.utils.logIt
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrailData(
    val items: List<TrailItem>,
) : Parcelable {

    val currentSelected: Int
        get() = items.indexOfFirst {
            it.isSelected
        }

    companion object {

        fun parse(unit: FileUnit): TrailData {
            return parse(path = unit.path)
        }

        fun parse(path: Path): TrailData {
            val segments = parseSegments(path)
            val result = mutableListOf<TrailItem>()

            for (index in segments.indices) {
                val item = segments[index]
                val isSelected = item == path

                result.add(
                    TrailItem(
                        value = FileUnit(item),
                        isSelected = isSelected,
                        isLast = index == segments.lastIndex
                    )
                )
            }

            return TrailData(
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

        private fun MutableList<Path>.toUnits() = map { FileUnit(it) }
    }

    fun navigateTo(
        unit: FileUnit,
        selected: FileUnit? = null,
        withPrefixChecking: Boolean = true,
    ): TrailData {
        val segments = parseSegments(unit.path)
        val result = mutableListOf<TrailItem>()

        for (index in segments.indices) {
            val item = segments[index]

            result.add(
                TrailItem(
                    value = FileUnit(item),
                    isSelected = (selected?.path ?: unit.path) == item,
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
    ): TrailData {
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

        return TrailData(items = result)
    }

}