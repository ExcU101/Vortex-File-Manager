package io.github.excu101.vortex.data.trail

import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrailNavigator : Iterable<PathItem> {

    data class SelectedItem(
        val index: Int,
        val item: PathItem?,
    )

    val size: Int
        get() = _items.value.size

    private val _items = MutableStateFlow(mutableListOf<PathItem>())
    val items: StateFlow<List<PathItem>>
        get() = _items.asStateFlow()

    private val _selection = MutableStateFlow(SelectedItem(
        index = -1,
        item = null
    ))
    val selection: StateFlow<SelectedItem>
        get() = _selection.asStateFlow()

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
        _selection.emit(SelectedItem(selectedIndex, segments[selectedIndex]))
    }

    suspend fun navigateLeft() {
        navigateTo(segments = take(selection.value.index).toMutableList())
    }

    suspend fun navigateRight() {
        navigateTo(segments = take(selection.value.index + 2).toMutableList())
    }

    override fun iterator(): Iterator<PathItem> {
        return items.value.iterator()
    }

}