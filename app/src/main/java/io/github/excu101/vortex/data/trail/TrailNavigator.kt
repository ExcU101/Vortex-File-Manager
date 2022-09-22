package io.github.excu101.vortex.data.trail

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.toPath
import io.github.excu101.vortex.data.PathItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrailNavigator : Iterable<PathItem> {

    val size: Int
        get() = _items.value.size

    private val _items = MutableStateFlow(mutableListOf<PathItem>())
    val items: StateFlow<List<PathItem>>
        get() = _items.asStateFlow()

    private val _selectedIndex = MutableStateFlow(-1)
    val selectedIndex: StateFlow<Int>
        get() = _selectedIndex.asStateFlow()

    private var _selectedItem = MutableStateFlow<PathItem?>(null)
    val selectedItem: PathItem?
        get() = items.value.getOrNull(selectedIndex.value)

    companion object {
        private fun parseSegments(path: Path): MutableList<PathItem> {
            val segments = mutableListOf<PathItem>()
            var cPath = path

            while (true) {
                if (cPath == "/storage/emulated".toPath()) break
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
        val items = _items.value
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
        _items.emit(segments)
        _selectedIndex.emit(selectedIndex)
    }

    suspend fun navigateLeft() {
        navigateTo(segments = take(selectedIndex.value).toMutableList())
    }

    suspend fun navigateRight() {
        navigateTo(segments = take(selectedIndex.value + 2).toMutableList())
    }

    override fun iterator(): Iterator<PathItem> {
        return items.value.iterator()
    }

}