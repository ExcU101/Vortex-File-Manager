package io.github.excu101.vortex.ui.screen.storage

import android.os.Build
import android.os.Environment.getExternalStorageDirectory
import io.github.excu101.filesystem.FileProvider
import io.github.excu101.filesystem.fs.collect
import io.github.excu101.filesystem.fs.observer.PathObservableEventType
import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.operation.observer
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.filesystem.unix.utils.unixCreateFile
import io.github.excu101.manager.ui.theme.ThemeText
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import io.github.excu101.vortex.ui.component.list.adapter.SuperItem
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningFullStorageAccessTitleKey
import io.github.excu101.vortex.ui.component.theme.key.fileListWarningStorageAccessTitleKey
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageScreen.DataResolver
import io.github.excu101.vortex.utils.DispatcherProvider
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import javax.inject.Inject

class StorageStateController @Inject constructor(
    private val provider: StorageProvider,
    private val dispatchers: DispatcherProvider,
) {
    private val _listeners = arrayListOf<Listener>()

    private val scope
        get() = dispatchers.StorageStateUpdateCoroutineScope

    companion object {
        private fun parseSegmentsSpeed(path: Path): ArrayList<PathItem> {
            if (path == getExternalStorageDirectory().asPath()) return arrayListOf(PathItem(path))
            val segments = arrayListOf<PathItem>()
            var cPath = path

            // TODO: Replace with for loop, exclude cPath variable and reverse() method
            while (true) {
                if (cPath == StorageProviderImpl.EXTERNAL_STORAGE.parent) break
                segments += PathItem(cPath)
                cPath = cPath.parent ?: break
            }

            segments.reverse()

            return segments
        }

        private fun parseSegmentsMemory(path: Path): ArrayList<PathItem> {
            if (path == getExternalStorageDirectory().asPath()) return arrayListOf(PathItem(path))

            /**
             *  Memory VS Speed
             *  Next lines describes how much we need to allocate memory to put
             *  path segments into list, so basically we just checking
             *  if segments of path don't get away from
             *  Environment.getExternalStorageDirectory() path
             *  **/
            val size = path.nameCount - 1
            var count = 0
            for (i in size downTo 0) {
                val parent = path.getParentAt(i) ?: break
                if (parent != getExternalStorageDirectory().parentFile?.asPath())
                    count++
                else break
            }

            val segments = ArrayList<PathItem>(count + 1)
            while (count >= 0)
                segments.add(PathItem(path.getParentAt(path.nameCount - count--) ?: path))

            return segments
        }
    }

    var list: List<SuperItem> = emptyList()
        private set
    var trail: List<PathItem> = emptyList()
        private set
    var currentIndex: Int = -1
        private set
    var isLoading: Boolean = true
        private set
    var message: String? = null
        private set

    val current: PathItem?
        get() = trail.getOrNull(currentIndex)

    fun addListener(listener: Listener) {
        _listeners.add(listener)
    }

    fun removeListeners() {
        _listeners.clear()
        provider.closeObservers()
    }

    fun interface Listener {
        fun onUpdateState(state: StorageStateController)
    }

    fun interface EventListener {
        fun onEvent(item: PathItem, event: PathObservableKey.Event)
    }

    fun checkPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (provider.requiresFullStorageAccess()) {
                updateState(
                    message = ThemeText(fileListWarningFullStorageAccessTitleKey)
                )
            }
        } else {
            if (provider.requiresPermissions()) {
                updateState(
                    message = ThemeText(fileListWarningStorageAccessTitleKey)
                )
            }
        }
    }

    fun createObserver(
        item: PathItem,
        vararg type: PathObservableEventType,
        listener: EventListener,
    ) {
        scope.launch {
            provider.registerObserver(item.value, *type).collect { event ->
                listener.onEvent(item, event)
            }
        }
    }

    fun navigate(
        item: PathItem,
        withPrefixChecking: Boolean = true,
    ) {
        if (item == current) {
            updateList(item)
            return
        }

        navigate(
            segments = parseSegmentsSpeed(item.value),
            withPrefixChecking = withPrefixChecking
        )
    }

    private fun updateList(item: PathItem) {
        scope.launch {
            updateState(
                list = DataResolver().run(provider.getItems(item).toList())
            )
        }
    }

    private fun navigate(
        segments: MutableList<PathItem>,
        selectedIndex: Int = segments.lastIndex,
        withPrefixChecking: Boolean = true,
    ) {
        updateState(isLoading = true)

        if (withPrefixChecking) {
            var hasPrefix = true
            for (index in segments.indices) {
                if (hasPrefix && index < trail.size)
                    if (trail[index] != segments[index]) hasPrefix = false
            }
            if (hasPrefix) {
                for (index in segments.size until trail.size)
                    segments.add(trail[index])
            }
        }

        scope.launch {
            updateState(
                currentIndex = selectedIndex,
                isLoading = false,
                trail = segments,
                list = DataResolver().run(provider.getItems(segments[selectedIndex]).toList())
            )
        }
    }

    fun open(
        file: PathItem,
    ) {

    }

    fun createFile(
        path: Path,
        mode: Int,
        flags: Int = Options.Open.CreateNew or Options.Open.Read or
                Options.Open.Write or
                Options.Open.Append,
    ) {
        scope.launch {
            val op = unixCreateFile(
                source = path,
                flags = flags,
                mode = mode,
            )

            FileProvider.runOperation(op, observer(
                onAction = { action ->

                },
                onError = { error ->

                },
                onComplete = {

                }
            ))
        }
    }

    fun navigateBack() {
        updateState(
            isLoading = true
        )

        scope.launch {
            currentIndex--

            updateState(
                list = DataResolver()
                    .run(provider.getItems(trail[currentIndex]).toList()),
                isLoading = false
            )
        }
    }

    private fun updateState(
        list: List<SuperItem> = emptyList(),
        trail: List<PathItem> = this.trail,
        currentIndex: Int = this.currentIndex,
        isLoading: Boolean = false,
        message: String? = null,
    ) {
        this.list = list
        this.trail = trail
        this.currentIndex = currentIndex
        this.isLoading = isLoading
        this.message = message

        notifyListeners()
    }

    private fun notifyListeners() {
        for (listener in _listeners) {
            listener.onUpdateState(state = this@StorageStateController)
        }
    }
}