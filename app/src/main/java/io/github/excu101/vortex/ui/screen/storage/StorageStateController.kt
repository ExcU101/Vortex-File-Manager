package io.github.excu101.vortex.ui.screen.storage

import android.net.Uri
import android.os.Build
import android.os.Environment.getExternalStorageDirectory
import androidx.annotation.RequiresApi
import io.github.excu101.filesystem.fs.collect
import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.operation.option.Options
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.vortex.base.utils.logIt
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.data.storage.PathItemSorters
import io.github.excu101.vortex.provider.storage.AndroidStorageHelper
import io.github.excu101.vortex.provider.storage.Filter
import io.github.excu101.vortex.provider.storage.StorageProvider
import io.github.excu101.vortex.provider.storage.impl.ListResultParser
import io.github.excu101.vortex.provider.storage.impl.StorageProviderImpl
import io.github.excu101.vortex.ui.component.list.adapter.SuperItem
import io.github.excu101.vortex.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class StorageStateController @Inject constructor(
    private val provider: StorageProvider,
    val helper: AndroidStorageHelper,
    dispatchers: DispatcherProvider,
) : StateController {
    private val listeners = arrayListOf<Listener>()

    override val scope: CoroutineScope = dispatchers.StorageStateUpdateCoroutineScope

    companion object {
        const val FLAG_EMPTY = -1
        const val FLAG_LOADING = 0
        const val FLAG_MESSAGE_EMPTY_DIR = 1

        @RequiresApi(30)
        const val FLAG_MESSAGE_RESTRICTED_DIR = 2
        const val FLAG_MESSAGE_REQUIRES_STORAGE_ACCESS = 3

        @RequiresApi(30)
        const val FLAG_MESSAGE_REQUIRES_FULL_STORAGE_ACCESS = 4

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

    private val parser = ListResultParser()
    private val sorter = PathItemSorters.Name
    private val filter = Filter<PathItem> { true }

    var list: List<SuperItem> = emptyList()
        private set
    var trail: List<PathItem> = emptyList()
        private set
    var currentIndex: Int = -1
        private set
    var flag: Int = FLAG_MESSAGE_EMPTY_DIR
        private set

    // E.G. SnackBar stuff
    var notification: String? = null
        private set

    val current: PathItem?
        get() = trail.getOrNull(currentIndex)

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun interface Listener {
        fun onUpdateState(state: StorageStateController)
    }

    fun checkPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (helper.requiresFullStorageAccess()) return updateState(
                flag = FLAG_MESSAGE_REQUIRES_FULL_STORAGE_ACCESS,
            )
        } else {
            if (helper.requiresPermissions()) return updateState(
                flag = FLAG_MESSAGE_REQUIRES_STORAGE_ACCESS,
            )
        }

        navigate(PathItem(getExternalStorageDirectory()))
    }

    fun createObserver(
        item: PathItem,
        types: Int,
        onEvent: (PathObservableKey.Event) -> Unit,
    ) = intent {
        provider.registerObserver(item.value, types).collect { event ->
            onEvent(event)
        }
    }

    fun navigate(
        item: PathItem,
        withPrefixChecking: Boolean = true,
    ) {
        if (!provider.hasObserver(item.value)) {
            if (item == current) {
                updateList(item)
                return
            }
        }

        navigate(
            segments = parseSegmentsSpeed(item.value),
            withPrefixChecking = withPrefixChecking
        )
    }

    private fun updateList(item: PathItem) = intent {
        updateState(
            list = parser.parse(provider.getItems(item).toList())
        )
    }


    private fun navigate(
        segments: MutableList<PathItem>,
        selectedIndex: Int = segments.lastIndex,
        withPrefixChecking: Boolean = true,
    ) = intent {
        updateState(flag = FLAG_LOADING)

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (helper.checkRestrictedPath(item = segments[selectedIndex])) {
                if (!helper.containsPersist(segments[selectedIndex].name)) {
                    return@intent updateState(
                        currentIndex = selectedIndex,
                        trail = segments,
                        flag = FLAG_MESSAGE_RESTRICTED_DIR
                    )
                } else {
                    navigateDocument(helper.buildDirectoryUri(segments[selectedIndex].name))
                }
            }
        }

        val list = provider.getItems(segments[selectedIndex]).toList()

        if (list.isEmpty()) updateState(
            currentIndex = selectedIndex,
            trail = segments,
            flag = FLAG_MESSAGE_EMPTY_DIR
        )
        else updateState(
            currentIndex = selectedIndex,
            trail = segments,
            list = parser.parse(list.sortedWith(sorter))
        )
    }

    @RequiresApi(30)
    fun navigateDocument(tree: Uri) {
        helper.readDocumentTree(uri = tree).forEach { document ->
            document.uri.toString().logIt()
        }
    }

    fun createFile(
        path: Path,
        mode: Int,
        flags: Int = Options.Open.CreateNew or Options.Open.Read or
                Options.Open.Write or
                Options.Open.Append,
    ) = intent {

    }

    fun navigateBack() = intent {
        updateState(flag = FLAG_LOADING)

        currentIndex--

        updateState(
            list = parser.parse(getItems(trail[currentIndex])),
        )
    }

    private suspend fun getItems(item: PathItem): List<PathItem> {
        return provider.getItems(item).filter(filter::get).toList()
    }

    private fun updateState(
        list: List<SuperItem> = this.list,
        trail: List<PathItem> = this.trail,
        flag: Int = FLAG_EMPTY,
        currentIndex: Int = this.currentIndex,
        notification: String? = null,
    ) {
        this.list = list
        this.trail = trail
        this.flag = flag
        this.currentIndex = currentIndex
        this.notification = notification

        notifyListeners()
    }

    private fun notifyListeners() = intent {
        for (listener in listeners) {
            listener.onUpdateState(state = this@StorageStateController)
        }
    }

    override fun onDestroy() {
        provider.closeObservers()
        listeners.clear()
        scope.cancel()
    }
}