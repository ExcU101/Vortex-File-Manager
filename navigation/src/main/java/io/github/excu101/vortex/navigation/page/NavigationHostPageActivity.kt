package io.github.excu101.vortex.navigation.page

import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.inspector.IntFlagMapping
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import io.github.excu101.vortex.navigation.NavPageController
import io.github.excu101.vortex.navigation.NavPageControllerGraph
import io.github.excu101.vortex.navigation.currentView

open class NavigationHostPageActivity : ComponentActivity() {

    protected var graph: NavPageControllerGraph? = null
    private var wrapper: FrameLayout? = null

    override fun onCreate(inState: Bundle?) {
        super.onCreate(inState)

        graph = NavPageControllerGraph(context = this)
        wrapper = FrameLayout(this).apply {

            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }

        graph?.controller?.bindWrapper(wrapper)
        graph?.controller?.currentView?.let {
            wrapper?.addView(graph?.controller?.currentView)
        }

        setContentView(wrapper)
    }

    protected fun requireGraph(): NavPageControllerGraph {
        return graph!!
    }

    protected fun requireController(): NavPageController {
        return requireGraph().controller
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (graph == null) return
        graph!!.saveNavigation(outState)

        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (graph?.controller?.isBackActivated() == true) return
        super.onBackPressed()
    }

}