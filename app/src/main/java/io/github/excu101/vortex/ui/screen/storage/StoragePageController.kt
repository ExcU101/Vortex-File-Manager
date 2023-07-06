package io.github.excu101.vortex.ui.screen.storage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.view.Gravity.CENTER
import android.view.View
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.excu101.filesystem.fs.path.Path
import io.github.excu101.filesystem.fs.utils.asPath
import io.github.excu101.vortex.theme.FormatterThemeText
import io.github.excu101.vortex.theme.ThemeColor
import io.github.excu101.vortex.theme.ThemeText
import io.github.excu101.vortex.theme.widget.ThemeFrameLayout
import io.github.excu101.vortex.ViewIds
import io.github.excu101.vortex.component
import io.github.excu101.vortex.data.Counter
import io.github.excu101.vortex.data.PathItem
import io.github.excu101.vortex.navigation.NavPageController
import io.github.excu101.vortex.provider.StorageActionContentProvider
import io.github.excu101.vortex.provider.contract.Contracts
import io.github.excu101.vortex.ui.component.ItemViewTypes
import io.github.excu101.vortex.ui.component.drawer.ItemBottomDrawerFragment
import io.github.excu101.vortex.ui.component.drawer.TaskBottomDrawer
import io.github.excu101.vortex.ui.component.info.InfoView
import io.github.excu101.vortex.ui.component.item.drawer.DrawerItem
import io.github.excu101.vortex.ui.component.item.text.TextItem
import io.github.excu101.vortex.ui.component.list.adapter.Item
import io.github.excu101.vortex.ui.component.list.adapter.ItemAdapter
import io.github.excu101.vortex.ui.component.list.adapter.SuperItem
import io.github.excu101.vortex.ui.component.list.adapter.register
import io.github.excu101.vortex.ui.component.list.adapter.registerLong
import io.github.excu101.vortex.ui.component.list.adapter.with
import io.github.excu101.vortex.ui.component.loading.LoadingView
import io.github.excu101.vortex.ui.component.menu.MenuAction
import io.github.excu101.vortex.ui.component.parcelable
import io.github.excu101.vortex.theme.key.fileListWarningEmptyTitleKey
import io.github.excu101.vortex.theme.key.fileListWarningFullStorageAccessActionTitleKey
import io.github.excu101.vortex.theme.key.fileListWarningFullStorageAccessTitleKey
import io.github.excu101.vortex.theme.key.fileListWarningStorageAccessActionTitleKey
import io.github.excu101.vortex.theme.key.fileListWarningStorageAccessTitleKey
import io.github.excu101.vortex.theme.key.storageListBackgroundColorKey
import io.github.excu101.vortex.ui.component.trail.TrailAdapter
import io.github.excu101.vortex.ui.component.trail.TrailListView
import io.github.excu101.vortex.ui.icon.Icons
import io.github.excu101.vortex.ui.navigation.ListPageController
import io.github.excu101.vortex.ui.navigation.NavEntryPageController
import io.github.excu101.vortex.ui.navigation.Navigation
import io.github.excu101.vortex.ui.navigation.VortexPageController
import io.github.excu101.vortex.ui.screen.navigation.NavigationActivity
import io.github.excu101.vortex.ui.screen.storage.Actions.BarActions
import io.github.excu101.vortex.ui.screen.storage.StorageStateController.Companion.FLAG_EMPTY
import io.github.excu101.vortex.ui.screen.storage.StorageStateController.Companion.FLAG_LOADING
import io.github.excu101.vortex.ui.screen.storage.StorageStateController.Companion.FLAG_MESSAGE_EMPTY_DIR
import io.github.excu101.vortex.ui.screen.storage.StorageStateController.Companion.FLAG_MESSAGE_REQUIRES_FULL_STORAGE_ACCESS
import io.github.excu101.vortex.ui.screen.storage.StorageStateController.Companion.FLAG_MESSAGE_REQUIRES_STORAGE_ACCESS
import io.github.excu101.vortex.ui.screen.storage.StorageStateController.Companion.FLAG_MESSAGE_RESTRICTED_DIR
import io.github.excu101.vortex.ui.screen.storage.StorageStateController.Listener
import io.github.excu101.vortex.utils.StorageItem
import javax.inject.Inject

