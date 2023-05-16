package io.github.excu101.vortex.ui.screen.storage

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.Environment
import android.view.View
import io.github.excu101.filesystem.fs.observer.PathObservableKey
import io.github.excu101.filesystem.fs.observer.event.CreateEvent
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.component
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.navigation.page.TitledPageController
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.animation.fade
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.SuperItem
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewListener
import io.github.excu101.vortex.ui.component.list.adapter.listener.ItemViewLongListener
import io.github.excu101.vortex.ui.component.list.adapter.register
import io.github.excu101.vortex.ui.component.list.adapter.registerLong
import io.github.excu101.vortex.ui.component.list.adapter.with
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.menu.MenuActionListener
import io.github.excu101.vortex.ui.component.parcelable
import io.github.excu101.vortex.ui.component.trail.TrailAdapter
import io.github.excu101.vortex.ui.navigation.RecyclerViewPageController
import io.github.excu101.vortex.ui.navigation.Routes
import io.github.excu101.vortex.ui.screen.storage.StorageStateController.EventListener
import io.github.excu101.vortex.ui.screen.storage.StorageStateController.Listener
import io.github.excu101.vortex.ui.screen.storage.page.list.StorageListPageBinding
import io.github.excu101.vortex.utils.StorageItem

private const val CURRENT_PATH_ITEM = "current_path_item"

class StoragePageController(
    context: Context,
) : RecyclerViewPageController<SuperItem>(
    context, ItemViewTypes.TextItem with TextItem,
    ItemViewTypes.StorageItem with PathItem
), TitledPageController, Listener,MenuActionListener,OnSharedPreferenceChangeListener,
EventListener {

    sealed interface Args {
        class NavDestination(val item: PathItem) : Args
        class CreateFile(val path: Path, val mode: Int, val flags: Int) : Args
    }

    override fun <A> setArgs(args: A?) {
        if (args is Args) {
            when (args) {
                is Args.CreateFile -> stateController?.createFile(
                    path = args.path,
                    mode = args.mode,
                    flags = args.flags
                )

                is Args.NavDestination -> stateController?.navigate(args.item)
            }
        }
    }

    override val title: String?
    get() = stateController?.current?.name

    private var stateController: StorageStateController? = null

    private var binding: StorageListPageBinding? = null

    private val trailAdapter: TrailAdapter = TrailAdapter()

    override fun getNavigationRoute(): Int = Routes.Storage

    private fun checkBinding(context: Context): StorageListPageBinding {
        if (binding == null) {
            binding = StorageListPageBinding(context)
        }

        return binding!!
    }

    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {

    }

    override fun onMenuActionCall(action: MenuAction): Boolean {
        return when (action.id) {
            ViewIds.Storage.Menu.DeleteId -> {

                true
            }

            ViewIds.Storage.Menu.MoveId -> {

                true
            }

            else -> false
        }
    }

    override fun onEvent(item: PathItem, event: PathObservableKey.Event) {
        when (event) {
            is CreateEvent -> {
                if (stateController?.current == item)
                    adapter.add(item)
            }

            else -> {

            }
        }
    }

    override fun onCreateView(context: Context): View {
        stateController = context.component.controller

        val binding = checkBinding(context)

        with(binding) {
            trail.adapter = trailAdapter
            trail.itemAnimator = null
            list.adapter = adapter
            list.itemAnimator = null

            trailAdapter.register(this@StoragePageController)
            trailAdapter.registerLong(this@StoragePageController)

            adapter.register(this@StoragePageController)
            adapter.registerLong(this@StoragePageController)

            stateController!!.addListener(this@StoragePageController)
            stateController!!.addListener { state ->
                adapter.replace(state.list)
                trailAdapter.replace(state.trail)
                trailAdapter.updateSelected(state.currentIndex)

                if (state.currentIndex >= 0)
                    trail.smoothScrollToPosition(state.currentIndex)

                list.fade(isOut = state.isLoading)
                loading.fade(isOut = !state.isLoading)
                warning.fade(isOut = state.message == null)
            }
        }

        stateController!!.navigate(PathItem(Environment.getExternalStorageDirectory().asPath()))

        return binding.root
    }

    override fun onUpdateState(state: StorageStateController) {

    }

    private fun createPrefixBundle(prefix: String): String {
        return prefix + CURRENT_PATH_ITEM
    }

    override fun onRestoreInstance(
        bundle: Bundle,
        prefix: String,
    ): Boolean = bundle.parcelable<PathItem>(createPrefixBundle(prefix))?.let { current ->
        stateController?.navigate(current)
        true
    } ?: super.onRestoreInstance(bundle, prefix)

    override fun onSaveInstance(
        bundle: Bundle,
        prefix: String,
    ): Boolean {
        if (stateController?.trail?.isEmpty() == true) return false
        if ((stateController?.currentIndex ?: -1) < 0) return false

        bundle.putParcelable(
            createPrefixBundle(prefix),
            stateController?.current
        )

        return true
    }

    override fun onClick(view: View, item: SuperItem, position: Int) {
        when (item) {
            is PathItem -> {
                if (item.isDirectory)
                    stateController?.navigate(item)
                else
                    stateController?.open(item)
            }
        }
    }

    override fun onLongClick(view: View, item: Item<*>, position: Int): Boolean {
        when (item) {
            is PathItem -> {

            }
        }
        return false
    }

    override fun onBackActivated(): Boolean {
        if (stateController?.current?.value != Environment.getExternalStorageDirectory().asPath()) {
            stateController?.navigateBack()
            return true
        }

        return super.onBackActivated()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isDestroyed) {
            stateController?.removeListeners()
        }
    }

    override fun onFocus() {
        stateController?.checkPerms()
    }

    override fun onPrepare() {

    }

    override fun onHide() {

    }

}