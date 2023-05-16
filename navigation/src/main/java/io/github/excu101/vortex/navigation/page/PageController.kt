package io.github.excu101.vortex.navigation.page

import android.content.Context
import android.os.Bundle
import android.view.View
import io.github.excu101.vortex.navigation.NavPageController

private const val STATE_DESTROYED = 1 shl 1
private const val STATE_ATTACHED_CONTROLLER = 1 shl 4

abstract class PageController(private val context: Context) : BackListener {

    private var _flag = 0
    protected val state: Int
        get() = _flag

    private var _view: View? = null
    private var _controller: NavPageController? = null
    protected val controller: NavPageController?
        get() = _controller

    protected abstract fun onCreateView(context: Context): View

    open fun <A> setArgs(args: A? = null) {

    }

    open fun getNavigationRoute(): Int = -1

    val isDestroyed: Boolean
        get() = _flag and STATE_DESTROYED != 0

    open fun onDestroy() {
        if (!isDestroyed) {
            _flag = _flag or STATE_DESTROYED
            _view = null
        }
    }

    open fun onPrepare() {}
    open fun onHide() {}
    open fun onFocus() {}

    override fun onBackActivated(): Boolean = false

    internal fun attachNavigationController(controller: NavPageController) {
        _controller = controller
        onAttachToNavigation(controller)
    }

    protected open fun onAttachToNavigation(controller: NavPageController) {

    }

    internal fun detachNavigationController() {
        _controller = null
        onDetachFromNavigation()
    }

    protected open fun onDetachFromNavigation() {

    }

    open fun onSaveInstance(bundle: Bundle, prefix: String): Boolean = false

    open fun onRestoreInstance(bundle: Bundle, prefix: String): Boolean = false

    fun getContentView(): View {
        if (_view == null) {
            _view = onCreateView(context = context)
        }
        return _view!!
    }

}