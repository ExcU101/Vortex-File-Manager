package io.github.excu101.vortex.data.trail

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrailNavigator : Iterable<PathItem> {

    data class Trail(
        val items: List<PathItem> = listOf(),
        val selectedIndex: Int = -1
    ) {
        operator fun component3() = selected
        val selected: PathItem?
            get() = items.getOrNull(selectedIndex)
    }

    data class SelectedItem(
        val index: Int,
        val item: PathItem?,
    )

    private val _trail = MutableStateFlow(Trail())
    val trail: StateFlow<Trail>
        get() = _trail.asStateFlow()

    val items: List<PathItem>
        get() = trail.value.items

    val selectedIndex: Int
        get() = trail.value.selectedIndex

    val selected: PathItem?
        get() = trail.value.selected

    companion object {
        private var isSlicePathEnabled = true

        private fun parseSegments(path: Path): MutableList<PathItem> {
            val segments = mutableListOf<PathItem>()
            var cPath = path

            while (true) {
                if (isSlicePathEnabled) {
                    if (cPath == StorageProviderImpl.EXTERNAL_STORAGE.parent) break
                }
                segments += PathItem(cPath)
                cPath = cPath.parent ?: break
            }

            segments.reverse()
            return segments
        }
    }

    suspend fun navigateTo(
        item: PathItem,
        withPrefixChecking: Boolean = true,
    ) = navigateTo(
        segments = parseSegments(item.value),
        withPrefixChecking = withPrefixChecking
    )

    suspend fun navigateTo(
        segments: MutableList<PathItem>,
        selectedIndex: Int = segments.lastIndex,
        withPrefixChecking: Boolean = true,
    ) {
        val items = items
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
        _trail.emit(Trail(segments, selectedIndex))
    }

    suspend fun navigateLeft() {
        navigateTo(segments = take(selectedIndex).toMutableList())
    }

    suspend fun navigateRight() {
        navigateTo(segments = take(selectedIndex + 2).toMutableList())
    }

    override fun iterator(): Iterator<PathItem> {
        return items.iterator()
    }

}