private const val CURRENT_PATH_ITEM = "current_path_item"

class StoragePageController @Inject constructor(
    private val context: NavigationActivity,
) : VortexPageController(
    context,
), ListPageController<SuperItem>, NavEntryPageController, Listener,
    OnSharedPreferenceChangeListener {

    sealed interface Args {
        class NavDestination(val item: PathItem) : Args
        class CreateFile(val path: Path, val mode: Int, val flags: Int) : Args
    }

    override fun <A> setArgs(args: A?) {
        when (args) {
            is Args.CreateFile -> stateController?.createFile(
                path = args.path,
                mode = args.mode,
                flags = args.flags
            )

            is Args.NavDestination -> stateController?.navigate(args.item)
        }
    }

    override val isInSelectionMode: Boolean
        get() = adapter.getSelectedCount() > 0

    override val title: String?
        get() = if (isInSelectionMode) adapter.getSelectedCount().toString() else null

    private var stateController: StorageStateController? = null

    private var container: io.github.excu101.vortex.theme.widget.ThemeFrameLayout? = null
    private var recycler: RecyclerView? = null

    private var trail: TrailListView? = null
    private var loading: LoadingView? = null
    private var info: InfoView? = null
    private val counter = Counter(0)

    private val trailAdapter: TrailAdapter = TrailAdapter()
    protected val adapter = ItemAdapter(
        ItemViewTypes.TextItem with TextItem,
        ItemViewTypes.StorageItem with PathItem
    )

    override fun getNavigationRoute(): Int = Navigation.Storage

    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {

    }

    override fun onMenuActionCall(action: MenuAction): Boolean {
        return when (action.id) {
            ViewIds.Storage.Menu.DeleteId -> {
                true
            }

            ViewIds.Storage.Menu.TasksId -> {
                TaskBottomDrawer(context).show()
                true
            }

            ViewIds.Storage.Menu.MoveId -> {

                true
            }

            ViewIds.Storage.Menu.ProvideStorageAccessId -> {
                context.startActivityForResult(
                    Contracts.Storage.StorageAccessIntent(),
                    Contracts.Storage.RequestStorageAccessCode
                )
                true
            }

            ViewIds.Storage.Menu.ProvideFullStorageAccessId -> {
                context.startActivityForResult(
                    Contracts.Storage.StorageFullAccessIntent(),
                    Contracts.Storage.RequestFullStorageAccessCode
                )
                true
            }

            ViewIds.Storage.Menu.ProvideRestrictedDirectoryAccess -> {
                stateController?.let {
                    it.helper.requestRestrictedPermission(it.current!!.name)?.let { intent ->
                        context.startActivityForResult(
                            intent,
                            Contracts.Storage.RequestRestrictedDirectoryAccess
                        )
                    }
                }
                true
            }

            else -> false
        }
    }

    private inline fun checkParent(
        context: Context,
        block: FrameLayout.() -> Unit,
    ): io.github.excu101.vortex.theme.widget.ThemeFrameLayout {
        if (container == null) {
            container = object : io.github.excu101.vortex.theme.widget.ThemeFrameLayout(context) {
                init {
                    setBackgroundColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.storageListBackgroundColorKey))
                    layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }

                override fun onColorChanged() {
                    setBackgroundColor(io.github.excu101.vortex.theme.ThemeColor(io.github.excu101.vortex.theme.key.storageListBackgroundColorKey))
                }
            }
        }

        return container!!.apply(block)
    }

    override fun onCreateView(context: Context): View = checkParent(context) {
        loading = LoadingView(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                gravity = CENTER
            }
        }

        info = InfoView(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        trail = TrailListView(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            adapter = trailAdapter
            itemAnimator = null
        }

        recycler = RecyclerView(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            adapter = this@StoragePageController.adapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            clipToPadding = false
        }
        ViewCompat.setOnApplyWindowInsetsListener(recycler!!) { view, insets ->
            view.updatePadding(
                top = trail!!.measuredHeight,
                bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            )
            insets
        }

        recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    controller?.bar?.hide()
                } else {
                    controller?.bar?.show()
                }

                if (recyclerView.canScrollVertically(-1)) {
                    trail?.elevation = 16F
                } else {
                    trail?.elevation = 0F
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

            }
        })

        trailAdapter.register(this@StoragePageController)
        trailAdapter.registerLong(this@StoragePageController)

        adapter.register(this@StoragePageController)
        adapter.registerLong(this@StoragePageController)
        adapter.registerItemSelection(this@StoragePageController)
        adapter.registerSelection(this@StoragePageController)

        addView(recycler, 0)
        addView(info, 1)
        addView(trail, 2)
        addView(loading, 3)
    }

    private fun prepareSwitchAnimator(state: StorageStateController): ValueAnimator {
        val animator = ValueAnimator.ofFloat(0F, 1F)

        animator.addUpdateListener { anim ->
            recycler?.alpha =
                if (state.flag == FLAG_EMPTY) anim.animatedFraction
                else 1F - anim.animatedFraction

            loading?.alpha =
                if (state.flag == FLAG_LOADING) anim.animatedFraction
                else 1F - anim.animatedFraction

            info?.alpha =
                if (state.flag > 0) anim.animatedFraction
                else 1F - anim.animatedFraction

        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                recycler?.isVisible = state.flag != FLAG_EMPTY
                loading?.isVisible = state.flag != FLAG_LOADING
                info?.isVisible = state.flag <= FLAG_LOADING && info?.isVisible == false
            }

            override fun onAnimationCancel(animation: Animator) {
                recycler?.isVisible = state.flag == FLAG_EMPTY
                loading?.isVisible = state.flag == FLAG_LOADING
                info?.isVisible = state.flag > FLAG_LOADING
            }

            override fun onAnimationEnd(animation: Animator) {
                recycler?.isVisible = state.flag == FLAG_EMPTY
                loading?.isVisible = state.flag == FLAG_LOADING
                info?.isVisible = state.flag > FLAG_LOADING
            }
        })

        animator.duration = 150L

        return animator
    }

    private fun reduceMessage(messageNumber: Int) {
        info?.apply {
            when (messageNumber) {
                FLAG_MESSAGE_REQUIRES_STORAGE_ACCESS -> {
                    icon = Icons.Rounded.Info
                    message =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListWarningStorageAccessTitleKey)
                    controller?.bar?.replaceItems(
                        listOf(
                            MenuAction(
                                ViewIds.Storage.Menu.ProvideStorageAccessId,
                                io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListWarningStorageAccessActionTitleKey),
                                Icons.Rounded.Check
                            )
                        )
                    )
                }

                FLAG_MESSAGE_REQUIRES_FULL_STORAGE_ACCESS -> {
                    icon = Icons.Rounded.Info
                    message =
                        io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListWarningFullStorageAccessTitleKey)
                    controller?.bar?.replaceItems(
                        listOf(
                            MenuAction(
                                ViewIds.Storage.Menu.ProvideFullStorageAccessId,
                                io.github.excu101.vortex.theme.ThemeText(io.github.excu101.vortex.theme.key.fileListWarningFullStorageAccessActionTitleKey),
                                Icons.Rounded.Check
                            )
                        )
                    )
                }

                FLAG_MESSAGE_EMPTY_DIR -> {
                    icon = Icons.Rounded.Folder
                    message = io.github.excu101.vortex.theme.FormatterThemeText(
                        io.github.excu101.vortex.theme.key.fileListWarningEmptyTitleKey,
                        stateController?.current?.name
                    )

                }

                FLAG_MESSAGE_RESTRICTED_DIR -> {
                    icon = Icons.Rounded.Folder
                    message = "Restricted directory"
                    controller?.bar?.replaceItems(
                        listOf(
                            MenuAction(
                                ViewIds.Storage.Menu.ProvideRestrictedDirectoryAccess,
                                "Provide restricted directory access",
                                Icons.Rounded.Check
                            )
                        )
                    )
                }
            }
        }
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
    } ?: false

    override fun onSaveInstance(
        bundle: Bundle,
        prefix: String,
    ): Boolean {
        if (stateController?.trail?.isEmpty() == true) return false
        if (stateController?.current == null) return false

        stateController?.current?.let { current ->
            bundle.putParcelable(
                createPrefixBundle(prefix),
                current
            )
        }

        return true
    }

    override fun onClick(view: View, item: SuperItem, position: Int) {
        when (item) {
            is DrawerItem -> {

            }

            is PathItem -> {
                when (view.id) {
                    ViewIds.Storage.Trail.RootId -> {
                        stateController?.navigate(item)
                    }

                    ViewIds.Storage.Item.IconId -> {
                        adapter.select(item)
                    }

                    ViewIds.Storage.Item.RootId -> {
                        if (item.isDirectory) stateController?.navigate(item)
//                        else stateController?.open(item)
                    }
                }
            }
        }
    }

    override fun onActivityResult(request: Int, result: Int, data: Intent?): Boolean {
        return when (request) {
            Contracts.Storage.RequestStorageAccessCode -> {
                stateController?.checkPerms()
                true
            }

            Contracts.Storage.RequestFullStorageAccessCode -> {
                stateController?.checkPerms()
                true
            }

            Contracts.Storage.RequestRestrictedDirectoryAccess -> {
                if (result == RESULT_OK) {
                    data?.data?.let { uri ->
                        stateController?.helper?.takePersistableUriPermission(uri)
                        stateController?.navigateDocument(uri)
                    }
                }
                true
            }

            else -> false
        }
    }

    override fun getNavigationActions(): List<MenuAction> {
        return BarActions
    }

    override fun onLongClick(view: View, item: Item<*>, position: Int): Boolean {
        return when (item) {
            is PathItem -> {
                when (view.id) {
                    ViewIds.Storage.Trail.RootId -> {
                        ItemBottomDrawerFragment(
                            context = context,
                            items = StorageActionContentProvider().onSingleItem(item)
                        ) { view, item, position ->
                            onClick(view, item, position)
                            dismiss()
                        }
                    }

                    ViewIds.Storage.Item.IconId -> {
                        ItemBottomDrawerFragment(
                            context = context,
                            items = if (isInSelectionMode)
                                StorageActionContentProvider().onSelectedItems(
                                    adapter.getSelectedCountInstance(PathItem::class)
                                )
                            else
                                StorageActionContentProvider().onSingleItem(item)
                        )
                    }

                    ViewIds.Storage.Item.RootId -> {
                        if (adapter.isSelected(item)) {
                            ItemBottomDrawerFragment(
                                context = context,
                                items = StorageActionContentProvider().onSelectedItems(
                                    adapter.getSelectedCountInstance(PathItem::class)
                                )
                            )
                        } else {
                            adapter.select(item)
                        }
                    }
                }
                true
            }

            else -> false

        }
    }

    override fun onItemSelectionChanged(
        item: SuperItem,
        isSelected: Boolean,
    ) {

    }

    override fun onSelectionChanged(selected: List<SuperItem>) {
        if (counter.replace(selected.size).isZero) {
            controller?.bar?.title = title
        } else {
            controller?.bar?.setTitleWithAnimation(
                title = counter.value.toString(),
                isReverse = counter.isBiggerThanLast
            )
        }
    }

    override fun onBackActivated(): Boolean {
        if (stateController?.current?.value != getExternalStorageDirectory().asPath()) {
            stateController?.navigateBack()
            return true
        }

        return super.onBackActivated()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isDestroyed) {
            stateController?.onDestroy()
            container?.removeAllViews()
            trail = null
            loading = null
            info = null
        }
    }

    override fun onFocus() {

    }

    override fun onPrepare() {
        stateController = context.component.storageStateController

        stateController?.addListener(this@StoragePageController)
        stateController?.addListener { state ->
            adapter.replace(state.list)
            trailAdapter.replace(state.trail)
            trailAdapter.updateSelected(state.currentIndex)

            if (state.currentIndex >= 0)
                trail?.smoothScrollToPosition(state.currentIndex)

            reduceMessage(messageNumber = state.flag)
            prepareSwitchAnimator(state).start()
        }
        stateController?.checkPerms()
    }

    override fun onHide() {

    }

    override fun getNavigationIconType(): Int {
        return if (isInSelectionMode) 0 else 1
    }

    override fun onEnterSelectionMode() {

    }

    override fun onLeaveSelectionMode() {

    }

    override fun onAttachToNavigation(controller: NavPageController) {

    }

    override fun onDetachFromNavigation() {

    }
}